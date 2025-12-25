package com.wishare.finance.infrastructure.remote.vo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 统一下单响应信息
 *
 * @Author dxclay
 * @Date 2022/12/15
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("统一下单响应信息")
public class TransactionV {

    @ApiModelProperty(value = "支付单号")
    private String payNo;

    @ApiModelProperty(value = "商户订单号", required = true)
    private String mchOrderNo;

    @ApiModelProperty(value = "支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭", required = true)
    private Integer payState;

    @ApiModelProperty(value = "错误代码")
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    private String errMsg;

    @ApiModelProperty(value = "支付数据")
    private String payData;

}
