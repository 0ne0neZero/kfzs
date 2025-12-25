package com.wishare.finance.infrastructure.remote.enums;


import java.util.Objects;

public enum ApproveProcessCompleteEnum {

    通过("通过", "通过"),
    ;
    private final String code;
    private final String value;

    ApproveProcessCompleteEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return Objects.equals(code, this.code);
    }

}
