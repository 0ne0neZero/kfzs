package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * 业主/租客、往来单位、开发商
 */
public enum ChangingObjectTypeEnum {
    业主租客(0, "业主/租客"),
    往来单位(1, "往来单位"),
    开发商(2, "开发商")
    ;

    private Integer code;

    private String des;

    public static ChangingObjectTypeEnum valueOfByCode(Integer code) {
        ChangingObjectTypeEnum e = null;
        for (ChangingObjectTypeEnum ee : ChangingObjectTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ChangingObjectTypeEnum(Integer code, String des) {
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
