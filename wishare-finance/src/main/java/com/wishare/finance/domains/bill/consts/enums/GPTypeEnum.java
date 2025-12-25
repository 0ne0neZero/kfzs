package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 收付类型
 * @author yancao
 */
public enum GPTypeEnum {

    收款(0, "收款"),
    付款(1, "付款"),
    ;

    private int code;
    private String value;

    GPTypeEnum(int code, String value) {
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

    public static GPTypeEnum valueOfByCode(int code){
        for (GPTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.PAYMENT_GP_TYPE_NOT_SUPPORT.msg());
    }

}
