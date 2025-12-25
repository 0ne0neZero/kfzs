package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReconciliationStatisticsF {
    @ApiModelProperty("金额单位")
    private String amountUnit = "元";
    @ApiModelProperty("实收金额")
    private String actualTotal;
    @ApiModelProperty("流水认领金额")
    private String flowClaimTotal;
    @ApiModelProperty("开票金额（元）")
    private String invoiceTotal;
    @ApiModelProperty("应收金额(元)")
    private String receivableAmount;
    @ApiModelProperty("退款/结转金额(元)")
    private String carriedAmount;
}
