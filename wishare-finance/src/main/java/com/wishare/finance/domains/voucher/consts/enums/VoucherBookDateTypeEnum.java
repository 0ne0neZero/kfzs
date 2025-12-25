package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 记账日期类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherBookDateTypeEnum {

    凭证生成日期(1, "凭证生成日期"),
    凭证同步日期(2, "凭证同步日期"),

    ;

    private int code;
    private String value;

    VoucherBookDateTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherBookDateTypeEnum valueOfByCode(int code){
        for (VoucherBookDateTypeEnum value : values()) {
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
