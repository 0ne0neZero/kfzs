package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VoucherBillZJMoneyV {

    @ApiModelProperty(value = "推单总金额")
    private BigDecimal money;
}
