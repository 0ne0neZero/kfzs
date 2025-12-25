package com.wishare.contract.domains.enums.revision;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/11  10:04
 */
public enum RevTypeEnum {

    支出合同(1,"支出合同"),
    收入合同(2,"收入合同"),
    ;

    @JsonValue
    @EnumValue
    private Integer code;
    private String name;

    RevTypeEnum(Integer code, String name) {
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
