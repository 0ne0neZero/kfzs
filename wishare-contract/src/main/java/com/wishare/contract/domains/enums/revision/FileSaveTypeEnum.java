package com.wishare.contract.domains.enums.revision;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * 关联附件保存表-业务类型
 */
public enum FileSaveTypeEnum {


    其他说明文件(1001, "其他说明文件", "其他说明文件"),
    引用合同范本(2001, "引用合同范本", "引用合同范本"),
    用章合同(3001, "用章合同", "用章合同"),



    保证金附件凭证(101, "保证金附件凭证", "保证金附件凭证"),
    保证金业务操作附件凭证(105, "保证金业务操作附件凭证", "保证金业务操作附件凭证"),
    ;

    private Integer code;
    private String name;

    private String aliasName;

    FileSaveTypeEnum(Integer code, String name, String aliasName) {
        this.code = code;
        this.name = name;
        this.aliasName = aliasName;
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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    @JsonCreator
    public static FileSaveTypeEnum parse(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values()).filter(v -> code.equals(v.code)).findAny().orElse(null);
    }
}
