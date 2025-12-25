package com.wishare.finance.domains.voucher.support.fangyuan.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum PushModeEnum {

    定时推送(1, "定时推送"),
    手动推凭(2, "手动推凭"),
 ;
    private int code;
    private String value;

    PushModeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static PushModeEnum valueOfByCode(int code){
        for (PushModeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_BUSINESS_BILL_TYPE_NOT_SUPPORT.msg());
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
}
