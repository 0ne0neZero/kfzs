package com.wishare.finance.domains.reconciliation.enums;

public enum FlowDifferenceReasonEnum {

    银行流水已扣除手续费(0, "银行流水已扣除手续费");

    private int code;
    private String value;

    FlowDifferenceReasonEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FlowDifferenceReasonEnum valueOfByCode(int code){
        for (FlowDifferenceReasonEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

    public static FlowDifferenceReasonEnum valueOfByName(String name){
        for (FlowDifferenceReasonEnum value : values()) {
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
