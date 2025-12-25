package com.wishare.finance.infrastructure.remote.enums;

/**
 * @author xujian
 * @date 2023/2/15
 * @Description: 减免形式：1 应收减免；2 实收减免；3 不减免
 */
public enum DeductionMethodEnum {

    应收减免(1, "应收减免"),
    实收减免(2, "实收减免"),
    不减免(3, "不减免"),

    ;

    private Integer code;

    private String des;

    public static DeductionMethodEnum valueOfByCode(Integer code) {
        DeductionMethodEnum e = null;
        for (DeductionMethodEnum ee : DeductionMethodEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    DeductionMethodEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public boolean equalsByCode(int code){
        return this.code == code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
