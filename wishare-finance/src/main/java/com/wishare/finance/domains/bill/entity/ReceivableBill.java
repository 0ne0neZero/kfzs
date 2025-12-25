package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.support.PayInfosJSONListTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 管理应收账单业务数据，具体账单数据参考【账单表】
 * @author dxclay
 * @since 2022-08-16
 */
@Getter
@Setter()
@Accessors(chain = true)
@TableName(value = TableNames.RECEIVABLE_BILL,autoResultMap = true)
@ToString
public class ReceivableBill extends Bill {

    public ReceivableBill() {
        setType(BillTypeEnum.应收账单.getCode());
    }

    /**
     * 应收账单类型
     */
    private Integer billType = BillTypeEnum.应收账单.getCode();

    /**
     * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
     */
    private Integer billMethod;

    /**
     * 计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积
     */
    private Integer billArea;

    /**
     * 计费数量，如计费方式为单价*数量时，计费
     */
    private Integer chargingCount;

    /**
     * 计费额度
     */
    private BigDecimal chargingArea;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 税额（单位：分）
     */
    private Long taxAmount;

    /**
     * 税额（单位：分）
     */
    private BigDecimal taxAmountNew;

    /**
     * 是否逾期：0未逾期，1已逾期
     */
    private Integer overdueState;

    /**
     * 账单已缴时间
     */
    private LocalDateTime chargeTime;

    /**
     * 付款方号码
     */
    private String payerPhone;

    /**
     * 扣款金额
     */
    private Long deductionAmount = 0L;

    /**
     * 支付信息
     */
    @TableField(typeHandler = PayInfosJSONListTypeHandler.class, javaType = true)
    private List<PayInfo> payInfos;

    /**
     * 费项类型
     */
    private Integer chargeItemType;

    /**
     * 业务类型编码
     */
    private String businessCode;

    /**
     * 业务单元id
     */
    private Long businessUnitId;

    /**
     * 业务类型名称
     */
    private String businessName;

    /**
     * 优惠金额
     */
    private Long preferentialAmount = 0L;

    /**
     * 优惠退款金额
     */
    private Long preferentialRefundAmount = 0L;

    /**
     * 是否是违约金：0-否/1-是
     */
    private Integer overdue = 0;

    /**
     * 关联账单id
     */
    private Long refBillId;

    /**
     * bpm跳收状态：0正常，1跳收审核中，2跳收审核通过3跳收拒绝
     */
    private Integer jumpState;

    /**
     * 冻结类型（0：无类型，1：通联银行代扣）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer freezeType;

    /**
     * 应收日
     */
    private Integer receivableDay;


    /**
     * 应收日(包含年月日)
     */
    private LocalDate receivableDate;

    /**
     * 调整金额是否准确（账单金额是否按照计费方式计算）1 是 2 否
     */
    private Integer isExact;

    /**
     * 是否初始化账单
     */
    private Boolean isInit;

    @TableField(exist = false)
    private String payStr;

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
     * 空间路径字符串
     */
    private String path;

    @ApiModelProperty("是否签约")
    private Boolean isSign;

    /**
     * 收款方手机号
     */
    @ApiModelProperty("收款方手机号")
    private String payeePhone;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "计提状态： 0-未计提， 1-计提中， 2-已计提")
    private Integer provisionStatus;

    @ApiModelProperty(value = "确收状态： 0-未确收， 1-确收中， 2-已确收")
    private Integer receiptConfirmationStatus;

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

    /**
     * 应收日先取ExtField8, 为空的话取receivableDate, receivableDate为空的话取结束时间
     * IFNULL(b.ext_field8,DATE_FORMAT(b.end_time, '%Y-%m-%d'))
     * @return
     */
    public LocalDate getReceivableDate() {
        LocalDate resDate = this.receivableDate;
        if (Objects.nonNull(resDate)) {
            return resDate;
        }

        try {
            if (getExtField8() != null){
                resDate = LocalDate.parse(getExtField8(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }catch (Exception e){
        }

        final LocalDateTime endTime = getEndTime();
        return  Objects.nonNull(resDate) ? resDate : Objects.isNull(endTime)? null : endTime.toLocalDate();
    }

    @Override
    public void init() {
        super.init();
        generalTaxAmount();
    }

    public void initPayInfos(Long payAmount,Integer payWay,String payChannel){
        PayInfo payInfo = new PayInfo(payWay,payChannel,payAmount);
        this.payInfos = List.of(payInfo);
    }

    /**
     * 设置税额，如果传了则覆盖，没传则计算
     */
    private void generalTaxAmount() {
        if (taxAmount == null && getTaxRate() != null) {
            BigDecimal oneAddTaxrate = new BigDecimal(1).add(getTaxRate());

            //税额=(数量*含税单价)*税率/(1+税率）
            BigDecimal denominator = new BigDecimal(1).multiply(new BigDecimal(getReceivableAmount())).multiply(getTaxRate());
            BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
            this.taxAmount  = tax.longValue();
        }
    }

//    @Override
//    public void generateIdentifier() {
//        if (Objects.isNull(getId())){
//            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.TEMPORARY_CHARGE_BILL));
//        }
//    }

    /**
     * 操作后刷新账单信息
     */
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

        if (getRemainingSettleAmount() <= 0L) {
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
    public boolean freezeBatchAddReason(Integer type) {
        super.freezeBatchAddReason(type);
        this.freezeType = type;
        return true;
    }

    @Override
    public Boolean isInit() {
        return this.isInit;
    }

    @Override
    public void setInit(boolean isInit) {
        this.isInit = isInit;
    }


    /*
    未收违约金 = 应收-实收-减免
     */
    public Long getNotReceivedOverdueAmount() {
        if (overdue == 0) {
            //不是违约金账单没有未收违约金
            return 0L;
        }
        return getReceivableAmount() - getSettleAmount() - getDiscountAmount();
    }

    /**
     * 计算违约金金额 （目前只针对未结算/未减免的应收账单）
     * @return 违约金金额
     */
    public Long calculateOverdue() {

        if (StringUtils.isAnyBlank(getExtField1(), getExtField2(), getExtField5())) {
            return null;
        }

        // 获取违约天数
        LocalDateTime overdueBeginDate = getOverdueBeginDate();
        if (overdueBeginDate.toLocalDate().isAfter(LocalDate.now())) {
            return 0L;
        }
        long days = Duration.between(overdueBeginDate, LocalDateTime.now()).toDays() + 1;

        // 计算每日违约金额
        BigDecimal lateFeeRate = new BigDecimal(getExtField5());
        long dayOverdue = lateFeeRate
                .multiply(BigDecimal.valueOf(getActualUnpayAmount()))
                .divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 0, RoundingMode.HALF_UP).longValue();
        return dayOverdue * days;
    }

    /**
     * 获取违约金起算时间
     * @return 违约金起算日期
     */
    public LocalDateTime getOverdueBeginDate() {
        if (StringUtils.isBlank(getExtField2())) {
            return null;
        }
        LocalDateTime result ;
        if (getExtField2().contains(":")) {
            result = LocalDateTime.parse(getExtField2(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }else {
            result = LocalDate.parse(getExtField2(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        }
        return result;
    }
}
