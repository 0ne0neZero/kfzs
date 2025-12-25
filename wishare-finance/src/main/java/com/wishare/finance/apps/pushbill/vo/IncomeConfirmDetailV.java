package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 通用收入确认明细
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
public class IncomeConfirmDetailV {


    @ApiModelProperty("交易币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty("交易金额")
    private BigDecimal transactionAmount;

    @ApiModelProperty("交易汇率")
    private BigDecimal transactionHL;

    @ApiModelProperty("转账附言")
    private BigDecimal transferRemarks;
}
