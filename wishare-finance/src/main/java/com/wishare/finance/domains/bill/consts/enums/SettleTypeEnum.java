package com.wishare.finance.domains.bill.consts.enums;


/**
 * 合并收费规则
 */
public enum SettleTypeEnum {
    自然月(1, "自然月"),
    自然季度(2, "自然季度"),
    自然半年度(3, "自然半年度"),
    自然年(4, "自然年");

    private int code;
    private String value;

    SettleTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code) {
        return code == this.code;
    }

    public static SettleTypeEnum valueOfByCode(int code) {
        for (SettleTypeEnum value : values()) {
            if (value.equalsByCode(code)) {
                return value;
            }
        }
        return null;
    }

}
