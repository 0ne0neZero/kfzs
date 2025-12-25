package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum InferenceStateEnum {

    未推凭(0, "未推凭"),
    已推凭(1, "已推凭"),
 ;
    private int code;
    private String value;

    InferenceStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static InferenceStateEnum valueOfByCode(int code){
        for (InferenceStateEnum value : values()) {
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
