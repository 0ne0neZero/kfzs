package com.wishare.finance.domains.bill.aggregate.approve;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.*;
import com.wishare.bizlog.operator.Operator;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.event.BillRefundMqDetail;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.service.ChargeOverdueService;
import com.wishare.finance.domains.bill.support.GatherOnBillApproveListener;
import com.wishare.finance.domains.invoicereceipt.consts.enums.RefundMethodEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.RefundTradeMethodEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.OverdueStateEnum;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.payment.PaymentOrderClient;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.finance.infrastructure.remote.fo.payment.PayParamF;
import com.wishare.finance.infrastructure.remote.fo.payment.RefundRequestF;
import com.wishare.finance.infrastructure.remote.fo.payment.YYNetF;
import com.wishare.finance.infrastructure.remote.vo.payment.RefundV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.RedisHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.bill.consts.enums.GatherTypeEnum.应收;
import static com.wishare.finance.domains.bill.consts.enums.GatherTypeEnum.预收;
import static com.wishare.finance.domains.bill.service.GatherBillDomainService.getAdvanceBills;
import static com.wishare.finance.domains.bill.service.GatherBillDomainService.getReceivableBills;

/**
 * @author xujian
 * @date 2023/1/5
 * @Description: 收款单退款监听类
 */
@Slf4j
public class GatherRefundApproveListener implements GatherOnBillApproveListener<GatherBill> {
    /**
     * 冲销后生成待审核，未结算的账单
     */
    private final String RefundInitBill = "RefundInitBill";

    @Override
    public void onAgree(GatherBill gatherBill, BillApproveE billApprove) {

        // 获取收款单相关资源库
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        BillRefundRepository refundRepository = Global.ac.getBean(BillRefundRepository.class);

        // 获取收款明细
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(gatherBill.getId()), gatherBill.getSupCpUnitId());
        // 获取退款记录
        BillRefundE billRefundE = refundRepository.getByBillApproveId(billApprove.getId());
        // 记录已退款需要重新生成的账单
        final List<ReceivableBill> fullRefundBillList = new ArrayList<>();

        // 构建付款单
        PayBill payBill = generalPayBill(gatherBill, billRefundE.getRefundAmount(), PayTypeEnum.退款付款.getCode());
        // 获取三方审核人信息
        IdentityInfo identityInfo = JSON.parseObject(RedisHelper.getG("RemoteRefundIdentityInfo_" + billApprove.getBillId()), IdentityInfo.class);
        Operator operator = Objects.isNull(identityInfo) ? LogContext.getOperator() : new Operator(Optional.ofNullable(identityInfo.getTenantId()).orElse("系统默认"), Optional.of(identityInfo.getUserName()).orElse("系统默认"));
        // 收款单退款
        gatherBillRefund(gatherBill, billRefundE.getRefundAmount());
        // 收款明细退款处理
        List<PayDetail> payDetailListRec = gatherDetailRefund(gatherBill, gatherDetailList, billRefundE, gatherDetailRepository, payBill, billApprove, fullRefundBillList);
        // 生成对应的付款单
        savePaybillAndDetail(payBill, payDetailListRec);

