package com.wishare.finance.domains.voucher.consts.enums;


/**
 * 推凭状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherStateEnum {

    待同步(0, "待同步"),
    成功(1, "成功"),
    失败(2, "失败"),
    同步中(3, "同步中"),

    ;

    private int code;
    private String value;

    VoucherStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherStateEnum valueOfByCode(int code){
        for (VoucherStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return 同步中;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
