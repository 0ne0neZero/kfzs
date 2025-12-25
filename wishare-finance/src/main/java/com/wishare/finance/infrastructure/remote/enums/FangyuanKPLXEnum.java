package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--开票类型
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanKPLXEnum {

    正票(1, "正票"),
    红票(2, "红票");

    private Integer code;

    private String des;


    public static FangyuanKPLXEnum valueOfByCode(Integer code) {
        FangyuanKPLXEnum e = null;
        for (FangyuanKPLXEnum ee : FangyuanKPLXEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanKPLXEnum(Integer code, String des) {
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
