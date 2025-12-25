package com.wishare.finance.infrastructure.remote.enums;

/**
 * 方圆传参发票主体--发票种类
 * @author dongpeng
 * @version 1.0
 * @since 2023/6/21
 */
public enum FangyuanSWJGDMEnum {

    专票("0", "专票"),
    普票("2", "普票"),
    卷式发票("41", "卷式发票"),
    电子普票("51", "电子普票"),
    电子专票("52", "电子专票"),
    数电普票("151", "数电普票"),
    数电专票("152", "数电专票");

    private String code;

    private String des;


    public static FangyuanSWJGDMEnum valueOfByCode(String code) {
        FangyuanSWJGDMEnum e = null;
        for (FangyuanSWJGDMEnum ee : FangyuanSWJGDMEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    FangyuanSWJGDMEnum(String code, String des) {
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
