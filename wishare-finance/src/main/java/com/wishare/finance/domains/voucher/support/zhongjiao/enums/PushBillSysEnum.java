package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


public enum PushBillSysEnum {

    中交系统(1, "中交系统");

    private Integer code;
    private String des;

    PushBillSysEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static PushBillSysEnum valueOfByCode(Integer code) {
        PushBillSysEnum e = null;
        for (PushBillSysEnum ee : PushBillSysEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
