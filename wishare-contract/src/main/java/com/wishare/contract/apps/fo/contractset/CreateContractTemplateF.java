package com.wishare.contract.apps.fo.contractset;


import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("创建合同范本入参信息")
public class CreateContractTemplateF {

    @ApiModelProperty(value = "合同范本名称",required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "合同分类id",required = true)
    @NotNull
    private Long categoryId;

    @ApiModelProperty("父合同范本id,创建时默认为0")
    private Long parentId;

    @ApiModelProperty(value = "文件",required = true)
    @NotBlank
    private FileVo file;

    @ApiModelProperty("备注描述")
    private String remark;

    @ApiModelProperty(value = "状态：0启用，1草稿，2禁用",required = true)
    @NotNull
    private Integer status;

    @ApiModelProperty("合同分类路径名")
    @NotNull
    private String categoryPathName;

}
