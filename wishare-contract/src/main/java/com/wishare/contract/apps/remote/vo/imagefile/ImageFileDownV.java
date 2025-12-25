package com.wishare.contract.apps.remote.vo.imagefile;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/10/18/11:49
 */
@Data
@ApiModel("下载影像系统参数列表")
public class ImageFileDownV {

    /**
     * 状态
     */
    @ApiModelProperty("状态")
    @JsonProperty("status")
    private Boolean status;

    /**
     * 错误编码
     */
    @ApiModelProperty("错误编码")
    @JsonProperty("errorCode")
    private String errorCode;

    /**
     * 错误编码
     */
    @ApiModelProperty("错误信息")
    @JsonProperty("errorMessage")
    private String errorMessage;

    /**
     * 错误编码
     */
    @ApiModelProperty("返回信息")
    @JsonProperty("data")
    private ImageFileDataV data;
}
