package com.wishare.contract.apps.remote.vo.imagefile;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("下载影像系统返回参数列表")
public class ImageFileDataV {

    /**
     * 文件下载地址
     */
    @ApiModelProperty("文件下载地址")
    @JsonProperty("downUrl")
    private String downUrl;


    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    @JsonProperty("filename")
    private String filename;

}
