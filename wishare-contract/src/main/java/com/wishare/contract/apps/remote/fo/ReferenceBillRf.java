package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("设置应用请求参数")
public class ReferenceBillRf {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "引用状态 0未被引用，1已被引用", required = true)
    @NotNull(message = "引用状态为空")
    private Integer referenceState;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;
}
