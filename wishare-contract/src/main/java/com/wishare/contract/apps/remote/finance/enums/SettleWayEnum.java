package com.wishare.contract.apps.remote.finance.enums;


/**
 * 结算方式
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum SettleWayEnum {

    线上(0, "线上"),
    线下(1, "线下"),
    ;

    private int code;
    private String value;

    SettleWayEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public static SettleWayEnum getEnum(Integer code) {
        SettleWayEnum e = null;
        for (SettleWayEnum ee : SettleWayEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

}
