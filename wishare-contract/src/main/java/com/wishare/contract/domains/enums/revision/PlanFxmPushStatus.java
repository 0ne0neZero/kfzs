package com.wishare.contract.domains.enums.revision;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanFxmPushStatus {

    FAILED(0,"失败"),

    SUCCESS(1,"成功");

    private final Integer code;

    private final String name;

}
