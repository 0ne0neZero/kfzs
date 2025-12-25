package com.wishare.finance.domains.voucher.consts.enums;

/**
 * @description: 账单来源
 * @author: pgq
 * @since: 2022/10/24 14:26
 * @version: 1.0.0
 */
public enum BillSourceEnum {

    财务中心("cwzx", "财务中心"),
    BPM("bpm", "BPM"),
    亿家U选("yjux", "亿家U选"),
    收费系统("sfxt", "收费系统"),
    合同("ht", "合同"),
    ;

    private String code;

    private String desc;

    BillSourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getDescByCode(String code) {
        for (BillSourceEnum value : BillSourceEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }
}
