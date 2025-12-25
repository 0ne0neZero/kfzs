package com.wishare.finance.domains.reconciliation.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StaCostEnum {

    STA(0, "法定单位"),

    COST(1, "成本中心");

    @JsonValue
    private final int code;

    private final String value;

}
