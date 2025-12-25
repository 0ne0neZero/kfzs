package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 最大账单周期查询
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/22
 */
@Getter
@Setter
@ApiModel("最大账单周期查询")
public class MaxGatherTimeQuery {

    @ApiModelProperty(value = "上级收费单元id（如:项目）", required = true)
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "收费单元id（如:房号）", required = true)
    @NotBlank(message = "收费单元id不能为空")
    private String cpUnitId;

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private String chargeItemId;

}
