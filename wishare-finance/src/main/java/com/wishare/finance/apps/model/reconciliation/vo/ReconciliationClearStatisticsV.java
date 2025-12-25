package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReconciliationClearStatisticsV {
    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty("账单结算金额（元）")
    private Long actualTotal;

    @ApiModelProperty("交易流水金额（元）")
    private Long flowClaimTotal;

    @ApiModelProperty("手续费(元)")
    private Long commission;
}
