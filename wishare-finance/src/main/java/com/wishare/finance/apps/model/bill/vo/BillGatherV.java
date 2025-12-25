package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单统一收款响应
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单统一收款响应")
public class BillGatherV {

    @ApiModelProperty(value = "支付单号", required = true)
    private String payNo;

    @ApiModelProperty(value = "交易订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "支付状态: 支付状态: 0待支付, 1支付中, 2支付成功, 3支付失败, 4已撤销 5退款中, 6部分退款, 7已退款, 8已关闭", required = true)
    private Integer payState;

    @ApiModelProperty(value = "错误代码")
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    private String errMsg;

    @ApiModelProperty(value = "支付数据")
    private String payData;

}
