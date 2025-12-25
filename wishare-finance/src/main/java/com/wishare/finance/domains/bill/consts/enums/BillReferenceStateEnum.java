package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 引用状态
 * @Author dxclay
 * @Date 2022/10/21
 * @Version 1.0
 */
public enum BillReferenceStateEnum {

    未被引用(0, "未结转"),
    已被引用(1, "待结转"),
    ;

    private Integer code;
    private String value;

    BillReferenceStateEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public static BillReferenceStateEnum valueOfByCode(int code){
        for (BillReferenceStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_REFERENCE_STATE_NOT_SUPPORT.msg());
    }

}
