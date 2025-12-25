package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  16:29
 */
public enum SealTypeEnum {

    合同专用章(1,"合同专用章"),
    公司公章(2,"公司公章"),
    ;

    private Integer code;
    private String name;

    SealTypeEnum(Integer code, String name) {
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
        for (SealTypeEnum value : SealTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
