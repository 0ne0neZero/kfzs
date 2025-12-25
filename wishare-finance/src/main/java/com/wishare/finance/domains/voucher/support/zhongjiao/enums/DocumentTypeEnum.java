package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum DocumentTypeEnum {

    收入确认单(1, "TYSRQR"),
    资金收款单(2, "ZJSK"),

    对下结算计提(3, "DXJS"),
    对下结算实签(4,"DXJS"),
    通用收入计提(5, "TYSRQR"),
    通用收入实签(6, "TYSRQR"),

    对下结算计提冲销(7,"DXJS"),
    收入确认计提冲销(8,"TYSRQR"),

    业务支付申请(9,"YWZFSQ")
 ;
    private int code;
    private String value;

    DocumentTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static DocumentTypeEnum valueOfByCode(int code){
        for (DocumentTypeEnum value : values()) {
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
