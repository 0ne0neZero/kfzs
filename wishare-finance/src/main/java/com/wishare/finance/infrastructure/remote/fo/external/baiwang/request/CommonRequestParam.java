package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/19 14:51
 * @descrption:
 */
@Data
@ApiModel(value = "百望系统通用请求参数实体")
public class CommonRequestParam {

    // 必填
    @ApiModelProperty(value = "接口指定唯一标识")
    private String method;

    // 必填
    @ApiModelProperty(value = "请求唯一标识")
    private String requestId;

    // 必填
    @ApiModelProperty(value = "版本号，当前为1.0")
    private String version = "1.0";

    @Override
    public String toString() {
        return "CommonRequestParam{" +
                "method='" + method + '\'' +
                ", requestId='" + requestId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
