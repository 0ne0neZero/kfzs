package com.wishare.contract.domains.enums.revision;

import java.util.Objects;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:20
 */
public enum PaymentMethodEnum {

    现金(0,"现金"),
    银行转帐(1,"银行转帐"),
    支票(2,"支票");

    private Integer code;
    private String name;

    PaymentMethodEnum(Integer code, String name) {
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
        if (Objects.isNull(code)){
            return null;
        }
        for (PaymentMethodEnum value : PaymentMethodEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
