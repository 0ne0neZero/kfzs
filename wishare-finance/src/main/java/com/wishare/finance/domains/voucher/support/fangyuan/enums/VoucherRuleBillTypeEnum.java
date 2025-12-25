package com.wishare.finance.domains.voucher.support.fangyuan.enums;

import lombok.Getter;

@Getter
public enum VoucherRuleBillTypeEnum {

    应收账单(1, "应收账单"),
    临时账单(3, "临时账单");

    private final int code;
    private final String value;

    VoucherRuleBillTypeEnum(int code, String value) {
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

}
