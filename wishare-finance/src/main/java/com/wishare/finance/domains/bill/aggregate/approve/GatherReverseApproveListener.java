package com.wishare.finance.domains.bill.aggregate.approve;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.operator.Operator;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.event.BillAction;
import com.wishare.finance.domains.bill.event.BillActionEvent;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.service.BillDomainService;
import com.wishare.finance.domains.bill.service.BillDomainServiceImpl;
import com.wishare.finance.domains.bill.support.GatherOnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.event.EventLifecycle;
import com.wishare.finance.infrastructure.event.EventMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title GatherReverseApproveListener
 * @date 2023.12.06  15:59
 * @description 收款单冲销审核监听类
 */
@Slf4j
public class GatherReverseApproveListener implements GatherOnBillApproveListener<GatherBill> {


    /**
     * 同意审核
     *
     * @param gatherBill        收款单
     * @param billApprove        审核信息
     */
    @Override
    public void onAgree(GatherBill gatherBill, BillApproveE billApprove) {
        // 获取所需Bean
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        BillReverseRepository reverseRepository = Global.ac.getBean(BillReverseRepository.class);
        BillDomainServiceImpl billDomainService = Global.ac.getBean("billDomainService", BillDomainServiceImpl.class);
        ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
        AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
        //获取收款单相关信息
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(gatherBill.getId()), gatherBill.getSupCpUnitId());
        // 获取待审核的冲销记录
        BillReverseE billReverseE = reverseRepository.getOne(new LambdaQueryWrapper<BillReverseE>().eq(BillReverseE::getBillId,gatherBill.getId())
                .eq(BillReverseE::getState,Const.State._0));
        Assert.validate(() -> Objects.nonNull(billReverseE), ()-> BizException.throw400("未能查询到关联的冲销审批单，请稍后重试"));
        // 更新收款单状态为已冲销 更新操作原因 操作人
        gatherBill.setReversed(Const.State._1);
        gatherBill.setExtField6(billApprove.getReason());
        gatherBill.setOperatorName(LogContext.getOperator().getName());
        gatherBill.setOperator(LogContext.getOperator().getId());
        // 更新收款明细关联子账单的冲销金额以及刷新账单状态
        oprGatherDetailAndBill(billApprove, billDomainService, receivableBillRepository, advanceBillRepository, gatherDetailList);
        // 更新收款单冲销记录状态
        Operator operator = LogContext.getOperator();
        billReverseE.setReverseTime(LocalDateTime.now());
        billReverseE.setState(RefundStateEnum.已退款.getCode());
        billReverseE.setCommunityId(gatherBill.getSupCpUnitId());
        billReverseE.setOperator(operator.getId());
        billReverseE.setOperatorName(operator.getName());
        billReverseE.setReverseAmount(-gatherBill.getRemainingCarriedAmount());
        reverseRepository.saveOrUpdate(billReverseE);
        log.info("MQ发送收款单冲销：{}", gatherBill.getId());
        EventLifecycle.push(EventMessage.builder()
                .headers("action", BillAction.GATHER_BILL_REVERSED)
                .payload(BillActionEvent.gatherBillReverse(gatherBill.getId(), gatherBill.getSupCpUnitId()))
                .build());
        // 收款单冲销通过动态发送
        BizLog.normal(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 收款单冲销", false))));
    }



