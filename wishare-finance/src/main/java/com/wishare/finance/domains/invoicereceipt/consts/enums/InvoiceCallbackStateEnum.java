package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author dongpeng
 * @date 2023/9/12 19:12
 */
public enum InvoiceCallbackStateEnum {
    开票完成(1, "开票完成"),
    开票失败(2, "开票失败"),
    开票成功签章失败(3, "开票成功签章失败(电票时)");

    private Integer code;

    private String des;

    public static InvoiceReceiptStateEnum valueOfByCode(Integer code) {
        InvoiceReceiptStateEnum e = null;
        for (InvoiceReceiptStateEnum ee : InvoiceReceiptStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public boolean equalsByCode(int code){
        return this.code == code;
    }

    InvoiceCallbackStateEnum(Integer code, String des) {
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
