package com.wishare.finance.apps.pushbill.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付明细
 * @Author dengjie03
 * @Description
 * @Date 2025-01-15
 */
@Data
public class PaymentApplicationFormPayMxV {
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
