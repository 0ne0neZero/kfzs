package com.wishare.finance.apps.model.configure.arrearsCategory.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@ApiModel("创建欠费类别数据")
public class CreateArrearsCategoryF {

    @ApiModelProperty(value = "欠费类别名称", required = true)
    @NotBlank(message = "欠费类别名称不能为空")
    private String arrearsCategoryName;

    @ApiModelProperty(value = "是否末级：0否,1是")
    @NotNull(message = "是否末级不能为空")
    private Integer lastLevel;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("父级类别名称")
    private String parentName;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    @NotNull(message = "启用状态不能为空")
    private Integer status;

    public String getParentName() {
        return parentName == null ? "" : parentName;
    }
}
