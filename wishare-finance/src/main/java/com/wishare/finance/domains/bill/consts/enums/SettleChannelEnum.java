package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 结算渠道
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum SettleChannelEnum {

    支付宝("ALIPAY", "支付宝"),
    微信支付("WECHATPAY", "微信"),
    现金("CASH", "现金","01"),
    POS机("POS", "POS机", "03"),
    POS机刷卡("POS_SWIPE", "POS机刷卡","03"),
    通联POS("ALLINPAY_UNIT_POS", "通联POS", "03"),
    银联("UNIONPAY", "银联"),
    线下银联("OFFLINE_UNION_PAY", "银联"),
    传统POS("SWIPE", "银行POS刷卡","03"),
    线下银联POS刷卡("UNIONPAYPOS","银联POS刷卡", "03"),
    线下支付宝("OFFLINEALIPAY","支付宝"),

    微信小程序支付2("WE_CHAT_APPLET","微信小程序支付"),
    线下微信("OFFLINEWECHAT","微信"),
    银行汇款("BANK", "汇款","04"),
    结转("CARRYOVER", "结转"),
    押金结转("DEPOSIT_CARRYOVER", "押金结转","46"),
    其他("OTHER", "其他","99"),
    支票("CHEQUE","支票","02"),
    招商银企直连("CBS","招商银企直连"),
    组合支付("COMPLEX","组合支付"),
    银行托收("BANK_WITHHOLD","银行托收"),
    开发支付("PROPERTY_REDUCTION","开发支付"),
    郑州银行("ZZ_BANK","郑州银行"),
    线上银联POS刷卡("ONLINE_SWIPE", "银联POS刷卡", "03"),
    通联微信扫码("ALLINPAY_UNIT_WX","通联微信扫码"),
    通联支付宝扫码("ALLINPAY_UNIT_ALI","通联支付宝扫码"),
    通联微信("ALLINPAY_WXAPPLET","通联微信"),
    通联支付宝("ALLINPAY_ALIAPPLET","通联支付宝"),
    银行划账("BANK_TRANSFER","银行划账","05"),

    对公转账("ALLINPAY_CORPORATEINFO_PAY","对公转账"),
    预交冲抵("ADVANCE_OFFSET","预交冲抵","06"),
    空置确认("VACANT_CONFIRM","空置确认","07"),
    开发商减免("DEVELOPERS_REDUCTION","开发商减免","08"),
    开发商赠送("DEVELOPERS_GIVE","开发商赠送","09"),
    物业减免("WUYE_REDUCTION","物业减免","10"),
    银行退款("BANK_REFUND","银行退款","15"),
    现金退款("CASH_REFUND","现金退款","16"),
    微信小程序支付("WECHAT_MINI_PROGRAM_PAY","微信小程序支付","20"),
    二维码收款("QR_CODE_PAY","二维码收款","22"),
    二维码("QR_CODE","二维码"),


    卡趴_微信小程序支付("KP_WXAPPLET","卡趴_微信小程序支付","23"),
    卡趴_支付宝("KP_ALIPAY","卡趴_支付宝","24"),
    卡趴_现金("KP_CASH","卡趴_现金","25"),
    卡趴_POS机刷卡("KP_POS","卡趴_POS机刷卡","26"),
    卡趴_汇款("KP_BANK","卡趴_汇款","27"),

    管理费折扣("MANAGEMENT_EXPENSE_DISCOUNT","管理费折扣","29"),

    预缴折扣("ADVANCE_DISCOUNT","预缴折扣","30"),
    悦圆("YUEYUAN","悦圆","31"),
    创通_支付宝微信现金("CT_ALIPAY_WECHAT_CASH","创通_支付宝微信现金","32"),
    餐费抵租金("MEALS_TO_RENT","餐费抵租金","98"),
    线上_支付宝支付("ALIPAY_ONLINE", "支付宝支付","18"),
    线上_微信支付("WECHATPAY_ONLINE", "微信支付","11"),

    科拓_现金("KEYTOP_CASH", "科拓_现金","56"),
    科拓_微信("KEYTOP_WECHATPAY", "科拓_微信","55"),
    科拓_支付宝("KEYTOP_ALIPAY",  "科拓_支付宝","54"),
    科拓_通联二维码收款("KEYTOP_UNIT_ALLINPAY_QRCODE", "科拓_通联二维码收款","57"),
    科拓_其他("KEYTOP_OTHER", "科拓_其他","58"),

    捷顺_微信("JIESHUN_WECHATPAY",  "捷顺_微信","47"),
    捷顺_支付宝("JIESHUN_ALIPAY","捷顺_支付宝","48"),
    捷顺_金科("JIESHUN_JINKE", "捷顺_金科","49"),
    捷顺_其他("JIESHUN_OTHER", "捷顺_其他","50"),
    捷顺_线下支付("JIESHUN_OFFLINE_PAY",  "捷顺_线下支付","51"),
    捷顺_现金("JIESHUN_CASH", "捷顺_现金","52"),
    冲销结转("WRITEOFFCARRIED", "冲销结转","53"),
    车场第三方收款 ("PARK_THIRD_PAYMENT", "车场第三方收款"),
    盛付通 ("SHENGFUTONG", "盛付通")
    ;

    private String code;
    private String value;

    //支付编码，方圆推nc使用
    private String fYPayNo;

    SettleChannelEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    SettleChannelEnum(String code, String value,String fYPayNo) {
        this.code = code;
        this.value = value;
        this.fYPayNo = fYPayNo;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
    public String getFYPayNo() {
        return fYPayNo;
    }
    public boolean equalsByCode(String code){
        return this.code.equals(code);
    }

    public static SettleChannelEnum valueOfByCode(String code){
        for (SettleChannelEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_SETTLE_CHANNEL_NOT_SUPPORT.msg());
    }

    public static String valueNameOfByCode(String code){
        for (SettleChannelEnum value : values()) {
            if (value.equalsByCode(code)){
                return value.getValue();
            }
        }
        return "";
    }

    public static String fYPayNoOfByCode(String code){
        for (SettleChannelEnum fYPayNo : values()) {
            if (fYPayNo.equalsByCode(code)){
                return fYPayNo.getFYPayNo();
            }
        }
        return "";
    }

}