        // 更新收款单关联的退款记录
        billRefundE.setRefundTime(LocalDateTime.now());
        billRefundE.setRefundNo(StringUtils.isBlank(billRefundE.getRefundNo()) ? IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20) : billRefundE.getRefundNo());
        billRefundE.setState(RefundStateEnum.已退款.getCode());
        billRefundE.setOperator(operator.getId());
        billRefundE.setOperatorName(operator.getName());

        // 涉及银联三方或者原路退款处理
        romoteRefundHandle(gatherBill, billRefundE, billRefundE.getRefundAmount());

        // 更新收款单关联的退款记录
        refundRepository.saveOrUpdate(billRefundE);

        // 退款后根据一些条件生成待审核，未结算的账单 1.是否全额退款
        if (CollectionUtils.isNotEmpty(fullRefundBillList)) {
            refundNewBillInfo(billApprove, fullRefundBillList);
        }

        // 日志记录
        BizLog.normal(String.valueOf(gatherBill.getId()), operator, LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", true)))
                        .option(new ContentOption(new PlainTextDataItem("退款金额为：", false)))
                        .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(billRefundE.getRefundAmount()), false), OptionStyle.normal()))
                        .option(new ContentOption(new PlainTextDataItem("元", false))));
        // 付款单日志记录
        BizLog.normal(String.valueOf(payBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.创建,
                new Content().option(new ContentOption(new PlainTextDataItem("账单退款生成", true)))
                        .option(new ContentOption(new PlainTextDataItem("退款账单：", false)))
                        .option(new ContentOption(new BusinessDataItem(gatherBill.getBillNo(), true, String.valueOf(gatherBill.getId()), BillTypeEnum.收款单.getValue()))));

    }

    /**
     * 已退款账单根据条件生成新账单 ，处理违约金账单退款
     * @param billApprove
     * @param fullRefundBillList
     */
    private void refundNewBillInfo(BillApproveE billApprove, List<ReceivableBill> fullRefundBillList) {
        for (ReceivableBill fullRefundBill : fullRefundBillList) {
            if (fullRefundBill.getBillType().equals(BillTypeEnum.应收账单.getCode())){
                if (fullRefundBill.getOverdue().equals(OverdueStateEnum.无违约金.getCode())){
                    reversedInitBillNew(fullRefundBill, BillTypeEnum.应收账单.getCode());
                }else {
                    if (RefundInitBill.equals(billApprove.getExtField1())){
                        reversedInitBillNew(fullRefundBill, BillTypeEnum.应收账单.getCode());
                    }else {
                        ChargeOverdueService chargeOverdueService = Global.ac.getBean(ChargeOverdueService.class);
                        ChargeOverdueE overdueE = chargeOverdueService.getOverdueByBillId(fullRefundBill.getId());
                        ReceivableBillRepository billRepository = Global.ac.getBean(ReceivableBillRepository.class);
                        billRepository.update(new UpdateWrapper<ReceivableBill>().eq("id",overdueE.getRefBillId())
                                .eq("sup_cp_unit_id",overdueE.getCommunityId()).set("ext_field1",1));
                        chargeOverdueService.deleteByBillIds(List.of(fullRefundBill.getId()));
                    }
                }
            }else if (fullRefundBill.getBillType().equals(BillTypeEnum.临时收费账单.getCode()) &&
                    RefundInitBill.equals(billApprove.getExtField1())){
                reversedInitBillNew(fullRefundBill, BillTypeEnum.临时收费账单.getCode());
            }
        }
    }

    /**
     * 三方退款对接支付端处理
     *
     * @param gatherBill       收款单
     * @param billRefundE      退款记录
     * @param refundAmount     退款金额
     */
    private void romoteRefundHandle(GatherBill gatherBill, BillRefundE billRefundE, Long refundAmount) {
        if (StringUtils.isNotBlank(billRefundE.getRefundMethod()) && billRefundE.getRefundMethod().equals(RefundMethodEnum.原路退款.getCode())){
            // 【原路退款】退款方式处理
            routeReturn(gatherBill, refundAmount, billRefundE);
        }
    }

    /**
     * 【原路退款】退款方式处理
     *
     * @param gatherBill   收款单
     * @param refundAmount 退款金额
     * @param billRefundE  退款记录单
     */
    private void routeReturn(GatherBill gatherBill, Long refundAmount, BillRefundE billRefundE) {
        // 判断当前收款单是否符合退款方式
        if (!((gatherBill.getPayWay().equals(SettleWayChannelEnum.微信支付.getType()) || gatherBill.getPayWay().equals(SettleWayChannelEnum.郑州银行.getType()))
                && (Objects.equals(gatherBill.getPayChannel(), SettleWayChannelEnum.微信支付.getCode())
                || Objects.equals(gatherBill.getPayChannel(), SettleWayChannelEnum.郑州银行.getCode())
                || Objects.equals(gatherBill.getPayChannel(), SettleWayChannelEnum.支付宝.getCode())))) {
            BizException.throw400("当前收款单状态不支持原路退回方式！");
        }
        log.info("本次原路退款涉及退款金额（分），退款渠道：{}{}", refundAmount, gatherBill.getPayChannel());

        PaymentOrderClient paymentOrderClient = Global.ac.getBean(PaymentOrderClient.class);

        // 构建参数
        RefundRequestF param = new RefundRequestF().setChannelOrderNo(gatherBill.getTradeNo()).setPaySource(gatherBill.getPaySource())
                .setMchRefundNo(billRefundE.getRefundNo()).setMethod(RefundTradeMethodEnum.valueOfByCode(gatherBill.getPayChannel()).getDes())
                .setRefundAmount(refundAmount).setSysSource(SysSourceEnum.收费系统.getCode());
        log.info("本次原路退款调用参数：{}", JSON.toJSONString(param));
        RefundV refundResult = paymentOrderClient.refund(param);
        log.info("本次原路退款调用结果：{}", JSON.toJSONString(refundResult));
        if (ObjectUtils.anyNull(refundResult, refundResult.getState())) {
            throw BizException.throw400("支付系统忙碌中，稍后重试");
        } else if (refundResult.getState() == 9) {
            throw BizException.throw400(refundResult.getErrMsg());
        } else if (refundResult.getState() != 7) {
            throw BizException.throw400("正在退款中，请稍后查看结果");
        } else {
            billRefundE.setOutRefundNo(refundResult.getPayNo());
            billRefundE.setRefundChannel(gatherBill.getPayChannel());
            billRefundE.setRefundWay(Const.State._0);
        }
    }


    /**
     * 银联退款金额处理
     *
     * @param gatherBill       收款单
     * @param gatherDetailList 收款明细
     * @param billRefundE      退款记录
     */
    private void unionPayOpr(GatherBill gatherBill, List<GatherDetail> gatherDetailList, BillRefundE billRefundE) {
        // 获取本次银联退款金额（目前银联只存在单次全缴全退）
        long refundMoney = gatherDetailList.stream().filter(a -> a.getPayWay() == 4 || a.getPayWay() == 0).mapToLong(GatherDetail::getRefundAmount).sum();
        log.info("本次退款涉及银联退款金额（分）为：{}", refundMoney);
        PaymentOrderClient paymentOrderClient = Global.ac.getBean(PaymentOrderClient.class);

        // 获取商户号
        String outMchNo = getStatutoryBodyInfo(gatherBill, gatherDetailList);
        ErrorAssertUtil.isTrueThrow402(Objects.nonNull(outMchNo), ErrorMessage.STATUTORY_BODY_NO_EXISTS);
//        MerchantAppDetailV merchantV = paymentOrderClient.getDetailByOutMchNo(outMchNo, 0);
//        if(Objects.isNull(merchantV)){
//            throw BizException.throw400("商户应用不存在");
//        }

        // 构建参数
        String method = RefundTradeMethodEnum.银联.getDes();
        RefundRequestF param = new RefundRequestF().setPayNo(gatherBill.getTradeNo()).setPaySource(gatherBill.getPaySource()).setMchRefundNo(billRefundE.getRefundNo()).setMethod(method)
                .setRefundAmount(refundMoney).setBusinessId(gatherBill.getSupCpUnitId()).setSysSource(SysSourceEnum.收费系统.getCode())
                .setPayParam(new PayParamF().setYyNetPay(new YYNetF().setTid(gatherBill.getDeviceNo()).setMid(gatherBill.getMchNo())));
        log.info("银联退款调用参数：{}", JSON.toJSONString(param));
        RefundV refundResult = paymentOrderClient.refund(param);
        log.info("银联退款调用结果：{}", JSON.toJSONString(refundResult));
        if (ObjectUtils.anyNull(refundResult, refundResult.getState())) {
            throw BizException.throw400("银联系统忙碌中，稍后重试");
        } else if (refundResult.getState() == 9) {
            throw BizException.throw400(refundResult.getErrMsg());
        } else if (refundResult.getState() != 7) {
            throw BizException.throw400("正在退款中，请稍后查看结果");
        } else {
            billRefundE.setOutRefundNo(refundResult.getPayNo());
            billRefundE.setRefundChannel(SettleChannelEnum.银联.getCode());
            billRefundE.setRefundWay(Const.State._0);
        }
    }

    /**
     * 根据收款单或账单获取法定单位id
     *
     * @param gatherBill       收款单
     * @param gatherDetailList 收款明细
     * @return {@link String}  法定单位id
     */
    private String getStatutoryBodyInfo(GatherBill gatherBill, List<GatherDetail> gatherDetailList) {
        if (Objects.nonNull(gatherBill.getStatutoryBodyId())) {
            return gatherBill.getStatutoryBodyId().toString();
        }
        if (gatherDetailList.stream().noneMatch(a -> 预收.getCode() == a.getGatherType())) {
            List<ReceivableBill> receivableBillList = getReceivableList(gatherDetailList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()), gatherBill.getSupCpUnitId());
            return Objects.nonNull(receivableBillList.get(0).getStatutoryBodyId()) ? receivableBillList.get(0).getStatutoryBodyId().toString() : null;
        } else {
            List<AdvanceBill> advanceBillList = getAdvanceList(gatherDetailList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()));
            return Objects.nonNull(advanceBillList.get(0).getStatutoryBodyId()) ? advanceBillList.get(0).getStatutoryBodyId().toString() : null;
        }
    }

    /**
     * 账单已退款后 若原账单为违约金账单 则换绑关联账单
     */
    private void overdueBillBindNew(){

    }


    /**
     * 全额退款生成已审核，未结算的账单
     *
     * @param bill
     * @param code
     */
    private void reversedInitBillNew(ReceivableBill bill, int code) {
        Long billId = null;
        if (BillTypeEnum.应收账单.getCode() == code) {
            ReceivableBill receivableBill = Global.mapperFacade.map(bill, ReceivableBill.class);
            receivableBill.setId(null);
            receivableBill.setBillNo(null);
            receivableBill.setPayTime(null);
            receivableBill.setPayInfos(null);
            receivableBill.init();
            receivableBill.resetState();
            receivableBill.resetAmount(bill.getTotalAmount());
            receivableBill.resetOperatorInfo();
            receivableBill.setSource("财务中心-全额退款新增");
            receivableBill.setDescription(BillTypeEnum.valueOfByCode(bill.getBillType()).getValue() + bill.getBillNo() + "收款明细退款");
            receivableBill.setApprovedState(BillApproveStateEnum.已审核.getCode());//冲销后生成已审核，未结算的应收账单
            receivableBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(receivableBill.getRoomId()));
            ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
            receivableBillRepository.save(receivableBill);
            billId = receivableBill.getId();
            ReceivableBill newBill = receivableBillRepository.getById(billId);
            // 若原账单为违约金账单，则将原先违约金管理记录绑定为最新的账单，
            ChargeOverdueService chargeOverdueService = Global.ac.getBean(ChargeOverdueService.class);
            // 获取老账单的违约金管理数据
            ChargeOverdueE overdueE = chargeOverdueService.getOverdueByBillId(bill.getId());
            if (Objects.nonNull(overdueE)){
                // 将其绑定账单信息切换
                overdueE.setBillId(newBill.getId());
                overdueE.setBillNo(newBill.getBillNo());
                overdueE.setBillSettleState(newBill.getSettleState());
                chargeOverdueService.updateById(overdueE);
            }
        } else if (BillTypeEnum.临时收费账单.getCode() == code) {
            TemporaryChargeBill temporaryChargeBill = Global.mapperFacade.map(bill, TemporaryChargeBill.class);
            temporaryChargeBill.setId(null);
            temporaryChargeBill.setBillNo(null);
            temporaryChargeBill.setPayTime(null);
            temporaryChargeBill.setPayInfos(null);
            temporaryChargeBill.init();
            temporaryChargeBill.resetState();
            temporaryChargeBill.resetAmount(bill.getTotalAmount());
            temporaryChargeBill.resetOperatorInfo();
            temporaryChargeBill.setSource("财务中心-全额退款新增");
            temporaryChargeBill.setDescription(BillTypeEnum.valueOfByCode(bill.getBillType()).getValue() + bill.getBillNo() + "收款明细退款");
            temporaryChargeBill.setApprovedState(BillApproveStateEnum.已审核.getCode());//冲销后生成已审核，未结算的应收账单
            temporaryChargeBill.setPath(Global.ac.getBean(SpacePermissionAppService.class).getSpacePath(temporaryChargeBill.getRoomId()));
            TemporaryChargeBillRepository temporaryChargeBillRepository = Global.ac.getBean(TemporaryChargeBillRepository.class);
            temporaryChargeBillRepository.save(temporaryChargeBill);
            billId = temporaryChargeBill.getId();
        }

        if (Objects.nonNull(billId)) {
            //日志记录
            BizLog.normal(String.valueOf(billId), LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("全额退款生成已审核，未结算的账单", true))));
        }
    }

    /**
     * 退款更新收款详情数据
     *
     * @param gatherBill             收款单
     * @param gatherDetailList       detail数据 已经按时间倒序排序
     * @param billRefundE            退款记录
     * @param gatherDetailRepository gatherDetailRepository
     * @param payBill                收款单付款单列表信息
     * @param billApprove            本次退款审核记录
     * @param fullRefundBillList     记录已退款需要重新生成的账单
     * @return {@link PayDetail} 账单付款单需生成列表
     */
    private List<PayDetail> gatherDetailRefund(GatherBill gatherBill, List<GatherDetail> gatherDetailList, BillRefundE billRefundE, GatherDetailRepository gatherDetailRepository
            , PayBill payBill, BillApproveE billApprove, List<ReceivableBill> fullRefundBillList) {
        // 结果集创建
        List<PayDetail> payDetailList = Lists.newArrayList();
        List<ReceivableBill> refundBillList = new ArrayList<>();
        List<AdvanceBill> refundAdBillList = new ArrayList<>();
        if (CollectionUtils.isEmpty(gatherDetailList)) {
            return payDetailList;
        }
        // 统计已退减金额
        Long refundedMoney = 0L;
        // 获取明细关联的应收账单列表
        List<ReceivableBill> receivableBillList = getReceivableList(gatherDetailList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()), gatherBill.getSupCpUnitId());
        Map<Long, List<ReceivableBill>> billMap = receivableBillList.stream().collect(Collectors.groupingBy(ReceivableBill::getId));
        // 获取明细关联的预收账单列表
        List<AdvanceBill> advanceBillList = getAdvanceList(gatherDetailList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()));
        Map<Long, List<AdvanceBill>> adBillMap = advanceBillList.stream().collect(Collectors.groupingBy(AdvanceBill::getId));
        // 不同方式收款明细处理
        handleDiffWayRefundDetail(gatherBill, gatherDetailList, billRefundE, payBill, billApprove, fullRefundBillList
                , payDetailList, refundBillList, refundAdBillList, refundedMoney, billMap, adBillMap);
        // 批量更新收款明细数据
        for (GatherDetail gatherDetail : gatherDetailList) {
            gatherDetailRepository.update(gatherDetail, new UpdateWrapper<GatherDetail>().eq("id", gatherDetail.getId())
                    .eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
        }
        //更新对应的应收账单
        ReceivableBillRepository billRepository = Global.ac.getBean(ReceivableBillRepository.class);
        for (ReceivableBill receivableBill : refundBillList) {
            billRepository.update(receivableBill, new UpdateWrapper<ReceivableBill>().eq("id", receivableBill.getId())
                    .eq("sup_cp_unit_id", receivableBill.getSupCpUnitId()));
        }
        //更新对应的预收账单
        Global.ac.getBean(AdvanceBillRepository.class).updateBatchById(refundAdBillList);

        return payDetailList;
    }

    /**
     * 不同方式收款明细处理
     */
    private void handleDiffWayRefundDetail(GatherBill gatherBill, List<GatherDetail> gatherDetailList, BillRefundE billRefundE, PayBill payBill, BillApproveE billApprove,List<ReceivableBill> fullRefundBillList, List<PayDetail> payDetailList,
                                           List<ReceivableBill> refundBillList, List<AdvanceBill> refundAdBillList, Long refundedMoney, Map<Long, List<ReceivableBill>> billMap, Map<Long, List<AdvanceBill>> adBillMap) {
        // 本次退款金额
        Long refundAmount = billRefundE.getRefundAmount();
        // 如果选用了具体的明细退款 则根据明细退款
        if (Objects.nonNull(gatherBill.getGatherMap())) {
            // 获取明细退款对应map
            Map<Long, BigDecimal> detailMap = gatherBill.getGatherMap().get(gatherBill.getId());
            // 过滤对应的收款明细
            List<GatherDetail> detailList = gatherDetailList.stream().filter(a -> detailMap.containsKey(a.getId())).collect(Collectors.toList());
            for (GatherDetail gatherDetail : detailList) {
                // 获取该账单可退款金额
                Long canRefundAmount = gatherDetail.getCanRefundAmount();
                // 获取本次申请退款金额
                Long applyRefundAmount = detailMap.get(gatherDetail.getId()).multiply(Const.BIG_DECIMAL_HUNDRED).longValue();
                // 校验金额是否充足
                Assert.validate(() -> canRefundAmount >= applyRefundAmount, () -> BizException.throw400("账单编号为" + gatherDetail.getRecBillNo() + "退款金额不足"));
                // 对应明细发送退款
                gatherDetail.setRefundAmount(gatherDetail.getRefundAmount() + applyRefundAmount);
                // 处理本条明细对应的账单主表进行退款
                handleRecBillRefund(applyRefundAmount, payBill, billApprove, billRefundE, payDetailList, refundBillList, refundAdBillList, billMap, adBillMap, gatherDetail, fullRefundBillList);
            }
        } else {
            // 否则默认根据时间倒序决定退款顺序
            for (GatherDetail gatherDetail : gatherDetailList) {
                // 本次退款金额全部用光时无需再遍历
                if (refundAmount.equals(refundedMoney)) {
                    break;
                }
                // 获取该笔收款明细的具体剩余收款金额
                long canRefundAmount = gatherDetail.getPayAmount() - gatherDetail.getRefundAmount() - gatherDetail.getCarriedAmount() - gatherDetail.getDeductionAmount();
                // 如果收款明细可退金额不足换下一个
                if (canRefundAmount <= 0) {
                    continue;
                }
                // 如果本条明细剩余可退金额小于等于还应退金额
                if (canRefundAmount <= refundAmount - refundedMoney) {
                    // 将收款明细剩余可退金额作为退款金额
                    gatherDetail.setRefundAmount(gatherDetail.getRefundAmount() + canRefundAmount);
                    // 处理本条明细对应的账单主表进行退款
                    handleRecBillRefund(canRefundAmount, payBill, billApprove, billRefundE, payDetailList, refundBillList, refundAdBillList, billMap, adBillMap, gatherDetail, fullRefundBillList);
                    // 统计已退减金额增加本次退款金额
                    refundedMoney += canRefundAmount;
                } else {
                    // 如果本条明细剩余可退金额大于还应退金额 直接将剩余未分配金额全部分配即可
                    gatherDetail.setRefundAmount(gatherDetail.getRefundAmount() + (refundAmount - refundedMoney));
                    // 处理本条明细对应的账单主表进行退款
                    handleRecBillRefund((refundAmount - refundedMoney), payBill, billApprove, billRefundE, payDetailList, refundBillList, refundAdBillList, billMap, adBillMap, gatherDetail, fullRefundBillList);
                    break;
                }
            }
        }
    }

    /**
     * 处理金额退款
     *
     * @param refundAmount       账单应退款金额
     * @param payBill            付款单
     * @param billApprove        审核记录
     * @param billRefundE        退款记录
     * @param payDetailList      收集付款单
     * @param refundBillList     收集应退账单列表
     * @param refundAdBillList   收集应退预收列表
     * @param fullRefundBillList fullRefundBillList
     */
    private void handleRecBillRefund(Long refundAmount, PayBill payBill, BillApproveE billApprove, BillRefundE billRefundE, List<PayDetail> payDetailList, List<ReceivableBill> refundBillList, List<AdvanceBill> refundAdBillList
            , Map<Long, List<ReceivableBill>> billMap, Map<Long, List<AdvanceBill>> adBillMap, GatherDetail gatherDetail, List<ReceivableBill> fullRefundBillList) {
        if (应收.getCode() == gatherDetail.getGatherType()) {
            // 处理应收/临时账单退款
            List<ReceivableBill> billList = billMap.get(gatherDetail.getRecBillId());
            Assert.validate(() -> CollectionUtils.isNotEmpty(billList), () -> BizException.throw400(gatherDetail.getRecBillId()+"收款明细关联账单未能找到"));
            ReceivableBill receivableBill = billList.get(0);
            handleBillRefund(refundAmount, receivableBill, payBill, billApprove, billRefundE, payDetailList);
            refundBillList.add(receivableBill);
            Optional.ofNullable(receivableBill.getRefundState()).filter(BillRefundStateEnum.已退款::equalsByCode)
                    .ifPresent(a -> fullRefundBillList.add(receivableBill));
        } else {
            // 处理预收账单退款
            List<AdvanceBill> adBillList = adBillMap.get(gatherDetail.getRecBillId());
            Assert.validate(() -> CollectionUtils.isNotEmpty(adBillList), () -> BizException.throw400(gatherDetail.getRecBillId()+"收款明细关联预收账单未能找到"));
            AdvanceBill advanceBill = adBillList.get(0);
            handleAdBillRefund(refundAmount, advanceBill, payBill, billApprove, billRefundE, payDetailList);
            refundAdBillList.add(advanceBill);
        }
    }

    /**
     * 应收账单退款
     */
    private void handleBillRefund(Long refundAmount, ReceivableBill receivableBill, PayBill payBill, BillApproveE billApprove, BillRefundE billRefundE, List<PayDetail> payDetailList) {
        //可退款金额 = 结算金额 - 结转金额 - 退款金额（上次退款金额） - 扣款金额
        Long remainingRefundAmount = receivableBill.getSettleAmount() - receivableBill.getCarriedAmount() - receivableBill.getRefundAmount() - receivableBill.getDeductionAmount();
        if (remainingRefundAmount >= refundAmount) {
            receivableBill.setRefundAmount(receivableBill.getRefundAmount() + refundAmount);
            //应收账单需要生成已经审核通过的退款记录
            saveReceivableBillRefund(receivableBill, refundAmount, billApprove, billRefundE);
            //发送退款mq
            refundMqRec(receivableBill, refundAmount);
            payDetailList.add(generalPayDetailRec(payBill, receivableBill, refundAmount));
        } else {
            if (remainingRefundAmount != 0L) {
                receivableBill.setRefundAmount(receivableBill.getRefundAmount() + remainingRefundAmount);
                //发送退款mq
                refundMqRec(receivableBill, remainingRefundAmount);
                payDetailList.add(generalPayDetailRec(payBill, receivableBill, remainingRefundAmount));
            }
            //应收账单需要生成已经审核通过的退款记录
            saveReceivableBillRefund(receivableBill, remainingRefundAmount, billApprove, billRefundE);
        }
        receivableBill.setRefundState(handleRefundState2(receivableBill.getSettleAmount() - receivableBill.getCarriedAmount() , receivableBill.getRefundAmount()));
        receivableBill.refresh();
    }

    /**
     * 预收账单退款
     */
    private void handleAdBillRefund(Long refundAmount, AdvanceBill advanceBill, PayBill payBill, BillApproveE billApprove, BillRefundE billRefundE, List<PayDetail> payDetailList) {
        //可退款金额 = 结算金额 - 结转金额 - 退款金额（上次退款金额）- 冲销金额
        Long remainingRefundAmount = advanceBill.getRemainingCarriedAmount();
        if (remainingRefundAmount >= refundAmount) {
            advanceBill.setRefundAmount(advanceBill.getRefundAmount() + refundAmount);
            //应收账单需要生成已经审核通过的退款记录
            saveAdvanceBillRefund(advanceBill, refundAmount, billApprove, billRefundE);
            //发送退款mq
            refundMqAdv(advanceBill, refundAmount);
            payDetailList.add(generalPayDetailAdv(payBill, advanceBill, refundAmount));
        } else {
            if (remainingRefundAmount != 0L) {
                advanceBill.setRefundAmount(advanceBill.getRefundAmount() + remainingRefundAmount);
                //发送退款mq
                refundMqAdv(advanceBill, remainingRefundAmount);
                payDetailList.add(generalPayDetailAdv(payBill, advanceBill, refundAmount));
            }
            //应收账单需要生成已经审核通过的退款记录
            saveAdvanceBillRefund(advanceBill, remainingRefundAmount, billApprove, billRefundE);
        }
        advanceBill.setRefundState(handleRefundState2(advanceBill.getReceivableAmount(), advanceBill.getRefundAmount()));
        advanceBill.refresh();
    }

    @Override
    public void onRefuse(GatherBill gatherBill, BillApproveE billApprove, String reason) {
        //根据收款单退款
        IdentityInfo identityInfo = JSON.parseObject(RedisHelper.getG("RemoteRefundIdentityInfo_" + billApprove.getBillId()), IdentityInfo.class);
        Operator operator = Objects.isNull(identityInfo) ? LogContext.getOperator() : new Operator(Optional.ofNullable(identityInfo.getTenantId()).orElse("系统默认"), Optional.of(identityInfo.getUserName()).orElse("系统默认"));
        BizLog.normal(String.valueOf(gatherBill.getId()), operator, LogObject.账单, LogAction.审核拒绝, new Content());
        // 对收款单包含的单退款申请日志记录
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(gatherBill.getId()), gatherBill.getSupCpUnitId());
        if (CollectionUtils.isNotEmpty(gatherDetailList)) {
            Long refundAmount = gatherBill.getRefundAmount();
            if (gatherDetailList.get(0).getGatherType().equals(0)) {
                // 当该收款单包含为应收临时账单
                List<ReceivableBill> receivableBillList = getReceivableList(gatherDetailList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()), gatherBill.getSupCpUnitId());
                // 当前收款单总可退款金额
                for (ReceivableBill receivableBill : receivableBillList) {
                    // 对应子账单实际退款金额计算
                    refundAmount = getaLong(refundAmount, receivableBill.getSettleAmount(), receivableBill.getId(), operator, reason);
                    if (refundAmount == null) {
                        break;
                    }
                    ;
                }
            } else {
                // 当该收款单包含为预收账单情况
                List<AdvanceBill> advanceBillList = getAdvanceList(gatherDetailList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList()));
                for (AdvanceBill advanceBill : advanceBillList) {
                    // 对应子账单实际退款金额计算
                    refundAmount = getaLong(refundAmount, advanceBill.getSettleAmount(), advanceBill.getId(), operator, reason);
                    if (refundAmount == null) {
                        break;
                    }
                    ;
                }
            }
        }
        // 更新收款单关联的退款记录
        BillRefundRepository refundRepository = Global.ac.getBean(BillRefundRepository.class);
        BillRefundE billRefundE = refundRepository.getByBillApproveId(billApprove.getId());
        billRefundE.setRefundTime(LocalDateTime.now());
        billRefundE.setState(RefundStateEnum.未生效.getCode());
        billRefundE.setOperator(operator.getId());
        billRefundE.setOperatorName(operator.getName());
        refundRepository.saveOrUpdate(billRefundE);
    }

    @Nullable
    private Long getaLong(Long refundAmount, Long settleAmount, Long id, Operator operator, String reason) {
        Long finalMoney = settleAmount <= refundAmount ? settleAmount : refundAmount;
        Content content = new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", true)));
        if (StringUtils.isNotBlank(reason)) {
            content = content.option(new ContentOption(new PlainTextDataItem("拒绝原因： " + reason, false)));
        }
        BizLog.normal(String.valueOf(id), operator, LogObject.账单, LogAction.审核拒绝, content);
        if (refundAmount.equals(finalMoney)) {
            return null;
        }
        refundAmount = BigDecimal.valueOf(refundAmount).subtract(BigDecimal.valueOf(settleAmount)).longValue();
        return refundAmount;
    }


    /**
     * 保存对应的付款单
     */
    private void savePaybillAndDetail(PayBill payBill, List<PayDetail> payDetailList) {
        Global.ac.getBean(PayBillRepository.class).save(payBill);
        Global.ac.getBean(PayDetailRepository.class).saveBatch(payDetailList);
        //账单日志记录
        BizLog.normal(String.valueOf(payBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成,
                new Content().option(new PlainTextDataItem("退款生成付款单")));
    }

    /**
     * 根据预收单ids获取预收单
     *
     * @param advanceIds
     * @return
     */
    private List<AdvanceBill> getAdvanceList(List<Long> advanceIds) {
        //根据收费结束时间和创建时间排序
        return getAdvanceBills(advanceIds);
    }

    /**
     * 获取应收账账单
     *
     * @param receivableBillIds
     * @return
     */
    private List<ReceivableBill> getReceivableList(List<Long> receivableBillIds, String supCpUnitId) {
        //根据收费结束时间和创建时间排序
        return getReceivableBills(receivableBillIds, supCpUnitId);
    }

    /**
     * 保存应收账单的退款记录
     */
    private void saveReceivableBillRefund(ReceivableBill receivableBill, Long refundAmount, BillApproveE billApproveE, BillRefundE billRefund) {
        BillRefundE billRefundE = new BillRefundE();
        billRefundE.setBillId(receivableBill.getId());
        billRefundE.setBillType(BillTypeEnum.应收账单.getCode());
        billRefundE.setRefundNo(IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20));
        billRefundE.setRefundChannel(SettleChannelEnum.其他.getCode());
        billRefundE.setRefundWay(SettleWayEnum.线上.getCode());
        billRefundE.setRefundMethod(billRefund.getRefundMethod());
        billRefundE.setFileInfo(billRefund.getFileInfo());
        billRefundE.setRefundAmount(refundAmount);
        billRefundE.setRefundTime(LocalDateTime.now());
        billRefundE.setBillApproveId(billApproveE.getId());
        billRefundE.setApproveTime(LocalDateTime.now());
        billRefundE.setRefunderType(receivableBill.getPayerType());
        billRefundE.setRefunderId(billRefund.getRefunderId());
        billRefundE.setRefunderName(billRefund.getRefunderName());
        billRefundE.setState(RefundStateEnum.已退款.getCode());
        billRefundE.setChargeStartTime(receivableBill.getStartTime());
        billRefundE.setChargeEndTime(receivableBill.getEndTime());
        billRefundE.setRemark(billApproveE.getReason());
        billRefundE.setInferenceState(BillInferStateEnum.未推凭.getCode());
        Global.ac.getBean(BillRefundRepository.class).save(billRefundE);
        // 三方审核通过回调时，审批人为三方最后负责人
        IdentityInfo identityInfo = JSON.parseObject(RedisHelper.getG("RemoteRefundIdentityInfo_" + billApproveE.getBillId()), IdentityInfo.class);
        if (refundAmount > 0L) {
            sendLog(identityInfo, receivableBill.getId());
        }
    }

    /**
     * 保存预收单的退款记录
     *
     * @param advanceBill
     * @param refundAmount
     * @param billApproveE
     */
    private void saveAdvanceBillRefund(AdvanceBill advanceBill, Long refundAmount, BillApproveE billApproveE, BillRefundE billRefund) {
        BillRefundE billRefundE = new BillRefundE();
        billRefundE.setBillId(advanceBill.getId());
        billRefundE.setBillType(BillTypeEnum.预收账单.getCode());
        billRefundE.setRefundNo(IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20));
        billRefundE.setRefundChannel(SettleChannelEnum.其他.getCode());
        billRefundE.setRefundWay(SettleWayEnum.线上.getCode());
        billRefundE.setRefundMethod(billRefund.getRefundMethod());
        billRefundE.setFileInfo(billRefund.getFileInfo());
        billRefundE.setRefundAmount(refundAmount);
        billRefundE.setRefundTime(LocalDateTime.now());
        billRefundE.setBillApproveId(billApproveE.getId());
        billRefundE.setApproveTime(LocalDateTime.now());
        billRefundE.setRefunderType(advanceBill.getPayerType());
        billRefundE.setRefunderId(billRefund.getRefunderId());
        billRefundE.setRefunderName(billRefund.getRefunderName());
        billRefundE.setState(RefundStateEnum.已退款.getCode());
        billRefundE.setChargeStartTime(advanceBill.getStartTime());
        billRefundE.setChargeEndTime(advanceBill.getEndTime());
        billRefundE.setRemark(billApproveE.getReason());
        billRefundE.setInferenceState(BillInferStateEnum.未推凭.getCode());
        Global.ac.getBean(BillRefundRepository.class).save(billRefundE);
        //账单日志记录
        IdentityInfo identityInfo = JSON.parseObject(RedisHelper.getG("RemoteRefundIdentityInfo_"), IdentityInfo.class);
        if (refundAmount > 0L) {
            sendLog(identityInfo, advanceBill.getId());
        }
    }

    /**
     * 审核退款通过回调日志
     *
     * @param identityInfo 操作人
     * @param id           账单id
     */
    private void sendLog(IdentityInfo identityInfo, Long id) {
        Operator operator = Objects.isNull(identityInfo) ? LogContext.getOperator() : new Operator(Optional.ofNullable(identityInfo.getTenantId()).orElse("系统默认"), Optional.of(identityInfo.getUserName()).orElse("系统默认"));
        BizLog.normal(String.valueOf(id), operator, LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", false))));

    }

    /**
     * 构建付款单
     */
    private PayBill generalPayBill(GatherBill gatherBill, Long refundAmount, Integer payType) {
        PayBill payBill = new PayBill();
        payBill.generateIdentifier();
        payBill.setPayType(payType);
        payBill.setRefundState(0);
        payBill.setDiscountAmount(0L);
        payBill.setRefundAmount(0L);
        payBill.setCarriedAmount(0L);
        payBill.setTotalAmount(refundAmount);
        payBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
        payBill.setPayTime(LocalDateTime.now());
        payBill.setPayeeId(gatherBill.getPayerId());
        payBill.setPayeeName(gatherBill.getPayerName());
        payBill.setPayerId(gatherBill.getPayeeId());
        payBill.setPayerName(gatherBill.getPayeeName());
        payBill.setDescription("退款");
        payBill.setPayChannel(SettleChannelEnum.其他.getCode());
        payBill.setPayWay(SettleWayEnum.线上.getCode());
        payBill.setStartTime(gatherBill.getStartTime());
        payBill.setEndTime(gatherBill.getEndTime());
        payBill.setStatutoryBodyId(gatherBill.getStatutoryBodyId());
        payBill.setStatutoryBodyName(gatherBill.getStatutoryBodyName());
        payBill.setSbAccountId(gatherBill.getSbAccountId());
        payBill.setSysSource(gatherBill.getSysSource());
        return payBill;
    }

    /**
     * 构建应收账单的付款单明细
     */
    private PayDetail generalPayDetailRec(PayBill payBill, ReceivableBill bill, Long refundAmount) {
        PayDetail payDetail = new PayDetail();
        payDetail.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.PAY_DETAIL));
        payDetail.setPayBillId(payBill.getId());
        payDetail.setPayBillNo(payBill.getBillNo());
        payDetail.setPayableBillId(bill.getId());
        payDetail.setCostCenterId(bill.getCostCenterId());
        payDetail.setCostCenterName(bill.getCostCenterName());
        payDetail.setChargeItemId(bill.getChargeItemId());
        payDetail.setChargeItemName(bill.getChargeItemName());
        payDetail.setPayChannel(SettleChannelEnum.其他.getCode());
        payDetail.setPayWay(SettleWayEnum.线上.getCode());
        payDetail.setRecPayAmount(bill.getActualUnpayAmount());
        payDetail.setPayAmount(refundAmount);
        payDetail.setPayerType(bill.getPayerType());
        payDetail.setPayerId(bill.getPayeeId());
        payDetail.setPayerName(bill.getPayeeName());
        payDetail.setPayeeId(bill.getPayerId());
        payDetail.setPayeeName(bill.getPayerName());
        payDetail.setPayTime(LocalDateTime.now());
        payDetail.setSupCpUnitId(bill.getCommunityId());
        payDetail.setSupCpUnitName(bill.getCommunityName());
        payDetail.setCpUnitId(bill.getRoomId());
        payDetail.setCpUnitName(bill.getRoomName());
        return payDetail;
    }

    /**
     * 构建应收账单的付款单明细
     */
    private PayDetail generalPayDetailAdv(PayBill payBill, AdvanceBill bill, Long refundAmount) {
        PayDetail payDetail = new PayDetail();
        payDetail.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.PAY_DETAIL));
        payDetail.setPayBillId(payBill.getId());
        payDetail.setPayBillNo(payBill.getBillNo());
        payDetail.setPayableBillId(bill.getId());
        payDetail.setCostCenterId(bill.getCostCenterId());
        payDetail.setCostCenterName(bill.getCostCenterName());
        payDetail.setChargeItemId(bill.getChargeItemId());
        payDetail.setChargeItemName(bill.getChargeItemName());
        payDetail.setPayChannel(SettleChannelEnum.其他.getCode());
        payDetail.setPayWay(SettleWayEnum.线上.getCode());
        payDetail.setRecPayAmount(bill.getActualUnpayAmount());
        payDetail.setPayAmount(refundAmount);
        payDetail.setPayerType(bill.getPayerType());
        payDetail.setPayerId(bill.getPayeeId());
        payDetail.setPayerName(bill.getPayeeName());
        payDetail.setPayeeId(bill.getPayerId());
        payDetail.setPayeeName(bill.getPayerName());
        payDetail.setPayTime(LocalDateTime.now());
        payDetail.setSupCpUnitId(bill.getCommunityId());
        payDetail.setSupCpUnitName(bill.getCommunityName());
        payDetail.setCpUnitId(bill.getRoomId());
        payDetail.setCpUnitName(bill.getRoomName());
        return payDetail;
    }

    /**
     * 发送退款mq
     * <p>
     * 一笔账单进行退款和结转时，优先去退款/结转未开票的金额。其次若一笔账单开了多笔票，按时间降序顺序进行票据的红冲/作废/回收
     *
     * @param bill         账单
     * @param refundAmount 当前退款金额
     */
    private void refundMqRec(ReceivableBill bill, Long refundAmount) {
        Long billRefundAmount = bill.getRefundAmount() - refundAmount;//账单退款金额
        Long billInvoiceAmount = bill.getInvoiceAmount();//账单开票金额
        Long billActualPayAmount = bill.getActualPayAmount() + refundAmount;//实收金额
        if (billActualPayAmount - billInvoiceAmount < refundAmount + billRefundAmount) {
            Long thisRefundAmount = refundAmount - (billActualPayAmount - billInvoiceAmount);
            //发送退款成功mq
            BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
            billRefundMqDetail.setRefundAmount(thisRefundAmount);
            EventLifecycle.push(EventMessage.builder().headers("action", BillAction.REFUND).payload(BillActionEvent.refund(bill.getId(), bill.getType(), billRefundMqDetail, bill.getSupCpUnitId())).build());
        }
    }

    /**
     * 发送退款mq
     * <p>
     * 一笔账单进行退款和结转时，优先去退款/结转未开票的金额。其次若一笔账单开了多笔票，按时间降序顺序进行票据的红冲/作废/回收
     *
     * @param bill         账单
     * @param refundAmount 当前退款金额
     */
    private void refundMqAdv(AdvanceBill bill, Long refundAmount) {
        Long billRefundAmount = bill.getRefundAmount() - refundAmount;//账单退款金额
        Long billInvoiceAmount = bill.getInvoiceAmount();//账单开票金额
        Long billActualPayAmount = bill.getActualPayAmount() + refundAmount;//实收金额
        if (billActualPayAmount - billInvoiceAmount < refundAmount + billRefundAmount) {
            Long thisRefundAmount = refundAmount - (billActualPayAmount - billInvoiceAmount);
            //发送退款成功mq
            BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
            billRefundMqDetail.setRefundAmount(thisRefundAmount);
            EventLifecycle.push(EventMessage.builder().headers("action", BillAction.REFUND).payload(BillActionEvent.refund(bill.getId(), bill.getType(), billRefundMqDetail, bill.getSupCpUnitId())).build());
        }
    }


    /**
     * 收款单退款
     *
     * @return
     */
    public GatherBill gatherBillRefund(GatherBill gatherBill, Long refundAmountAll) {
        //账单实收金额
        Long actualPayAmount = gatherBill.getTotalAmount() - gatherBill.getRefundAmount() - gatherBill.getCarriedAmount() - gatherBill.getDeductionAmount();
        //应缴总金额
        Long actualUnpayAmount = gatherBill.getTotalAmount();
        if (refundAmountAll.longValue() < actualPayAmount) {
            gatherBill.setRefundAmount(gatherBill.getRefundAmount() + refundAmountAll);
        } else {
            gatherBill.setRefundAmount(gatherBill.getRefundAmount() + actualPayAmount);
        }
        gatherBill.setRefundState(handleRefundState(actualUnpayAmount, gatherBill.getRefundAmount()));
        return gatherBill;
    }

    /**
     * 处理退款状态
     *
     * @param settleAmount
     * @param refundAmount
     * @return
     */
    private Integer handleRefundState(Long settleAmount, Long refundAmount) {
        if (refundAmount.longValue() == 0L) {
            return BillRefundStateEnum.未退款.getCode();
        } else if (settleAmount.longValue() > refundAmount.longValue()) {
            return BillRefundStateEnum.部分退款.getCode();
        } else if (settleAmount.longValue() == refundAmount.longValue()) {
            return BillRefundStateEnum.已退款.getCode();
        }
        return null;
    }

    /**
     * 处理应收单退款状态
     *
     * @param receivableAmount 应收金额
     * @param refundAmount     总退款金额
     * @return
     */
    private Integer handleRefundState2(Long receivableAmount, Long refundAmount) {
        if (refundAmount.longValue() == 0L) {
            return BillRefundStateEnum.未退款.getCode();
        } else if (receivableAmount.longValue() > refundAmount.longValue()) {
            return BillRefundStateEnum.部分退款.getCode();
        } else {
            return BillRefundStateEnum.已退款.getCode();
        }
    }
}
