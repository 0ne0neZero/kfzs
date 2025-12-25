package com.wishare.finance.domains.configure.businessunit.enums;

/**
 */
public enum BusinessDetailTypeEnum {
    //关联类型：1法定单位，2成本中心，3行政组织

    法定单位(1, "法定单位"),
    成本中心(2, "成本中心"),
    行政组织(3, "行政组织"),
    银行账号(4, "银行账号"),

    ;

    private Integer code;

    private String des;

    public static BusinessDetailTypeEnum valueOfByCode(Integer code) {
        BusinessDetailTypeEnum e = null;
        for (BusinessDetailTypeEnum ee : BusinessDetailTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    BusinessDetailTypeEnum(Integer code, String des) {
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
