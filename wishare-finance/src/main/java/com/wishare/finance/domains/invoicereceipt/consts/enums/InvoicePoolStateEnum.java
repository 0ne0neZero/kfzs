package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description: 领用状态：1 未领用；2 已领用
 */
public enum InvoicePoolStateEnum {

    未领用(1, "未领用"),
    已领用(2, "已领用"),

    ;

    private Integer code;

    private String des;

    InvoicePoolStateEnum(Integer code, String des) {
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
