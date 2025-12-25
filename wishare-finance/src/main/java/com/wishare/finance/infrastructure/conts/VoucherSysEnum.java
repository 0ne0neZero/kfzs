package com.wishare.finance.infrastructure.conts;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description:
 */
public enum VoucherSysEnum {

    用友Ncc系统(1, "用友Ncc系统");

    private Integer code;
    private String des;

    VoucherSysEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static VoucherSysEnum valueOfByCode(Integer code) {
        VoucherSysEnum e = null;
        for (VoucherSysEnum ee : VoucherSysEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
