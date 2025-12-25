package com.wishare.finance.domains.bill.aggregate;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wishare.finance.apps.model.reconciliation.vo.FlowClaimDetailV;
import com.wishare.finance.apps.service.reconciliation.FlowClaimAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.BillApplyCommand;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.entity.*;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.support.OnBillApplyListener;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileRunStateEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.owl.util.Assert;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 账单审核聚合
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 * @param <B> 账单
 */
@Getter
@Setter
@Slf4j
public class BillApproveA<B extends Bill> extends BillApproveE {
    private final String FANG_YUAN = "fangyuan";
    /**
     * 账单信息
     */
    private B bill;

    public BillApproveA() {
    }

    public BillApproveA(B bill) {
       initBill(bill);
    }

    public BillApproveA<B> init(BillApproveE billApprove){
        if (billApprove != null) {
            Global.mapperFacade.map(billApprove, this);
        }
        return this;
    }

    /**
     * 初始化默认审核信息
     */
    public BillApproveA initDefaultApprove(){
        generateIdentifier();
        setBillId(bill.getId());
        setApprovedState(BillApproveStateEnum.待审核.getCode());
        setBillType(bill.getType());
        setLastApproveState(bill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.生成审核.getCode());
        setSupCpUnitId(bill.getSupCpUnitId());
        return this;
    }

    public BillApproveE getApproveE(){
        return this;
    }

    /**
     * 申请审核
     * @param applyInfo
     * @return
     */
    public boolean apply(BillApplyCommand applyInfo, OnBillApplyListener listener){
        //校验
        bill.verifyOperate();
        //申请前置操作
        if (Objects.nonNull(listener)){
            listener.before(bill, this,applyInfo.getDetail());
        }
        //存在审核中的审核记录不允许进行申请
        ErrorAssertUtil.isFalseThrow402(getId() != null, ErrorMessage.BILL_IS_APPROVING);
        switch (applyInfo.getApproveOperateType()){
            case 作废:
                applyInvalid(applyInfo.getReason(),applyInfo.getOutApproveId(),applyInfo.getSupCpUnitId(),applyInfo.getOperationRemark());
                break;
            case 结转:
                ErrorAssertUtil.isTrueThrow402(applyInfo.getDetail() instanceof BillCarryoverE, ErrorMessage.BILL_APPLY_PARAM_ERROR);
                applyCarryover((BillCarryoverE) applyInfo.getDetail(), applyInfo.getReason(),applyInfo.getOutApproveId(), applyInfo.getExtField1(),applyInfo.getSupCpUnitId());
                break;
            case 调整:
                ErrorAssertUtil.isTrueThrow402(applyInfo.getDetail() instanceof BillAdjustE, ErrorMessage.BILL_APPLY_PARAM_ERROR);
                applyAdjust((BillAdjustE) applyInfo.getDetail(), applyInfo.getReason(), applyInfo.getLastApprovedState(),applyInfo.getOutApproveId(),applyInfo.getSupCpUnitId());
                break;
            case 减免:
                ErrorAssertUtil.isTrueThrow402(applyInfo.getDetail() instanceof BillAdjustE, ErrorMessage.BILL_APPLY_PARAM_ERROR);
                applyDeduction((BillAdjustE) applyInfo.getDetail(), applyInfo.getReason(), applyInfo.getLastApprovedState(),applyInfo.getOutApproveId(),applyInfo.getSupCpUnitId());
                break;
            case 退款:
                ErrorAssertUtil.isTrueThrow402(applyInfo.getDetail() instanceof BillRefundE, ErrorMessage.BILL_APPLY_PARAM_ERROR);
                applyRefund((BillRefundE) applyInfo.getDetail(), applyInfo.getReason(),applyInfo.getOutApproveId(),applyInfo.getExtField1(),applyInfo.getSupCpUnitId());
                break;
            case 冲销:
                applyReverse(applyInfo.getReason(),applyInfo.getOutApproveId(),applyInfo.getExtField1(),applyInfo.getSupCpUnitId(),applyInfo.getOperationRemark());
                break;
            case 跳收:
                applyJump((BillJumpE) applyInfo.getDetail(), applyInfo.getReason(), applyInfo.getLastApprovedState(),applyInfo.getOutApproveId(),applyInfo.getSupCpUnitId());
                break;
                // ps：billApprove表是存在supCpUnitId的 记得新增case时，内部进行参数赋值 ,后果自负----> setSupCpUnitId(supCpUnitId);
            default: return false;
        }
        //申请后置操作（预操作）
        if (Objects.nonNull(listener)){
            listener.after(bill,this, applyInfo.getDetail());
        }
        return true;
    }


