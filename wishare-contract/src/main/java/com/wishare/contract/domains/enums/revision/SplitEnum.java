package com.wishare.contract.domains.enums.revision;

import lombok.Getter;

/**
 * 主要方便看
 *
 * @author 龙江锋
 * @date 2023/8/18 14:49
 */
@Getter
public enum SplitEnum {
    once(1, "一次性", 0),
    year(2, "按年", 12),
    halfYear(3, "按半年", 6),
    quarter(4, "按季度", 3),
    month(5, "按年", 1);

    private final Integer mode;
    private final String name;
    private final Integer way;

    SplitEnum(Integer mode, String name, Integer way) {
        this.name = name;
        this.mode = mode;
        this.way = way;
    }
}
