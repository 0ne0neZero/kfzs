package com.wishare.finance.domains.configure.accountbook.consts.enums;

/**
 * @author xujian
 * @date 2022/8/19
 * @Description: 历史费项标识：0 当前费项，1 历史费项
 */
public enum ChargeItemHistoryTypeEnum {

    当前费项(0, "当前费项"),
    历史费项(1, "历史费项"),

    ;

    private Integer code;

    private String des;

    ChargeItemHistoryTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
