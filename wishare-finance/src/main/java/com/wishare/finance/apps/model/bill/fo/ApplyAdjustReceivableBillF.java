package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@ApiModel("调整应收账单信息")
public class ApplyAdjustReceivableBillF extends ApplyAdjustBillF {

    @ApiModelProperty(value = "计费面积 (单位：m²)")
    private BigDecimal chargingArea;


}
