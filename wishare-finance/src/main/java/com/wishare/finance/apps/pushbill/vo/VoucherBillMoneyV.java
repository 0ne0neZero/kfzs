package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("方圆汇总单据总金额")
public class VoucherBillMoneyV {

    @ApiModelProperty(value = "推单总金额")
    private BigDecimal money;
}