package com.wishare.finance.infrastructure.conts;

public enum DataDisabledEnum {

    启用(0, false,"启用"),
    禁用(1, true,"禁用");

    private int code;
    private boolean value;

    private String des;

    DataDisabledEnum(int code, boolean value, String des) {
        this.code = code;
        this.value = value;
        this.des = des;
    }

    public static DataDisabledEnum ofByValue(boolean value){
        return value ? DataDisabledEnum.禁用 : DataDisabledEnum.启用;
    }

    public static DataDisabledEnum valueOfByCode(int code) {
        DataDisabledEnum e = null;
        for (DataDisabledEnum ee : DataDisabledEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public boolean equalsByCode(int code){
        return this.code == code;
    }

    public int getCode() {
        return code;
    }

    public boolean isValue() {
        return value;
    }

    public String getDes() {
        return des;
    }
}
