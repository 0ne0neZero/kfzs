package com.wishare.finance.domains.bill.aggregate.approve;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.BusinessDataItem;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.domains.bill.aggregate.BillCarryoverA;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.CarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.CarryoverTypeEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillCarryoverDetailE;
import com.wishare.finance.domains.bill.entity.BillCarryoverE;
import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.event.BillRefundMqDetail;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverDetailRepository;
import com.wishare.finance.domains.bill.repository.BillCarryoverRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 结转审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
@Slf4j
public class CarryoverApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    @Override
    public void onAgree(B bill, BillApproveE billApprove) {
        //收款明细资源库
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        //收款单资源库
        GatherBillRepository gatherBillRepository = Global.ac.getBean(GatherBillRepository.class);
        //结转资源库
        BillCarryoverRepository carryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
        BillCarryoverDetailRepository carryoverDetailRepository = Global.ac.getBean(BillCarryoverDetailRepository.class);
        //获取结转的账单信息
        BillCarryoverE carryoverE = carryoverRepository.getByApproveId(billApprove.getId());
        ErrorAssertUtil.notNullThrow300(carryoverE, ErrorMessage.BILL_CARRYOVER_NOT_EXIST);
        //应收账单资源库
        ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
        List<Long> targetBillIds = carryoverE.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toList());
        List<ReceivableBill> receivableBills = receivableBillRepository.getlistByIds(targetBillIds,bill.getSupCpUnitId());
        BillCarryoverA<B,ReceivableBill> carryoverA = new BillCarryoverA<>(bill, billApprove, carryoverE, receivableBills);
        Long actualSettleAmount = bill.getActualSettleAmount();
        //更新被结转账单信息
        if (TenantUtil.bf4()) {
            for (ReceivableBill targetBill : carryoverA.getTargetBills()) {
                targetBill.setState(BillStateEnum.正常.getCode());
                receivableBillRepository.update(targetBill, new UpdateWrapper<ReceivableBill>()
                        .eq("id", targetBill.getId())
                        .eq("sup_cp_unit_id", targetBill.getSupCpUnitId()));
            }
        }
        carryoverA.carryover();
        carryoverA.getGatherBill().setTotalAmount(carryoverA.getCarryoverAmount());
        log.info("实际结转信息：{}",JSON.toJSONString(carryoverA.getCarryoverDetail()));
        //更新结转信息
        carryoverRepository.updateById(carryoverA);
        //添加成功结转详情信息表
        saveCarryoverDetail(carryoverA,carryoverDetailRepository);
        //更新被结转账单信息
        for (ReceivableBill targetBill : carryoverA.getTargetBills()) {
            receivableBillRepository.update(targetBill,new UpdateWrapper<ReceivableBill>()
                    .eq("id",targetBill.getId()).eq("sup_cp_unit_id",targetBill.getSupCpUnitId()));
        }
        //插入预收账单
        List<GatherDetail> settleList = new ArrayList<>();

        if (Objects.nonNull(carryoverA.getAdvanceBill())) {
            //预收账单资源库
            Global.ac.getBean(AdvanceBillRepository.class).save(carryoverA.getAdvanceBill());
            settleList.add(carryoverA.getAdvanceBillSettle());
            //收款单结转日志记录
            BizLog.normal(String.valueOf(carryoverA.getAdvanceBill().getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("账单结转生成", true)))
                            .option(new ContentOption(new PlainTextDataItem("收款金额为：", false)))
                            .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(carryoverA.getAdvanceBill().getTotalAmount()), false), OptionStyle.normal()))
                            .option(new ContentOption(new PlainTextDataItem("元", true)))
            );
        }
        //插入结算明细
        settleList.addAll(carryoverA.getTargetSettles());
        gatherDetailRepository.saveBatch(settleList);

        CarryoverTypeEnum carryoverTypeEnum = CarryoverTypeEnum.valueOfByCode(carryoverA.getCarryoverType());

        //获取结转账单的收款单
        List<GatherDetail> gatherDetailList = Optional
                .ofNullable(gatherDetailRepository.getListByRecBillId(bill.getId(), bill.getSupCpUnitId()))
                .orElse(new ArrayList<>())
                .stream().filter(e -> e.getRemainingCarriedAmount() > 0)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(gatherDetailList)) {
            // key -> gatherBillId, value -> List<GatherDetail>
            Map<Long, List<GatherDetail>> gatherDetailMap =
                    gatherDetailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherBillId));
            // 额本次需结转金
            Long totalCarryoverAmount = Math.min(carryoverA.getCarryoverAmount(),actualSettleAmount);

            for (Map.Entry<Long, List<GatherDetail>> item : gatherDetailMap.entrySet()) {
                if (totalCarryoverAmount == 0L) {
                    break;
                }
                List<GatherDetail> gatherDetails = item.getValue();
                Long gatherBillCarriedAmount = 0L;
                for (GatherDetail gatherDetail : gatherDetails) {
                    if (totalCarryoverAmount == 0L) {
                        break;
                    }
                    Long remainingCarriedAmount = gatherDetail.getRemainingCarriedAmount();
                    if (totalCarryoverAmount > remainingCarriedAmount) {
                        gatherBillCarriedAmount += gatherDetail.getRemainingCarriedAmount();
                        totalCarryoverAmount -= gatherDetail.getRemainingCarriedAmount();
                        gatherDetail.setCarriedAmount(gatherDetail.getCarriedAmount()
                                + gatherDetail.getRemainingCarriedAmount());
                    } else {
                        gatherBillCarriedAmount += totalCarryoverAmount;
                        gatherDetail.setCarriedAmount(gatherDetail.getCarriedAmount() + totalCarryoverAmount);
                        totalCarryoverAmount = 0L;
                        break;
                    }
                }
                GatherBill gatherBill = gatherBillRepository.getOne(new QueryWrapper<GatherBill>()
                        .eq("id", item.getKey()).eq("sup_cp_unit_id", bill.getSupCpUnitId()));
                gatherBill.setCarriedAmount(gatherBill.getCarriedAmount() + gatherBillCarriedAmount);
                gatherBill.restCarriedState();
                if (gatherBill.getPreferential()== Const.State._1){
                    // 如果被结转的原账单收款单涉及优惠赠送规则，则标识将传染下去
                    Optional.ofNullable(carryoverA.getGatherBill()).ifPresent(a->a.setPreferential(Const.State._1));
                }
                gatherBillRepository.update(gatherBill, new UpdateWrapper<GatherBill>().eq("id", gatherBill.getId())
                        .eq(StringUtils.isNotBlank(gatherBill.getSupCpUnitId()), "sup_cp_unit_id", gatherBill.getSupCpUnitId()));
                gatherDetails.forEach(b -> {
                    gatherDetailRepository.update(b, new UpdateWrapper<GatherDetail>().eq("id", b.getId())
                            .eq(StringUtils.isNotBlank(b.getSupCpUnitId()), "sup_cp_unit_id", b.getSupCpUnitId()));
                });
                //结转账单日志记录
                BizLog.initiate(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.结转,
                        new Content().option(new ContentOption(new PlainTextDataItem("结转方式为：" + carryoverTypeEnum.getValue(), true)))
                                .option(new ContentOption(new PlainTextDataItem(carryoverTypeEnum.getValue() + "金额为：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(gatherBillCarriedAmount), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", false))));
            }
        }

        if (Objects.nonNull(carryoverA.getGatherBill())) {
            gatherBillRepository.save(carryoverA.getGatherBill());
            //收款单结转日志记录
            BizLog.normal(String.valueOf(carryoverA.getGatherBill().getId()), LogContext.getOperator(), LogObject.账单, LogAction.生成,
                    new Content().option(new ContentOption(new PlainTextDataItem("账单结转生成", true)))
                            .option(new ContentOption(new PlainTextDataItem("收款金额为：", false)))
                            .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(carryoverA.getGatherBill().getTotalAmount()), false), OptionStyle.normal()))
                            .option(new ContentOption(new PlainTextDataItem("元", true)))
            );
        }

        //发送结转mq，用于发票自动红冲
        if (carryoverA.getCarryoverAmount() > 0) {
            refundMq(bill, carryoverA.getCarryoverAmount());
            BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(bill.getType());

            //账单结转日志记录
            BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                    new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单结转", true)))
                            .option(new ContentOption(new PlainTextDataItem("结转方式为：" + carryoverTypeEnum.getValue(), true)))
                            .option(new ContentOption(new PlainTextDataItem(carryoverTypeEnum.getValue() + "金额为：", false)))
                            .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(carryoverA.getCarryoverAmount()), false), OptionStyle.normal()))
                            .option(new ContentOption(new PlainTextDataItem("元", false))));
            //结转账单日志记录
            carryoverA.getCarryoverDetail().stream().filter(a->Objects.nonNull(a.getActualCarryoverAmount()) && a.getActualCarryoverAmount() > 0).forEach(i -> {
                BizLog.initiate(String.valueOf(i.getTargetBillId()),
                        LogContext.getOperator(), LogObject.账单, LogAction.收款,
                        new Content().option(new ContentOption(new PlainTextDataItem("收款方式： 账单结转", true)))
                                .option(new ContentOption(new PlainTextDataItem("收款金额为：", false)))
                                .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(i.getActualCarryoverAmount()), false), OptionStyle.normal()))
                                .option(new ContentOption(new PlainTextDataItem("元", true)))
                                .option(new ContentOption(new PlainTextDataItem("结转账单：", false)))
                                .option(new ContentOption(new BusinessDataItem(bill.getBillNo(),true, String.valueOf(bill.getId()), billTypeEnum.getValue())))
                );
            });
        }else {
            //账单结转日志记录
            BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                    new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单结转", true)))
                            .option(new ContentOption(new PlainTextDataItem("结转方式为：预存优惠赠送金额结转" , true))));
        }

        // 交账用的mq
        BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
        billRefundMqDetail.setRefundAmount(0L);  // 交账时
        EventLifecycle.push(EventMessage.builder().headers("action", BillAction.REFUND).payload(BillActionEvent.refund(bill.getId(), bill.getType(),billRefundMqDetail, bill.getSupCpUnitId())).build());
    }


    /**
     * 发送退款mq
     * 其实是结转
     *  一笔账单进行退款和结转时，优先去退款/   结转未开票的金额。   其次若一笔账单开了多笔票，按时间降序顺序进行票据的红冲/作废/回收
     * @param bill 账单
     * @param carryoverAmount 当前结转金额
     */
    private void refundMq(B bill, Long carryoverAmount) {
        // 方圆环境结转不自动红冲 +中交
        if (TenantUtil.bf10()) {
            return;
        }
        Long billInvoiceAmount = bill.getInvoiceAmount();//账单开票金额
        Long billActualPayAmount = bill.getActualPayAmount() + carryoverAmount;//实收金额
        log.info("发送结转自动红冲mq,账单id：{},账单开票金额:{},实收金额:{}",bill.getId(),billInvoiceAmount,billActualPayAmount);
        if (billActualPayAmount - billInvoiceAmount < bill.getCarriedAmount()) {
            //结转相当于对原发票退款红冲，走同一个逻辑
            Long thisRefundAmount = carryoverAmount - (billActualPayAmount - billInvoiceAmount);
            //发送退款成功mq
            BillRefundMqDetail billRefundMqDetail = new BillRefundMqDetail();
            billRefundMqDetail.setRefundAmount(thisRefundAmount);
            EventLifecycle.push(EventMessage.builder().headers("action", BillAction.REFUND).payload(BillActionEvent.refund(bill.getId(), bill.getType(), billRefundMqDetail, bill.getSupCpUnitId())).build());
        }
    }

    /**
     * 添加结转详情数据
     *
     * @param billCarryoverA            结转单
     * @param carryoverDetailRepository carryoverDetailRepository
     */
    private void saveCarryoverDetail(BillCarryoverE billCarryoverA, BillCarryoverDetailRepository carryoverDetailRepository) {
        if (Objects.isNull(billCarryoverA) || Objects.isNull(billCarryoverA.getCarryoverAmount()) || billCarryoverA.getCarryoverAmount()<=0){
            return;
        }
        List<BillCarryoverDetailE> resultList = Lists.newArrayList();
        log.info("结转详情数据：{}", JSON.toJSONString(billCarryoverA.getCarryoverDetail()));
        for (CarryoverDetail carryoverDetail : billCarryoverA.getCarryoverDetail()) {
            if (carryoverDetail.getActualCarryoverAmount() == null || carryoverDetail.getActualCarryoverAmount()<=0){
                continue;
            }
            BillCarryoverDetailE billCarryoverDetailE = new BillCarryoverDetailE();
            billCarryoverDetailE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_CARRYOVER_DETAIL));
            billCarryoverDetailE.setBillType(billCarryoverA.getBillType());
            billCarryoverDetailE.setCarriedBillId(billCarryoverA.getCarriedBillId());
            billCarryoverDetailE.setCarriedBillNo(billCarryoverA.getCarriedBillNo());
            billCarryoverDetailE.setTargetBillId(carryoverDetail.getTargetBillId());
            billCarryoverDetailE.setTargetBillNo(carryoverDetail.getTargetBillNo());
            billCarryoverDetailE.setCarryoverType(billCarryoverA.getCarryoverType());
            billCarryoverDetailE.setCarryoverAmount(carryoverDetail.getActualCarryoverAmount());
            billCarryoverDetailE.setCarryoverTime(billCarryoverA.getCarryoverTime());
            billCarryoverDetailE.setBillCarryoverId(billCarryoverA.getId());
            billCarryoverDetailE.setRemark(billCarryoverA.getRemark());
            resultList.add(billCarryoverDetailE);
        }
        carryoverDetailRepository.saveBatch(resultList);
    }

    /**
     * 结转拒绝
     *
     * @param bill         账单
     * @param billApprove  审核信息
     * @param reason       原因
     */
    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        BillCarryoverRepository carryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
        //根据审核记录id获取结转信息
        BillCarryoverE billCarryoverE = carryoverRepository.getByApproveId(billApprove.getId());
        /* 结转状态：0待审核，1审核中，2已生效，3未生效 */
        billCarryoverE.setState(CarryoverStateEnum.未生效.getCode());
        //结转审核拒绝不更新备注YYFWYC-2373
//        billCarryoverE.setRemark(reason);
        //更新结转记录
        carryoverRepository.updateById(billCarryoverE);
        //更新账单结转状态
        bill.resetCarriedState();
        //更新被结转账单信息
        ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
        if (TenantUtil.bf4()) {
            List<Long> targetBillIds = billCarryoverE.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toList());
            List<ReceivableBill> receivableBills = receivableBillRepository.getlistByIds(targetBillIds,bill.getSupCpUnitId());
            // 审核后释放
            for (ReceivableBill targetBill : receivableBills) {
                targetBill.setState(BillStateEnum.正常.getCode());
                receivableBillRepository.update(targetBill,new UpdateWrapper<ReceivableBill>()
                        .eq("id",targetBill.getId()).eq("sup_cp_unit_id",targetBill.getSupCpUnitId()));
            }
        }
        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝, new Content());
    }
}
