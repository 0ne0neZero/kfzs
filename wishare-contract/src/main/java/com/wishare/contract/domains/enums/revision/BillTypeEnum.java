package com.wishare.contract.domains.enums.revision;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:29
 */
public enum BillTypeEnum {

    增值税电子发票(0,"增值税电子发票"),
    增值税纸质发票(1,"增值税纸质发票"),
    增值税专业发票(2,"增值税专业发票");

    private Integer code;
    private String name;

    BillTypeEnum(Integer code, String name) {
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
        for (BillTypeEnum value : BillTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
