package com.wishare.finance.infrastructure.remote.enums.financialcloud;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author longhuadmin
 * 财务云-计税方式 枚举
 */
public enum FinancialTaxTypeEnum {

    GENERAL(1, "一般计税"),
    SIMPLE(2, "简单计税"),
    NOT_SUITABLE(3, "不适用"),
    ;

    private Integer code;
    private String value;

    static BigDecimal standardTaxRate = BigDecimal.valueOf(5);
    static String specialTaxRate = "差额纳税";

    FinancialTaxTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static FinancialTaxTypeEnum getEnumByTaxRate(String val) {
        if (StringUtils.isBlank(val)){
            throw new IllegalArgumentException("税率内容异常");
        }
        if (StringUtils.equals(specialTaxRate,val.trim())){
            return FinancialTaxTypeEnum.NOT_SUITABLE;
        }
        BigDecimal value = new BigDecimal(val.replaceAll("%",""));
        if (standardTaxRate.compareTo(value) < 0){
            return FinancialTaxTypeEnum.GENERAL;
        } else {
            return FinancialTaxTypeEnum.SIMPLE;
        }
    }
}
