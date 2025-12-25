package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证类别
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherTypeEnum {

    记账凭证(1, "记账凭证"),

    ;

    private int code;
    private String value;

    VoucherTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTypeEnum valueOfByCode(int code){
        for (VoucherTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_MAKE_TYPE_NOT_SUPPORT.msg());
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
