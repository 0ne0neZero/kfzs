package com.wishare.finance.domains.invoicereceipt.enums;


public enum InvoiceReceiptSourceEnum {

    系统生成(1, "系统生成"),
    系统导入(2, "系统导入");

    private final int code;

    private final String name;

    InvoiceReceiptSourceEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static InvoiceReceiptSourceEnum valueOfByCode(int code) {
        for (InvoiceReceiptSourceEnum value : InvoiceReceiptSourceEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }
}
