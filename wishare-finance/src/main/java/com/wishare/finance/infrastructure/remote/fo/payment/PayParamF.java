package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 支付额外参数
 *
 * @Author dxclay
 * @Date 2022/12/27
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("支付额外参数")
@Accessors(chain = true)
public class PayParamF {

    @ApiModelProperty(value = "二维码支付类型 BASE64: 返回二维码图片的base64字符串, URL: 返回支付的url")
    private String qrCodeType;

    @ApiModelProperty(value = "cbs 支付额外参数")
    private CbsF cbsPay;

    @ApiModelProperty(value = "远洋银联支付额外参数")
    private YYNetF yyNetPay;

}
