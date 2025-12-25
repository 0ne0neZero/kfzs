package com.wishare.contract.domains.enums.revision.bond;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/27  11:47
 */
public enum BondDealWayEnum {

    现金("cash","现金", ""),
    ;

    private String code;
    private String name;
    private String financeCode;

    BondDealWayEnum(String code, String name, String financeCode) {
        this.name = name;
        this.code = code;
        this.financeCode = financeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public void setFinanceCode(String financeCode) {
        this.financeCode = financeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(String code) {
        for (BondTypeEnum value : BondTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
