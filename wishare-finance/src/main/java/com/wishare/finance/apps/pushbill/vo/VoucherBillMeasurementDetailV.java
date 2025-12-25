package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("对下结算单下的 计量清单")
public class VoucherBillMeasurementDetailV {

    @ApiModelProperty("合同清单项目")
    private String contractItem;

    @ApiModelProperty(value = "含税金额")
    private Double taxIncludAmount;

    @ApiModelProperty("不含税金额")
    private Double taxExcludedAmount;
}
