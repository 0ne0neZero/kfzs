package com.wishare.contract.apps.remote.fo.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 影像系统接受附件接口入参
 *
 * @author 龙江锋
 * @date 2023/8/8 13:39
 */
@Data
@ApiModel("影像系统接受附件接口入参")
public class ImageFileF {
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
    private ImageParams params;
}
