package com.wishare.finance.domains.voucher.consts.enums;

/**
 * @description: 推凭系统
 * @author: pgq
 * @since: 2023/2/17 9:14
 * @version: 1.0.0
 */
public enum VoucherInferSystem {
    NC(1, "nc系统");

    /**
     * 编号
     */
    private int code;

    /**
     * 系统名称
     */
    private String sysName;

    VoucherInferSystem(int code, String sysName) {
        this.code = code;
        this.sysName = sysName;
    }

    public int getCode() {
        return code;
    }

    public String getSysName() {
        return sysName;
    }

    public static VoucherInferSystem getEnumByCode(int code) {
        for (VoucherInferSystem value : VoucherInferSystem.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return VoucherInferSystem.NC;
    }
}
