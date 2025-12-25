package com.wishare.contract.domains.enums.revision;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:23
 */
public enum SettleStatusEnum {

    未结算(0,"未结算"),
    未完成(1,"未完成"),
    已完成(2,"已完成");

    private Integer code;
    private String name;

    SettleStatusEnum(Integer code, String name) {
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
        if (null == code) {
            return null;
        }
        for (SettleStatusEnum value : SettleStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
