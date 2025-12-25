package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 是否冲销
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillReverseStateEnum {

    未冲销(0, "未冲销"),
    已冲销(1, "已冲销"),
    ;

    private int code;
    private String value;

    BillReverseStateEnum(int code, String value) {
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

    public static BillReverseStateEnum valueOfByCode(int code){
        for (BillReverseStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_REVERSE_STATE_NOT_SUPPORT.msg());
    }

}
