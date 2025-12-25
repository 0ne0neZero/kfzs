package com.wishare.finance.infrastructure.enums;

public enum ExcelSheetEnum {

    ALL("all", "all"), VALID_CHOICE_INFO("validChoiceInfo", "validChoiceInfo"),

    REMOTE("remote", "remote"), NO("no", "no");
    private String type;
    private String name;


    private ExcelSheetEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }
}
