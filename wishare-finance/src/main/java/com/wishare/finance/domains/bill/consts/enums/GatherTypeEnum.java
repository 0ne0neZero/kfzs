package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 收款类型
 * @author yancao
 */
public enum GatherTypeEnum {

    应收(0, "应收"),
    预收(1, "预收"),
    ;

    private int code;
    private String value;

    GatherTypeEnum(int code, String value) {
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

    public static GatherTypeEnum valueOfByCode(int code){
        for (GatherTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.GATHER_TYPE_NOT_SUPPORT.msg());
    }

}
