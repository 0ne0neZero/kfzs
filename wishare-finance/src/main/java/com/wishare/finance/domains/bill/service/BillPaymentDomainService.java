package com.wishare.finance.domains.bill.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.apps.model.bill.fo.BillPrepayByMchNoUpdateF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoAddF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoUpdateF;
import com.wishare.finance.apps.model.bill.vo.BillPrepayInfoV;
import com.wishare.finance.apps.service.bill.prepay.BillPrepayInfoAppService;
import com.wishare.finance.apps.service.yuanyang.OrderTitleGet100FirstStringSplitter;
import com.wishare.finance.domains.bill.command.ReceivableBillPreTransactionCommand;
import com.wishare.finance.domains.bill.command.ReceivableBillTransactionConfirmCommand;
import com.wishare.finance.domains.bill.command.TransactionCallbackCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.ConfirmTransactDto;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillBatchActionEvent;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.CacheEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.payment.PaymentOrderClient;
import com.wishare.finance.infrastructure.remote.fo.payment.PayeeF;
import com.wishare.finance.infrastructure.remote.fo.payment.PayerF;
import com.wishare.finance.infrastructure.remote.fo.payment.SceneF;
import com.wishare.finance.infrastructure.remote.fo.payment.TransactionF;
import com.wishare.finance.infrastructure.remote.vo.payment.PaymentOrderDetailRv;
import com.wishare.finance.infrastructure.remote.vo.payment.TransactionV;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 账单支付相关领域服务
 *
 * @Author dxclay
 * @Date 2022/12/19
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillPaymentDomainService {

    private final GatherBillRepository gatherBillRepository;
    private final ReceivableBillRepository receivableBillRepository;
    private final TransactionTemplate transactionTemplate;

    private final GatherDetailRepository gatherDetailRepository;

    private final PaymentOrderClient paymentOrderClient;

    private final TransactionOrderRepository transactionOrderRepository;
    private final PayBillRepository payBillRepository;
    private final PayDetailRepository payDetailRepository;

    private final BillPrepayInfoAppService billPrepayInfoAppService;

    /**
     * 应收账单预支付
     *
     * @param command
     * @return
     */
    public String preTransact(ReceivableBillPreTransactionCommand command) {
        List<Long> billIds = command.getBillIds().stream().map(Long::valueOf).collect(Collectors.toList());
        //根据id获取账单类列表
        List<ReceivableBill> receivableBills = receivableBillRepository.list(new QueryWrapper<ReceivableBill>()
                .in("id", billIds)
            .eq("sup_cp_unit_id", command.getSupCpUnitId()));
        //校验账单信息
        receivableBills.forEach(Bill::verifyOperate);
        //获取当前时间
        LocalDateTime nowTime= LocalDateTime.now();
        LocalDateTime expireTime = command.getExpireTime();
        if (Objects.nonNull(expireTime)) {
            ErrorAssertUtil.isTrueThrow402(expireTime.compareTo(nowTime)>=0, ErrorMessage.PAYMENT_PRE_BILL_LOCK_TIME);
        }
        long actualUnpayAmount = receivableBills.stream().mapToLong(Bill::getActualUnpayAmount).sum();
        ErrorAssertUtil.isTrueThrow402(command.getTotalAmount().compareTo(actualUnpayAmount) == 0, ErrorMessage.PAYMENT_PRE_TRANSACT_AMOUNT_ERROR);
        List<String> strBillIds = receivableBills.stream().map(b -> String.valueOf(b.getId())).collect(Collectors.toList());
        int second = Objects.isNull(expireTime) ? 24*60*60 : (int) Duration.between(LocalDateTime.now(),
                expireTime).toSeconds();
//        //锁定账单信息
//        try {
//            BusinessLockHelper.tryMultiLock(MultiLocker.of(LockerEnum.BILL_LOCK, strBillIds, second));
//        }catch (LockException e){
//            ErrorAssertUtil.throw400(ErrorMessage.PAYMENT_PRE_TRANSACT_PAYING);
//        }
        command.setBillIds(strBillIds);
        //颁发预支付id
        String preTradeId = IdentifierFactory.getInstance().generateUUID();
        BillPrepayInfoAddF prepayInfoAddF = new BillPrepayInfoAddF();
        prepayInfoAddF.setSupCpUnitId(command.getSupCpUnitId());
        prepayInfoAddF.setBillIds(billIds);
        prepayInfoAddF.setStartTime(LocalDateTime.now());
        prepayInfoAddF.setExpireTime(Objects.isNull(expireTime)?LocalDateTime.now().plusMinutes(15L):expireTime);
        prepayInfoAddF.setPayState(BillPayStateEnum.支付中.getCode());
        prepayInfoAddF.setMchOrderNo(preTradeId);
        prepayInfoAddF.setQrCodeUrl(JSONObject.toJSONString(command));
        // 预支付更新账单状态
        billPrepayInfoAppService.billPrepay(prepayInfoAddF);
        //将预支付信息放入缓存,默认1天过期
        log.info("预支付信息缓存创建：preTradeId：{},key: {}",preTradeId,CacheEnum.PAYMENT_TRANSACTION.getCacheKey(preTradeId));
        RedisHelper.setAtExpire(CacheEnum.PAYMENT_TRANSACTION.getCacheKey(preTradeId), second, JSONObject.toJSONString(command));
        return preTradeId;
    }

    /**
     * 支付结果响应信息
     *
     * @param command
     * @return
     */
    public ConfirmTransactDto confirmTransaction(ReceivableBillTransactionConfirmCommand command) {
        String preTransactCommandJson = RedisHelper.get(CacheEnum.PAYMENT_TRANSACTION.getCacheKey(command.getPreTradeId()));
        if (StringUtils.isNotBlank(command.getBankFlowNo())) {
            String messageKey = CacheEnum.TRADE_NO.getCacheKey(command.getBankFlowNo());
            if (!RedisHelper.setNotExists(messageKey, "1") && command.getPayState().equals(Const.State._1)) {
                log.info("交易流水号重复请求：{}", command.getBankFlowNo());
                throw BizException.throw402("请勿重复支付");
            }
            RedisHelper.expire(messageKey, 15L);
        }
        String tradeNo = command.getOutTradeNo();
        String supCpUnitId = command.getSupCpUnitId();
        if(StringUtils.isNotBlank(tradeNo)){
            //防止移动端同一笔订单流水重复插入
            Integer count = gatherBillRepository.queryCountByTradeNo(tradeNo, supCpUnitId);
            if(Objects.nonNull(count) && count > 0){
                log.info("预支付确认tradeNo重复----->tradeNo:{}",tradeNo);
                throw BizException.throw402("预支付确认tradeNo重复" + tradeNo);
            }
        }else if (StringUtils.isBlank(tradeNo) && command.getPayState() == 1){
            log.info("预支付确认tradeNo为空----->unitaryEnterF:{}",JSON.toJSONString(command));
            throw BizException.throw402("预支付确认tradeNo为空");
        }


        // 如果缓存里未找到 从本地数据库里查询是否存在预支付信息
        if (StringUtils.isBlank(preTransactCommandJson)){
            List<BillPrepayInfoV> infoV = billPrepayInfoAppService.getByMchNo(command.getPreTradeId(), BillPayStateEnum.支付中.getCode());
            if (CollectionUtils.isNotEmpty(infoV) && StringUtils.isNotBlank(infoV.get(0).getQrCodeUrl())){
                preTransactCommandJson = infoV.get(0).getQrCodeUrl();
            }
        }
        ErrorAssertUtil.notNullThrow300(preTransactCommandJson, ErrorMessage.PAYMENT_PRE_TRANSACT_NOT_EXIST);
        Long billId = null;
        ReceivableBillPreTransactionCommand preTransactionCommand = JSONObject.parseObject(preTransactCommandJson, ReceivableBillPreTransactionCommand.class);
        List<String> billIds = preTransactionCommand.getBillIds();
        List<Long> billIdsList = preTransactionCommand.getBillIds().stream().map(Long::valueOf).collect(Collectors.toList());
        if (command.getPayState() == 1) {
            ErrorAssertUtil.notNullThrow301(command.getPayTime(), ErrorMessage.PAYMENT_PRE_TRANSACT_TIME_NOT_NULL);
            ErrorAssertUtil.notNullThrow301(command.getPayWay(), ErrorMessage.PAYMENT_PRE_TRANSACT_WAY_NOT_NULL);
            ErrorAssertUtil.notNullThrow301(command.getPayChannel(), ErrorMessage.PAYMENT_PRE_TRANSACT_CHANNEL_NOT_NULL);
            GatherBill gatherBill = Global.mapperFacade.map(preTransactionCommand, GatherBill.class);
            gatherBill.setTradeNo(command.getOutTradeNo());
            gatherBill.setOutBusNo(command.getOutTradeNo());
            gatherBill.setPayWay(command.getPayWay());
            gatherBill.setPayChannel(command.getPayChannel());
            gatherBill.setPayTime(command.getPayTime());
            gatherBill.setPaySource(command.getPaySource());
            gatherBill.setMchNo(command.getMchNo());
            gatherBill.setDeviceNo(command.getDeviceNo());
            gatherBill.setSupCpUnitId(command.getSupCpUnitId());
            gatherBill.setBankFlowNo(command.getBankFlowNo());
            gatherBill.setPayeeName(command.getPayeeName());
            gatherBill.setPayeeId(command.getPayeeId());
            // 支付成功更新账单结算状态
            BillPrepayInfoUpdateF infoUpdateF = new BillPrepayInfoUpdateF()
                    .setPayState(BillPayStateEnum.支付成功.getCode());
            infoUpdateF.setBillIds(billIdsList);
            infoUpdateF.setSupCpUnitId(command.getSupCpUnitId());
            billPrepayInfoAppService.releasePaymentOpr(infoUpdateF);
            List<ReceivableBill> receivableBills = receivableBillRepository.list(new QueryWrapper<ReceivableBill>().in("id", billIds)
                    .eq("sup_cp_unit_id", command.getSupCpUnitId()));
            log.info("应收查询结果：{}",JSON.toJSONString(receivableBills));
            if(CollectionUtils.isNotEmpty(receivableBills)){
                ReceivableBill receivableBill = receivableBills.get(0);
                gatherBill.setSupCpUnitName(receivableBill.getCommunityName());
                gatherBill.setStatutoryBodyName(receivableBill.getStatutoryBodyName());
                gatherBill.setStatutoryBodyId(receivableBill.getStatutoryBodyId());
            }
            gatherBill.transact(receivableBills, preTransactionCommand.getPayerPhone());
            try {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(@NotNull TransactionStatus transactionStatus) {
                        gatherBillRepository.save(gatherBill);
                        receivableBills.forEach(bill ->
                        {
                            bill.initPayInfos(bill.getTotalAmount(), command.getPayWay(), command.getPayChannel());
                            String payeeName = bill.getPayeeName();
                            String payeeId = bill.getPayeeId();
                            if(StringUtils.isBlank(payeeName)){
                                bill.setPayeeName(gatherBill.getPayeeName());
                            }
                            if(StringUtils.isBlank(payeeId)){
                                bill.setPayeeId(gatherBill.getPayeeId());
                            }
                        });
                        receivableBills.forEach(v ->
                        {
                            receivableBillRepository.update(v, new QueryWrapper<ReceivableBill>().eq("id", v.getId())
                                    .eq("sup_cp_unit_id", command.getSupCpUnitId()));
                        });
                        gatherDetailRepository.saveBatch(gatherBill.getGatherDetails());
                    }
                });
            }catch (Exception e){
                log.error("预支付确认错误：{},{}",e,e.getMessage());
                // 支付失败更新账单结算状态
                billPrepayInfoAppService.updateByMchNo(new BillPrepayByMchNoUpdateF().setMchOrderNo(command.getPreTradeId())
                        .setPayStatus(BillPayStateEnum.支付失败.getCode()).setSupCpUnitId(command.getSupCpUnitId()));
                throw e;
            }
            billId  = gatherBill.getId();
            EventLifecycle.push(EventMessage.builder().headers("action", BillAction.SETTLED_BATCH).payload(
                    BillBatchActionEvent.settle(billIds.stream().map(Long::valueOf).collect(Collectors.toList()),
                            BillTypeEnum.应收账单.getCode(), "结算", command.getSupCpUnitId())).build());
        }else if (command.getPayState() == Const.State._2){
            // 支付取消回调状态
            BillPrepayInfoUpdateF infoUpdateF = BillPrepayInfoUpdateF.builder().reason(BillPayStateEnum.支付取消.getValue())
                    .payState(BillPayStateEnum.支付取消.getCode()).build();
            infoUpdateF.setBillIds(billIdsList);
            infoUpdateF.setSupCpUnitId(command.getSupCpUnitId());
            billPrepayInfoAppService.releasePaymentOpr(infoUpdateF);
        }
