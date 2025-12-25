package com.wishare.finance.apps.pushbill.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class VoucherBillZJConvertDetailV {

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("项目名称")
    private String chargeItemName;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("收款金额")
    private BigDecimal settleAmount;
}
