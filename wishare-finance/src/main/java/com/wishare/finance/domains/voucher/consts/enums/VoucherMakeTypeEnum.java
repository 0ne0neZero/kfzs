package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 录制方式
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherMakeTypeEnum {

    自动(0, "自动录制"),
    手动(1, "手动录制"),

    ;

    private int code;
    private String value;

    VoucherMakeTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherMakeTypeEnum valueOfByCode(int code){
        for (VoucherMakeTypeEnum value : values()) {
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
