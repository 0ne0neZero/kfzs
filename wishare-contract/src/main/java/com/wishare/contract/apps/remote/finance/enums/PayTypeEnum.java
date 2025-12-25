package com.wishare.contract.apps.remote.finance.enums;

/**
 * @author xujian
 * @date 2022/12/30
 * @Description:  付款类型：0 普通付款，1 退款付款
 */
public enum PayTypeEnum {

    普通付款(0,"普通付款"),
    退款付款(1,"退款付款"),


    ;

    private Integer code;

    private String des;

    PayTypeEnum(Integer code, String des) {
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
