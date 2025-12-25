package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 报账明细
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
public class PaymentApplicationZFDetailV {


    @ApiModelProperty("交易币种(货币代码)（CNY:人民币）")
    private String currency = "人民币";

    @ApiModelProperty("交易金额")
    private BigDecimal transactionAmount;

    @ApiModelProperty("交易汇率")
    private BigDecimal transactionHL;

    @ApiModelProperty("转账附言")
    private String transferRemarks;
}
