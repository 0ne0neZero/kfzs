package com.wishare.finance.domains.bill.consts.enums;

/**
 * @author xujian
 * @date 2022/9/26
 * @Description: 开票状态：0 未开票，1 开票中，2 部分开票，3 已开票
 */
public enum InvoiceStateEnum {

    未开票(0, "未开票"),
    开票中(1, "开票中"),
    部分开票(2, "部分开票"),
    已开票(3, "已开票"),

    ;

    private Integer code;

    private String des;

    public static InvoiceStateEnum valueOfByCode(Integer code) {
        InvoiceStateEnum e = null;
        for (InvoiceStateEnum ee : InvoiceStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    InvoiceStateEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
