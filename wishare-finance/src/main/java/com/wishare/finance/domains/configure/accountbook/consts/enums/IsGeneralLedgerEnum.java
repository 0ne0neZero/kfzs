package com.wishare.finance.domains.configure.accountbook.consts.enums;

/**
 * @author xujian
 * @date 2022/8/19
 * @Description: 是否总账：0 不是，1 是
 */
public enum IsGeneralLedgerEnum {

    非总账(0, "非总账"),
    总账(1, "总账"),

    ;

    private Integer code;

    private String des;

    public static IsGeneralLedgerEnum valueOfByCode(Integer code) {
        IsGeneralLedgerEnum e = null;
        for (IsGeneralLedgerEnum ee : IsGeneralLedgerEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    IsGeneralLedgerEnum(Integer code, String des) {
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
