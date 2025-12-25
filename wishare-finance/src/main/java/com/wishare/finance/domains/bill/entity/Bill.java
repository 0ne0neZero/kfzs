package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.support.BillSerialNumberFactory;
import com.wishare.finance.domains.bill.support.IBill;
import com.wishare.finance.domains.bill.support.PayInfosJSONListTypeHandler;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.enums.DeductionMethodEnum;
import com.wishare.starter.consts.Const;
import com.wishare.starter.utils.ErrorAssertUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.wishare.finance.domains.bill.repository.BillRepository.FANG_YUAN;

/**
 * 账单实体
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class Bill implements IBill,Cloneable, Serializable {

    private static final long serialVersionUID = -2278438603293197232L;

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 账单标识（空.无标识 1.冲销标识）
     */
    private Integer billLabel;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 成本中心id
     */
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    /**
     * 项目ID
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 收费组织id
     */
    private String cpOrgId;

    /**
     * 收费组织名称
     */
    private String cpOrgName;

    /**
     * 上级收费单元id
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String supCpUnitId;

    /**
     * 上级收费单元名称
     */
    private String supCpUnitName;

    /**
     * 收费单元id
     */
    private String cpUnitId;

    /**
     * 收费单元名称
     */
    private String cpUnitName;

    /**
     * 系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;

    /**
     * 收款账号id
     */
    private Long sbAccountId;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 外部账单编号
     */
    private String outBillNo;

    /**
     * 外部业务单号
     */
    private String outBusNo;

    /**
     * 外部业务id(用于跳转)
     */
    private String outBusId;

    /**
     * 账单说明
     */
    private String description;

    /**
     * CNY	币种(货币代码)（CNY:人民币）
     */
    private String currency;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单,4:应付账单）
     */
    @TableField(exist = false)
    private Integer type;

    /**
     * 账单金额
     */
    private Long totalAmount = 0L;

    /**
     * 应收金额
     */
    private Long receivableAmount = 0L;

    /**
     * 应收减免金额
     */
    private Long deductibleAmount = 0L;

    /**
     * 违约金金额
     */
    private Long overdueAmount = 0L;

    /**
     * 实收减免金额
     */
    private Long discountAmount = 0L;

    /**统计账单金额总数
     * 实收金额
     */
    private Long settleAmount = 0L;

    /**
     * 退款金额
     */
    private Long refundAmount = 0L;

    /**
     * 结转金额
     */
    private Long carriedAmount = 0L;

    /**
     * 开票金额
     */
    private Long invoiceAmount = 0L;

    /**
     * 冲销金额
     */
    private Long reverseAmount = 0L;

    /**
     * 付款方类型（0:业主，1开发商，2租客，3客商，4法定单位）
     */
    private Integer payerType;

    /**
     * 收款方类型（0:业主，1开发商，2租客，3客商，4法定单位）
     */
    private Integer payeeType;

    /**
     * 账单费用分类 1历史欠费,2当期应收,3预收款项
     */
    private Integer billCostType;
    /**
     * 收款方ID
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 增值税普通发票
     *   1: 增值税普通发票
     *   2: 增值税专用发票
     *   3: 增值税电子发票
     *   4: 增值税电子专票
     *   5: 收据
     *   6：电子收据
     */
    private String invoiceType;

    /**
     * 收费对象属性（1个人，2企业）
     */
    private Integer payerLabel;

    /**
     * 扩展参数
     */
    private String attachParams;

    /**
     * 账单来源
     */
    private String source;

    /**
     * 账单状态（0正常，1冻结，2作废，3挂账）
     */
    private Integer state = 0;

    /**
     * 是否挂账：0未挂账，1已挂账
     */
    private Integer onAccount = 0;

    /**
     * 结算状态（0未结算，1部分结算，2已结算）
     */
    private Integer settleState = 0;

    /**
     * 退款状态（0未退款，1退款中，2部分退款，已退款）
     */
    private Integer refundState = 0;

    /**
     * 核销状态（0未核销，1已核销）
     */
    private Integer verifyState = 0;

    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    private Integer approvedState = 0;

    /**
     * 结转状态：0未结转，1待结转，2部分结转，3已结转
     */
    private Integer carriedState = 0;

    /**
     * 开票状态：0未开票，1开票中，2部分开票，3已开票
     */
    private Integer invoiceState = 0;

    /**
     * 账票流水对账状态：0未核对，1部分核对，2已核对，3核对失败
     */
    private Integer reconcileState = 0;

    /**
     * {@linkplain ReconcileResultEnum}
     * 商户清分对账状态：0未核对，1部分核对，2已核对，3核对失败
     */
    @ApiModelProperty("商户清分对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer mcReconcileState;

    /**
     * 是否交账：0未交账，1已交账
     */
    private Integer accountHanded = 0;

    /**
     * 是否已推凭过： 0未推凭，1已推凭
     */
    private Integer inferenceState ;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 是否拆单：0未拆单，1已拆单
     */
    private Integer separated;

    /**
     * 是否冲销：0未冲销，1已冲销
     */
    private Integer reversed;

    /**
     * 是否调整：0未调整，1已调整
     */
    private Integer adjusted;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用名称
     */
    private String appNumber;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;


    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 收费对象ID
     */
    private String customerId;

    /**
     * 收费对象名称
     */
    private String customerName;

    /**
     * 收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）
     */
    private Integer customerType;

    /**
     * 收费对象属性（1个人，2企业）
     */
    private Integer customerLabel;

    /**
     * 扩展字段1
     */
    private String extField1;

    /**
     * 扩展字段2
     */
    private String extField2;

    /**
     * 扩展字段3
     */
    private String extField3;

    /**
     * 扩展字段4
     */
    private String extField4;

    /**
     * 扩展字段5
     */
    private String extField5;

    /**
     * 扩展字段6(废弃原因---作废或冲销的原因)
     */
    private String extField6;

    /**
     * 扩展字段7
     */
    private String extField7;

    /**
     * 扩展字段8
     */
    private String extField8;

    /**
     * 扩展字段9
     */
    private String extField9;

    /**
     * 扩展字段10
     */
    private String extField10;

    /**
     * 实际缴费金额
     */
    @TableField(exist = false)
    private Long actualPayAmount;

    @TableField(exist = false)
    private Long actualPayAmountNew;

    /**
     * 是否负数手续费 0：否 1：是
     */
    @TableField(exist = false)
    private Integer negativeCommission = 0;

    /**
     * 实际应缴金额
     */
    @TableField(exist = false)
    private Long actualUnpayAmount;

    /**
     * 归属年
     */
    private Integer accountYear;

    /**
     * 归属月（账期）
     */
    private LocalDate accountDate;

    /**
     * 备注
     */
    private String remark;

    @Override
    public Long getIdentifier() {
        return this.id;
    }

    @Override
    public void setIdentifier(Long id) {
        this.id = id;
    }

    /**
     * 初始化账单信息
     */
    public void init(){
        init(null);
    }

    public void init(String envConfig){
        //自动加载账单id
        generateIdentifier();
        //自动加载账单编号
        generateBillNo();
        resetReceivableAmount();
        resetCpUint();
        resetCustomer();
        if (FANG_YUAN.equals(envConfig)) {
            setCostType();
        }
        //归属年默认为归属月对应的年份
        if (Objects.isNull(accountYear) && Objects.nonNull(accountDate)){
            accountYear = accountDate.getYear();
        }
    }

    /**
     * 原房号变成收款单元
     */
    public void resetCpUint(){
        if(Objects.isNull(supCpUnitId)){
            this.supCpUnitId = communityId;
        }
        if(Objects.isNull(supCpUnitName)){
            this.supCpUnitName = communityName;
        }
        if(Objects.isNull(cpUnitId)){
            this.cpUnitId = roomId;
        }
        if(Objects.isNull(cpUnitName)){
            this.cpUnitName = roomName;
        }
    }

    /**
     * 原付款方置为收费对象
     */
    public void resetCustomer(){
        if(Objects.isNull(customerId)){
            this.customerId = payerId;
        }
        if(Objects.isNull(customerName)){
            this.customerName = payerName;
        }
        if(Objects.isNull(customerType)){
            this.customerType = payerType;
        }
        if(Objects.isNull(customerLabel)){
            this.customerLabel = payerLabel;
        }
    }

    /**
     * 自动加载账单编号
     *
     */
    public void generateBillNo() {
        if (billNo == null){
            billNo = BillSerialNumberFactory.getInstance().serialNumber();
        }
    }

    @Override
    public boolean approve(BillApproveStateEnum approveState) {
        this.approvedState = approveState.getCode();
        return true;
    }

    @Override
    public boolean apply() {
        this.approvedState = BillApproveStateEnum.待审核.getCode();
        return true;
    }

    @Override
    public boolean applyCarryover() {
        verifyOperate();
        //1.检查账单状态
        BillApproveStateEnum approveStateEnum = BillApproveStateEnum.valueOfByCode(getApprovedState());
        ErrorAssertUtil.isTrueThrow402(BillApproveStateEnum.已审核 == approveStateEnum, ErrorMessage.BILL_CARRYOVER_STATE_NOT_SUPPORT, approveStateEnum.getValue());
        BillSettleStateEnum settleStateEnum = BillSettleStateEnum.valueOfByCode(getSettleState());
        ErrorAssertUtil.isTrueThrow402(BillSettleStateEnum.已结算 == settleStateEnum ||
                BillSettleStateEnum.部分结算 == settleStateEnum,
                ErrorMessage.BILL_CARRYOVER_STATE_NOT_SUPPORT, settleStateEnum.getValue());
        this.carriedState = BillCarryoverStateEnum.待结转.getCode();
        return apply();
    }

    @Override
    public boolean applyAdjust() {
        verifyOperate();
        this.adjusted = BillAdjustStateEnum.未调整.getCode();
        return apply();
    }

    @Override
    public boolean applyDeduction() {
        verifyOperate();
        this.adjusted = BillAdjustStateEnum.未调整.getCode();
        return apply();
    }

    @Override
    public boolean applyRefund() {
        verifyOperate();
        return apply();
    }

    @Override
    public boolean refund(Long amount) {
        verifyOperate();
        //可退款金额 = 结算金额 - 结转金额 - 退款金额（上次退款金额）- 冲销金额
        long remainingRefundAmount = settleAmount - carriedAmount - refundAmount - reverseAmount;
        ErrorAssertUtil.isTrueThrow402(remainingRefundAmount >= amount, ErrorMessage.BILL_REFUND_AMOUNT_ERROR);
        refundAmount += amount;
        if (remainingRefundAmount > amount){
            this.refundState = BillRefundStateEnum.部分退款.getCode();
        }else {
            this.refundState = BillRefundStateEnum.已退款.getCode();
        }
        return true;
    }

    @Override
    public boolean derate(Long adjustAmount, int way) {
        // 可支付金额处理后多出来的金额
        if (way == 2){
            discountAmount = discountAmount + adjustAmount;
        }else {
            deductibleAmount = deductibleAmount + adjustAmount;
            receivableAmount -= adjustAmount;
            ErrorAssertUtil.isTrueThrow402(receivableAmount >= discountAmount, ErrorMessage.BILL_ADJUST_AMOUNT_DISCOUNT_ERROR);
        }
        return true;
    }

    @Override
    public boolean adjust(Long adjustAmount, BillAdjustTypeEnum adjustType,Integer type) {
        afterVerifyOperate(type);
        //待审核或未通过
        switch (adjustType){
            case 调高:
                totalAmount = totalAmount + adjustAmount;
                receivableAmount = receivableAmount + adjustAmount;
                if (BillSettleStateEnum.已结算.equalsByCode(this.settleState)) {
                    this.settleState = BillSettleStateEnum.部分结算.getCode();
                }
                // 调高账单金额后，账单若为已核对状态，则变为部分核对
                this.mcReconcileState = (mcReconcileState == ReconcileResultEnum.已核对.getCode())
                        ? ReconcileResultEnum.部分核对.getCode() : mcReconcileState;
                this.reconcileState = (reconcileState == ReconcileResultEnum.已核对.getCode())
                        ? ReconcileResultEnum.部分核对.getCode() : reconcileState;
                break;
            case 调低:
                    totalAmount -= adjustAmount;
                    receivableAmount -= adjustAmount;
                    ErrorAssertUtil.isTrueThrow402(receivableAmount >= 0,
                            ErrorMessage.BILL_ADJUST_RECEIVABLE_AMOUNT_ERROR);
                break;
            default: return false;
        }
        ErrorAssertUtil.isTrueThrow402(receivableAmount >= 0, ErrorMessage.BILL_ADJUST_AMOUNT_DEDUCTIBLE_ERROR);
        setAdjusted(BillAdjustStateEnum.已调整.getCode());
        return true;
    }

    @Override
    public boolean adjust2(Long adjustAmount, BillAdjustTypeEnum adjustType, Integer deductionMethod) {
        verifyOperate();
        //待审核或未通过
        switch (adjustType){
            case 调高:
                totalAmount = totalAmount + adjustAmount;
                if (DeductionMethodEnum.实收减免.getCode().equals(deductionMethod)){
                    settleAmount += adjustAmount;
                }else {
                    receivableAmount += adjustAmount;
                }
                break;
            case 调低:
                if (DeductionMethodEnum.实收减免.getCode().equals(deductionMethod)){
                    settleAmount -= adjustAmount;
                    ErrorAssertUtil.isTrueThrow402(settleAmount >= 0, ErrorMessage.BILL_ADJUST_SETTLE_AMOUNT_ERROR);
                }else {
                    receivableAmount -= adjustAmount;
                    ErrorAssertUtil.isTrueThrow402(receivableAmount >= 0, ErrorMessage.BILL_ADJUST_RECEIVABLE_AMOUNT_ERROR);
                }
                break;
            case 减免:
                if (DeductionMethodEnum.实收减免.getCode().equals(deductionMethod)){
                    discountAmount = discountAmount + adjustAmount;
                    settleAmount -= adjustAmount;
                    ErrorAssertUtil.isTrueThrow402(settleAmount >= 0, ErrorMessage.BILL_ADJUST_SETTLE_AMOUNT_ERROR);
                }else {
                    deductibleAmount = deductibleAmount + adjustAmount;
                    receivableAmount -= adjustAmount;
                    ErrorAssertUtil.isTrueThrow402(receivableAmount >= discountAmount, ErrorMessage.BILL_ADJUST_AMOUNT_DISCOUNT_ERROR);
                }
                break;
            default: return false;
        }
        ErrorAssertUtil.isTrueThrow402(receivableAmount >= 0, ErrorMessage.BILL_ADJUST_AMOUNT_DEDUCTIBLE_ERROR);
        setAdjusted(BillAdjustStateEnum.已调整.getCode());
        return true;
    }

    @Override
    public boolean carryover() {
        return true;
    }

    @Override
    public boolean settle(long settleAmount,long discountAmount) {
        verifyOperate();
        ErrorAssertUtil.isTrueThrow402(settleAmount <= getRemainingSettleAmount(), ErrorMessage.BILL_SETTLE_AMOUNT_ERROR);
        this.settleAmount += settleAmount;
        long remainingSettleAmount = getRemainingSettleAmount(); //当前已结算的金额
        if (remainingSettleAmount == 0){
            this.settleState = BillSettleStateEnum.已结算.getCode();
        }else if (remainingSettleAmount > 0){
            setSettleState(BillSettleStateEnum.部分结算.getCode());
        }
        if (BillOnAccountEnum.已挂账.equalsByCode(onAccount)) {
            if(BillSettleStateEnum.已结算.equalsByCode(this.settleState)){
                this.onAccount = BillOnAccountEnum.已销账.getCode();
            }
        }
        this.discountAmount += discountAmount;
        return true;
    }

    @Override
    public long canOverFlowSettle(long settleAmount, long discountAmount) {
        verifyOperate();
        this.settleAmount += settleAmount;
        this.discountAmount += discountAmount;
        long remainingSettleAmount = getRemainingSettleAmount();
        long overFlowAmount;
        if (remainingSettleAmount <= 0){
            this.settleState = BillSettleStateEnum.已结算.getCode();
            overFlowAmount = Math.abs(remainingSettleAmount);
        }else {
            setSettleState(BillSettleStateEnum.部分结算.getCode());
            overFlowAmount = 0L;
        }
        if (BillOnAccountEnum.已挂账.equalsByCode(onAccount)) {
            if(BillSettleStateEnum.已结算.equalsByCode(this.settleState)){
                this.onAccount = BillOnAccountEnum.已销账.getCode();
            }
        }
        return overFlowAmount;
    }

    @Override
    public boolean delete() {
        verifyOperate();
        this.deleted = DataDeletedEnum.DELETED.getCode();
        return true;
    }

    @Override
    public boolean deapprove() {
        verifyOperate();
        this.approvedState = BillApproveStateEnum.待审核.getCode();
        this.setInit(true);
        return true;
    }

    @Override
    public boolean freeze() {
        verifyOperate();
        this.state = BillStateEnum.冻结.getCode();
        return true;
    }

    @Override
    public boolean freezeBatchAddReason(Integer type) {
        verifyOperate();
        this.state = BillStateEnum.冻结.getCode();
        return true;
    }

    @Override
    public boolean unfreeze() {
        this.state = BillStateEnum.正常.getCode();
        return true;
    }

    @Override
    public boolean handAccount() {
        verifyOperate();
        this.accountHanded = BillAccountHandedStateEnum.已交账.getCode();
        return true;
    }

    @Override
    public boolean handReversal() {
        verifyOperate();
        this.accountHanded = BillAccountHandedStateEnum.未交账.getCode();
        return true;
    }

    @Override
    public boolean onAccount() {
        verifyOperate();
        //已审核、未结算、已开票可以进行挂账
        BillApproveStateEnum billApproveStateEnum = BillApproveStateEnum.valueOfByCode(this.approvedState);
        ErrorAssertUtil.isTrueThrow402(BillApproveStateEnum.已审核 == billApproveStateEnum,ErrorMessage.BILL_IS_OPERATING, billApproveStateEnum.getValue());
        BillSettleStateEnum billSettleStateEnum = BillSettleStateEnum.valueOfByCode(this.settleState);
        ErrorAssertUtil.isTrueThrow402(BillSettleStateEnum.未结算 == billSettleStateEnum,ErrorMessage.BILL_IS_OPERATING, billSettleStateEnum.getValue());
        InvoiceStateEnum invoiceStateEnum = InvoiceStateEnum.valueOfByCode(this.invoiceState);
        ErrorAssertUtil.isTrueThrow402(InvoiceStateEnum.已开票 == invoiceStateEnum,ErrorMessage.BILL_IS_OPERATING, invoiceStateEnum.getDes());
        this.onAccount = BillOnAccountEnum.已挂账.getCode();
        return true;
    }

    /**
     * 修改 是否挂账：0未挂账，1已挂账
     * @return
     */
    @Override
    public boolean onAutoAccount() {
        //已审核、未结算、开票成功后可以进行自动挂账
        this.onAccount = BillOnAccountEnum.已挂账.getCode();
        return true;
    }

    @Override
    public boolean offAccount() {
        //当已挂账的账单进行作废、冲销、红冲/作废关联的票据成功时，账单的挂账状态自动变为「未挂账」。
        if (this.onAccount == BillOnAccountEnum.已挂账.getCode()) {
            this.onAccount = BillOnAccountEnum.未挂账.getCode();
        }
        return true;
    }

    @Override
    public boolean writeOff() {
        verifyOperate();
        // 只有已挂账的账单可以销账
        BillOnAccountEnum billOnAccountEnum = BillOnAccountEnum.valueOfByCode(this.onAccount);
        ErrorAssertUtil.isTrueThrow402(BillOnAccountEnum.已挂账 == billOnAccountEnum, ErrorMessage.BILL_IS_OPERATING, billOnAccountEnum.getValue());
        this.onAccount = BillOnAccountEnum.已销账.getCode();
        this.settleState = BillSettleStateEnum.已结算.getCode();
        return true;
    }


    /**
     * 发起开票
     * @return
     */
    @Override
    public boolean invoice() {
        /*verifyOperate();*/
        this.invoiceState = BillInvoiceStateEnum.开票中.getCode();
        return true;
    }


    /**
     * 根据入参来修改开票状态
     * @return
     */
    @Override
    public boolean invoice(Integer status) {
        this.invoiceState = status;
        return true;
    }

    /**
     * 修改开票状态、挂账状态、开票金额
     * @param invoiceAmount
     * @param success
     * @return
     */
    @Override
    public boolean finishInvoice(Long invoiceAmount, boolean success) {
        /*verifyOperate();*/
        //总的开票金额
        Long allInvoiceAmount = getAllInvoiceAmount();
        //开票成功
        if (success) {
            if (settleState == BillSettleStateEnum.未结算.getCode()) {
                /**开票金额 */
                this.invoiceAmount += invoiceAmount;
            } else {
                /**开票金额 */
                this.invoiceAmount += invoiceAmount;
                // 开蓝票时，开票金额不能大于应收金额
                if (invoiceAmount > 0) {
                    ErrorAssertUtil.isTrueThrow402(this.invoiceAmount <= allInvoiceAmount, ErrorMessage.BILL_INVOICE_AMOUNT_ERROR);
                }
            }
        }
        if (this.invoiceAmount == 0L) {
            /**开票状态：0未开票，1开票中，2部分开票，3已开票*/
            this.invoiceState = InvoiceStateEnum.未开票.getCode();
        } else if (this.invoiceAmount < allInvoiceAmount) {
            /**开票状态：0未开票，1开票中，2部分开票，3已开票*/
            this.invoiceState = InvoiceStateEnum.部分开票.getCode();
        } else if (this.invoiceAmount.longValue() == allInvoiceAmount.longValue()) {
            /**开票状态：0未开票，1开票中，2部分开票，3已开票*/
            this.invoiceState = InvoiceStateEnum.已开票.getCode();
        }
        //只针对应收账单 当已审核、未结算账单进行开票后，开票成功，挂账状态自动变为已挂账
        if (this.type == BillTypeEnum.应收账单.getCode() && this.approvedState == BillApproveStateEnum.已审核.getCode()
                && this.getSettleState() == BillSettleStateEnum.未结算.getCode()
                && !this.invoiceState.equals(InvoiceStateEnum.未开票.getCode())) {
            /** 修改 是否挂账：0未挂账，1已挂账 */
            this.onAutoAccount();
        }
        return true;
    }

    @Override
    public boolean voidBatch(Long invoiceAmount) {
        long afterInvoiceAmount = getInvoiceAmount() - invoiceAmount;
        if (afterInvoiceAmount == 0L) {
            this.invoiceState = InvoiceStateEnum.未开票.getCode();
        } else {
            this.invoiceState = InvoiceStateEnum.部分开票.getCode();
        }
        this.invoiceAmount = afterInvoiceAmount;
        return true;
    }

    @Override
    public boolean reverse() {
        verifyOperate();
        this.reversed = BillReverseStateEnum.已冲销.getCode();
        return true;
    }

    @Override
    public boolean invalid() {
        verifyOperate();
        this.state = BillStateEnum.作废.getCode();
        return true;
    }

    @Override
    @Deprecated
    public boolean reconcile(boolean result) {
        if (result){
            this.reconcileState = BillSettleStateEnum.已结算.equalsByCode(settleState) ?
                    BillReconcileStateEnum.已对平.getCode() : BillReconcileStateEnum.部分对平.getCode();
        }else{
            this.reconcileState = BillReconcileStateEnum.未对平.getCode();
        }
        return true;
    }

    @Override
    public boolean infer() {
        this.inferenceState = BillInferStateEnum.已推凭.getCode();
        return true;
    }


    /**
     * 变更账单收费人
     *
     * @param payerId    收费人id
     * @param payerName  收费人名称
     * @param payerType  收费人类型
     * @param payerPhone 收费人手机号
     */
    public void changePayer(String payerId, String payerName, Integer payerType, String payerPhone, Integer payerLabel){
        this.payerId = payerId;
        this.payerName = payerName;
        this.payerType = payerType;
        this.payerLabel = payerLabel;
        if (this instanceof ReceivableBill){
            ((ReceivableBill) this).setPayerPhone(payerPhone);
        }else if (this instanceof TemporaryChargeBill){
            ((TemporaryChargeBill) this).setPayerPhone(payerPhone);
        }else if (this instanceof AdvanceBill){
            ((AdvanceBill) this).setPayerPhone(payerPhone);
        }
        this.customerId = payerId;
        this.customerName = payerName;
        this.customerType = payerType;
        this.customerLabel = payerLabel;

    }

    /**
     * 校验操作是否允许
     */
    public void verifyOperate(){
        ErrorAssertUtil.isFalseThrow402(BillApproveStateEnum.审核中.equalsByCode(approvedState), ErrorMessage.BILL_IS_OPERATING, BillApproveStateEnum.审核中.getValue());
        ErrorAssertUtil.isFalseThrow402(BillRefundStateEnum.退款中.equalsByCode(refundState), ErrorMessage.BILL_IS_OPERATING, BillRefundStateEnum.退款中.getValue());
        ErrorAssertUtil.isFalseThrow402(BillSettleStateEnum.结算中.equalsByCode(settleState), ErrorMessage.BILL_IS_OPERATING, BillSettleStateEnum.结算中.getValue());
        ErrorAssertUtil.isFalseThrow402(BillCarryoverStateEnum.待结转.equalsByCode(carriedState), ErrorMessage.BILL_IS_OPERATING, BillCarryoverStateEnum.待结转.getValue());
        ErrorAssertUtil.isFalseThrow402(BillReverseStateEnum.已冲销.equalsByCode(reversed == null ? BillReverseStateEnum.未冲销.getCode() : reversed), ErrorMessage.BILL_IS_OPERATING, BillReverseStateEnum.已冲销.getValue());
        ErrorAssertUtil.isFalseThrow402(InvoiceStateEnum.开票中.equalsByCode(invoiceState), ErrorMessage.BILL_IS_OPERATING, InvoiceStateEnum.valueOfByCode(invoiceState));
        ErrorAssertUtil.isTrueThrow402(BillStateEnum.正常.equalsByCode(state), ErrorMessage.BILL_IS_OPERATING, BillStateEnum.valueOfByCode(state));
        Optional.ofNullable(negativeCommission).filter(a-> Const.State._1!=negativeCommission)
                .ifPresent(a->ErrorAssertUtil.isFalseThrow402(totalAmount < 0, ErrorMessage.NEGATIVE_BILL_NO_OPERATE));
    }

    /**
     * 后置操作校验操作是否允许,type:1,后置操作
     */
    public void afterVerifyOperate(Integer type){
        if (type != null && type != 1 ){
            ErrorAssertUtil.isFalseThrow402(BillApproveStateEnum.审核中.equalsByCode(approvedState), ErrorMessage.BILL_IS_OPERATING, BillApproveStateEnum.审核中.getValue());
        }
        ErrorAssertUtil.isFalseThrow402(BillRefundStateEnum.退款中.equalsByCode(refundState), ErrorMessage.BILL_IS_OPERATING, BillRefundStateEnum.退款中.getCode());
        ErrorAssertUtil.isFalseThrow402(BillCarryoverStateEnum.待结转.equalsByCode(carriedState), ErrorMessage.BILL_IS_OPERATING, BillCarryoverStateEnum.待结转.getValue());
        ErrorAssertUtil.isFalseThrow402(BillReverseStateEnum.已冲销.equalsByCode(reversed == null ? BillReverseStateEnum.未冲销.getCode() : reversed), ErrorMessage.BILL_IS_OPERATING, BillReverseStateEnum.已冲销.getValue());
        ErrorAssertUtil.isFalseThrow402(InvoiceStateEnum.开票中.equalsByCode(invoiceState), ErrorMessage.BILL_IS_OPERATING, InvoiceStateEnum.valueOfByCode(invoiceState));
        ErrorAssertUtil.isTrueThrow402(BillStateEnum.正常.equalsByCode(state), ErrorMessage.BILL_IS_OPERATING, BillStateEnum.valueOfByCode(state));
    }

    /**
     * 获取审核中状态
     * @return 审核状态
     */
    public static List<Integer> getApprovingState(){
        return List.of(BillApproveStateEnum.待审核.getCode(), BillApproveStateEnum.审核中.getCode());
    }


    /**
     * 获取剩余可结转的金额
     * @return  可结转的金额 = 结算金额 - 退款金额 - 结转金额
     */
    public Long getRemainingCarriedAmount(){
        return settleAmount - refundAmount - carriedAmount - reverseAmount;
    }

    /**
     * 获取实际收款金额
     * @return  可结转的金额 = 结算金额 - 退款金额 - 结转金额 - 冲销金额
     */
    public Long getActualSettleAmount(){
        actualPayAmountNew=settleAmount - refundAmount - carriedAmount;
        return settleAmount - refundAmount - carriedAmount - reverseAmount;
    }

    /**
     * 获取实际应缴总金额
     *
     * @return long
     */
    public long getActualUnpayAmount() {
        return getRemainingSettleAmount();
    }

    /**
     * 计算实收金额
     */
    public long getActualPayAmount() {
        return getActualSettleAmount();
    }

    /**
     * 获取账单开票金额
     * @return 账单总的开票的金额 = 应收金额
     */
    public Long getAllInvoiceAmount(){
        return receivableAmount;
    }

    /**
     * 获取剩余可支付金额
     * @return  可支付金额 = 应收金额 + 违约金金额 - 实收减免金额 + 退款金额 + 结算金额（上次结算金额）-收款金额
     */
    public Long getRemainingSettleAmount(){
        return receivableAmount + overdueAmount - discountAmount + refundAmount + carriedAmount + reverseAmount - settleAmount;
    }

    /**
     * 根据父账单生成子账单清理其他相关数据(不包含金额处理)
     */
    public void copyReset() {
        this.id = null;
        this.billNo = null;
        resetState();
        this.payTime = null;
        this.approvedState = BillApproveStateEnum.已审核.getCode();
        this.payerLabel = null;
        this.outBillNo = null;
        this.outBusId = null;
        this.outBusNo = null;
        this.extField1 = null;
        this.extField2 = null;
        this.extField3 = null;
        this.extField4 = null;
        this.extField5 = null;
        this.extField6 = null;
        this.extField7 = null;
        this.extField8 = null;
        this.extField9 = null;
        this.extField10 = null;
    }

    /**
     * 重置账单所有的状态
     */
    public void resetState(){
        this.state = BillStateEnum.正常.getCode();
        this.onAccount = BillOnAccountEnum.未挂账.getCode();
        this.settleState = BillSettleStateEnum.未结算.getCode();
        this.refundState = BillRefundStateEnum.未退款.getCode();
        this.verifyState = BillVerifyStateEnum.未核销.getCode();
        this.approvedState = BillApproveStateEnum.待审核.getCode();
        this.carriedState = BillCarryoverStateEnum.未结转.getCode();
        this.invoiceState = BillInvoiceStateEnum.未开票.getCode();
        this.accountHanded = BillAccountHandedStateEnum.未交账.getCode();
        this.reversed = BillReverseStateEnum.未冲销.getCode();
        this.reconcileState = ReconcileResultEnum.未核对.getCode();
        this.mcReconcileState = ReconcileResultEnum.未核对.getCode();
    }

    /**
     * 重置金额
     * @param totalAmount
     */
    public void resetAmount(Long totalAmount){
        this.totalAmount = totalAmount;
        deductibleAmount = 0L;
        overdueAmount = 0L;
        discountAmount = 0L;
        settleAmount = 0L;
        refundAmount = 0L;
        carriedAmount = 0L;
        invoiceAmount = 0L;
        reverseAmount = 0L;
        resetReceivableAmount();
    }

    /**
     * 重置操作人信息
     */
    public void resetOperatorInfo(){
        operator = null;
        operatorName = null;
        gmtModify = null;
        creator = null;
        creatorName = null;
        gmtCreate = null;
    }

    /**
     * 重置应收金额
     */
    public void resetReceivableAmount(){
        if (SysSourceEnum.合同系统.getCode().equals(this.sysSource)
                && ("收入合同".equals(this.source) || "支出合同".equals(this.source))
                && this.totalAmount != this.receivableAmount) {
            // 合同系统调整过账单应收金额的账单收款对应收金额特殊处理
            this.receivableAmount = this.receivableAmount - Optional.ofNullable(this.deductibleAmount).orElse(0L);
            return;
        }
        this.receivableAmount = this.totalAmount - Optional.ofNullable(this.deductibleAmount).orElse(0L);
    }

    /**
     * 重置结转状态
     */
    public void resetCarriedState(){
        if (getCarriedAmount() <= 0L){
            setCarriedState(BillCarryoverStateEnum.未结转.getCode());
        }else {
            if (getCarriedAmount() + getRefundAmount() + getReverseAmount() < getSettleAmount()) {
                setCarriedState(BillCarryoverStateEnum.部分结转.getCode());
            }else {
                setCarriedState(BillCarryoverStateEnum.已结转.getCode());
            }
        }
    }

    /**
     * 重置结算状态
     */
    public void resetSettleState(){
            long remainingSettleAmount = getRemainingSettleAmount(); //当前已结算的金额
        if (remainingSettleAmount == 0){
            this.settleState = BillSettleStateEnum.已结算.getCode();
        }else if (remainingSettleAmount > 0 && settleAmount != 0L &&
                getActualSettleAmount() != 0L &&
                !BillCarryoverStateEnum.已结转.equalsByCode(this.carriedState)){
            setSettleState(BillSettleStateEnum.部分结算.getCode());
        } else {
            setSettleState(BillSettleStateEnum.未结算.getCode());
        }
    }

    public void refresh() {
        resetReceivableAmount();
        //更新结转状态
        resetCarriedState();
        //更新结算状态
        resetSettleState();
    }

    /**
     * 获取本次需要支付的金额
     * @param settleAmount 支付的金额
     * @return 账单本次可支付的金额
     */
    public long getCurrentSettleAmount(long settleAmount){
        //当前可支付的金额
        Long remainingSettleAmount = getRemainingSettleAmount();
        return remainingSettleAmount > settleAmount ? settleAmount : remainingSettleAmount;

    }


    /**
     * 设置费用类型（方圆定制）
     */
    public void setCostType() {
        if (Objects.nonNull(type) && Objects.isNull(billCostType)) {
            if (type == BillTypeEnum.预收账单.getCode()) {
                billCostType = BillCostTypeEnum.预收款项.getCode();
            }
            if (type == BillTypeEnum.临时收费账单.getCode()) {
                billCostType = BillCostTypeEnum.当期应收.getCode();
            }
            if (type == BillTypeEnum.应收账单.getCode()) {
                billCostType = setCostTypeByDate(accountDate);
            }
        }
    }

    public Integer setCostTypeByDate(LocalDate billTime) {
        if(Objects.nonNull(billTime)){
            int nowYear = LocalDate.now().getYear();
            int billYear = accountDate.getYear();
            if(nowYear < billYear){
                return BillCostTypeEnum.预收款项.getCode();
            }
            if(nowYear > billYear){
                return BillCostTypeEnum.历史欠费.getCode();
            }
            if(nowYear == billYear){
                int quarterNow = (LocalDate.now().getMonthValue() - 1) / 3 + 1;
                int quarterBill = (billTime.getMonthValue() - 1) / 3 + 1;
                if (quarterBill < quarterNow) {
                    return BillCostTypeEnum.历史欠费.getCode();
                }
                if (quarterBill == quarterNow) {
                    return BillCostTypeEnum.当期应收.getCode();
                }
                if (quarterBill > quarterNow) {
                    return BillCostTypeEnum.预收款项.getCode();
                }
            }
        }
        return null;
    }

    public Boolean isInit() {
        return null;
    }

    public void setInit(boolean isInit) {

    }


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }


}
