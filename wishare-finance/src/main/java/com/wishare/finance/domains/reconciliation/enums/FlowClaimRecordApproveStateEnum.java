package com.wishare.finance.domains.reconciliation.enums;

public enum FlowClaimRecordApproveStateEnum {

    已审核(0, "已审核"),
    审核中(1, "审核中"),
    ;
    private final int code;
    private final String value;

    FlowClaimRecordApproveStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
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

}
