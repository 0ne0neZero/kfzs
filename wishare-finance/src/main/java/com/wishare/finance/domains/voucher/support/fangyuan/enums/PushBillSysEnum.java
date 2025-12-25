package com.wishare.finance.domains.voucher.support.fangyuan.enums;


public enum PushBillSysEnum {

    方圆系统(1, "方圆系统"),
    中交系统(2, "中交系统");

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
