package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:  发票来源：1.开具的发票 2.收入的发票
 */
public enum InvSourceEnum {

    开具的发票(1, "开具的发票"),
    收入的发票(2, "收入的发票"),


    ;

    private Integer code;

    private String des;

    public static InvSourceEnum valueOfByCode(Integer code) {
        InvSourceEnum e = null;
        for (InvSourceEnum ee : InvSourceEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    InvSourceEnum(Integer code, String des) {
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
