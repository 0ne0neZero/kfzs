package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 结转方式
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum CarryoverTypeEnum {

    抵扣(1, "抵扣"),
    结转预收(2, "结转预收")
    ;

    private int code;
    private String value;

    CarryoverTypeEnum(int code, String value) {
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

    public static CarryoverTypeEnum valueOfByCode(int code){
        for (CarryoverTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_CARRYOVER_TYPE_NOT_SUPPORT.msg());
    }

}
