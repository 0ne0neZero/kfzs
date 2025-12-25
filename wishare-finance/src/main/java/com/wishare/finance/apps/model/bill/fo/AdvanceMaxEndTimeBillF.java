package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author fengxiaolin
 * @date 2023/6/5
 */
@Getter
@Setter
@ApiModel("预收账单查询最大账单结束时间入参")
public class AdvanceMaxEndTimeBillF {

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
