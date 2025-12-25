package com.wishare.finance.domains.expensereport.enums;

import lombok.Getter;

/**
 * 金蝶推送场景
 */
@Getter
public enum KingDeePushSceneTypeEnum {

    开票计提(1, "开票计提"),
    收款(2, "收款"),
    ;

    private final int code;

    private final String name;

    KingDeePushSceneTypeEnum(int  code, String name) {
        this.code = code;
        this.name = name;
    }

}
