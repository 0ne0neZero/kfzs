package com.wishare.contract.apps.fo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 删除文件入参
 *
 * @author yancao
 */
@Data
@ApiModel("删除文件入参")
public class DeleteFileF {

    @ApiModelProperty(value = "文件key")
    @NotBlank(message = "文件key不能为空")
    private String fileKey;
}
