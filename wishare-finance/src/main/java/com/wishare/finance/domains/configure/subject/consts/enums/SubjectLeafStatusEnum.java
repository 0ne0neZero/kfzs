package com.wishare.finance.domains.configure.subject.consts.enums;

public enum SubjectLeafStatusEnum {
    非叶子节点(0, "非叶子节点"),
    叶子节点(1, "叶子节点"),
    ;

    private int code;
    private String value;

    SubjectLeafStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
