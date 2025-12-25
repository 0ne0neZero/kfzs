package com.wishare.finance.apps.process.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum BelongRegionEnum {

    PROJECT(0, "总部"),

    NORTH(1, "华北区域"),

    SOUTH(2, "华南区域"),

    EAST(3, "华东区域"),

    WEST(4, "西部区域");

    private final Integer code;

    private final String name;

    public static Integer getCodeByName(String name) {
        for (BelongRegionEnum regionEnum : BelongRegionEnum.values()) {
            if (StringUtils.equals(regionEnum.getName(), name)) {
                return regionEnum.getCode();
            }
        }
        return null;
    }
}
