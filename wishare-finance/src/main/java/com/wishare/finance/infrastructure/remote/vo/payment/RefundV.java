package com.wishare.finance.infrastructure.remote.vo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 退款响应信息
 */
@Getter
@Setter
@ApiModel("退款响应信息")
public class RefundV {
    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty(value = "支付单号")
    private String payNo;

    @ApiModelProperty("当前退款金额")
    private Long refundAmount;

    @ApiModelProperty(value = "退款状态5退款中, 6部分退款, 7已退款, 8已关闭, 9退款失败", required = true)
    private Integer state;

    @ApiModelProperty(value = "错误代码")
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    private String errMsg;
}
