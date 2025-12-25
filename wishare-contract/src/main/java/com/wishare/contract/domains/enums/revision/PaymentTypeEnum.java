package com.wishare.contract.domains.enums.revision;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:19
 */
public enum PaymentTypeEnum {

    有票付款(0,"有票付款"),
    无票付款(1,"无票付款");

    private Integer code;
    private String name;

    PaymentTypeEnum(Integer code, String name) {
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
        for (PaymentTypeEnum value : PaymentTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
