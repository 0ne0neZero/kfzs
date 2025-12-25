package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票明细--零税率标识
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanLSLBSEnum {

    非零税率("", "非零税率"),
    出口零税("0", "出口零税"),
    免税("1", "免税"),
    不征税("2", "不征税"),
    普通零税率("3", "普通零税率"),;

    private String code;

    private String des;


    public static FangyuanLSLBSEnum valueOfByCode(String code) {
        FangyuanLSLBSEnum e = null;
        for (FangyuanLSLBSEnum ee : FangyuanLSLBSEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanLSLBSEnum(String code, String des) {
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
