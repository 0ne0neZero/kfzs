package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel("账单合计信息")
public class BillTotalRv {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "房号总数", required = true)
    private Long roomTotal;

    @ApiModelProperty(value = "账单总数", required = true)
    private Long billTotal;

    @ApiModelProperty(value = "账单总金额 （单位：分）", required = true)
    private Long amountTotal;

    @ApiModelProperty(value = "账单应收总金额 （单位：分）", required = true)
    private Long receivableAmountTotal;

    @ApiModelProperty(value = "应收减免总金额 （单位：分）", required = true)
    private Long deductibleAmountTotal;

    @ApiModelProperty(value = "实收总金额 （单位：分）", required = true)
    private Long settleAmountTotal;

    @ApiModelProperty(value = "实收减免金额 （单位：分）", required = true)
    private Long discountAmountTotal;

    @ApiModelProperty(value = "退款总金额（单位：分）",required = true)
    private Long refundAmountTotal;
}
