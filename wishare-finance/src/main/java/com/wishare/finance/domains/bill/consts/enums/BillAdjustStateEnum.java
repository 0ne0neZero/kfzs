package com.wishare.finance.domains.bill.consts.enums;


/**
 * 账单中的调整
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BillAdjustStateEnum {

    未调整(0, "未调整"),
    已调整(1, "已调整"),
    ;

    private int code;
    private String value;

    BillAdjustStateEnum(int code, String value) {
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
