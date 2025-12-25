package com.wishare.contract.domains.enums.revision;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:23
 */
public enum IncomeSettleStatusEnum {

    未确收(0,"未确收"),
    确收中(1,"确收中"),
    已确收(2,"已确收");

    private Integer code;
    private String name;

    IncomeSettleStatusEnum(Integer code, String name) {
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
        for (IncomeSettleStatusEnum value : IncomeSettleStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
