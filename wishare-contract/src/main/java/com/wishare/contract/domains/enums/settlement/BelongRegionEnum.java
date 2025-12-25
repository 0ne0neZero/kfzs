package com.wishare.contract.domains.enums.settlement;

/**
 * @author longhuadmin
 */
public enum BelongRegionEnum {
    /**
     * 结算单-合同-所属区域枚举
     * 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域
     **/
    PROJECT(0,"总部"),
    NORTH(1,"华北区域"),
    SOUTH(2,"华南区域"),
    EAST(3,"华东区域"),
    WEST(4,"西部区域"),
    MIDDLE(5,"华中区域");

    private Integer code;
    private String name;

    BelongRegionEnum(Integer code, String name) {
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
