package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--操作代码
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanCZDMEnum {

    正票正常开具("10", "正票正常开具"),
    退货折让红票("20", "退货折让红票");

    private String code;

    private String des;


    public static FangyuanCZDMEnum valueOfByCode(String code) {
        FangyuanCZDMEnum e = null;
        for (FangyuanCZDMEnum ee : FangyuanCZDMEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanCZDMEnum(String code, String des) {
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
