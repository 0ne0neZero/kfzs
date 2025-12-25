package com.wishare.finance.domains.enums;

public enum FinanceFileRecordBusinessTypeEnum {

    蓝票补录导入(1, "发票导入模板");

    private int code;
    private String name;

    private FinanceFileRecordBusinessTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getName(int code) {
        for (FinanceFileRecordBusinessTypeEnum d : FinanceFileRecordBusinessTypeEnum.values()) {
            if (d.getCode() == code) {
                return d.name;
            }
        }
        return null;
    }

    public static Integer getCode(String name) {
        for (FinanceFileRecordBusinessTypeEnum d : FinanceFileRecordBusinessTypeEnum.values()) {
            if (d.getName() == name) {
                return d.code;
            }
        }
        return null;
    }

}
