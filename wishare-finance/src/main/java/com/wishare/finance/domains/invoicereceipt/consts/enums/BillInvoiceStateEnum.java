package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/22
 * @Description: 开票状态：0 未开票，1 开票中，2 部分开票，3 已开票
 */
public enum BillInvoiceStateEnum {

    未开票(0, "未开票"),
    开票中(1, "开票中"),
    部分开票(2, "部分开票"),
    已开票(3, "已开票"),

    ;

    private Integer code;

    private String des;

    public static BillInvoiceStateEnum valueOfByCode(Integer code) {
        BillInvoiceStateEnum e = null;
        for (BillInvoiceStateEnum ee : BillInvoiceStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    BillInvoiceStateEnum(Integer code, String des) {
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
