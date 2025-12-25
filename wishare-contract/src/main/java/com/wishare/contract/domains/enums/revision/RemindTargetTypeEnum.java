package com.wishare.contract.domains.enums.revision;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemindTargetTypeEnum {

    PERSON(0,"人员"),

    ROLE(1,"角色"),

    HANDLER(2,"经办人");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String desc;

}
