package com.wishare.contract.domains.enums.revision;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerifyStatusEnum {

    PASS(0,"验证通过"),

    NOT_PASS(1,"验证未通过");

    private final Integer code;

    private final String name;

}
