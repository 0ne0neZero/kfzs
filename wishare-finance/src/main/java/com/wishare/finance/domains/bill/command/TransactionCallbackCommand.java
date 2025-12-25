package com.wishare.finance.domains.bill.command;

import java.time.LocalDateTime;

/**
 * 交易回调处理命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/28
 */
public class TransactionCallbackCommand {

    /**
     * 支付订单编号
     */
    private String payNo;
    /**
     * 商户交易单号
     */
    private String mchOrderNo;
    /**
     * 支付状态
     */
    private Integer payState;
    /**
     * 支付渠道商支付单号
     */
    private String chanelTradeNo;
    /**
     * 支付成功时间
     */
    private LocalDateTime successTime;

    public String getPayNo() {
        return payNo;
    }

    public TransactionCallbackCommand setPayNo(String payNo) {
        this.payNo = payNo;
        return this;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public TransactionCallbackCommand setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
        return this;
    }

    public Integer getPayState() {
        return payState;
    }

    public TransactionCallbackCommand setPayState(Integer payState) {
        this.payState = payState;
        return this;
    }

    public String getChanelTradeNo() {
        return chanelTradeNo;
    }

    public TransactionCallbackCommand setChanelTradeNo(String chanelTradeNo) {
        this.chanelTradeNo = chanelTradeNo;
        return this;
    }

    public LocalDateTime getSuccessTime() {
        return successTime;
    }

    public TransactionCallbackCommand setSuccessTime(LocalDateTime successTime) {
        this.successTime = successTime;
        return this;
    }
}
