package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 借贷类型
 * @author dxclay
 * @since  2023/3/15
 * @version 1.0
 */
public enum VoucherLoanTypeEnum {

    贷方("credit", "贷方"),
    借方("debit", "借方"),
    ;

    private String code;
    private String value;

    VoucherLoanTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public boolean equalsByCode(String code){
        return this.code.equals(code);
    }

    public String getValue() {
        return value;
    }
}
