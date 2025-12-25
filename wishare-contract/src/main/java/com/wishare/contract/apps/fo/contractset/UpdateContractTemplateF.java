package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("启用/禁用合同范本入参信息")
public class UpdateContractTemplateF {

    @ApiModelProperty(value = "范本id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "状态：0启用，2禁用",required = true)
    @NotNull
    private Integer status;
}
