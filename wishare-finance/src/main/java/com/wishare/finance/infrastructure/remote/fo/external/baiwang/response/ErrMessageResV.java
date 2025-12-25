package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:18
 * @descrption: 百望响应message
 */
@Data
@ApiModel(value = "百望系统响应message")
public class ErrMessageResV {

    @ApiModelProperty(value = "调用失败后，返回的错误码, 服务端错误码或业务层错误码")
    private String errorCode;

    @ApiModelProperty(value = "调用失败后，返回的错误信息")
    private String errorMessage;

    @ApiModelProperty(value = "下游业务层的request_id，若为业务调用失败，则返回此字段")
    private String innerRequestId;

    @ApiModelProperty(value = "调用成功后，返回的成功信息")
    private String successMessage;
}
