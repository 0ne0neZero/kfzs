package com.wishare.contract.apis.file.enums;

import lombok.Getter;

/**
 * 附件表-业务类型
 */
@Getter
public enum FileBusinessTypeEnum {

    FINANCE_OTHER_FILE(1004,"结算审批-财务结算-其他附件");

    private Integer code;
    private String name;

    FileBusinessTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
