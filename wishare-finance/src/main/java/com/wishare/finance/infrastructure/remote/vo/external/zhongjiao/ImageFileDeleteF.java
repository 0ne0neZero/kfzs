package com.wishare.finance.infrastructure.remote.vo.external.zhongjiao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("删除影像系统参数列表")
public class ImageFileDeleteF {

    /**
     * 安全校验节点
     */
    @ApiModelProperty("安全校验节点")
    @NotNull(message = "安全校验节点不能为空")
    private SafetyF safety;

    /**
     * 业务参数对象节点
     */
    @ApiModelProperty("业务参数对象节点")
    private ImageFileDeleteParamF params;
}