package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author xujian
 * @date 2022/11/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("新增支付渠道配置信息")
public class AddPaymentChannelRf {

    @ApiModelProperty(value = "渠道商名称")
    private String name;

    @ApiModelProperty(value = "渠道商类型：1微信，2支付宝，3银联，4工商银行，5光大银行，6农业银行")
    private Integer channelType;

    @ApiModelProperty(value = "渠道配置参数JSON，根据不同渠道，配置的参数不同")
    private String channelParams;

    @ApiModelProperty(value = "应用主id")
    @NotNull(message = "应用主id不能为空")
    private Long applicationId;
}
