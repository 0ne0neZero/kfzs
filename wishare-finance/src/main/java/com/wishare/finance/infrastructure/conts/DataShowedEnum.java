package com.wishare.finance.infrastructure.conts;

public enum DataShowedEnum {

    隐藏(0, false),
    显示(1, true);

    private int code;
    private boolean value;

    DataShowedEnum(int code, boolean value) {
        this.code = code;
        this.value = value;
    }

    public static DataShowedEnum ofByValue(boolean value){
        return value ? DataShowedEnum.隐藏 : DataShowedEnum.显示;
    }

    public int getCode() {
        return code;
    }

    public boolean isValue() {
        return value;
    }

}
