package com.wishare.finance.domains.pushbill.enums;

public enum VoucherChargeTypeEnums {
    暂时性扣款("01", "暂时性扣款"),
    核销预付款("02", "核销预付款"),
    返还款("03", "返还款"),
    实际应支付("04", "实际应支付"),
    永久性扣款("05", "永久性扣款"),
    支付预付款("06", "支付预付款"),


    ;

    private String code;
    private String value;
    VoucherChargeTypeEnums(String code, String value){
        this.code = code;
        this.value = value;
    }

    public static VoucherChargeTypeEnums valueOfByCode(String code){
        for (VoucherChargeTypeEnums value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return code.equals(this.code);
    }
}
