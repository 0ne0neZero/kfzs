package com.wishare.contract.domains.enums;

/**
 * 入账状态
 */
public enum IriStatusEnum {

    no(1,"未入账"),
    yes(2,"入账");

    private Integer code;
    private String name;

    IriStatusEnum(Integer code, String name) {
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
        for (IriStatusEnum value : IriStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
