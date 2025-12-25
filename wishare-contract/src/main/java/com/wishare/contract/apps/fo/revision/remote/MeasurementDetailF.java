package com.wishare.contract.apps.fo.revision.remote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@ApiModel("计量明细信息")
public class MeasurementDetailF {
    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("费项Id")
    private Long chargeItemId;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("合同清单项目")
    private String contractItem;

    @ApiModelProperty("含税金额(单位：分)")
    private Long taxIncludedAmount;

    @ApiModelProperty("不含税金额(单位：分)")
    private Long taxExcludedAmount;
}
