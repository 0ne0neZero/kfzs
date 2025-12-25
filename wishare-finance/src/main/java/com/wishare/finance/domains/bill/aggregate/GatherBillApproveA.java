package com.wishare.finance.domains.bill.aggregate;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.domains.bill.command.BillApplyCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.bill.support.GatherOnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 账单审核聚合
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
@Slf4j
public class GatherBillApproveA extends BillApproveE {

    /**
     * 账单信息
     */
    private GatherBill gatherBill;

    public GatherBillApproveA() {
    }

    public GatherBillApproveA(GatherBill bill) {
       initBill(bill);
    }

    public GatherBillApproveA init(BillApproveE billApprove){
        if (billApprove != null) {
            Global.mapperFacade.map(billApprove, this);
        }
        return this;
    }

    private void initBill(GatherBill bill){
        if (Objects.isNull(bill)){
            throw new IllegalArgumentException("审核聚合中账单不能为空");
        }
        this.gatherBill = bill;
    }

    /**
     * 初始化默认审核信息
     */
    public GatherBillApproveA initDefaultApprove(){
        generateIdentifier();
        setBillId(gatherBill.getId());
        setApprovedState(BillApproveStateEnum.待审核.getCode());
        setBillType(BillTypeEnum.收款单.getCode());
        setLastApproveState(gatherBill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.生成审核.getCode());
        setSupCpUnitId(gatherBill.getSupCpUnitId());
        return this;
    }

