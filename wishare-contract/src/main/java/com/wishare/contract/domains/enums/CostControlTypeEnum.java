package com.wishare.contract.domains.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 成本管控方式枚举
 */
public enum CostControlTypeEnum {
    /**
     * 年控
     */
    YEAR(10, "年控"),
    SEASON(15,"季控"),
    MONTH(20, "月控"),
    NO_CONTROL(30, "不控"),

    ;
    CostControlTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CostControlTypeEnum getEnumByCode(int code){
        for (CostControlTypeEnum enums : CostControlTypeEnum.values()) {
            if (code == enums.getCode()){
                return enums;
            }
        }
        return null;
    }

    public static Integer getEnumByName(String name){
        for (CostControlTypeEnum enums : CostControlTypeEnum.values()) {
            if (enums.getName().equals(name)){
                return enums.getCode();
            }
        }
        return null;
    }

    public static Map<Integer, String> getMap() {
        return Stream.of(values()).collect(Collectors.toMap(CostControlTypeEnum::getCode, CostControlTypeEnum::getName));
    }
}
