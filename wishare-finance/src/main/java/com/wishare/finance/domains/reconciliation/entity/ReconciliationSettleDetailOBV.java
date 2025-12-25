package com.wishare.finance.domains.reconciliation.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付信息值对象
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class ReconciliationSettleDetailOBV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "支付id")
    private Long id;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "支付流水号")
    private String settleNo;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer settleWay;

    @ApiModelProperty(value = "结算金额（单位：分）")
    private Long settleAmount;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime settleTime;

    @ApiModelProperty(value = "付款人id")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer payerLabel;

    @ApiModelProperty(value = "收款人id")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

}
