package com.wishare.finance.domains.reconciliation.enums;

/**
 * 对账执行状态
 * @author yancao
 */
public enum ReconcileExecuteStateEnum {

    待运行(0, "待运行"),
    运行中(1, "运行中"),
    ;

    private int code;
    private String value;

    ReconcileExecuteStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ReconcileExecuteStateEnum valueOfByCode(int code){
        for (ReconcileExecuteStateEnum value : values()) {
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
