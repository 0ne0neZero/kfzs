package com.wishare.finance.domains.configure.subject.consts.enums;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description: 映射单元类型（1 费项 2 辅助核算）
 */
public enum SubMapTypeEnum {

    费项(1, "费项"),
    辅助核算(2, "辅助核算"),

    ;

    private Integer code;
    private String des;

    SubMapTypeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static SubMapTypeEnum valueOfByCode(Integer code) {
        SubMapTypeEnum e = null;
        for (SubMapTypeEnum ee : SubMapTypeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
