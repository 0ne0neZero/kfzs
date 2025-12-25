package com.wishare.contract.domains.enums.revision;

/**
 * 合同类型(1:合同订立支出 2:合同订立收入)
 *
 * @author long
 * @date 2023/7/19 14:36
 */
public enum ContractConcludeEnum {
    PAY(1, "合同订立支出"),
    INCOME(2, "合同订立收入"),
    PAY_SETTLEMENT(3, "付款结算单"),

    INCOME_SETTLEMENT(6, "收款结算单"),
    SETTLEMENT_FUND(5,"收付款单"),
    SETTLEMENT_NK(10,"NK结算单"),

    PROJECT_INITIATION_COST_CONFIRM(31,"立项管理成本确认"),
    PROJECT_INITIATION_ORDER_FOR_JD(32,"立项管理慧采下单"),
    ;

    private final int code;

    private final String value;

    ContractConcludeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code) {
        return code == this.code;
    }

    public static ContractConcludeEnum valueOfByCode(int code) {
        for (ContractConcludeEnum value : values()) {
            if (value.equalsByCode(code)) {
                return value;
            }
        }
        return null;
    }
}
