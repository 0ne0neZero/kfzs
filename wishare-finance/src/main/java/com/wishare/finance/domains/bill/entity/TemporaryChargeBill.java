package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.BillReferenceStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.support.PayInfosJSONListTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 管理应收账单业务数据，具体账单数据参考【账单表】
 *
 * @author dxclay
 * @since 2022-08-16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.TEMPORARY_CHARGE_BILL,autoResultMap = true)
public class TemporaryChargeBill extends Bill {

    public TemporaryChargeBill() {
        setType(BillTypeEnum.临时收费账单.getCode());
    }

    /**
     * 应收账单类型
     */
    private Integer billType = BillTypeEnum.临时收费账单.getCode();

    /**
     * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
     */
    private Integer billMethod;

    /**
     * 计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积
     */
    private Integer billArea;

    /**
     * 是否逾期：0未逾期，1已逾期
     */
    private Integer overdueState;

    /**
     * 计费额度
     */
    private BigDecimal chargingArea;

    /**
     * 计费数量
     */
    private Integer chargingCount;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactPhone;

    /**
     * 费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型
     */
    private Integer chargeItemType;

    /**
     * 是否引用：0未被引用，1已被引用
     */
    private Integer referenceState;

    /**
     * 收付类型：0收款（临时收款），1付款（临时付款）
     */
    private String payType;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    @TableField(exist = false)
    private String settleChannel;

    /**
     * 结算方式(0线上，1线下)
     */
    @TableField(exist = false)
    private Integer settleWay;

    /**
     * 税额（单位：分）
     */
    private Long taxAmount;

    /**
     * 税额（单位：分）
     */
    private BigDecimal taxAmountNew;

    /**
     * 账单已缴时间
     */
    private LocalDateTime chargeTime;

    /**
     * 业务单元id
     */
    private Long businessUnitId;

    /**
     * 支付信息
     */
    @TableField(typeHandler = PayInfosJSONListTypeHandler.class, javaType = true)
    private List<PayInfo> payInfos;

    /**
     * 付款人手机号码
     */
    private String payerPhone;

    /**
     * 扣款金额
     */
    private Long deductionAmount = 0L;

    /**
     * 业务类型编码
     */
    private String businessCode;

    /**
     * 业务类型名称
     */
    private String businessName;

    /**
     * 是否是违约金：0-否/1-是
     */
    private Integer overdue = 0;

    /**
     * 关联账单id
     */
    private Long refBillId;

    /**
     * 优惠金额
     */
    private Long preferentialAmount = 0L;

    /**
     * 优惠退款金额
     */
    private Long preferentialRefundAmount = 0L;

    /**
     * bpm跳收状态：0正常，1跳收审核中，2跳收审核通过3跳收拒绝
     */
    private Integer jumpState;

    /**
     * 冻结类型（0：无类型，1：通联银行代扣）
     */
    private Integer freezeType;

    /**
     * 调整金额是否准确（账单金额是否按照计费方式计算）1 是 2 否
     */
    private Integer isExact;

    /**
     * 是否初始化账单
     */
    private Boolean isInit;

    /**
     * 应收日
     */
    private Integer receivableDay;


    /**
     * 应收日(包含年月日)
     */
    private LocalDate receivableDate;

    /**
     * 空间路径
     */
    private String path;

    @ApiModelProperty("是否签约")
    private Boolean isSign;

    /**
     * 合同编号
     */
    private String contractNo;

    // 审核信息
    /**
     * 审核原因
     */
    @TableField(exist = false)
    private String oprReason;
    /**
     * 审核操作类型
     */
    @TableField(exist = false)
    private Integer oprType;
    /**
     * 审核备注
     */
    @TableField(exist = false)
    private String oprRemark;

    /**
     * 合同名称
     */
    private String contractName;


    /**
     * 收款方手机号
     */
    @ApiModelProperty("收款方手机号")
    private String payeePhone;

    @ApiModelProperty(value = "计提状态： 0-未计提， 1-计提中， 2-已计提")
    private Integer provisionStatus;

    @ApiModelProperty(value = "确收状态： 0-未确收， 1-确收中， 2-已确收")
    private Integer receiptConfirmationStatus;

    @ApiModelProperty(value = "收入报表使用-合同 业务系统-确收状态： 0-未确收，1-确收中，2-已确收")
    private Integer businessReceiptConfirmationStatus;

    @ApiModelProperty(value = "确收时间")
    private LocalDateTime receiptConfirmationTime;

    @ApiModelProperty(value = "结算状态： 0-未结算， 1-结算中， 2-已结算")
    private Integer settlementStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer provisionVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer settlementVoucherPushingStatus;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer receiptConfirmationVoucherPushingStatus;

    @ApiModelProperty(value = "支付申请单状态 0-未锁定， 1-锁定中， 2-已锁定")
    private Integer payAppStatus;

    @ApiModelProperty(value = "支付申请单 是否推凭：0未推凭，1已推凭")
    private Integer payAppPushStatus;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal reductionAmount;

    @ApiModelProperty(value = "外部系统账单id")
    private String uniqueId;

    /**
     * 设置引用
     * @param referenceState
     * @return
     */
    public boolean reference(BillReferenceStateEnum referenceState){
        this.referenceState = referenceState.getCode();
        return true;
    }

    /**
     * 重置账单周期
     */
    public void resetBillCycle(){
       setStartTime(null);
       setEndTime(null);
    }

    @Override
    public void refresh() {
        super.refresh();
        resetChargeTime();
    }

    /**
     * 重置缴费时间
     */
    public void resetChargeTime() {
        if (Objects.isNull(getSettleAmount()) || getSettleAmount() == 0L) {
            this.chargeTime = getStartTime();
            return;
        }
        if (getRemainingSettleAmount() == 0L) {
            this.chargeTime = getEndTime();
            return;
        }
        // 应收款金额
        long needCashAmount = getReceivableAmount() - Optional.ofNullable(getDiscountAmount()).orElse(0L);
        // 实际账单已缴金额
        long actualSettleAmount = getActualSettleAmount();
        // 账单计费周期天数
        long days = Duration.between(getStartTime(), getEndTime()).toDays() + 1;
        long chargeDays = (actualSettleAmount * days) / needCashAmount;
        long overFlowAmount = (actualSettleAmount * days) % needCashAmount;
        if (overFlowAmount > 0) {
            chargeDays++;
        }
        this.chargeTime = chargeDays > 0 ? getStartTime().plusDays(chargeDays).minusSeconds(1) : getStartTime();
    }

    /**
     * 获取剩余可结转的金额
     * @return  可结转的金额 = 结算金额 - 退款金额 - 结转金额
     */
    @Override
    public Long getRemainingCarriedAmount(){
        return super.getRemainingCarriedAmount() - deductionAmount;
    }

    @Override
    public boolean unfreeze() {
        boolean superResult = super.unfreeze();
        if (superResult) {
            this.freezeType = null;
        }
        return superResult;
    }

    @Override
    public Boolean isInit() {
        return this.isInit;
    }

    @Override
    public void setInit(boolean isInit) {
        this.isInit = isInit;
    }

    public void generalTaxAmount() {
        if (taxAmount == null && getTaxRate() != null) {
            BigDecimal oneAddTaxrate = new BigDecimal(1).add(getTaxRate());

            //税额=(数量*含税单价)*税率/(1+税率）
            BigDecimal denominator = new BigDecimal(1).multiply(new BigDecimal(getReceivableAmount())).multiply(getTaxRate());
            BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
            this.taxAmount  = tax.longValue();
        }
    }
}
