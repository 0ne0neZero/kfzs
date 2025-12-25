package com.wishare.finance.domains.invoicereceipt.consts.enums;

import java.util.Objects;

/**
 * @author yyx
 * @date 2023/8/26 15:43
 * 退款方式method枚举
 */
public enum RefundTradeMethodEnum {

    银联("UNIONPAY", "unionpay.trade.refund"),
    线上微信("WECHATPAY", "wxpay.trade.refund"),
    线上支付宝("ALIPAY", "alipay.trade.refund"),
    线上郑州银行("ZZ_BANK", "zzbankpay.trade.refund"),


    ;

    private String code;

    private String des;

    public static RefundTradeMethodEnum valueOfByCode(String code) {
        RefundTradeMethodEnum e = null;
        for (RefundTradeMethodEnum ee : RefundTradeMethodEnum.values()) {
            if (Objects.equals(ee.getCode(), code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    RefundTradeMethodEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
