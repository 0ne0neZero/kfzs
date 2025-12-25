package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

import java.util.Objects;

/**
 * 结算方式
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum SettleWayEnum {

    线上(0, "线上"),
    线下(1, "线下"),
    支付宝(2,"支付宝"),
    微信(3,"微信"),
    银联(4,"银联"),
    结转(5,"结转"),
    ;

    private int code;
    private String value;

    SettleWayEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public static SettleWayEnum valueOfByCode(int code){
        for (SettleWayEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_SETTLE_WAY_NOT_SUPPORT.msg());
    }

    public static String valueNameOfByCode(Integer code){
        if (Objects.isNull(code)) {
            return "";
        }
        for (SettleWayEnum value : values()) {
            if (value.equalsByCode(code)){
                return value.getValue();
            }
        }
        return "";
    }

}
