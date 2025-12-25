package com.wishare.finance.domains.configure.subject.consts.enums;

/**
 * 科目映射类型
 * @author dxclay
 * @since  2023/3/17
 * @version 1.0
 */
public enum SubjectRuleMapTypeEnum {

    科目(1, "科目"),
    现金流量(2, "现金流量"),

    ;

    private int code;
    private String des;

    SubjectRuleMapTypeEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

}
