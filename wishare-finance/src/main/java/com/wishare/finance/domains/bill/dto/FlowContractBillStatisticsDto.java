package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 统计合同流水金额信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("统计合同流水金额信息")
public class FlowContractBillStatisticsDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("账单数")
    private Long billTotal;

    @ApiModelProperty("账单金额")
    private Long billAmountTotal;

    public FlowContractBillStatisticsDto() {
        billTotal = 0L;
        billAmountTotal = 0L;
    }
}
