package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.apps.model.bill.fo.BillSettleChannelInfo;
import com.wishare.finance.domains.bill.consts.enums.BillCarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.RefundStateEnum;
import com.wishare.finance.domains.bill.support.PayInfosJSONListTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.consts.Const;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 管理预收账单业务数据，具体账单数据参考【账单表】
 *
 * @author yancao
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.ADVANCE_BILL)
@ToString
public class AdvanceBill extends Bill {

    public AdvanceBill() {
        setType(BillTypeEnum.预收账单.getCode());
    }

    /**
     * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
     */
    private Integer billMethod;

    /**
     * 计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积
     */
    private Integer billArea;

    /**
     * 计费面积
     */
    private BigDecimal chargingArea;

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
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 渠道交易单号
     */
    private String tradeNo;

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
     * 行号
     */
    @TableField(exist = false)
    private Long rowNumber;

    /**
     * 结算渠道
     */
    private String payChannel;

    /**
     * 结算方式(0线上，1线下)
     */
    private Integer payWay;

    /**
     * 税额（单位：分）
     */
    private Long taxAmount;

    /**
     * 优惠金额
     */
    private Long preferentialAmount = 0L;

    /**
     * 赠送金额
     */
    private Long presentAmount = 0L;

    /**
     * 优惠退款金额
     */
    private Long preferentialRefundAmount = 0L;

    /**
     * 付款方号码
     */
    private String payerPhone;

    @ApiModelProperty("支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端")
    @TableField(exist = false)
    private Integer paySource;

    @ApiModelProperty(value = "支付渠道商户号")
    @TableField(exist = false)
    private String mchNo;

    @ApiModelProperty(value = "支付渠道设备号")
    @TableField(exist = false)
    private String deviceNo;

    @ApiModelProperty(value = "银行流水号")
    @TableField(exist = false)
    private String bankFlowNo;

    @ApiModelProperty(value = "组合支付信息")
    @TableField(exist = false)
    private List<BillSettleChannelInfo> settleChannelInfos;

    /**
     * 业务单元id
     */
    private Long businessUnitId;

    /**
     * 空间路径字符串
     */
    private String path;

    /**
     * 冻结类型（0：无类型，1：通联银行代扣）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer freezeType;

//    @Override
//    public void generateIdentifier() {
//        if (Objects.isNull(getId())){
//            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.ADVANCE_BILL));
//        }
//    }

    /**
     * 重置账单周期
     */
    public void resetBillCycle(){
        setStartTime(null);
        setEndTime(null);
    }

    /**
     * 重置结算状态
     */
    @Override
    public void resetSettleState(){
        this.setSettleState(BillSettleStateEnum.已结算.getCode());
    }

    public boolean isEffectiveBill() {
        return (BillStateEnum.正常.equalsByCode(this.getState()) || BillStateEnum.冻结.equalsByCode(this.getState()))
                && !BillCarryoverStateEnum.已结转.equalsByCode(this.getCarriedState())
                && Const.State._0 == this.getDeleted()
                && Const.State._0 == this.getReversed()
                && Objects.isNull(this.getBillLabel())
                && !RefundStateEnum.未生效.getCode().equals(this.getRefundState());

    }
    @Override
    public void resetCarriedState(){
        if (getCarriedAmount() == 0L && getDeductibleAmount().equals((getPreferentialAmount()+getPresentAmount()))){
            setCarriedState(BillCarryoverStateEnum.未结转.getCode());
        }else if (BillSettleStateEnum.已结算.equalsByCode(getSettleState()) && (getCarriedAmount() + getRefundAmount()) == (getSettleAmount())
        && getDeductibleAmount() == 0L){
            setCarriedState(BillCarryoverStateEnum.已结转.getCode());
        }else {
            setCarriedState(BillCarryoverStateEnum.部分结转.getCode());
        }
    }

    @Override
    public boolean freezeBatchAddReason(Integer type) {
        super.freezeBatchAddReason(type);
        this.freezeType = type;
        return true;
    }

    /**
     * 获取剩余可结转的金额
     * @return  可结转的金额 = 结算金额 - 退款金额 - 结转金额 + 应收减免金额
     */
    @Override
    public Long getRemainingCarriedAmount(){
        return super.getRemainingCarriedAmount() + super.getDeductibleAmount();
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
    public void init() {
        super.init();
        generalTaxAmount();
    }

    private void generalTaxAmount() {
        if (taxAmount == null && getTaxRate() != null) {
            BigDecimal oneAddTaxrate = new BigDecimal(1).add(getTaxRate());

            //税额=(数量*含税单价)*税率/(1+税率）
            BigDecimal denominator = new BigDecimal(1).multiply(new BigDecimal(getReceivableAmount())).multiply(getTaxRate());
            BigDecimal tax = denominator.divide(oneAddTaxrate, 2, BigDecimal.ROUND_HALF_EVEN);
            this.taxAmount  = tax.longValue();
        }
    }
}
