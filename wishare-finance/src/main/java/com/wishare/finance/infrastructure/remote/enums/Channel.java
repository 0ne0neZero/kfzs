package com.wishare.finance.infrastructure.remote.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 渠道商类型
 *
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum Channel {

    微信("wechatpay", "微信","1"),
    支付宝("alipay", "支付宝","2"),
    银联("unionpay", "银联","3"),
    工商银行("cibcpay", "工商银行",""),
    光大银行("cebbankpay", "光大银行",""),
    农业银行("abchinapay", "农业银行",""),
    招商银企直连("cbspay", "招商银企直连",""),
    郑州银行("zzbankpay", "郑州银行","8"),
    通联支付("allinpay","通联支付","9"),
    广发银行("gfbankpay", "广发银行",""),
    建设银行("ccbbankpay", "建设银行","11"),
    中信银行("ccbankpay","中信银行","12"),
    招商银行("cmbpay","招商银行","13"),
    ;

    private String code;
    private String value;

    private String channelCode;
    Channel(String code, String value, String channelCode) {
        this.code = code;
        this.value = value;
        this.channelCode = channelCode;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getChannelCode(){
        return channelCode;
    }

    public boolean equalsByCode(String code) {
        return this.code.equals(code);
    }
    public boolean equalsByChannelCode(String code) {
        return this.channelCode.equals(code);
    }
    public static Channel valueOfByCode(String code) {
        for (Channel channelCode : values()) {
            if (channelCode.equalsByCode(code)) {
                return channelCode;
            }
        }
        throw BizException.throw400(ErrorMessage.CHANNEL_TYPE_NOT_SUPPORT.msg());
    }
    public static Channel valueOfByChannelCode(String code) {
        for (Channel channelCode : values()) {
            if (channelCode.equalsByChannelCode(code)) {
                return channelCode;
            }
        }
        throw BizException.throw400(ErrorMessage.CHANNEL_TYPE_NOT_SUPPORT.msg());
    }
}
