package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/9/28 18:28
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("账单结算渠道类型")
public class BillSettleChannelV {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 账单id
     */
    @ApiModelProperty("账单id")
    private Long billId;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    @ApiModelProperty("结算渠道"
        + "ALIPAY：支付宝，"
        + "WECHATPAY:微信支付，"
        + "CASH:现金，"
        + "POS: POS机，"
        + "UNIONPAY:银联，"
        + "SWIPE: 刷卡，"
        + "BANK:银行汇款，"
        + "CARRYOVER:结转，"
        + "CHEQUE: 支票，"
        + "OTHER: 其他")
    private String settleChannel;
}
