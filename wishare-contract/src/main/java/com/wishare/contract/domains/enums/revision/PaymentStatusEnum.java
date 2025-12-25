package com.wishare.contract.domains.enums.revision;

import java.util.Objects;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/28/11:22
 */
public enum PaymentStatusEnum {
    未结算(0,"未完成"),
    已完成(1,"已完成");

    private Integer code;
    private String name;

    PaymentStatusEnum(Integer code, String name) {
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
        for (PaymentStatusEnum value : PaymentStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
