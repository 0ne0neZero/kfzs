package com.wishare.finance.infrastructure.remote.enums;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description: 财务组织 组织类型枚举 1 法定单位 2 成本中心
 */
public enum OrgFinanceTypeEnum {

    法定单位(1, "法定单位"),
    成本中心(2, "成本中心"),

    ;

    private Integer code;

    private String des;

    public static OrgFinanceTypeEnum valueOfByCode(Integer code) {
        OrgFinanceTypeEnum e = null;
        for (OrgFinanceTypeEnum ee : OrgFinanceTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    OrgFinanceTypeEnum(Integer code, String des) {
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