    /**
     * 拒绝
     *
     * @param gatherBill  收款单
     * @param billApprove 审核信息
     * @param reason      拒绝原因
     */
    @Override
    public void onRefuse(GatherBill gatherBill, BillApproveE billApprove, String reason) {
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
        AdvanceBillRepository advanceBillRepository = Global.ac.getBean(AdvanceBillRepository.class);
        //获取收款单相关信息
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(gatherBill.getId()), gatherBill.getSupCpUnitId());
        // 包含明细账单发送拒绝动态
        for (GatherDetail gatherDetail : gatherDetailList) {
            final long canReverseAmount = gatherDetail.getCanRefundAmount();
            // 若关联子账单可用金额为0 则无需参与本次冲销
            if (canReverseAmount<=0){continue;}
            // 根据预收 应收/临时 区分处理
            if (gatherDetail.getGatherType()==0){
                ReceivableBill receivableBill = receivableBillRepository.getOne(new QueryWrapper<ReceivableBill>().eq("id", gatherDetail.getRecBillId())
                        .eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
                Assert.validate(() -> Objects.nonNull(receivableBill), ()-> BizException.throw400("未能查询到关联的账单，请稍后重试"));
                BizLog.normal(String.valueOf(receivableBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝,
                        new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单冲销", true)))
                                .option(new ContentOption(new PlainTextDataItem("拒绝原因： "+reason, false))));
            }else {
                // 处理预收账单
                AdvanceBill advanceBill = advanceBillRepository.getOne(new QueryWrapper<AdvanceBill>().eq("id", gatherDetail.getRecBillId())
                        .eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
                Assert.validate(() -> Objects.nonNull(advanceBill), ()-> BizException.throw400("未能查询到关联的预收账单，请稍后重试"));
                BizLog.normal(String.valueOf(advanceBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝,
                        new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单冲销", true)))
                                .option(new ContentOption(new PlainTextDataItem("拒绝原因： "+reason, false))));
            }
        }
        // 收款单发送拒绝动态
        BizLog.normal(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 收款单冲销", false))));
    }


    /*
     Private Method ***********************************************************************************************************
     */

    /**
     * 更新收款明细关联子账单的冲销金额以及刷新账单状态
     * @param billApprove 审批记录
     * @param billDomainService billDomainService
     * @param receivableBillRepository receivableBillRepository
     * @param advanceBillRepository advanceBillRepository
     * @param gatherDetailList gatherDetailList
     */
    private void oprGatherDetailAndBill(BillApproveE billApprove, BillDomainServiceImpl billDomainService, ReceivableBillRepository receivableBillRepository, AdvanceBillRepository advanceBillRepository, List<GatherDetail> gatherDetailList) {
        // 搜集需要冲销的结转记录
        List<Long> billCarryIds = new ArrayList<>();
        for (GatherDetail gatherDetail : gatherDetailList) {
            final long canReverseAmount = gatherDetail.getCanRefundAmount();
            final long payAmount = gatherDetail.getPayAmount();
            // 若关联子账单可用金额为0 则无需参与本次冲销
            if (canReverseAmount<=0){continue;}
            // 根据预收 应收/临时 区分处理
            if (gatherDetail.getGatherType()==0){
                ReceivableBill receivableBill = receivableBillRepository.getOne(new QueryWrapper<ReceivableBill>().eq("id", gatherDetail.getRecBillId())
                        .eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
                if (Objects.isNull(receivableBill)){log.info(gatherDetail.getRecBillId()+"未能查询到关联的账单，请稍后重试");}
                if (Objects.isNull(receivableBill) || receivableBill.getSettleAmount()<=0){continue;}
                // 如果存在结转其他账单的金额，金额自动回退原账单，且更新原账单的结转状态以及实收金额，
                if (gatherDetail.getPayWay().equals(SettleWayEnum.结转.getCode()) && Objects.nonNull(gatherDetail.getBillCarryoverId())) {
                    billDomainService.reverseCarryoverBill(receivableBill, canReverseAmount,gatherDetail.getBillCarryoverId());
                }
                receivableBill.setSettleAmount(receivableBill.getSettleAmount() - canReverseAmount);
                receivableBill.refresh();
                receivableBill.setVerifyState(BillVerifyStateEnum.未核销.getCode());
                receivableBillRepository.update(receivableBill,new UpdateWrapper<ReceivableBill>()
                        .eq("id",receivableBill.getId()).eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
                billCarryIds.add(gatherDetail.getBillCarryoverId());
                // 生成对应账单的冲销记录
                saveBillReverse(gatherDetail,canReverseAmount,receivableBill.getTotalAmount(), billApprove,receivableBill.getBillType());
            }else {
                // 处理预收账单
                AdvanceBill advanceBill = advanceBillRepository.getOne(new QueryWrapper<AdvanceBill>().eq("id", gatherDetail.getRecBillId())
                        .eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
                if (Objects.isNull(advanceBill)){log.info(gatherDetail.getRecBillId()+"未能查询到关联的预收账单，请稍后重试");}
                if (Objects.isNull(advanceBill) || advanceBill.getSettleAmount()<=0){continue;}
                // 预收冲销
                advanceBill.setReversed(Const.State._1);
                advanceBill.setVerifyState(BillVerifyStateEnum.未核销.getCode());
                // 预收账单的减免明细标识删除
                Global.ac.getBean(BillAdjustRepository.class).update(new UpdateWrapper<BillAdjustE>().eq("bill_id",advanceBill.getId())
                        .set("deleted",Const.State._1));
                // 冲销预收账单金额
                advanceBillRepository.update(advanceBill,new UpdateWrapper<AdvanceBill>()
                        .eq("id",advanceBill.getId()).eq("sup_cp_unit_id", gatherDetail.getSupCpUnitId()));
                // 生成对应账单的冲销记录
                saveBillReverse(gatherDetail,canReverseAmount,advanceBill.getTotalAmount(), billApprove, BillTypeEnum.预收账单.getCode());
            }
            // gatherDetail状态置为available=1
            GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
            gatherDetail.setAvailable(Const.State._1);
            gatherDetailRepository.update(gatherDetail,new UpdateWrapper<GatherDetail>().eq("id",gatherDetail.getId())
                    .eq("sup_cp_unit_id",gatherDetail.getSupCpUnitId()));
        }
        // 将关联的结转明细置为冲销
        if (CollectionUtils.isNotEmpty(billCarryIds)) {
            BillCarryoverRepository billCarryoverRepository = Global.ac.getBean(BillCarryoverRepository.class);
            BillCarryoverDetailRepository billCarryoverDetailRepository = Global.ac.getBean(BillCarryoverDetailRepository.class);
            billCarryoverRepository.update(new UpdateWrapper<BillCarryoverE>().set("reversed", Const.State._1).in("id", billCarryIds));
            billCarryoverDetailRepository.update(new UpdateWrapper<BillCarryoverDetailE>().set("reversed", Const.State._1).in("bill_carryover_id", billCarryIds));
        }
    }

    /**
     * 保存账单的冲销记录
     * @param gatherDetail 收款明细
     * @param reverseAmount 子账单本次冲销金额
     * @param totalAmount   子账单原账单金额
     * @param billApproveE  审批信息
     * @param billType      账单类型
     */
    private void saveBillReverse(GatherDetail gatherDetail,Long reverseAmount,Long totalAmount,BillApproveE billApproveE,Integer billType) {
        BillReverseE billReverseE = new BillReverseE();
        billReverseE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_REVERSE));
        billReverseE.setBillId(gatherDetail.getRecBillId());
        billReverseE.setBillType(billType);
        billReverseE.setPayChannel(gatherDetail.getPayChannel());
        billReverseE.setCommunityId(gatherDetail.getSupCpUnitId());
        billReverseE.setTotalAmount(totalAmount);
        billReverseE.setReverseAmount(-reverseAmount);
        billReverseE.setReverseTime(LocalDateTime.now());
        billReverseE.setBillApproveId(billApproveE.getId());
        billReverseE.setApproveTime(LocalDateTime.now());
        billReverseE.setState(BillApproveReverseStateEnum.已冲销.getCode());
        billReverseE.setReason(billApproveE.getReason());
        Global.ac.getBean(BillReverseRepository.class).save(billReverseE);
        // 发送审核通过动态
        BizLog.normal(String.valueOf(gatherDetail.getRecBillId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单冲销", false))));
    }


}