    /**
     * 审核
     * @param state   通过状态
     * @param reason  通过原因
     * @return
     */
    public boolean approve(BillApproveStateEnum state, String reason, OnBillApproveListener listener){
        //如果未通过审核，则状态状态重置到上一次审核状态
        if (BillApproveStateEnum.未通过.equals(state)){
            bill.setApprovedState(getLastApproveState());
            setApprovedState(state.getCode());
            approveGatherBill(bill.getSupCpUnitId(),BillApproveStateEnum.已审核.getCode());
        }else if (BillApproveStateEnum.已审核.equals(state)){
            if (FANG_YUAN.equals(EnvData.config) && Objects.isNull(bill.getBillCostType())) {
               bill.setCostType();
            }
            setApprovedState(state.getCode());
            bill.setApprovedState(state.getCode());
            approveGatherBill(bill.getSupCpUnitId(),BillApproveStateEnum.已审核.getCode());
        }else{

            throw BizException.throw400(ErrorMessage.BILL_APPROVE_STATE_NOT_SUPPORT.msg());
        }
        setApprovedRemark(reason);
        setGmtModify(Optional.ofNullable(getGmtModify()).orElse(LocalDateTime.now()));
        if (Objects.nonNull(listener)){
            if (BillApproveStateEnum.已审核.equals(state)){
                listener.onAgree(bill, this);
            }else {
                listener.onRefuse(bill, this, reason);
            }
        }
        // 刷新账单信息
        bill.refresh();
        return true;
    }

    private void initBill(B bill){
        if (Objects.isNull(bill)){
            throw new IllegalArgumentException("审核聚合中账单不能为空");
        }
        this.bill = bill;
    }

    /**
     * 将账单关联的收款单也置为审核中
     * @param supCpUnitId 上级收费单元ID
     * @param approveState 变更状态值
     */
    private void approveGatherBill(String supCpUnitId,Integer approveState){
        List<GatherDetail> detailList = Global.ac.getBean(GatherDetailRepository.class).getListByRecBillId(bill.getId(), supCpUnitId);
        if (CollectionUtils.isNotEmpty(detailList)) {
            List<Long> gatherBillIds = detailList.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
            Global.ac.getBean(GatherBillRepository.class).update(new UpdateWrapper<GatherBill>().in("id", gatherBillIds).eq("sup_cp_unit_id",supCpUnitId)
                    .set("approved_state",approveState));
        }
    }

