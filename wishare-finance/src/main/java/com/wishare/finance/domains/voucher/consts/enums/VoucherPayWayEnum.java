package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 结算方式
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherPayWayEnum {

//    线上支付宝(0, "ONLINE_ALIPAY", "线上-支付宝"),
//    线上微信支付(0, "ONLINE_WECHATPAY", "线上-微信支付"),
//    线上POS机(0, "ONLINE_POS", "线上-POS机"),
//    线上银联(0, "ONLINE_UNIONPAY", "线上-银联"),
//    线上刷卡(0, "ONLINE_SWIPE", "线上-刷卡"),
//    线上银行汇款(0, "ONLINE_BANK", "线上-银行汇款"),
//    线上结转(0, "ONLINE_CARRYOVER", "线上-结转"),
//    线上招商银企直连(0, "ONLINE_CBS","线上-招商银企直连"),
//    线上银行代扣(0, "ONLINE_BANK_WITHHOLD","线上-银行代扣"),
    线上支付宝(4, "ONLINE_ALIPAY", "线上-支付宝"),
    线上微信支付(4, "ONLINE_WECHATPAY", "线上-微信"),
    线上银联(0, "ONLINE_UNIONPAY", "线上-银联"),
    线下汇款(1,"OFFLINE_BANK","线下-汇款"),
    线下开发支付(1,"OFFLINE_PROPERTY_REDUCTION","线下-开发支付"),
    线下银行POS刷卡(1,"ONLINE_SWIPE","线下-银行POS刷卡"),
    结转(5,"ONLINE_CARRYOVER","结转"),
    线上组合支付(0, "ONLINE_COMPLEX","线上-组合支付"),
    线下支票(1, "OFFLINE_CHEQUE","线下-支票"),
    线下现金(1, "OFFLINE_CASH", "线下-现金"),
//    线下其他(1, "OFFLINE_OTHER", "线下-其他"),
    冲销结转(1, "WRITEOFFCARRIED","冲销结转"),
    车场第三方收款(1, "OFFLINE_PARK_THIRD_PAYMENT","车场第三方收款"),

    线下银联POS刷卡(4,"OFFLINE_UNIONPAYPOS","线下-银联POS刷卡"),
    线下银联(4,"OFFLINE_OFFLINE_UNION_PAY","线下-银联"),
    线下支付宝(2,"OFFLINE_OFFLINEALIPAY","线下-支付宝"),
    线下微信(3,"OFFLINE_OFFLINEWECHAT","线下-微信"),
    线下银行托收(1,"OFFLINE_BANK_WITHHOLD","线下-银行托收"),
    线上银联POS刷卡(4,"ONLINE_ONLINE_SWIPE","线上-银联POS刷卡"),
    ;

    private int type;
    private String code;
    private String value;

    VoucherPayWayEnum(int type, String code, String value) {
        this.type = type;
        this.code = code;
        this.value = value;
    }

    public static VoucherPayWayEnum valueOfByCode(String code){
        for (VoucherPayWayEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_PAY_WAY_NOT_SUPPORT.msg());
    }

    public String getCode() {
        return code;
    }

    public int getType() {
        return type;
    }

    public String getPayCode() {
        return code.substring(code.indexOf("_") + 1);
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return this.code.equals(code);
    }

}
