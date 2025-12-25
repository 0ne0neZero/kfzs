package com.wishare.finance.apps.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "请求响应信息")
public class ProcessCreateResultV {

    @ApiModelProperty(value = "流程请求id")
    private String requestid;

    @ApiModelProperty(value = "状态码 0:成功，1:失败")
    private String code;

    @ApiModelProperty(value = "状态信息 成功/失败")
    private String msg;

}
