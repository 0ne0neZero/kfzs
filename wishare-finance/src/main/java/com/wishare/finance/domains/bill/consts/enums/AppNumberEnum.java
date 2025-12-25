package com.wishare.finance.domains.bill.consts.enums;


/**
 * 系统编码
 *
 * @author yancao
 */
public enum AppNumberEnum {

    合同系统(0, "CONTRACT_SYS"),
    收费系统(1, "CHARGE_SYS"),
    ;

    private int code;
    private String value;

    AppNumberEnum(int code, String value) {
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

}
