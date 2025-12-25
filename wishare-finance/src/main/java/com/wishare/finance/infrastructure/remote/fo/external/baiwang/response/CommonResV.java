package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:16
 * @descrption: 百望系统通用响应体
 */
@Data
public class CommonResV<V> {

    @ApiModelProperty(value = "接口调用成功或失败的标志 true：成功，false：失败")
    private Boolean success;

    @ApiModelProperty(value = "请求唯一标识， 与调用接口时传入的request_id一致")
    private String requestId;

    @ApiModelProperty(value = "业务接口调用成功后返回的具体响应信息，不同的接口返回的model不同")
    private V model;

    @ApiModelProperty(value = "响应message")
    private ErrMessageResV message;
}
