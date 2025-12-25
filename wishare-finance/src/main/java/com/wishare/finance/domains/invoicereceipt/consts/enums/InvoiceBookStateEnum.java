package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description: 票本状态：1.未领用 2.部分领用 3.全部领用
 */
public enum InvoiceBookStateEnum {


    未领用(1, "未领用"),
    部分领用(2, "部分领用"),
    全部领用(3, "全部领用"),

    ;

    private Integer code;

    private String des;

    public static InvoiceBookStateEnum valueOfByCode(Integer code) {
        InvoiceBookStateEnum e = null;
        for (InvoiceBookStateEnum ee : InvoiceBookStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    InvoiceBookStateEnum(Integer code, String des) {
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
