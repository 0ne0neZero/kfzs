package com.wishare.finance.infrastructure.remote.enums.payment;

/**
 * 支付接口
 *
 * @Author dxclay
 * @Date 2022/12/12
 * @Version 1.0
 */
public class PaymentMethod {

    //支付宝支付接口
    public static final String ALIPAY = "alipay";
    public static final String ALIPAY_APP = "alipay.trade.app";
    public static final String ALIPAY_PHONE = "alipay.trade.phone";
    public static final String ALIPAY_PC = "alipay.trade.pc";
    public static final String ALIPAY_QRCODE = "alipay.trade.qrcode";
    public static final String ALIPAY_CODE = "alipay.trade.code.pay";
    public static final String ALIPAY_QUERY = "alipay.trade.query";
    public static final String ALIPAY_REFUND = "alipay.trade.refund";
    public static final String ALIPAY_CLOSE = "alipay.trade.close";
    public static final String ALIPAY_REFUND_QUERY = "alipay.refund.query";

    //微信支付接口
    public static final String WXPAY = "wxpay";
    public static final String WXPAY_APP = "wxpay.trade.app";
    public static final String WXPAY_H5 = "wxpay.trade.jsapi";
    public static final String WXPAY_NATIVE = "wxpay.trade.native";
    public static final String WXPAY_APPLET = "wxpay.trade.applet";
    public static final String WXPAY_QRCODE = "wxpay.trade.qrcode";
    public static final String WXPAY_CLOSE = "wxpay.trade.close";
    public static final String WXPAY_QUERY = "wxpay.trade.query";
    public static final String WXPAY_REFUND = "wxpay.trade.refund";
    public static final String WXPAY_REFUND_QUERY = "wxpay.refund.query";

    //银联支付接口
    public static final String UNIONPAY = "unionpay";
    public static final String UNIONPAY_QRCODE = "unionpay.trade.qrcode.pay";
    public static final String UNIONPAY_QRCODE_UPDATE = "unionpay.trade.qrcode.update";
    public static final String UNIONPAY_QRCODE_CLOSE = "unionpay.trade";
    public static final String UNIONPAY_CLOSE = "unionpay.trade.close";
    public static final String UNIONPAY_QUERY = "unionpay.trade.query";
    public static final String UNIONPAY_REFUND = "unionpay.trade.refund";
    public static final String UNIONPAY_REFUND_QUERY = "unionpay.refund.query";


    //银企直连
    public static final String CBSPAY = "cbspay";
    public static final String CBSPAY_APP = "cbspay.trade.app";

}
