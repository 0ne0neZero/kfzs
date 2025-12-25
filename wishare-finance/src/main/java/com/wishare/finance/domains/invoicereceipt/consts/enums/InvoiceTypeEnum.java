package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/24
 * @Description: 开票类型：1:蓝票;2:红票
 */
public enum InvoiceTypeEnum {

    蓝票(1, "蓝票"),
    红票(2, "红票"),


    ;

    private Integer code;

    private String des;

    public static InvoiceTypeEnum valueOfByCode(Integer code) {
        InvoiceTypeEnum e = null;
        for (InvoiceTypeEnum ee : InvoiceTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    InvoiceTypeEnum(Integer code, String des) {
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
