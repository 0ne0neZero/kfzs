package com.wishare.finance.apps.pushbill.vo;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@ApiModel("方圆汇总单据明细总金额")
public class VoucherBillDetailMoneyV {
    @ApiModelProperty(value = "含税金额")
    private BigDecimal taxIncludAmount;
    @ApiModelProperty(value = "不含税金额")
    private BigDecimal taxExcludAmount;
}