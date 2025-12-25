package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value = "contract_template", description = "分页查询合同范本入参信息")
public class PageContractTemplateF{

    @ApiModelProperty("范本名称")
    private String name;

    @ApiModelProperty("合同分类id")
    private Long categoryId;
}
