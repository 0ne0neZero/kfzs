package com.wishare.contract.domains.vo.revision.projectInitiation.cost;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础响应类
 */
@Data
public class CostBaseResponse<T> {

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "响应状态码", example = "0")
    private String retCode;

    @ApiModelProperty(value = "响应消息", example = "成功")
    private String message;

    @ApiModelProperty("扩展状态码")
    private String extraCode;

    @ApiModelProperty("扩展消息")
    private String extraMessage;

    @ApiModelProperty("跟踪堆栈")
    private String tracestack;

    @ApiModelProperty(value = "是否成功", example = "true")
    private Boolean success;

    @ApiModelProperty(value = "是否失败", example = "false")
    private Boolean fail;
}