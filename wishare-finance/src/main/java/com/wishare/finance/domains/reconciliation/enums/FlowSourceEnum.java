package com.wishare.finance.domains.reconciliation.enums;

/**
 * 流水来源
 * @author yancao
 */
public enum FlowSourceEnum {

    导入(0, "导入"),
    同步(1, "同步"),
    ;

    private int code;
    private String value;

    FlowSourceEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FlowSourceEnum valueOfByCode(int code){
        for (FlowSourceEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

    public static FlowSourceEnum valueOfByName(String name){
        for (FlowSourceEnum value : values()) {
            if (value.equalsByValue(name)){
                return value;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public boolean equalsByValue(String value){
        return value.equals(this.value);
    }


}
