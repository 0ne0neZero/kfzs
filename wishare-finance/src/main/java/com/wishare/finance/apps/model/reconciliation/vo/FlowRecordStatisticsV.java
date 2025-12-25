package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("认领记录统计")
public class FlowRecordStatisticsV {

    @ApiModelProperty("认领流水金额")
    private BigDecimal claimAmount;

    /**
     * 认领单据金额
     */
    @ApiModelProperty("认领单据金额")
    private BigDecimal settleAmount;

    /**
     * 认领差额
     */
    @ApiModelProperty("认领差额")
    private BigDecimal compareAmount;
}
