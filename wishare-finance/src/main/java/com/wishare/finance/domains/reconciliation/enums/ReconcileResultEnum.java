package com.wishare.finance.domains.reconciliation.enums;

/**
 * 商户清分对账结果枚举
 * 账票流水对账结果枚举
 * 收款单、账单对账状态枚举
 * @author yancao
 */
public enum ReconcileResultEnum {

    未核对(0, "未核对"),
    部分核对(1, "部分核对"),
    已核对(2, "已核对"),
    核对失败(3, "核对失败"),
    ;
    private int code;
    private String value;

    ReconcileResultEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ReconcileResultEnum valueOfByCode(int code){
        for (ReconcileResultEnum value : values()) {
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
