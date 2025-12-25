package com.wishare.finance.domains.bill.consts.enums;

/**
 * @description: 账单状态
 * @author: pgq
 * @since: 2022/9/29 11:17
 * @version: 1.0.0
 */
public enum BillAccountHandedEnum {

    UNHANDED(0, "未交账"),
    PART_HANDED(1, "部分交账"),
    HANDED(2, "已交账")
    ;

    private int state;

    private String desc;

    BillAccountHandedEnum(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }

    public int getState() {
        return state;
    }

    public String getDesc() {
        return desc;
    }
}
