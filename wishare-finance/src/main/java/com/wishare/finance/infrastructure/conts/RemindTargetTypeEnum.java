package com.wishare.finance.infrastructure.conts;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RemindTargetTypeEnum {

    PERSON(0,"人员"),

    ROLE(1,"角色");

    private final Integer code;

    private final String desc;

}
