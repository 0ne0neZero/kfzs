package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--开票类型
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanQdKPLXEnum {

    蓝票(0, "蓝票"),
    红票(1, "红票");

    private Integer code;

    private String des;


    public static FangyuanQdKPLXEnum valueOfByCode(Integer code) {
        FangyuanQdKPLXEnum e = null;
        for (FangyuanQdKPLXEnum ee : FangyuanQdKPLXEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanQdKPLXEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public String getDes() {
        return des;
    }

}
