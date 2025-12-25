package com.wishare.finance.domains.bill.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 收款方式
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Getter
@Setter
public class PayWay {

    /**
     * 收款方式
     */
    @ApiModelProperty(value = "结算方式(0线上，1线下)", required = true)
    private Integer payWay;

    /**
     * 收款渠道
     */
    @ApiModelProperty(value = "结算渠道\n" +
            "   ALIPAY：支付宝，\n" +
            "   WECHATPAY:微信支付，\n" +
            "   CASH:现金，\n" +
            "   POS: POS机，\n" +
            "   UNIONPAY:银联，\n" +
            "   SWIPE: 刷卡，\n" +
            "   BANK:银行汇款，\n" +
            "   CARRYOVER:结转，\n" +
            "   CHEQUE: 支票\n" +
            "   OTHER: 其他\n" +
            "   COMPLEX：组合支付", required = true)
    private String payChannel;

    public PayWay() {
    }

    public PayWay(Integer payWay, String payChannel) {
        this.payWay = payWay;
        this.payChannel = payChannel;
    }
}
