package com.wishare.finance.domains.expensereport.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 金蝶推送状态
 */
@Getter
public enum KingDeePushStateEnum {

    未推送("0", "未推送"),
    推送中("1", "推送中"),
    已推送("2", "已推送"),
    已制单("3", "已制单"),
    推送失败("4", "推送失败"),

    ;

    private final String code;

    private final String name;

    KingDeePushStateEnum(String  code, String name) {
        this.code = code;
        this.name = name;
    }

    public boolean equalsByCode(String code){
        return StringUtils.equals(code, this.code);
    }

    public static KingDeePushStateEnum valueOfCode(String code) {
        for (KingDeePushStateEnum state : KingDeePushStateEnum.values()) {
            if (StringUtils.equals(state.getCode(), code)) {
                return state;
            }
        }
        return null;
    }
}
