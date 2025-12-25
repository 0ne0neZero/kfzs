package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 收款类型
 * @author dp
 */
public enum InvoiceGatherTypeEnum {

    收款单(0, "收款单"),
    收款明细(1, "收款明细"),
    ;

    private int code;
    private String value;

    InvoiceGatherTypeEnum(int code, String value) {
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

    public static InvoiceGatherTypeEnum valueOfByCode(int code){
        for (InvoiceGatherTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

}
