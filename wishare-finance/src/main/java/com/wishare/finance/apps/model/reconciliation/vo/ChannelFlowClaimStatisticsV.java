package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@ApiModel("支付流水数据统计")
public class ChannelFlowClaimStatisticsV {
    @ApiModelProperty("交易流水金额（元）")
    private BigDecimal flowClaimTotal;

    @ApiModelProperty("手续费(元)")
    private BigDecimal commission;
}
