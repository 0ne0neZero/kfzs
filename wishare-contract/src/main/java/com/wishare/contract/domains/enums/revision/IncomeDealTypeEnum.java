package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  16:26
 */
public enum IncomeDealTypeEnum {

    关联交易("1","关联交易"),
    非关联交易("2","非关联交易"),
    ;

    private String code;
    private String name;

    IncomeDealTypeEnum(String code, String name) {
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
        for (IncomeDealTypeEnum value : IncomeDealTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
