package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:45
 */
public enum ContractProjectOrderPlatformEnum {

    线下采购(0,"线下采购"),
    京东慧采(1,"京东慧采"),
    中交云彩(2,"中交云彩"),
    交心易购(3,"交心易购"),
    其他线上平台(4,"其他线上平台"),
    ;

    private Integer code;
    private String name;

    ContractProjectOrderPlatformEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(Integer code) {
        for (ContractProjectOrderPlatformEnum value : ContractProjectOrderPlatformEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractProjectOrderPlatformEnum value : ContractProjectOrderPlatformEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
