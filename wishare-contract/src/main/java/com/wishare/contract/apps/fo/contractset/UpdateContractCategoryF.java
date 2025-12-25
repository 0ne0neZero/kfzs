package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 更新合同分类入参信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("更新合同分类入参信息")
public class UpdateContractCategoryF {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "合同分类名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "合同分类路径名")
    private String categoryPathName;

    @ApiModelProperty("中台业务类型id")
    private Long natureTypeId;
    @ApiModelProperty("中台业务类型code")
    private String natureTypeCode;
    @ApiModelProperty("中台业务类型名称")
    private String natureTypeName;

    @ApiModelProperty("是否具有采购事项，1是，0否")
    private Integer isBuy;
}
