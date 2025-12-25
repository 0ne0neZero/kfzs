package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发票请求头信息
 * @author dongpeng
 * @date 2023/10/25 20:20
 */
@Data
@ApiModel("发票请求头信息")
public class InvoiceReqF {

    @ApiModelProperty(value = "企业应用标识(企业授权应用标识，全局唯一)",required = true)
    private String appid;

    @ApiModelProperty(value = "接口类型编号(区分业务类型，QDP-FP-10001)",required = true)
    private String serviceid;

    @ApiModelProperty(value = "签名方式(0HmacSHA256,1MD5,3不签名)",required = true)
    private String signType;

    @ApiModelProperty(value = "签名值(signType为0和1时必填)",required = true)
    private String signature;

    @ApiModelProperty(value = "报文主体，Base64编码",required = true)
    private String content;
}
