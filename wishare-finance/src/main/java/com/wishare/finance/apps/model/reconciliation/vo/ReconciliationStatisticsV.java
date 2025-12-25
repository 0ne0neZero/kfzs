package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReconciliationStatisticsV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "元";
    @ApiModelProperty("实收金额")
    private Long actualTotal;
    @ApiModelProperty("流水认领金额")
    private Long flowClaimTotal;
    @ApiModelProperty("开票金额（元）")
    private Long invoiceTotal;
    @ApiModelProperty("应收金额(元)")
    private Long receivableAmount;
    @ApiModelProperty("退款/结转金额(元)")
    private Long carriedAmount;

}
