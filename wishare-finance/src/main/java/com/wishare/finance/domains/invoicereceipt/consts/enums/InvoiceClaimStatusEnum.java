package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/9/21
 * @Description: 认领状态：0 未认领，1 已认领
 */
public enum InvoiceClaimStatusEnum {

    未认领(0, "未认领"),
    已认领(1, "已认领"),


            ;

    private Integer code;

    private String des;

    public static InvoiceClaimStatusEnum valueOfByCode(Integer code) {
        InvoiceClaimStatusEnum e = null;
        for (InvoiceClaimStatusEnum ee : InvoiceClaimStatusEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    InvoiceClaimStatusEnum(Integer code, String des) {
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
