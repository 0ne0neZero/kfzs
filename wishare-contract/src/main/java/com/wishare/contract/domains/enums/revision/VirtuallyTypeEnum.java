package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:50
 */
public enum VirtuallyTypeEnum {

    非虚拟合同(0,"非虚拟合同"),
    虚拟合同(1,"虚拟合同"),
            ;

    private Integer code;
    private String name;

    VirtuallyTypeEnum(Integer code, String name) {
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
        for (VirtuallyTypeEnum value : VirtuallyTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (VirtuallyTypeEnum value : VirtuallyTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
