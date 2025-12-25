package com.wishare.finance.domains.bill.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @description:receivable_bill表pay_infos字段json解析
* @author: zhenghui
* @date: 2023/3/29 10:19
*/
@Getter
@Setter
public class PayInfo {

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

    @ApiModelProperty(value = "支付金额", required = true)
    private Long amount;

    @ApiModelProperty("支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端")
    private Integer paySource = 0;

    public PayInfo() {
    }

    public PayInfo(Integer payWay, String payChannel) {
        this.payWay = payWay;
        this.payChannel = payChannel;
    }

    public PayInfo(Integer payWay, String payChannel, Long amount) {
        this.payWay = payWay;
        this.payChannel = payChannel;
        this.amount = amount;
    }

    public PayInfo(Integer payWay, String payChannel, Long amount,Integer paySource) {
        this.payWay = payWay;
        this.payChannel = payChannel;
        this.amount = amount;
        this.paySource = paySource;
    }
}
