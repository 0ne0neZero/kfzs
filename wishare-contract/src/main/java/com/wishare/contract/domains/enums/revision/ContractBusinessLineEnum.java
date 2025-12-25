package com.wishare.contract.domains.enums.revision;

import lombok.Getter;

import java.util.Objects;

/**
 * @author hhb
 * @describe
 * @date 2025/11/1 14:46
 */
@Getter
public enum ContractBusinessLineEnum {

    物管(1,"物管"),
    建管(2,"建管"),
    商管(3,"商管"),
    全部(4,"全部"),
    ;

    private Integer code;
    private String name;

    ContractBusinessLineEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public static String parseName(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ContractBusinessLineEnum value : ContractBusinessLineEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractBusinessLineEnum value : ContractBusinessLineEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }
}
