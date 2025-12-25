package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:52
 */
public enum SigningMethodEnum {

    新签(0,"新签"),
    补充(1,"补充"),
    续签(2,"续签"),
    变更(3,"变更"),
            ;

    private Integer code;
    private String name;

    SigningMethodEnum(Integer code, String name) {
        this.name = name;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String parseName(Integer code) {
        for (SigningMethodEnum value : SigningMethodEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
