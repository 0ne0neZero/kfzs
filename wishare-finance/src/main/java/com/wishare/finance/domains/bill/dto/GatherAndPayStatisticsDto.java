package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:
 */
@Getter
@Setter
@ApiModel("收付款记录统计")
public class GatherAndPayStatisticsDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("收款单总计")
    private Long gatherAmountSum = 0L;

    @ApiModelProperty("付款单总计")
    private Long payAmountSum = 0L;

    public GatherAndPayStatisticsDto(Long gatherAmountSum, Long payAmountSum) {
        this.gatherAmountSum = gatherAmountSum != null ? gatherAmountSum : 0L;
        this.payAmountSum = payAmountSum != null ? payAmountSum : 0L;
    }
}
