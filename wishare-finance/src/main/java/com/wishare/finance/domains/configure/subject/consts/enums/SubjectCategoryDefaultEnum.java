package com.wishare.finance.domains.configure.subject.consts.enums;

/**
 * 默认科目类型
 *
 * @author yancao
 */
public enum SubjectCategoryDefaultEnum {
    资产(1,"资产"),
    成本(2,"成本"),
    所有者权益(3,"所有者权益"),
    损益(4,"损益"),
    负债(5,"负债");

    private int code;
    private String value;

    SubjectCategoryDefaultEnum(int code, String value) {
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

    public static SubjectCategoryDefaultEnum valueOfByCode(int code){
        for (SubjectCategoryDefaultEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }
}
