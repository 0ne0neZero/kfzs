package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--代开标志
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanDKBZEnum {

    自开("0", "自开"),
    代开("1", "代开");

    private String code;

    private String des;


    public static FangyuanDKBZEnum valueOfByCode(String code) {
        FangyuanDKBZEnum e = null;
        for (FangyuanDKBZEnum ee : FangyuanDKBZEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanDKBZEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }

    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

}
