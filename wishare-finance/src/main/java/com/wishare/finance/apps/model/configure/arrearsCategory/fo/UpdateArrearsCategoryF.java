package com.wishare.finance.apps.model.configure.arrearsCategory.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@ApiModel("更新欠费类别")
public class UpdateArrearsCategoryF {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

//    @ApiModelProperty(value = "父费项id")
//    private Long parentId;

    @ApiModelProperty("欠费类别名称")
    private String arrearsCategoryName;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty("是否禁用 0-禁用;1-启用;")
    private Integer status;

}
