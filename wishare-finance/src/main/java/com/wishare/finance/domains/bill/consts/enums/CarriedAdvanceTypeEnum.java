package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 是否结转预收
 * @Author dxclay
 * @Date 2022/9/16
 * @Version 1.0
 */
public enum CarriedAdvanceTypeEnum {

    不结转(0, "不结转"),
    结转(1, "结转"),
    ;

    private int code;
    private String value;

    CarriedAdvanceTypeEnum(int code, String value) {
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

    public static CarriedAdvanceTypeEnum valueOfByCode(int code){
        for (CarriedAdvanceTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_CARRYOVER_ADVANCE_NOT_SUPPORT.msg());
    }
}
