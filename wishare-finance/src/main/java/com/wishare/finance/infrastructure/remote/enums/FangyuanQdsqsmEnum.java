package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--清单标志
 * @author dongpeng
 * @version 1.0
 * @since 2023/7/5
 */
public enum FangyuanQdsqsmEnum {

    销方申请("0", "销方申请"),
    购方申请("1", "购方申请");


    private String code;

    private String des;


    public static FangyuanQdsqsmEnum valueOfByCode(String code) {
        FangyuanQdsqsmEnum e = null;
        for (FangyuanQdsqsmEnum ee : FangyuanQdsqsmEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanQdsqsmEnum(String code, String des) {
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
