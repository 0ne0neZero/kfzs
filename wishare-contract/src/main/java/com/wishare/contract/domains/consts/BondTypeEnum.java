package com.wishare.contract.domains.consts;

import com.wishare.contract.apps.remote.finance.enums.SettleWayEnum;

/**
 * @author xujian
 * @date 2023/2/7
 * @Description: 保证金类型 0 收取类 1 缴纳类
 */
public enum BondTypeEnum {

    收取类(0, "收取类"),
    缴纳类(1, "缴纳类"),

    ;

    private Integer code;

    private String des;

    public static BondTypeEnum getEnum(Integer code) {
        BondTypeEnum e = null;
        for (BondTypeEnum ee : BondTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    BondTypeEnum(Integer code, String des) {
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
