package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 账单结算详情
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/22
 */
@Getter
@Setter
@ApiModel("支付结算详情")
public class BillSettleDetailCommand {

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，CHEQUE: 支票，COMPLEX: 组合支付，OTHER: 其他）")
    @NotNull(message = "结算渠道不能为空")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    @NotNull(message = "结算方式不能为空")
    private Integer payWay;

    @ApiModelProperty(value = "支付金额 （区间为[1, 1000000000]）", required = true)
    @NotNull(message = "支付金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    private Long payAmount;

    @ApiModelProperty(value = "支付时间 格式：yyyy-MM-dd HH:mm:ss", required = true)
    @NotNull(message = "支付时间不能为空")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "渠道交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty(value = "收费结束时间")
    private LocalDateTime chargeEndTime;

}
