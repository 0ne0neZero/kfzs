package com.wishare.contract.domains.enums.revision;

import lombok.Getter;

/**
 * 结算周期枚举
 * @author admin
 */
@Getter
public enum SettlementCycleEnum {
    once(1, "一次性拆分", 0),
    year(2, "按年拆分", 12),
    halfYear(3, "按半年拆分", 6),
    quarter(4, "按季度拆分", 3),
    month(5, "按月拆分", 1);

    private final Integer mode;
    private final String name;
    private final Integer way;

    SettlementCycleEnum(Integer mode, String name, Integer way) {
        this.name = name;
        this.mode = mode;
        this.way = way;
    }

    public static String parseName(Integer code) {
        for (SettlementCycleEnum value : SettlementCycleEnum.values()) {
            if (value.getMode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
