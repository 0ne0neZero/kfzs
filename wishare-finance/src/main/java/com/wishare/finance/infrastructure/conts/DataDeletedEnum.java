package com.wishare.finance.infrastructure.conts;

public enum DataDeletedEnum {

    NORMAL(0, false, "未删除"),
    DELETED(1, true, "已删除"),

    ;


    private int code;

    private Boolean isDeleted;

    private String des;


    DataDeletedEnum(int code, Boolean isDeleted, String des) {
        this.code = code;
        this.isDeleted = isDeleted;
        this.des = des;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
