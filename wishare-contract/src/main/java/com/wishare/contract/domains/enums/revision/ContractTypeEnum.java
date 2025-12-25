package com.wishare.contract.domains.enums.revision;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chenglong
 * @since： 2023/6/25  14:45
 */
public enum ContractTypeEnum {

    普通合同(0,"普通合同"),
    框架合同(1,"框架合同"),
    补充协议(2,"补充协议"),
    结算合同(3,"结算合同"),
    修改合同(4,"修改合同")
    ;

    private Integer code;
    private String name;

    ContractTypeEnum(Integer code, String name) {
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
        for (ContractTypeEnum value : ContractTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

    public static Integer parseCode(String name) {
        for (ContractTypeEnum value : ContractTypeEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return null;
    }

}
