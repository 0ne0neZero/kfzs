package com.wishare.finance.domains.bill.consts.enums;

/**
 * @description: 推凭状态
 * @author: pgq
 * @since: 2022/11/4 9:48
 * @version: 1.0.0
 */
public enum BillInferStateEnum {

    未推凭(0, "未推凭"),
    已推凭(1, "已推凭")
    ;

    private int code;

    private String value;

    BillInferStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
