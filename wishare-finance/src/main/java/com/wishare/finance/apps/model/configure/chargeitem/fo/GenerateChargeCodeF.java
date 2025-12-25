package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 生成费项编码入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("生成费项编码入参")
public class GenerateChargeCodeF {

    @ApiModelProperty(value = "父费项id", required = true)
    @NotNull(message = "父费项id不能为空")
    private Long parentChargeId;

    @ApiModelProperty(value = "类型：1同级费项，2子级费项", required = true)
    @NotNull(message = "类型不能为空")
    private Integer type;

}
