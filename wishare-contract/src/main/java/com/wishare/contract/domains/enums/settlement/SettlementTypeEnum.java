package com.wishare.contract.domains.enums.settlement;

import java.util.Arrays;

/**
 * @author longhuadmin
 */
public enum SettlementTypeEnum {
    /**
     * 结算单-结算方式枚举
     * 0中期结算 1最终结算
     **/
    MIDDLE(0, "中期结算"),
    FINAL(1,"最终结算");

    private Integer code;
    private String name;

    SettlementTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SettlementTypeEnum getByCode(int code) {
        return Arrays.stream(values())
                .filter(type -> type.getCode() == code)
                .findFirst()
                .orElse(null);
    }
}
