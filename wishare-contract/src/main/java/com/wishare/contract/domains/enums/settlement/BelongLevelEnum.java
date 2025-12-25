package com.wishare.contract.domains.enums.settlement;

/**
 * @author longhuadmin
 */
public enum BelongLevelEnum {
    /**
     * 所属层级 0项目 1区域公司
     **/
    PROJECT_LEVEL(0, "项目"),
    REGIONAL_COMPANY_LEVEL(1, "区域公司");

    private Integer code;
    private String name;

    BelongLevelEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
