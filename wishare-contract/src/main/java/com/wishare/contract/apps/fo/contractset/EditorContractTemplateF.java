package com.wishare.contract.apps.fo.contractset;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("编辑合同范本入参信息")
public class EditorContractTemplateF {

    @ApiModelProperty(value = "范本id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "父级范本id",required = true)
    @NotNull
    private Long parentId;

    @ApiModelProperty(value = "合同范本名称",required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "合同分类id",required = true)
    @NotNull
    private Long categoryId;

    @ApiModelProperty(value = "是否变更了文件 0否 1是",required = true)
    @NotNull
    private Integer changeFile;

    @ApiModelProperty("文件名,changeFile为0时传，否则为空")
    private String fileName;

    @ApiModelProperty("文件url,changeFile为0时传，否则为空")
    private String fileUrl;

    @ApiModelProperty("文件,changeFile为1时传，否则为空")
    private FileVo file;

    @ApiModelProperty("备注描述")
    private String remark;

    @ApiModelProperty(value = "状态：0启用，1草稿，2禁用",required = true)
    @NotNull
    private Integer status;

    @ApiModelProperty(value = "合同分类路径名",required = true)
    @NotNull
    private String categoryPathName;

}