    /**
     * 申请作废
     *
     * @param reason
     * @param outApproveId
     * @param supCpUnitId
     */
    private void applyInvalid(String reason, String outApproveId, String supCpUnitId,String operationRemark){
        // 作废前置条件检验 未结算且未开票 [如果为负数录入银行托收的临时账单，可以直接作废
        if (bill.getTotalAmount()>0) {
            ErrorAssertUtil.isTrueThrow402(InvoiceStateEnum.未开票.equalsByCode(bill.getInvoiceState()), ErrorMessage.BILL_INVALID_STATE_NOT_SUPPORT);
            if (!EnvConst.FANGYUAN.equals(EnvData.config) && !EnvConst.ZHONGJIAO.equals(EnvData.config)) {
                ErrorAssertUtil.isTrueThrow402(BillSettleStateEnum.未结算.equalsByCode(bill.getSettleState()), ErrorMessage.BILL_INVALID_STATE_NOT_SUPPORT);
            }
        }
        setOutApproveId(outApproveId);
        setBillId(bill.getId());
        setLastApproveState(bill.getApprovedState());
        setBillType(bill.getType());
        setSupCpUnitId(supCpUnitId);
        setApprovedState(BillApproveStateEnum.待审核.getCode());
        setOperateType(BillApproveOperateTypeEnum.作废.getCode());
        setReason(reason);
        bill.setExtField6(operationRemark);
        bill.apply();
        // 如果由第三方审核系统审核，账单状态为审核中状态
        if (StringUtils.isNotBlank(outApproveId)) {
            setApprovedState(BillApproveStateEnum.审核中.getCode());
            bill.setApprovedState(BillApproveStateEnum.审核中.getCode());
        }else {
            setApprovedState(BillApproveStateEnum.待审核.getCode());
        }
    }

    /**
     * 结转
     *
     * @param billCarryoverE 结转明细
     * @param reason         申请原因
     * @param outApproveId   外部审批标识
     * @param supCpUnitId
     */
    private void applyCarryover(BillCarryoverE billCarryoverE, String reason, String outApproveId , String extField1, String supCpUnitId){
        //可结转余额不能小于结转金额
        ErrorAssertUtil.isTrueThrow402(bill.getRemainingCarriedAmount() >= billCarryoverE.getCarryoverAmount(), ErrorMessage.BILL_CARRYOVER_AMOUNT_NO_ENOUGH);
        ReceivableBillRepository receivableBillRepository = Global.ac.getBean(ReceivableBillRepository.class);
        // 已挂账的应收临时账单 不可被结转支付
        if (CollectionUtils.isNotEmpty(billCarryoverE.getCarryoverDetail()) && TenantUtil.bf24()){
            List<Long> targetBillIds = billCarryoverE.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toList());
            List<ReceivableBill> receivableBills = receivableBillRepository.getlistByIds(targetBillIds,bill.getSupCpUnitId());
            Optional.ofNullable(receivableBills).filter(a->CollectionUtils.isNotEmpty(receivableBills)).ifPresent(a->{
                Assert.validate(()->a.stream().noneMatch(b-> b.getOnAccount().equals(BillOnAccountEnum.已挂账.getCode())),
                        ()->BizException.throw400("已挂账的应收临时账单 不可被结转支付"));
            });
        }
        if (TenantUtil.bf4()){
            List<Long> targetBillIds = billCarryoverE.getCarryoverDetail().stream().map(CarryoverDetail::getTargetBillId).collect(Collectors.toList());
            List<ReceivableBill> receivableBills = receivableBillRepository.getlistByIds(targetBillIds,bill.getSupCpUnitId());
            // 将被结转账单状态冻结 审核后释放
            for (ReceivableBill targetBill : receivableBills) {
                targetBill.setState(BillStateEnum.冻结.getCode());
                receivableBillRepository.update(targetBill,new UpdateWrapper<ReceivableBill>()
                        .eq("id",targetBill.getId()).eq("sup_cp_unit_id",targetBill.getSupCpUnitId()));
            }
        }
        //2.生成一笔新结转的审核记录
        generateIdentifier();
        setOutApproveId(outApproveId);
        setBillId(bill.getId());
        setApprovedState(BillApproveStateEnum.待审核.getCode());
        approveGatherBill(supCpUnitId,BillApproveStateEnum.待审核.getCode());

