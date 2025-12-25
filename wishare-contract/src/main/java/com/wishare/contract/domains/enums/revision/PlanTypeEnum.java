package com.wishare.contract.domains.enums.revision;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanTypeEnum {

    INCOME(0,"收入计划"),

    PAY(1,"成本计划");

    private final Integer code;

    private final String name;

}
