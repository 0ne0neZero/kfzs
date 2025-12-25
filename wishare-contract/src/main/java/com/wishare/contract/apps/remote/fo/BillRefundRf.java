package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BillRefundRf {
    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty(value = "退款金额", required = true)
    @NotNull(message = "退款金额不能为空")
    @Min(value = 0)
    private Long refundAmount;

    @ApiModelProperty("结算渠道 :  ALIPAY：支付宝 WECHATPAY:微信支付，CASH:现金，POS: POS机，UNIONPAY:银联，SWIPE: 刷卡， BANK:银行汇款，CARRYOVER:结转，OTHER: 其他")
    private String refundChannel;

    @ApiModelProperty("退款方式(0线上，1线下)")
    private Integer refundWay;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("外部退款编号（支付宝退款单号，银行流水号等）")
    private String outRefundNo;

    @ApiModelProperty("收费对象类型")
    private Integer refunderType;

    @ApiModelProperty("退款人ID")
    private String refunderId;

    @ApiModelProperty(value = "退款人名称",required = true)
    @NotBlank(message = "退款人名称不能为空")
    private String refunderName;

    @ApiModelProperty("收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty("收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty("备注")
    private String remark;
}
