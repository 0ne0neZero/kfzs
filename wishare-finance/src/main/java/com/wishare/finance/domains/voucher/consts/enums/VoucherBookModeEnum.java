package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 推凭账簿模式
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherBookModeEnum {

    指定账簿(1, "指定账簿"),
    映射账簿(2, "映射账簿"),

    ;

    private int code;
    private String value;

    VoucherBookModeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherBookModeEnum valueOfByCode(int code){
        for (VoucherBookModeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_BOOK_MODE_NOT_SUPPORT.msg());
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
