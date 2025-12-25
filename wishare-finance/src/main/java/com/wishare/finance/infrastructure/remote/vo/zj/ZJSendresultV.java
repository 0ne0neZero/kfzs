package com.wishare.finance.infrastructure.remote.vo.zj;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("中交返参")
public class ZJSendresultV {
    @ApiModelProperty("响应状态")
    private Integer code;
    @ApiModelProperty("响应消息")
    private String message;
    @ApiModelProperty("响应数据")
    private String data;
}