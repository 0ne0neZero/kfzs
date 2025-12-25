package com.wishare.finance.domains.configure.subject.consts.enums;

/**
 * 科目相关状态
 *
 * @author yancao
 */
public enum SubjectStatusEnum {

    SUBJECT_CATEGORY_MAX_LEVEL(10, "科目类别最大层级"),
    SUBJECT_CATEGORY_LEVEL_START_NUM(1, "科目类别开始层级"),
    SUBJECT_MAX_LEVEL(5, "科目最大层级"),
    SUBJECT_LEVEL_START_NUM(1, "科目开始层级"),
    ;

    private int code;
    private String value;

    SubjectStatusEnum(int code, String value) {
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
