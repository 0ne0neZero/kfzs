package com.wishare.finance.domains.reconciliation.enums;

/**
 * 对账状态
 * @author yancao
 */
public enum ReconcileRunStateEnum {

    待运行(0, "待运行"),
    运行中(1, "运行中"),
    已完成(2, "已完成"),
    ;

    private int code;
    private String value;

    ReconcileRunStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ReconcileRunStateEnum valueOfByCode(int code){
        for (ReconcileRunStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }
    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
