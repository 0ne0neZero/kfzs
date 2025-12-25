package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@ApiModel("其他金额")
public class OtherAmountRv {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("其他金额名称")
    private String OtherAmountName;

    @ApiModelProperty("其他金额单价")
    private BigDecimal price;

    @ApiModelProperty("其他金额数量")
    private Long num;

    @ApiModelProperty("其他金额小计（单位：分）")
    private BigDecimal otherAmount;

    @ApiModelProperty("其他金额备注")
    private String remark;
}
