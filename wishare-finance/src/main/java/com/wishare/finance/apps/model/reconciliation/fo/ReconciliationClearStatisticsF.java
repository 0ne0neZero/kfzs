package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReconciliationClearStatisticsF {
    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty("账单结算金额（元）")
    private String actualTotal;

    @ApiModelProperty("交易流水金额（元）")
    private String flowClaimTotal;

    @ApiModelProperty("手续费(元)")
    private String commission;
}
