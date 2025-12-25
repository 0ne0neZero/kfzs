package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票明细--优惠政策标识
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanYHZCBSEnum {

    不使用("0", "不使用"),
    使用("1", "使用");

    private String code;

    private String des;


    public static FangyuanYHZCBSEnum valueOfByCode(String code) {
        FangyuanYHZCBSEnum e = null;
        for (FangyuanYHZCBSEnum ee : FangyuanYHZCBSEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanYHZCBSEnum(String code, String des) {
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
