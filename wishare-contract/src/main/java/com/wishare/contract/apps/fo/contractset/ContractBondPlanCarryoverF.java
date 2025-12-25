package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("保证金计划结转入参")
@Data
public class ContractBondPlanCarryoverF {

    @ApiModelProperty("合同id")
    @NotNull
    private Long contractId;

    @ApiModelProperty("保证金计划id")
    @NotNull
    private Long contractBondPlanId;

    @ApiModelProperty("结转金额")
    @NotNull
    private BigDecimal carryoverAmount;

    @ApiModelProperty("备注")
    private String remark;
}
