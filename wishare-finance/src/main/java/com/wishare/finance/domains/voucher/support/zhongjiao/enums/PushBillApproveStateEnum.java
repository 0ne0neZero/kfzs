package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


public enum PushBillApproveStateEnum {

    已审核(0, "已审核"),
    审核中(1, "审核中"),
    已驳回(2, "已驳回"),
    待发起(3, "待发起"),
    ;
    private final int code;
    private final String value;

    PushBillApproveStateEnum(int code, String value) {
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
