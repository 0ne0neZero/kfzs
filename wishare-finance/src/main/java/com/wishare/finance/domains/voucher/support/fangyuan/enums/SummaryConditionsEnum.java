package com.wishare.finance.domains.voucher.support.fangyuan.enums;


public enum SummaryConditionsEnum {

    费项(1, "charge_item_id"),
    归属月(2, "account_date"),
    合同(3, "contract_id"),
    收费对象类型(4, "payer_type"),
    业务类型(5, "business_type");
    private Integer code;
    private String des;

    SummaryConditionsEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static SummaryConditionsEnum valueOfByCode(Integer code) {
        SummaryConditionsEnum e = null;
        for (SummaryConditionsEnum ee : SummaryConditionsEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
