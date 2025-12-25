package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/11/24
 * @Description:
 */
@Getter
@Setter
@ApiModel("新增外部普通商户渠道商入参")
public class AddPaymentChannelByOutMchF {

    @ApiModelProperty(value = "外部商户编号")
    @NotBlank(message = "外部商户编号不能为空")
    private String outMchNo;

    @ApiModelProperty(value = "商户名称")
    @NotBlank(message = "商户名称不能为空")
    private String mchName;

    @ApiModelProperty(value = "渠道商名称")
    private String name;

    @ApiModelProperty(value = "渠道商类型：1微信，2支付宝，3银联，4工商银行，5光大银行，6农业银行")
    private Integer channelType;

    @ApiModelProperty(value = "渠道配置参数JSON，根据不同渠道，配置的参数不同")
    private String channelParams;

    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;
}
