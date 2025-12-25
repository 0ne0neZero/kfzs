package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  14:19
 */
public enum ActionTypeEnum {

    变更(1,"变更"),
    续签(2,"续签"),
    ;

    private Integer code;
    private String name;

    ActionTypeEnum(Integer code, String name) {
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
        for (ActionTypeEnum value : ActionTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
