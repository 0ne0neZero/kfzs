package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * 账单付款信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单付款信息")
public class BillPayDataV {

    @ApiModelProperty(value = "交易订单号")
    private String orderNo;

    @ApiModelProperty(value = "支付渠道订单号")
    private String channelOrderNo;

    @ApiModelProperty(value = "付款状态: 1付款中, 2付款成功, 3付款失败")
    private Integer payState;

    @ApiModelProperty(value = "支付成功时间")
    private LocalDateTime successTime;

    @ApiModelProperty(value = "支付扩展信息，支付通知原样返回")
    @Length(max = 256, message = "支付扩展信息格式不正确")
    private String payAttachParam;

    @ApiModelProperty(value = "付款账单信息")
    private BillPayInfoV billInfo;

    @ApiModelProperty(value = "凭证信息")
    private BillPayVoucherInfoV voucherInfo;

}