    /**
     * 将收款单关联的账单置为审核中
     * @param supCpUnitId 上级收费单元ID
     * @param approveState 变更状态值
     */
    private void approveBill(String supCpUnitId,Integer approveState){
        List<GatherDetail> detailList = Global.ac.getBean(GatherDetailRepository.class).getByGatherBillIds(List.of(gatherBill.getId()), supCpUnitId);
        if (CollectionUtils.isNotEmpty(detailList)) {
            Map<Integer, List<GatherDetail>> gatherTypeMap = detailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherType));
            List<GatherDetail> advList = gatherTypeMap.get(GatherTypeEnum.预收.getCode());
            List<GatherDetail> recList = gatherTypeMap.get(GatherTypeEnum.应收.getCode());
            if (CollectionUtils.isNotEmpty(recList)){
                List<Long> billIds = recList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Global.ac.getBean(ReceivableBillRepository.class).update(new UpdateWrapper<ReceivableBill>().in("id", billIds).eq("sup_cp_unit_id",supCpUnitId)
                        .set("approved_state",approveState));
            }else if (CollectionUtils.isNotEmpty(advList)){
                List<Long> billIds = advList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Global.ac.getBean(AdvanceBillRepository.class).update(new UpdateWrapper<AdvanceBill>().in("id", billIds).eq("sup_cp_unit_id",supCpUnitId)
                        .set("approved_state",approveState));
            }
        }
    }

    /**
     * 将收款单关联的账单置为冻结/解冻
     * @param supCpUnitId 上级收费单元ID
     * @param state 账单状态
     * @param freezeType 冻结类型
     */
    private void approveFreezeBill(String supCpUnitId,Integer state,Integer freezeType){
        List<GatherDetail> detailList = Global.ac.getBean(GatherDetailRepository.class).getByGatherBillIds(List.of(gatherBill.getId()), supCpUnitId);
        if (CollectionUtils.isNotEmpty(detailList)) {
            Map<Integer, List<GatherDetail>> gatherTypeMap = detailList.stream().collect(Collectors.groupingBy(GatherDetail::getGatherType));
            List<GatherDetail> advList = gatherTypeMap.get(GatherTypeEnum.预收.getCode());
            List<GatherDetail> recList = gatherTypeMap.get(GatherTypeEnum.应收.getCode());
            if (CollectionUtils.isNotEmpty(recList)){
                List<Long> billIds = recList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Global.ac.getBean(ReceivableBillRepository.class).update(new UpdateWrapper<ReceivableBill>().in("id", billIds).eq("sup_cp_unit_id",supCpUnitId)
                        .set("state",state).set("freeze_type",freezeType).set(BillStateEnum.正常.getCode()==state,"approved_state",2));
            }else if (CollectionUtils.isNotEmpty(advList)){
                List<Long> billIds = advList.stream().map(GatherDetail::getRecBillId).collect(Collectors.toList());
                Global.ac.getBean(AdvanceBillRepository.class).update(new UpdateWrapper<AdvanceBill>().in("id", billIds).eq("sup_cp_unit_id",supCpUnitId)
                        .set("state",state).set("freeze_type",freezeType).set(BillStateEnum.正常.getCode()==state,"approved_state",2));
            }
        }
    }

    /**
     * 获取审核记录
     *
     * @return
     */
    public BillApproveE getApproveE(){
        return this;
    }

    /**
     * 申请审核
     * @param applyInfo
     * @return
     */
    public boolean apply(BillApplyCommand applyInfo){
        //校验
        verifyOperate(gatherBill);
        //存在审核中的审核记录不允许进行申请
        ErrorAssertUtil.isFalseThrow402(getId() != null, ErrorMessage.BILL_IS_APPROVING);
        switch (applyInfo.getApproveOperateType()){
            case 收款单退款:
                ErrorAssertUtil.isTrueThrow402(applyInfo.getDetail() instanceof BillRefundE, ErrorMessage.BILL_APPLY_PARAM_ERROR);
                // 校验是否有涉及优惠赠送规则的收款单
                Assert.validate(()->gatherBill.getPreferential().equals(Const.State._0), ()->BizException.throw400("存在收款单涉及预存赠送/预存优惠活动的收款单，无法退款"));
                applyRefund((BillRefundE) applyInfo.getDetail(), applyInfo.getReason(),applyInfo.getOutApproveId(),applyInfo.getExtField1(),applyInfo.getSupCpUnitId());
                break;
            case 收款单冲销:
                ErrorAssertUtil.isTrueThrow402(applyInfo.getDetail() instanceof BillReverseE, ErrorMessage.BILL_APPLY_PARAM_ERROR);
                applyReverse((BillReverseE) applyInfo.getDetail(), applyInfo.getReason(),applyInfo.getOutApproveId(),applyInfo.getExtField1(),applyInfo.getSupCpUnitId());
                break;
            default: return false;
        }
        return true;
    }



    /**
     * 审核
     * @param state   通过状态
     * @param reason  通过原因
     * @return {@link Boolean}
     */
    public boolean approve(BillApproveStateEnum state, String reason, GatherOnBillApproveListener<GatherBill> listener){
        //如果未通过审核，则状态状态重置到上一次审核状态
        if (BillApproveStateEnum.未通过 == state){
            gatherBill.setApprovedState(BillApproveStateEnum.已审核.getCode());
            setApprovedState(state.getCode());
        }else if (BillApproveStateEnum.已审核 == state){
            setApprovedState(state.getCode());
            gatherBill.setApprovedState(state.getCode());
        }else{
            throw BizException.throw400(ErrorMessage.BILL_APPROVE_STATE_NOT_SUPPORT.msg());
        }
        //解冻关联账单
        approveFreezeBill(gatherBill.getSupCpUnitId(),BillStateEnum.正常.getCode(), FreezeTypeEnum.无类型.getCode());
        setApprovedRemark(reason);
        setGmtModify(LocalDateTime.now());
        if (Objects.nonNull(listener)){
            if (BillApproveStateEnum.已审核 == state){
                listener.onAgree(gatherBill, this);
            }else if (BillApproveStateEnum.未通过 == state){
                listener.onRefuse(gatherBill, this, reason);
            }
        }
        return true;
    }

    /**
     * 申请退款收款单
     */
    private void applyRefund(BillRefundE billRefund, String reason, String outApproveId, String extField1,String supCpUnitId){
        generateIdentifier();
        setOutApproveId(outApproveId);
        setBillId(gatherBill.getId());
        setSupCpUnitId(supCpUnitId);
        setReason(reason);
        setReason(billRefund.getRemark());
        // 已结算或者部分结算的收款单才可以退款
        if (StringUtils.isNotBlank(extField1) && (extField1.contains("BPM") || extField1.contains("FY_REFUND") ||
        extField1.contains("IN_BPM")) ) {
            String[] extField1s = extField1.split("_");
            if(extField1s.length == 2){
                setExtField1(extField1s[1]);
            }
            setApprovedState(BillApproveStateEnum.审核中.getCode());
            gatherBill.setApprovedState(BillApproveStateEnum.审核中.getCode());
            billRefund.setState(BillApproveRefundStateEnum.退款中.getCode());
        }else {
            setExtField1(extField1);
            setApprovedState(BillApproveStateEnum.待审核.getCode());
            gatherBill.setApprovedState(BillApproveStateEnum.待审核.getCode());
            billRefund.setState(BillApproveRefundStateEnum.待退款.getCode());
        }
        approveFreezeBill(supCpUnitId,BillStateEnum.冻结.getCode(), FreezeTypeEnum.收款单退款流程.getCode());
        setBillType(BillTypeEnum.收款单.getCode());
        setLastApproveState(gatherBill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.收款单退款.getCode());
        setReason(reason);

        //更新调整审核状态
        billRefund.setBillId(getBillId());
        billRefund.setRefundNo(IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20));
        billRefund.setBillType(BillTypeEnum.收款单.getCode());
        billRefund.setBillApproveId(getId());
        billRefund.setApproveTime(LocalDateTime.now());
        billRefund.setOutRefundNo(outApproveId);

        // 保存退款信息，记录日志
        sendRefundAdLogs(billRefund, gatherBill);

    }


    /**
     * 申请冲销收款单
     */
    private void applyReverse(BillReverseE detail, String reason, String outApproveId, String extField1, String supCpUnitId) {
        // 审批数据参数构建
        generateIdentifier();
        setOutApproveId(outApproveId);
        setBillId(gatherBill.getId());
        setSupCpUnitId(supCpUnitId);
        setExtField1(extField1);
        setApprovedState(BillApproveStateEnum.待审核.getCode());
        gatherBill.setApprovedState(BillApproveStateEnum.待审核.getCode());
        approveFreezeBill(supCpUnitId,BillStateEnum.冻结.getCode(), FreezeTypeEnum.收款单冲销流程.getCode());
        setBillType(BillTypeEnum.收款单.getCode());
        setLastApproveState(gatherBill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.收款单冲销.getCode());
        setReason(reason);

        // 更新调整审核状态
        detail.setBillId(getBillId());
        detail.setBillApproveId(getId());
        detail.setApproveTime(LocalDateTime.now());
        detail.setReason(reason);
        detail.setCommunityId(supCpUnitId);
        detail.setState(BillApproveReverseStateEnum.待冲销.getCode());
        detail.setTotalAmount(gatherBill.getTotalAmount());
        detail.setPayChannel(gatherBill.getPayChannel());
        detail.setBillType(BillTypeEnum.收款单.getCode());
        // 保存冲销信息，记录日志
        sendReverseLogs(detail, gatherBill);
    }

    /**
     * 校验操作是否允许
     */
    public void verifyOperate(GatherBill gatherBill){
        ErrorAssertUtil.isFalseThrow402(BillApproveStateEnum.审核中.equalsByCode(gatherBill.getApprovedState()), ErrorMessage.BILL_IS_OPERATING, BillApproveStateEnum.审核中.getValue());
        ErrorAssertUtil.isFalseThrow402(BillRefundStateEnum.退款中.equalsByCode(gatherBill.getApprovedState()), ErrorMessage.BILL_IS_OPERATING, BillRefundStateEnum.退款中.getCode());
        ErrorAssertUtil.isFalseThrow402(BillCarryoverStateEnum.待结转.equalsByCode(gatherBill.getCarriedState()), ErrorMessage.BILL_IS_OPERATING, BillCarryoverStateEnum.待结转.getValue());
        ErrorAssertUtil.isFalseThrow402(InvoiceStateEnum.开票中.equalsByCode(gatherBill.getInvoiceState()), ErrorMessage.BILL_IS_OPERATING, InvoiceStateEnum.valueOfByCode(gatherBill.getInvoiceState()));
        ErrorAssertUtil.isTrueThrow402(BillStateEnum.正常.equalsByCode(gatherBill.getState()), ErrorMessage.BILL_IS_OPERATING, BillStateEnum.valueOfByCode(gatherBill.getState()));
    }

    /**
     * 收款单冲销审核发送动态
     * @param billReverseE billReverseE
     * @param gatherBill   gatherBill
     */
    private void sendReverseLogs(BillReverseE billReverseE, GatherBill gatherBill) {
        // 添加冲销记录
        BillReverseRepository billReverseRepository = Global.ac.getBean(BillReverseRepository.class);
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        billReverseRepository.save(billReverseE);
        // 对收款单包含的单冲销申请日志记录
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(gatherBill.getId()), gatherBill.getSupCpUnitId());
        if (CollectionUtils.isNotEmpty(gatherDetailList)){
            // 收款单冲销动态打印 动态生成
            BizLog.initiate(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.冲销申请, new Content());
            for (GatherDetail gatherDetail : gatherDetailList) {
                // 获取该账单可退款金额 即为当前可冲销金额
                Long canRefundAmount = gatherDetail.getCanRefundAmount();
                if (canRefundAmount > 0){
                    // 收款明细对应的账单 动态生成
                    BizLog.initiate(String.valueOf(gatherDetail.getRecBillId()), LogContext.getOperator(), LogObject.账单, LogAction.冲销申请,
                            new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单冲销", true)))
                                    .option(new ContentOption(new PlainTextDataItem("冲销金额为：", false)))
                                    .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(canRefundAmount), false), OptionStyle.normal()))
                                    .option(new ContentOption(new PlainTextDataItem("元", true)))
                                    .option(new ContentOption(new PlainTextDataItem("冲销原因： "+billReverseE.getReason(), true))));
                }
            }
        }
    }

    /**
     * 收款单退款审核发送动态
     * @param detail detail
     * @param gatherBill gatherBill
     */
    private void sendRefundAdLogs(BillRefundE detail, GatherBill gatherBill) {
        // 获取所需数据层
        BillRefundRepository refundRepository = Global.ac.getBean(BillRefundRepository.class);
        GatherDetailRepository gatherDetailRepository = Global.ac.getBean(GatherDetailRepository.class);
        // 添加收款单退款记录
        refundRepository.save(detail);
        // 对收款单包含的单退款申请日志记录
        List<GatherDetail> gatherDetailList = gatherDetailRepository.getByGatherBillIds(Lists.newArrayList(gatherBill.getId()), gatherBill.getSupCpUnitId());
        if (CollectionUtils.isEmpty(gatherDetailList)){return;}
        // 当前收款单总可退款金额
        Long refundAmount = detail.getRefundAmount();
        // 判断是否存在明细退款 根据不同方案进行退款
        handlerRecDetailRefund(detail, gatherDetailList, refundAmount);
        // 发送收款单退款动态
        BizLog.initiate(String.valueOf(gatherBill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.退款申请, new Content());
    }

    /**
     * 处理明细账单退款动态发送
     * @param refundAmount 剩余可退金额
     * @param canRefundAmount 明细对应可退金额
     * @param id 明细关联账单ID
     * @return {@link Long} 更新可退金额
     */
    private Long refundRecBill(Long refundAmount, Long canRefundAmount, Long id) {
        Long finalMoney = canRefundAmount <= refundAmount ? canRefundAmount : refundAmount;
        sendBillRefund(finalMoney, id);
        if (refundAmount.equals(finalMoney)){
            return null;
        }
        refundAmount = BigDecimal.valueOf(refundAmount).subtract(BigDecimal.valueOf(canRefundAmount)).longValue();
        return refundAmount;
    }

    // 退款申请日志模版
    private void sendBillRefund(Long finalMoney, Long id) {
        BizLog.initiate(String.valueOf(id), LogContext.getOperator(), LogObject.账单, LogAction.退款申请,
                new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", true)))
                        .option(new ContentOption(new PlainTextDataItem("退款金额为：", false)))
                        .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount(finalMoney), false), OptionStyle.normal()))
                        .option(new ContentOption(new PlainTextDataItem("元", false))));
    }

    /**
     * 根据不同方案发起退款申请
     * @param detail 退款记录
     * @param gatherDetailList 包含的收款明细
     * @param refundAmount 本次申请退款金额
     */
    private void handlerRecDetailRefund(BillRefundE detail, List<GatherDetail> gatherDetailList, Long refundAmount) {
        if (Objects.nonNull(detail.getGatherDetailList())){
            // 获取明细退款对应map
            Map<Long, BigDecimal> detailMap = detail.getGatherDetailList();
            // 过滤对应的收款明细
            List<GatherDetail> detailList = gatherDetailList.stream().filter(a->detailMap.containsKey(a.getId())).collect(Collectors.toList());
            for (GatherDetail gatherDetail : detailList) {
                // 获取该账单可退款金额
                Long canRefundAmount = gatherDetail.getCanRefundAmount();
                // 获取本次申请退款金额
                Long applyRefundAmount = detailMap.get(gatherDetail.getId()).multiply(Const.BIG_DECIMAL_HUNDRED).longValue();
                // 校验金额是否充足
                Assert.validate(()->canRefundAmount >= applyRefundAmount, ()->BizException.throw400("账单编号为"+gatherDetail.getRecBillNo()+"退款金额不足"));
                // 对应子账单发送退款申请
                sendBillRefund(applyRefundAmount,gatherDetail.getRecBillId());
            }
        }else {
            // 如果不存在明细退款，则按照时间倒序顺序发送退款动态
            for (GatherDetail gatherDetail : gatherDetailList) {
                // 当前收款明细 为应收/临时账单
                // 获取该账单可退款金额
                Long canRefundAmount = gatherDetail.getCanRefundAmount();
                if (canRefundAmount>0) {
                    // 对应子账单实际退款金额计算
                    refundAmount = refundRecBill(refundAmount, canRefundAmount, gatherDetail.getRecBillId());
                    // 金额退光后结束
                    if (refundAmount == null) {break;}
                }
            }
        }
    }
}
