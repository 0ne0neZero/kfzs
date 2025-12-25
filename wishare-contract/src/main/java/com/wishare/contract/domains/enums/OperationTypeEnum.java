package com.wishare.contract.domains.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 动态成本触点枚举
 */
public enum OperationTypeEnum {

    RELEASE(30, "释放"),
    CHECK(10, "检查"),
    OCCUPY(20, "占用"),
    ARCHIVE_OCCUPY(21, "归档占用"),
    ;
    OperationTypeEnum(Integer code, String name){
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

    public static OperationTypeEnum getEnumByCode(int code){
        for (OperationTypeEnum enums : OperationTypeEnum.values()) {
            if (code == enums.getCode()){
                return enums;
            }
        }
        return null;
    }

    public static Integer getEnumByName(String name){
        for (OperationTypeEnum enums : OperationTypeEnum.values()) {
            if (enums.getName().equals(name)){
                return enums.getCode();
            }
        }
        return null;
    }

    public static Map<Integer, String> getMap() {
        return Stream.of(values()).collect(Collectors.toMap(OperationTypeEnum::getCode, OperationTypeEnum::getName));
    }

}
