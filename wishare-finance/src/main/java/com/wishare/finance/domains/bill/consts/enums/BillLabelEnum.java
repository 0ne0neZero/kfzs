package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * @author xujian
 * @date 2022/10/21
 * @Description: 账单标识（空.无标识 1.冲销标识）
 */
public enum BillLabelEnum {

    冲销标识(1, "冲销标识"),
    ;

    private int code;
    private String value;

    BillLabelEnum(int code, String value) {
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

    public static BillLabelEnum valueOfByCode(int code){
        for (BillLabelEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_ON_ACCOUNT_STATE_NOT_SUPPORT.msg());
    }
}
