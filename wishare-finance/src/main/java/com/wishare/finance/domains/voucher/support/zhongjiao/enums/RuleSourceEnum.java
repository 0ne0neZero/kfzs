package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum RuleSourceEnum {

    方圆(0, "方圆"),
    中交(1, "中交"),
 ;
    private int code;
    private String value;

    RuleSourceEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static RuleSourceEnum valueOfByCode(int code){
        for (RuleSourceEnum value : values()) {
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
