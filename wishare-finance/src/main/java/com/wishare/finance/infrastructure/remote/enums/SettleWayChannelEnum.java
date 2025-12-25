package com.wishare.finance.infrastructure.remote.enums;

import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;

/**
 * @author xujian
 * @date 2022/11/4
 * @Description: type 0线上，1线下
 */
public enum SettleWayChannelEnum {

    支付宝(4,"银联","ALIPAY", "线上-支付宝"),
    微信支付(4,"银联","WECHATPAY", "线上-微信"),
    现金(1,"线下","CASH", "现金"),
    POS机(0,"线上","POS", "POS机"),
    POS机刷卡(SettleWayEnum.线上, "POS_SWIPE","POS机刷卡"),
    //中交有使用
    通联POS(0,"线上","ALLINPAY_UNIT_POS", "通联POS"),
    银联(0,"线上","UNIONPAY", "银联"),
    招商银企直连(1,"线上","CBS", "招商银企直连"),
    传统POS(1,"线下","SWIPE", "线下-银行POS刷卡"),
    银行汇款(1,"线下","BANK", "汇款"),
    结转(5,"结转","CARRYOVER", "结转"),
    押金结转(5,"押金结转","DEPOSIT_CARRYOVER", "押金结转"),
    支票(1,"线下","CHEQUE", "支票"),
    其他(1,"线下","OTHER", "其他"),
    组合支付(0, "线上", "COMPLEX","组合支付"),
    银行托收(1, "线下", "BANK_WITHHOLD","银行托收"),
    开发支付(1, "线下", "PROPERTY_REDUCTION","开发支付"),
    线下银联POS刷卡(4, "银联","UNIONPAYPOS","线下-银联POS刷卡"),
    线下银联(4, "银联","OFFLINE_UNION_PAY","线下-银联"),
    线下支付宝(2, "支付宝","OFFLINEALIPAY","线下-支付宝"),
    线下微信(3, "微信","OFFLINEWECHAT","线下-微信"),

    郑州银行(0,"线上","ZZ_BANK","郑州银行"),
    线上银联POS刷卡(4,"银联","ONLINE_SWIPE", "线上-银联POS刷卡"),
    通联微信扫码(SettleWayEnum.线上, "ALLINPAY_UNIT_WX","通联微信扫码"),
    通联支付宝扫码(SettleWayEnum.线上,"ALLINPAY_UNIT_ALI","通联支付宝扫码"),
    通联微信(SettleWayEnum.线上, "ALLINPAY_WXAPPLET","通联微信"),
    通联支付宝(SettleWayEnum.线上,"ALLINPAY_ALIAPPLET","通联支付宝"),
    银行划账(SettleWayEnum.线上, "BANK_TRANSFER","银行划账"),

    预交冲抵(SettleWayEnum.线上, "ADVANCE_OFFSET","预交冲抵"),
    空置确认(SettleWayEnum.线上, "VACANT_CONFIRM","空置确认"),
    开发商减免(SettleWayEnum.线上,"DEVELOPERS_REDUCTION","开发商减免"),
    开发商赠送(SettleWayEnum.线上, "DEVELOPERS_GIVE","开发商赠送"),
    物业减免(SettleWayEnum.线上, "WUYE_REDUCTION","物业减免"),
    银行退款(SettleWayEnum.线上, "BANK_REFUND","银行退款"),
    现金退款(SettleWayEnum.线上, "CASH_REFUND","现金退款"),
    微信小程序支付(SettleWayEnum.线上, "WECHAT_MINI_PROGRAM_PAY","微信小程序支付"),
    二维码收款(SettleWayEnum.线上, "QR_CODE_PAY","二维码收款"),
    二维码(SettleWayEnum.线上, "QR_CODE","二维码"),


    卡趴_微信小程序支付(SettleWayEnum.线上, "KP_WXAPPLET","卡趴_微信小程序支付"),
    卡趴_支付宝(SettleWayEnum.线上, "KP_ALIPAY","卡趴_支付宝"),
    卡趴_现金(SettleWayEnum.线上, "KP_CASH","卡趴_现金"),
    卡趴_POS机刷卡(SettleWayEnum.线上, "KP_POS","卡趴_POS机刷卡"),
    卡趴_汇款(SettleWayEnum.线上, "KP_BANK","卡趴_汇款"),

    管理费折扣(SettleWayEnum.线上, "MANAGEMENT_EXPENSE_DISCOUNT","管理费折扣"),

    预缴折扣(SettleWayEnum.线上, "ADVANCE_DISCOUNT","预缴折扣"),
    悦圆(SettleWayEnum.线上, "YUEYUAN","悦圆"),
    创通_支付宝微信现金(SettleWayEnum.线上, "CT_ALIPAY_WECHAT_CASH","创通_支付宝微信现金"),
    餐费抵租金(SettleWayEnum.线上, "MEALS_TO_RENT","餐费抵租金"),
    线上_支付宝支付(SettleWayEnum.线上, "ALIPAY_ONLINE", "支付宝支付"),
    线上_微信支付(SettleWayEnum.线上, "WECHATPAY_ONLINE", "微信支付"),
    微信小程序支付2(SettleWayEnum.线上, "WE_CHAT_APPLET", "微信小程序支付"),

    科拓_现金(SettleWayEnum.线下,"KEYTOP_CASH", "科拓_现金"),
    科拓_微信(SettleWayEnum.线上,"KEYTOP_WECHATPAY", "科拓_微信"),
    科拓_支付宝(SettleWayEnum.线上,"KEYTOP_ALIPAY",  "科拓_支付宝"),
    科拓_通联二维码收款(SettleWayEnum.线下,"KEYTOP_UNIT_ALLINPAY_QRCODE", "科拓_通联二维码收款"),
    科拓_其他(SettleWayEnum.线上,"KEYTOP_OTHER", "科拓_其他"),


    捷顺_微信(SettleWayEnum.线上,"JIESHUN_WECHATPAY", "捷顺_微信"),
    捷顺_支付宝(SettleWayEnum.线上,"JIESHUN_ALIPAY", "捷顺_支付宝"),
    捷顺_金科(SettleWayEnum.线上,"JIESHUN_JINKE","捷顺_金科"),
    捷顺_其他(SettleWayEnum.线上,"JIESHUN_OTHER", "捷顺_其他"),
    捷顺_线下支付(SettleWayEnum.线下,"JIESHUN_OFFLINE_PAY", "捷顺_线下支付"),
    捷顺_现金(SettleWayEnum.线上,"JIESHUN_CASH", "捷顺_现金"),
    冲销结转(SettleWayEnum.线下, "WRITEOFFCARRIED", "冲销结转"),
    车场第三方收款(SettleWayEnum.线下, "PARK_THIRD_PAYMENT", "车场第三方收款"),
    ;



    private Integer type;

    private String typeStr;
    private String code;
    private String value;

    public Integer getType() {
        return type;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    SettleWayChannelEnum(Integer type, String typeStr, String code, String value) {
        this.type = type;
        this.typeStr = typeStr;
        this.code = code;
        this.value = value;
    }

    SettleWayChannelEnum(SettleWayEnum settleWayEnum, String code, String value) {
        this.type = settleWayEnum.getCode();
        this.typeStr = settleWayEnum.getValue();
        this.code = code;
        this.value = value;
    }

    public static SettleWayChannelEnum valueOfByCode(String code) {
        SettleWayChannelEnum e = null;
        for (SettleWayChannelEnum ee : SettleWayChannelEnum.values()) {
            if (ee.getCode().equalsIgnoreCase(code) ) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
