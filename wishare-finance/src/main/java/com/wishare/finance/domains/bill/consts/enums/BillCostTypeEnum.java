package com.wishare.finance.domains.bill.consts.enums;


/**
 * 费用分类
 */
public enum BillCostTypeEnum {

    历史欠费(1, "历史欠费"),
    当期应收(2, "当期应收"),
    预收款项(3, "预收款项"),
    ;

    private int code;
    private String value;

    BillCostTypeEnum(int code, String value) {
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
