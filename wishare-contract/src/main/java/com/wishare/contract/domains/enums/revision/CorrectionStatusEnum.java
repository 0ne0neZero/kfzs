package com.wishare.contract.domains.enums.revision;

import java.util.Arrays;
import java.util.List;

//修正审批状态
public enum CorrectionStatusEnum {
    草稿(0,"草稿"),
    审批中(1,"审批中"),
    已通过(2,"已通过"),
    已驳回(3,"已驳回"),
    通过后修改确收计划(99,"通过后修改确收计划"),
    ;

    private Integer code;
    private String name;

    CorrectionStatusEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(Integer code) {
        for (CorrectionStatusEnum value : CorrectionStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (CorrectionStatusEnum value : CorrectionStatusEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }


}
