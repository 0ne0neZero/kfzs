package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 创建合同分类入参信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("创建合同分类入参信息")
public class CreateContractCategoryF {

    @ApiModelProperty(value = "合同分类名称", required = true)
    @NotBlank(message = "合同分类名称不能为空")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("父分类id,第一级不传默认为0")
    private Long parentId;

    @ApiModelProperty("中台业务类型id")
    private Long natureTypeId;
    @ApiModelProperty("中台业务类型code")
    private String natureTypeCode;
    @ApiModelProperty("中台业务类型名称")
    private String natureTypeName;
}
