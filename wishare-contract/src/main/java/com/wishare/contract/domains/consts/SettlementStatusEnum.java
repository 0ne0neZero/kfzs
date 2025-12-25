package com.wishare.contract.domains.consts;

/**
 * 0:没有，1:部分，2:完全
 */
public enum SettlementStatusEnum {

    未确收(0),
    部分确收(1),
    已确收(2);
    ;

    private Integer code;

    public static SettlementStatusEnum getEnum(Integer code) {
        SettlementStatusEnum e = null;
        for (SettlementStatusEnum ee : SettlementStatusEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    SettlementStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
