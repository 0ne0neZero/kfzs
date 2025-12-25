package com.wishare.finance.domains.configure.organization.consts.enums;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description: 账户类型：1.基本账户，2 一般账户，3 专用账户
 */
public enum StatutoryBodyAccountTypeEnum {
    基本账户(1, "基本账户"),
    一般账户(2, "一般账户"),
    专用账户(3, "专用账户"),
    临时账户(4, "临时账户"),

    ;

    private Integer code;

    private String des;

    StatutoryBodyAccountTypeEnum(Integer code, String des) {
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
