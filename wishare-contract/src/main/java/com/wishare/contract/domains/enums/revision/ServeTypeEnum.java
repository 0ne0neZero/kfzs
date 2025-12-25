package com.wishare.contract.domains.enums.revision;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum ServeTypeEnum {

    OTHER(0, "其它"),

    SBYF(1, "四保一服");

    @JsonValue
    private final Integer code;

    private final String name;

    public static ServeTypeEnum convert(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (ServeTypeEnum type : ServeTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

}
