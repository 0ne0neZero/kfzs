package com.wishare.finance.domains.voucher.enums;


public enum VoucherBillTypeEnums {

    应收账单(1, "应收账单"),
    预收账单(2, "预收账单"),
    临时收费账单(3, "临时收费账单"),
    手续费(4, "手续费");

    private int code;
    private String value;

    VoucherBillTypeEnums(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static VoucherBillTypeEnums valueOfByCode(int code){
        for (VoucherBillTypeEnums value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }
    public boolean equalsByCode(int code){
        return code == this.code;
    }
}
