package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApplyBatchDeductionV {

    @ApiModelProperty("审批id")
    private List<Long> approveIds;

    @ApiModelProperty("账单调整id")
    private List<Long> billAdjustIds;

}