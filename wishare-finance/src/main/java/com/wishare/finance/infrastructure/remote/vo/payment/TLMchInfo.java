package com.wishare.finance.infrastructure.remote.vo.payment;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * TL支付渠道配置出参
 *
 * @Author zyj
 * @Date 2023/09/05
 * @Version 1.0
 */

@Getter
@Setter
@ApiModel("TL支付渠道配置出参")
public class TLMchInfo {

    @ApiModelProperty(value = "私钥")
    private String privateKey;

    @ApiModelProperty(value = "公钥")
    private String publicKey;

    @ApiModelProperty(value = "商户ID")
    private String merchantId;

    @ApiModelProperty(value = "商户名称")
    private String userName;

}
