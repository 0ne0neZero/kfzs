package com.wishare.contract.domains.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlanFxmType {

    INCOME(0,"收款计划"),

    PAY(1,"结算计划");

    private final Integer code;

    private final String name;

}
