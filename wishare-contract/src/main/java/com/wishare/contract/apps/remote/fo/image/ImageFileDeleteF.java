package com.wishare.contract.apps.remote.fo.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/10/18/11:49
 */
@Data
@ApiModel("删除影像系统参数列表")
public class ImageFileDeleteF {

    /**
     * 安全校验节点
     */
    @ApiModelProperty("安全校验节点")
    @NotNull(message = "安全校验节点不能为空")
    @JsonProperty("safety")
    private SafetyF safety;

    /**
     * 业务参数对象节点
     */
    @ApiModelProperty("业务参数对象节点")
    @JsonProperty("params")
    private ImageFileDeleteParamF params;
}
