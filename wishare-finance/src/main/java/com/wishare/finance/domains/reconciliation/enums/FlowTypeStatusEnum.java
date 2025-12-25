package com.wishare.finance.domains.reconciliation.enums;

/**
 * 流水类型状态
 * @author yancao
 */
public enum FlowTypeStatusEnum {

    收入(1, "收入"),
    退款(2, "退款"),
    ;

    private int code;
    private String value;

    FlowTypeStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FlowTypeStatusEnum valueOfByCode(int code){
        for (FlowTypeStatusEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

    public static FlowTypeStatusEnum valueOfByName(String name){
        for (FlowTypeStatusEnum value : values()) {
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
