package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description: 使用状态：1 待使用 2 已使用
 */
public enum InvoiceReceiveDetailedStateEnum {

    待使用(1, "待使用"),
    已使用(2, "已使用"),

    ;

    private Integer code;

    private String des;

    public static InvoiceReceiveDetailedStateEnum valueOfByCode(Integer code) {
        InvoiceReceiveDetailedStateEnum e = null;
        for (InvoiceReceiveDetailedStateEnum ee : InvoiceReceiveDetailedStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    InvoiceReceiveDetailedStateEnum(Integer code, String des) {
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
