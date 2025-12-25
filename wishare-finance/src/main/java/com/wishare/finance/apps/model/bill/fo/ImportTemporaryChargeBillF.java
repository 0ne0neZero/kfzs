package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value = "导入临时账单请求信息", parent = AddTemporaryChargeBillF.class)
public class ImportTemporaryChargeBillF extends AddTemporaryChargeBillF{

    @ApiModelProperty(value = "结算金额")
    private Long settleAmount;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer settleWay;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty(value = "账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty(value = "行号标识", required = true)
    private Integer index;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;

}
