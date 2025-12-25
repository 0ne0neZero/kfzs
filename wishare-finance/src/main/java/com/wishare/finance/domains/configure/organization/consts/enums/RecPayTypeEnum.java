package com.wishare.finance.domains.configure.organization.consts.enums;

/**
 * @author xujian
 * @date 2022/12/26
 * @Description: 收款付款类型：1.收款付款，2.收款，3.付款
 */
public enum RecPayTypeEnum {

    收款付款(1, "收款付款"),
    收款(2, "收款"),
    付款(3, "付款"),

    ;

    private Integer code;

    private String des;

    RecPayTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
