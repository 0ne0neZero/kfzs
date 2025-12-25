package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 支付申请单-款项明细(PaymentApplicationFormKxmx)实体类
 *
 * @since 2025-01-10 17:36:52
 */
@Getter
@Setter
@TableName(value = TableNames.PAYMENT_APPLICATION_FORM_PAY_MX, autoResultMap = true)
public class PaymentApplicationFormPayMx extends BaseEntity {
    private static final long serialVersionUID = 314304426079833324L;
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 交易币种
     */
    private String currency;
    /**
     * 交易金额
     */
    private BigDecimal transactionAmount;
    /**
     * 交易汇率
     */
    private BigDecimal transactionHL;
    /**
     * 转账附言
     */
    private String transferRemarks;

    private String payAppId;

}

