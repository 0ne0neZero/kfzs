package com.wishare.finance.domains.invoicereceipt.consts.enums;

public enum TaxpayerTypeEnum {

    小规模纳税人(1, "小规模纳税人"),
    一般纳税人(2, "一般纳税人"),
    简易征收纳税人(3, "简易征收纳税人"),
    政府机关(4, "政府机关");



    private Integer code;

    private String des;

    public static TaxpayerTypeEnum valueOfByCode(Integer code) {
        TaxpayerTypeEnum e = null;
        for (TaxpayerTypeEnum ee : TaxpayerTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    TaxpayerTypeEnum(Integer code, String des) {
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
