package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class VoucherBillZJConvertMoneyV {

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("收款金额")
    private BigDecimal settleAmount;
}
