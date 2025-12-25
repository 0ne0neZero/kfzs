package com.wishare.finance.domains.voucher.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author longhuadmin
 */
public enum TaxTypeEnum {

    /**
     * 计税方式
     **/
    GENERAL("1", "一般计税"),
    SIMPLE("2", "建议计税");

    private String code;
    private String name;

    TaxTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(String code){
        if (StringUtils.isBlank(code)){
            return null;
        }
        for (TaxTypeEnum e : TaxTypeEnum.values()) {
            if (e.getCode().equals(code)){
                return e.getName();
            }
        }
        return null;
    }
}
