package com.wishare.finance.domains.configure.subject.consts.enums;

public enum SubjectDisabledEnum {

    启用(0, false),
    禁用(1, true);

    private int code;
    private boolean value;

    SubjectDisabledEnum(int code, boolean value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public boolean isValue() {
        return value;
    }

}
