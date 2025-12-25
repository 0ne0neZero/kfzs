package com.wishare.finance.infrastructure.remote.enums;

/**
 * 诺诺申请方（录入方）身份
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/14
 */
public enum NuonuoApplySourceEnum {

    销方(0, "销方"),
    购方(1, "购方");

    private Integer code;

    private String des;


    public static NuonuoApplySourceEnum valueOfByCode(Integer code) {
        NuonuoApplySourceEnum e = null;
        for (NuonuoApplySourceEnum ee : NuonuoApplySourceEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    NuonuoApplySourceEnum(Integer code, String des) {
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
