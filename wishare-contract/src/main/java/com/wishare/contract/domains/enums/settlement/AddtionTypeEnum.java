package com.wishare.contract.domains.enums.settlement;

/**
 * @author longhuadmin
 */
public enum AddtionTypeEnum {
    /**
     * 增值类 0空间运营 1到家服务 2零售业务 3美居业务 4资产业务 5餐饮业务 6业态运营
     **/
    SPACE_OPERATION(0, "空间运营"),
    TO_HOME_SERVICE(1, "到家服务"),
    RETAIL_BUSINESS(2, "零售业务"),
    MEJU_BUSINESS(3, "美居业务"),
    ASSETS_BUSINESS(4, "资产业务"),
    FOOD_BUSINESS(5, "餐饮业务"),
    FORMAT_OPERATION(6, "业态运营")
    ;

    private Integer code;
    private String name;

    AddtionTypeEnum(Integer code, String name) {
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
