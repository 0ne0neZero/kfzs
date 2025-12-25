package com.wishare.finance.domains.voucher.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum VoucherTemplateTypeEnum {

    常规(1, "常规"),
    BPM(2, "BPM");

    private int code;
    private String value;

    VoucherTemplateTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateTypeEnum valueOfByCode(int code){
        for (VoucherTemplateTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_TEMPLATE_TYPE_NOT_SUPPORT.msg());
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
