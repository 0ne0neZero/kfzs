package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 录制方式
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherSystemEnum {

    用友NCC(1, "用友NCC"),

    ;

    private int code;
    private String value;

    VoucherSystemEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherSystemEnum valueOfByCode(int code){
        for (VoucherSystemEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_SYSTEM_NOT_SUPPORT.msg());
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