//        //账单解锁
//        BusinessLockHelper.unLock(MultiLocker.of(LockerEnum.BILL_LOCK, billIds));
        //将预支付信息从缓存中删除
        RedisHelper.delete(CacheEnum.PAYMENT_TRANSACTION.getCacheKey(command.getPreTradeId()));
        log.info("预支付信息缓存删除：preTradeId：{}，key：{}",command.getPreTradeId(),CacheEnum.PAYMENT_TRANSACTION.getCacheKey(command.getPreTradeId()));
        return new ConfirmTransactDto(billId, billIds);
    }

    /**
     * 组合支付
     * @param transactionOrder 支付订单
     * @return 结果
     */
    public boolean combineTransact(TransactionOrder transactionOrder){
        TransactionOrder transaction = transactionOrderRepository.getByBizTransactionNo(transactionOrder.getBizTransactionNo());
        if (Objects.nonNull(transaction)){
            transactionOrder.setTransactState(transaction.getTransactState());
            transactionOrder.setInvoiceState(transaction.getInvoiceState());
            transactionOrder.setVoucherState(transaction.getVoucherState());
            ErrorAssertUtil.isFalseThrow402(BillTransactStateEnum.交易中.equalsByCode(transaction.getTransactState()), ErrorMessage.PAYMENT_TRADING);
            ErrorAssertUtil.isFalseThrow402(BillTransactStateEnum.交易成功.equalsByCode(transaction.getTransactState()), ErrorMessage.PAYMENT_TRADED);
        }
        TransactionV transactionV = null;
        TransactionOrder subTransaction;
        for (TransactionOrder subOrder : transactionOrder.getSubOrders()) {
            //查询子单子信息如果已经支付，返回已支付即可
            subTransaction = transactionOrderRepository.getByBizTransactionNo(subOrder.getBizTransactionNo());
            if (Objects.nonNull(subTransaction)){
                PaymentOrderDetailRv paymentOrderDetail = paymentOrderClient.getDetailByMchOrderNo(subOrder.getPayNo());
                if (Objects.nonNull(paymentOrderDetail) && paymentOrderDetail.getState() == 2){
                    subOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    subOrder.setSuccessTime(LocalDateTime.now());
                    subOrder.setPayNo(paymentOrderDetail.getPayNo());
                    continue;
                }
            }

            try {
                if (SettleWayEnum.线上.equalsByCode(transactionOrder.getPayWay()) && SettleChannelEnum.招商银企直连.equalsByCode(transactionOrder.getPayChannel())) {
                    subOrder.generateIdentifier();
                    transactionV = paymentOrderClient.trade(convertPaymentTransaction(subOrder));
                }else if(SettleWayEnum.线下.equalsByCode(transactionOrder.getPayWay())){
                    transactionV = new TransactionV();
                    transactionV.setPayState(2); //线下默认支付成功
                }
                subOrder.setPayNo(transactionV.getPayNo());
                //支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭
                if (transactionV.getPayState() == 1){
                    subOrder.setTransactState(BillTransactStateEnum.交易中.getCode());
                } else if (transactionV.getPayState() == 2) {
                    subOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    subOrder.setSuccessTime(LocalDateTime.now());
                } else {
                    subOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                    subOrder.setErrCode(transactionV.getErrCode());
                    subOrder.setErrMsg(transactionV.getErrMsg());
                }
            }catch (BizException e){
                int failCode = e.getFailInfo().getCode();
                if ("1001".equals(failCode)){
                    subOrder.setTransactState(BillTransactStateEnum.交易中.getCode());
                }else if ("1002".equals(failCode)){
                    subOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    subOrder.setSuccessTime(LocalDateTime.now());
                }else{
                    subOrder.setErrCode(String.valueOf(e.getFailInfo().getCode()));
                    subOrder.setErrMsg(e.getFailInfo().getMsg());
                    subOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                }
            }catch (Exception e){
                log.error("发起交易支付异常", e);
                subOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                subOrder.setErrCode("500");
                subOrder.setErrMsg("支付异常，请稍后再试");
            }
        }
        List<TransactionOrder> transactionOrders = transactionOrder.getSubOrders();
        // transactionOrders.add(transactionOrder);
        transactionOrders.forEach(v->{
            if (StrUtil.isNotBlank(v.getTransactionTitle()) && v.getTransactionTitle().length() >150){
                v.setTransactionTitle(v.getTransactionTitle().substring(0,149));
            }
        });
        return transactionOrderRepository.saveBatch(transactionOrders);

    }


    /**
     * 发起交易
     * @param transactionOrder
     * @return
     */
    public boolean transact(TransactionOrder transactionOrder){
        //1.生成支付订单
        TransactionOrder transaction = transactionOrderRepository.getByBizTransactionNo(transactionOrder.getBizTransactionNo());
        if (Objects.nonNull(transaction)){
            //如果交易失败，则查询是否已经支付过，如果已经支付过则返回支付成功
            if (BillTransactStateEnum.交易失败.equalsByCode(transaction.getTransactState())){
                PaymentOrderDetailRv paymentOrderDetail = paymentOrderClient.getDetailByMchOrderNo(transaction.getPayNo());
                //TODO 待优化
                if (Objects.nonNull(paymentOrderDetail) && paymentOrderDetail.getState() == 2){
                    transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    transactionOrder.setPayNo(paymentOrderDetail.getPayNo());
                    transactionOrder.generateIdentifier(); //生成id和流水号
                    return transactionOrderRepository.save(transactionOrder);
                }
            }else {
                transactionOrder.setTransactState(transaction.getTransactState());
                transactionOrder.setInvoiceState(transaction.getInvoiceState());
                transactionOrder.setVoucherState(transaction.getVoucherState());
                ErrorAssertUtil.isFalseThrow402(BillTransactStateEnum.交易中.equalsByCode(transaction.getTransactState()), ErrorMessage.PAYMENT_TRADING);
                ErrorAssertUtil.isFalseThrow402(BillTransactStateEnum.交易成功.equalsByCode(transaction.getTransactState()), ErrorMessage.PAYMENT_TRADED);
            }
        }
        transactionOrder.generateIdentifier(); //生成id和流水号
        //2.发起支付
        TransactionF transactionF = convertPaymentTransaction(transactionOrder);
        TransactionV transactionV = null;
        boolean isPayException = false;
        try {
            if (SettleWayEnum.线上.equalsByCode(transactionOrder.getPayWay()) && SettleChannelEnum.招商银企直连.equalsByCode(transactionOrder.getPayChannel())) {
                transactionV = paymentOrderClient.trade(transactionF);
            }else if(SettleWayEnum.线下.equalsByCode(transactionOrder.getPayWay())){
                transactionV = new TransactionV();
                transactionV.setPayState(2); //线下默认支付成功
            }
            transactionOrder.setPayNo(transactionV.getPayNo());
            //支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭
            if (transactionV.getPayState() == 1){
                transactionOrder.setTransactState(BillTransactStateEnum.交易中.getCode());
            } else if (transactionV.getPayState() == 2) {
                transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
            } else {
                transactionOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                transactionOrder.setErrCode(transactionV.getErrCode());
                transactionOrder.setErrMsg(transactionV.getErrMsg());
            }
        }catch (BizException e){
            int failCode = e.getFailInfo().getCode();
            if ("1001".equals(failCode)){
                transactionOrder.setTransactState(BillTransactStateEnum.交易中.getCode());
            }else if ("1002".equals(failCode)){
                transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
            }else{
                transactionOrder.setErrCode(String.valueOf(e.getFailInfo().getCode()));
                transactionOrder.setErrMsg(e.getFailInfo().getMsg());
                transactionOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
            }
        }catch (Exception e){
            log.error("发起交易支付异常", e);
            transactionOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
            transactionOrder.setErrCode("500");
            transactionOrder.setErrMsg("支付异常，请稍后再试");
            isPayException = true;
        }

        if (BillTransactStateEnum.交易成功.equalsByCode(transactionOrder.getTransactState())
                || BillTransactStateEnum.交易中.equalsByCode(transactionOrder.getTransactState()) || isPayException){

            if (StrUtil.isNotBlank(transactionOrder.getTransactionTitle()) && transactionOrder.getTransactionTitle().length() >150){
                transactionOrder.setTransactionTitle(transactionOrder.getTransactionTitle().substring(0,149));
            }
            transactionOrderRepository.save(transactionOrder);
        }
        return true;
    }

    /**
     * 发起交易
     * @param transactionOrder
     * @return
     */
    public boolean multiTransact(TransactionOrder transactionOrder){
        List<TransactionOrder> subOrders = transactionOrder.getSubOrders();
        TransactionV transactionV = null;
        TransactionOrder subTransaction;
        for (TransactionOrder subOrder : subOrders) {
            //查询子单子信息如果已经支付，返回已支付即可
            subTransaction = transactionOrderRepository.getByBizTransactionNo(subOrder.getBizTransactionNo());
            if (Objects.nonNull(subTransaction)){
                if (BillTransactStateEnum.交易失败.equalsByCode(subTransaction.getTransactState())) {
                    transactionOrderRepository.update(new LambdaUpdateWrapper<TransactionOrder>()
                            .set(TransactionOrder::getDeleted, 1)
                            .eq(TransactionOrder::getBizTransactionNo, subTransaction.getBizTransactionNo()));
                }
            }
            try {
                subOrder.generateIdentifier();
                if (SettleWayEnum.线上.equalsByCode(subOrder.getPayWay()) && SettleChannelEnum.招商银企直连.equalsByCode(subOrder.getPayChannel())) {
                    log.info("请求payment日志:{}", JSON.toJSONString(subOrder));
                    transactionV = paymentOrderClient.trade(convertPaymentTransaction(subOrder));
                    log.info("请求payment日志返回结果{}", JSON.toJSONString(transactionV));
                }else if(SettleWayEnum.线下.equalsByCode(subOrder.getPayWay())){
                    transactionV = new TransactionV();
                    transactionV.setPayState(2); //线下默认支付成功
                }
                subOrder.setPayNo(transactionV.getPayNo());
                //支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭
                if (transactionV.getPayState() == 1){
                    subOrder.setTransactState(BillTransactStateEnum.交易中.getCode());
                } else if (transactionV.getPayState() == 2) {
                    subOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    subOrder.setSuccessTime(LocalDateTime.now());
                } else {
                    subOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                    subOrder.setErrCode(transactionV.getErrCode());
                    subOrder.setErrMsg(transactionV.getErrMsg());
                }
            }catch (BizException e){
                int failCode = e.getFailInfo().getCode();
                if (1001 == failCode){
                    subOrder.setTransactState(BillTransactStateEnum.交易中.getCode());
                }else if (1002 == failCode){
                    subOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    transactionOrder.setTransactState(BillTransactStateEnum.交易成功.getCode());
                    subOrder.setSuccessTime(LocalDateTime.now());
                }else{
                    subOrder.setErrCode(String.valueOf(e.getFailInfo().getCode()));
                    subOrder.setErrMsg(e.getFailInfo().getMsg());
                    subOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                }
            }catch (Exception e){
                log.error("发起交易支付异常", e);
                subOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                subOrder.setErrCode("500");
                subOrder.setErrMsg("支付异常，请稍后再试");
                transactionOrder.setTransactState(BillTransactStateEnum.交易失败.getCode());
                transactionOrder.setErrCode("500");
                transactionOrder.setErrMsg("支付异常，请稍后再试");
            }
        }
        log.info("transactionOrder:{}", JSON.toJSONString(transactionOrder));
        subOrders.forEach(v->{
            if (StrUtil.isNotBlank(v.getTransactionTitle()) && v.getTransactionTitle().length() >150){
                v.setTransactionTitle(v.getTransactionTitle().substring(0,149));
            }
        });
        transactionOrderRepository.saveBatch(subOrders);
        return true;
    }

    /**
     * 入账信息
     * @param transactionOrder 交易订单
     */
    public PayBill enterTransactionPayBill(TransactionOrder transactionOrder){
        TransactionBillOBV transactionBillOBV = transactionOrder.getBillParam();
        if (BillTypeEnum.付款单.equalsByCode(transactionBillOBV.getBillType())){
            PayBill payBill = Global.mapperFacade.map(transactionBillOBV, PayBill.class);
            payBill.generateIdentifier();
            payBill.setTransactionNo(transactionOrder.getTransactionNo());
            payBill.setState(BillStateEnum.正常.getCode());
            payBill.setOnAccount(BillOnAccountEnum.未挂账.getCode());
            payBill.setRefundState(BillRefundStateEnum.未退款.getCode());
            payBill.setVerifyState(BillVerifyStateEnum.未核销.getCode());
            payBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
            payBill.setCarriedState(BillCarriedStateEnum.未结转.getCode());
            payBill.setInvoiceState(BillInvoiceStateEnum.已开票.getCode());
            List<PayDetail> payDetails = Global.mapperFacade.mapAsList(transactionBillOBV.getTransactionBillDetails(), PayDetail.class);
            payDetails.forEach(payDetail -> {
                payDetail.setPayBillId(payBill.getId());
                payDetail.setPayBillNo(payBill.getBillNo());
            });
            payBillRepository.save(payBill);
            payDetailRepository.saveBatch(payDetails);
            transactionBillOBV.setBillId(payBill.getId());
            transactionBillOBV.setBillNo(payBill.getBillNo());
            //记录日志
            BizLog.normal(payBill.getId().toString(), LogContext.getOperator(), LogObject.账单, LogAction.生成, new Content()
                    .option(new PlainTextDataItem("【" + transactionOrder.getTransactionTitle() + "】 生成")));
            return payBill;
        }
        return null;
    }


    /**
     * 入账收款单
     * @param transactionOrder
     * @return
     */
    public GatherBill enterTransactionGatherBill(TransactionOrder transactionOrder){
        TransactionBillOBV transactionBillOBV = transactionOrder.getBillParam();
        if (BillTypeEnum.收款单.equalsByCode(transactionBillOBV.getBillType())){
            return null;
        }
        GatherBill gatherBill = Global.mapperFacade.map(transactionBillOBV, GatherBill.class);
        gatherBill.generateIdentifier();
        gatherBill.setState(BillStateEnum.正常.getCode());
        gatherBill.setOnAccount(BillOnAccountEnum.未挂账.getCode());
        gatherBill.setRefundState(BillRefundStateEnum.未退款.getCode());
        gatherBill.setVerifyState(BillVerifyStateEnum.未核销.getCode());
        gatherBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        gatherBill.setCarriedState(BillCarriedStateEnum.未结转.getCode());
        gatherBill.setInvoiceState(BillInvoiceStateEnum.已开票.getCode());
        List<GatherDetail> gatherDetails = Global.mapperFacade.mapAsList(transactionBillOBV.getTransactionBillDetails(), GatherDetail.class);
        gatherDetails.forEach(gatherDetail -> {
            gatherDetail.setGatherBillId(gatherBill.getId());
            gatherDetail.setGatherBillNo(gatherBill.getBillNo());
        });
        gatherBillRepository.save(gatherBill);
        gatherDetailRepository.saveBatch(gatherDetails);
        transactionBillOBV.setBillId(gatherBill.getId());
        transactionBillOBV.setBillNo(gatherBill.getBillNo());
        return gatherBill;
    }

    /**
     * 更新交易信息
     * @param transactionOrder
     * @return
     */
    public boolean updateTransaction(TransactionOrder transactionOrder) {
        return transactionOrderRepository.updateById(transactionOrder);
    }

    /**
     * 批量更新
     * @param transactionOrders
     * @return
     */
    public boolean updateBatchTransaction(List<TransactionOrder> transactionOrders) {
        return transactionOrderRepository.updateBatchById(transactionOrders);
    }

    private TransactionF convertPaymentTransaction(TransactionOrder transactionOrder){
        TransactionF transactionF = new TransactionF();
        transactionF.setAmount(transactionOrder.getAmount());
        transactionF.setMchOrderNo(transactionOrder.getTransactionNo());
        String first100Characters = OrderTitleGet100FirstStringSplitter.getFirst100CharactersNoExc(transactionOrder.getTransactionTitle());
        log.info("OrderTitleGet100FirstStringSplitter->入参:{},出参->{}",transactionOrder.getTransactionTitle(),first100Characters);
        transactionF.setOrderTitle(first100Characters);
        LocalDateTime expireTime = transactionOrder.getExpireTime();
        transactionF.setExpireTime(Objects.nonNull(expireTime) ? expireTime : LocalDateTime.now().plusMinutes(15));
        transactionF.setGoods(new ArrayList<>());
        Payer payer = transactionOrder.getPayer();
        if (Objects.nonNull(payer)){
            PayerF payerF = new PayerF();
            payerF.setPayerId(payer.getPayerId());
            payerF.setPayerName(payer.getPayerName());
            payerF.setPhone(payer.getPayerPhone());
            payerF.setPayerAccount(payer.getPayerAccount());
            payerF.setPayNumber(payer.getPayNumber());
            payerF.setBackType(payer.getBackType());
            payerF.setBankNo(payer.getBankNo());
            transactionF.setPayer(payerF);
        }
        Payee payee = transactionOrder.getPayee();
        if (Objects.nonNull(payee)){
            PayeeF payeeF = new PayeeF();
            payeeF.setPayeeId(payee.getPayeeId());
            payeeF.setPayeeName(payee.getPayeeName());
            payeeF.setPhone(payee.getPayeePhone());
            payeeF.setPayeeAccount(payee.getPayeeAccount());
            payeeF.setPayeeBank(payee.getPayeeBank());
            payeeF.setBackType(payee.getBackType());
            payeeF.setBankNo(payee.getBankNo());
            transactionF.setPayee(payeeF);
        }
        Scene scene = transactionOrder.getScene();
        if (Objects.nonNull(scene)){
            SceneF sceneF = new SceneF();
            sceneF.setDeviceId(sceneF.getDeviceId());
            sceneF.setClientIp(scene.getClientIp());
            transactionF.setScene(sceneF);
        }
        transactionF.setNotifyUrl("wishare://wisharepay/mq");
        transactionF.setAttachParam(transactionOrder.getAttachParam());
        transactionF.setSysSource(SysSourceEnum.BPM系统.getCode());
        transactionF.setMethod((TransactionOrder.payMethod(transactionOrder.getPayWay(), transactionOrder.getPayChannel())));
        transactionF.setMchNo("");
        return transactionF;
    }

    /**
     * 处理交易回调
     * @param command 回到命令
     * @return
     */
    public boolean transactCallback(TransactionCallbackCommand command) {
        //获取根据订单号交易订单
        TransactionOrder transactionOrder = transactionOrderRepository.getByTransactionNo(command.getMchOrderNo());
        ErrorAssertUtil.notNullThrow403(transactionOrder, ErrorMessage.PAYMENT_TRADED_NOT_EXIST);
        boolean transactCallback = transactionOrder.transactCallback(command);
        return transactCallback ? transactionOrderRepository.updateById(transactionOrder) : false;
    }


    public TransactionOrder getTransactionOrderByNo(String transactionNo){
        return transactionOrderRepository.getByTransactionNo(transactionNo);
    }

    public TransactionOrder getTransactionOrderByBizNo(String bizTransactionNo){
        return transactionOrderRepository.getByBizTransactionNo(bizTransactionNo);
    }

    public List<TransactionOrder> getTransactionOrderByBizNos(List<String> bizTransactionNos){
        return transactionOrderRepository.getByBizTransactionNos(bizTransactionNos);
    }

}
