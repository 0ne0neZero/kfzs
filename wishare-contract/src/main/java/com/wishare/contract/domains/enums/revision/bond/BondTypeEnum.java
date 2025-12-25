package com.wishare.contract.domains.enums.revision.bond;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/27  16:58
 */
public enum BondTypeEnum {

    履约保证金("101","履约保证金"),
    投标保证金("102","投标保证金"),
    质量保证金("103","质量保证金"),
    其他保证金("104","其他保证金"),
    ;

    private String code;
    private String name;

    BondTypeEnum(String code, String name) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
