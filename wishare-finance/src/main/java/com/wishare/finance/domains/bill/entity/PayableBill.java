package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 应付账单
 *
 * @author yancao
 */
@Getter
@Setter()
@Accessors(chain = true)
@TableName(TableNames.PAYABLE_BILL)
@ToString
public class PayableBill extends Bill {

    public PayableBill() {
        setType(BillTypeEnum.应付账单.getCode());
    }

    /**
     * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
     */
    private Integer billMethod;

    /**
     * 计费面积
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
     * 账单已缴时间
     */
    private LocalDateTime chargeTime;

    /**
     * 收款方账户
     */
    private String payeeAccount;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    @TableField("")
    private String settleChannel;

    /**
     * 业务类型编码
     */
    private String businessCode;

    /**
     * 业务类型名称
     */
    private String businessName;

    @Override
    public void init() {
        super.init();
        generalTaxAmount();
    }

    /**
     * 设置税额，如果传了则覆盖，没传则计算
     */
    private void generalTaxAmount() {
        if (taxAmount == null && getTaxRate() != null) {
            BigDecimal multiply = new BigDecimal(getReceivableAmount()).multiply(getTaxRate());
            this.taxAmount = multiply.longValue();
        }
    }

//    @Override
//    public void generateIdentifier() {
//        if (Objects.isNull(getId())){
//            setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.TEMPORARY_CHARGE_BILL));
//        }
//    }

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
        if (Objects.isNull(getStartTime()) || Objects.isNull(getEndTime()) ||
                Objects.isNull(getSettleAmount()) || getSettleAmount() == 0L) {
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
        this.chargeTime = getStartTime().plusDays(chargeDays - 1);
    }
}