        setBillType(bill.getType());
        setLastApproveState(bill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.结转.getCode());
        setGmtCreate(LocalDateTime.now());
        setReason(reason);
        setSupCpUnitId(supCpUnitId);
        setExtField1(extField1);
        //先设置审核记录的上一次状态在更新账单状态
        bill.applyCarryover();
        // 3.生成一笔结转记录
        billCarryoverE.setBillApproveId(getId())
                    .setApproveTime(LocalDateTime.now())
                    .setState(CarryoverStateEnum.待审核.getCode())
                    .setCarriedBillNo(bill.getBillNo())
                    .setRemark(reason)
                    .setBillType(bill.getType());
    }

    /**
     * 申请调整账单
     */
    private void applyDeduction(BillAdjustE billAdjust, String reason, Integer lastApprovedState, String outApproveId, String supCpUnitId){
        // 已核对的账单不允许调低和减免
        ErrorAssertUtil.isFalseThrow402(bill.getMcReconcileState().equals(ReconcileResultEnum.已核对.getCode()) || bill.getReconcileState().equals(ReconcileResultEnum.已核对.getCode()), ErrorMessage.BILL_APPLY_DEDUCTION_ERROR);
        // 部分核对的账单不允许减免或者调低金额涉及到实收金额
        ErrorAssertUtil.isFalseThrow402((bill.getMcReconcileState().equals(ReconcileResultEnum.部分核对.getCode()) || bill.getReconcileState().equals(ReconcileResultEnum.部分核对.getCode())) && billAdjust.getDeductionMethod().equals(DeductionMethodEnum.应收减免.getCode())
                && (bill.getTotalAmount() - bill.getDeductibleAmount() - bill.getDiscountAmount() - billAdjust.getAdjustAmount()) < bill.getRemainingCarriedAmount(), ErrorMessage.BILL_RECONCILE_APPLY_DEDUCTION_ERROR);
        ErrorAssertUtil.isFalseThrow402((bill.getMcReconcileState().equals(ReconcileResultEnum.部分核对.getCode()) || bill.getReconcileState().equals(ReconcileResultEnum.部分核对.getCode())) && billAdjust.getDeductionMethod().equals(DeductionMethodEnum.实收减免.getCode())
                && (bill.getTotalAmount() - bill.getDeductibleAmount() - bill.getDiscountAmount() - billAdjust.getAdjustAmount()) < bill.getRemainingCarriedAmount(), ErrorMessage.BILL_RECONCILE_APPLY_DEDUCTION_ERROR);

        //生成审核记录
        generateIdentifier();
        setBillId(bill.getId());
        setBillType(bill.getType());
        setOperateType(BillApproveOperateTypeEnum.减免.getCode());
        BillAdjustReasonEnum reasonEnum = BillAdjustReasonEnum.valueOfByCode(
                Optional.ofNullable(billAdjust.getReason()).orElse(99));
        setReason(reasonEnum != null ? reasonEnum.getValue() : null);
        setRemark(billAdjust.getRemark());
        setSupCpUnitId(supCpUnitId);
        setGmtCreate(LocalDateTime.now());
        setLastApproveState(Objects.isNull(lastApprovedState) ? bill.getApprovedState() : lastApprovedState);

        //更新调整审核状态
        billAdjust.setBillType(bill.getType());
        billAdjust.setBillApproveId(getId());
        billAdjust.setBillId(getBillId());
        billAdjust.setBillAmount(bill.getTotalAmount());
        billAdjust.setApproveTime(Optional.ofNullable(billAdjust.getApproveTime()).orElse(LocalDateTime.now()));
        billAdjust.setDeductionMethod(billAdjust.getDeductionMethod());
        billAdjust.setActualLostAmount(billAdjust.getAdjustAmount());
        billAdjust.setAdjustAmount(- billAdjust.getAdjustAmount());
        bill.applyDeduction();
        // 如果由第三方审核系统审核，账单状态为审核中状态
        if (StringUtils.isNotBlank(outApproveId)) {
            setOutApproveId(outApproveId);
            setApprovedState(BillApproveStateEnum.审核中.getCode());
            bill.setApprovedState(BillApproveStateEnum.审核中.getCode());
            approveGatherBill(supCpUnitId,BillApproveStateEnum.审核中.getCode());
        }else {
            setApprovedState(BillApproveStateEnum.审核中.getCode());
            approveGatherBill(supCpUnitId,BillApproveStateEnum.审核中.getCode());
        }
    }

    /**
     * 申请调整账单
     */
    private void applyAdjust(BillAdjustE billAdjust, String reason, Integer lastApprovedState, String outApproveId, String supCpUnitId){
        ErrorAssertUtil.notNullThrow403(billAdjust.getAdjustWay(), ErrorMessage.BILL_ADJUST_WAY_NOT_SUPPORT);
//        if(BillAdjustWayEnum.RECEIVABLE_AREA.equalsByCode(billAdjust.getAdjustWay())){
//            //面积调整设置调整类型和真实调整金额
//            resetAdjustArea(billAdjust);
//        }


        //生成审核记录
        generateIdentifier();
        setBillId(bill.getId());
        setBillType(bill.getType());
        setSupCpUnitId(supCpUnitId);
        setOperateType(BillApproveOperateTypeEnum.调整.getCode());
        BillAdjustReasonEnum reasonEnum = BillAdjustReasonEnum.valueOfByCode(
                Optional.ofNullable(billAdjust.getReason()).orElse(99));
        setReason(reasonEnum != null ? reasonEnum.getValue() : null);
        setRemark(billAdjust.getRemark());
        setGmtCreate(Optional.ofNullable(billAdjust.getApproveTime()).orElse(LocalDateTime.now()));
        setLastApproveState(Objects.isNull(lastApprovedState) ? bill.getApprovedState() : lastApprovedState);

        //更新调整审核状态
        billAdjust.setBillType(bill.getType());
        billAdjust.setBillApproveId(getId());
        billAdjust.setBillId(getBillId());
        billAdjust.setBillAmount(bill.getTotalAmount());
        billAdjust.setApproveTime(LocalDateTime.now());
        billAdjust.setDeductionMethod(billAdjust.getDeductionMethod());

        Integer adjustType = billAdjust.getAdjustType();
        if (Objects.nonNull(adjustType) && BillAdjustTypeEnum.调低.equalsByCode(adjustType)){
            // 已核对的账单不允许调低和减免
            ErrorAssertUtil.isFalseThrow402(bill.getMcReconcileState().equals(ReconcileResultEnum.已核对.getCode()) || bill.getReconcileState().equals(ReconcileResultEnum.已核对.getCode()), ErrorMessage.BILL_APPLY_DEDUCTION_ERROR);
            // 部分核对的账单不允许减免或者调低金额涉及到实收金额
            ErrorAssertUtil.isFalseThrow402((bill.getMcReconcileState().equals(ReconcileResultEnum.部分核对.getCode()) || bill.getReconcileState().equals(ReconcileResultEnum.部分核对.getCode()))
                    && (bill.getTotalAmount() - bill.getDeductibleAmount() - bill.getDiscountAmount() - billAdjust.getAdjustAmount()) < bill.getRemainingCarriedAmount(), ErrorMessage.BILL_RECONCILE_APPLY_DEDUCTION_ERROR);
            ErrorAssertUtil.isFalseThrow402((bill.getMcReconcileState().equals(ReconcileResultEnum.部分核对.getCode()) || bill.getReconcileState().equals(ReconcileResultEnum.部分核对.getCode()))
                    && (bill.getTotalAmount() - bill.getDeductibleAmount() - bill.getDiscountAmount() - billAdjust.getAdjustAmount()) < bill.getRemainingCarriedAmount(), ErrorMessage.BILL_RECONCILE_APPLY_DEDUCTION_ERROR);
            billAdjust.setAdjustAmount(- billAdjust.getAdjustAmount());
        }
        bill.applyAdjust();
        // 如果由第三方审核系统审核，账单状态为审核中状态
        if (StringUtils.isNotBlank(outApproveId)) {
            setOutApproveId(outApproveId);
            setApprovedState(BillApproveStateEnum.审核中.getCode());
            bill.setApprovedState(BillApproveStateEnum.审核中.getCode());
            approveGatherBill(supCpUnitId,BillApproveStateEnum.审核中.getCode());
        }else {
            setApprovedState(BillApproveStateEnum.待审核.getCode());
            approveGatherBill(supCpUnitId,BillApproveStateEnum.待审核.getCode());
        }
    }

    /**
     * 申请退款账单
     */
    private void applyRefund(BillRefundE billRefund, String reason, String outApproveId, String extField1, String supCpUnitId){
        generateIdentifier();
        setOutApproveId(outApproveId);
        setBillId(bill.getId());
        if (StringUtils.isNotBlank(extField1) && extField1.equals("BPM")) {
            setApprovedState(BillApproveStateEnum.审核中.getCode());
        }else {
            setApprovedState(BillApproveStateEnum.待审核.getCode());
        }
        setBillType(bill.getType());
        setSupCpUnitId(supCpUnitId);
        setLastApproveState(bill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.退款.getCode());
        setReason(reason);

        //更新调整审核状态
        billRefund.setBillId(getBillId());
        billRefund.setRefundNo(IdentifierFactory.getInstance().serialNumber("refund_no", "TK", 20));
        billRefund.setBillType(bill.getType());
        billRefund.setBillApproveId(getId());
        billRefund.setApproveTime(LocalDateTime.now());
        billRefund.setState(BillApproveRefundStateEnum.未生效.getCode());
        bill.applyRefund();
    }

    /**
     * 申请冲销账单
     *
     * @param reason
     * @param outApproveId
     * @param supCpUnitId
     */
    private void applyReverse(String reason, String outApproveId, String extField1, String supCpUnitId,String operationRemark) {
        generateIdentifier();
        if (bill.getReversed() == BillReverseStateEnum.已冲销.getCode()) {
            throw BizException.throw400("该账单已经冲销，不可重复申请");
        }
        ErrorAssertUtil.isTrueThrow402(bill.getMcReconcileState()!=ReconcileResultEnum.部分核对.getCode()&&bill.getMcReconcileState()!=ReconcileResultEnum.已核对.getCode(), ErrorMessage.BILL_APPLY_REVERSE_ERROR);
        ErrorAssertUtil.isTrueThrow402(bill.getReconcileState()!=ReconcileResultEnum.部分核对.getCode()&&bill.getReconcileState()!=ReconcileResultEnum.已核对.getCode(), ErrorMessage.BILL_APPLY_REVERSE_ERROR);
        Optional.of(bill).filter(a->a instanceof AdvanceBill && (((AdvanceBill) a).getPreferentialAmount()>0||((AdvanceBill) a).getPresentAmount()>0)
                        && (a.getCarriedAmount() > 0 || (((AdvanceBill) a).getPreferentialAmount()+((AdvanceBill) a).getPresentAmount()) != (a.getDeductibleAmount())))
                .ifPresent(a-> {throw BizException.throw400("该预存优惠账单已发生结转，不可冲销");});
        // 已开票或  已结算 或部分结算  的账单才可冲销
        if (bill.getSettleState().equals(BillSettleStateEnum.未结算.getCode()) && !bill.getInvoiceState().equals(InvoiceStateEnum.已开票.getCode())){
            throw BizException.throw400("已开票或已结算或部分结算的账单才可冲销");
        }
        // 存在已认领流水的收款单的账单不可冲销
        List<GatherDetail> detailList = Global.ac.getBean(GatherDetailRepository.class).getListByRecBillId(bill.getId(), bill.getSupCpUnitId());
        if (CollectionUtils.isNotEmpty(detailList)) {
            List<Long> gatherIds = detailList.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
            List<FlowClaimDetailV> recGatherIdFlowClaimRecord = Global.ac.getBean(FlowClaimAppService.class).getRecGatherIdFlowClaimRecord(gatherIds, bill.getSupCpUnitId());
            Assert.validate(() -> CollectionUtils.isEmpty(recGatherIdFlowClaimRecord), () -> BizException.throw400("无法冲销，账单下存在收款单已认领流水。请您先解除认领"));
        }
        setOutApproveId(outApproveId);
        setBillId(bill.getId());
        setApprovedState(BillApproveStateEnum.待审核.getCode());
        setBillType(bill.getType());
        setSupCpUnitId(supCpUnitId);
        setLastApproveState(bill.getApprovedState());
        setOperateType(BillApproveOperateTypeEnum.冲销.getCode());
        setExtField1(extField1);
        setReason(reason);
        bill.setExtField6(operationRemark);
        bill.apply();
    }

    /**
     * 面积调整设置调整类型和真实调整金额
     */
    private void resetAdjustArea(BillAdjustE billAdjust){
        Long adjustAmount = Math.abs(billAdjust.getAdjustAmount());
        billAdjust.setAdjustType(adjustAmount >= bill.getTotalAmount() ? BillAdjustTypeEnum.调高.getCode() : BillAdjustTypeEnum.调低.getCode());
        billAdjust.setAdjustAmount(Math.abs(adjustAmount - bill.getTotalAmount()));
        double sum = (double)billAdjust.getAdjustAmount() / 100;
        /*if(BillAdjustWayEnum.ACTUAL_PRICE.equalsByCode(billAdjust.getAdjustWay())){
            if(BillAdjustTypeEnum.调高.equalsByCode(billAdjust.getAdjustType())){
                billAdjust.setContent("通过:实收调整-实测面积，调高"+sum+"元");
            }else{
                billAdjust.setContent("通过:实收调整-实测面积，调低"+ sum +"元");
            }
        }*/
        if(BillAdjustWayEnum.RECEIVABLE_AREA.equalsByCode(billAdjust.getAdjustWay())){
            if(BillAdjustTypeEnum.调高.equalsByCode(billAdjust.getAdjustType())){
                billAdjust.setContent("通过:应收调整-实测面积，调高"+sum+"元");
            }else{
                billAdjust.setContent("通过:应收调整-实测面积，调低"+sum+"元");
            }
        }
    }


    /**
     * 申请跳收账单
     */
    private void applyJump(BillJumpE billJumpE, String reason, Integer lastApprovedState, String outApproveId, String supCpUnitId){

        //生成审核记录
        generateIdentifier();
        setBillId(bill.getId());
        setBillType(bill.getType());
        setOperateType(BillApproveOperateTypeEnum.跳收.getCode());
        setReason(billJumpE.getReason());
        setRemark(billJumpE.getRemark());
        setGmtCreate(LocalDateTime.now());
        setLastApproveState(Objects.isNull(lastApprovedState) ? bill.getApprovedState() : lastApprovedState);
        setSupCpUnitId(supCpUnitId);
        //更新调整审核状态
        billJumpE.setBillType(bill.getType());
        billJumpE.setBillApproveId(getId());
        billJumpE.setBillId(getBillId());
        // 如果由第三方审核系统审核，账单状态为审核中状态
        if (StringUtils.isNotBlank(outApproveId)) {
            setOutApproveId(outApproveId);
            setApprovedState(BillApproveStateEnum.审核中.getCode());
            bill.setApprovedState(BillApproveStateEnum.审核中.getCode());
            if(bill instanceof ReceivableBill){
                ((ReceivableBill) bill).setJumpState(BillJumpStateEnum.跳收审核中.getCode());
            }
        }else {
            setApprovedState(BillApproveStateEnum.待审核.getCode());
        }
    }

    public void initJumpState(int operateType,int approveState){
        if(BillApproveOperateTypeEnum.跳收.getCode() == operateType && bill instanceof ReceivableBill){
            if(BillApproveStateEnum.已审核.getCode() == approveState){
                ((ReceivableBill) bill).setJumpState(JumpStateEnum.跳收审核通过.getCode());
            }else if(BillApproveStateEnum.未通过.getCode() == approveState){
                ((ReceivableBill) bill).setJumpState(JumpStateEnum.跳收拒绝.getCode());
            }
        }

    }

}
