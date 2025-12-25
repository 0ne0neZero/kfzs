package com.wishare.contract.domains.enums.revision.bond;

import com.wishare.contract.domains.enums.revision.ContractTypeEnum;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/26  14:57
 */
public enum BondStatusEnum {

    待提交(0,"待提交"),
    审批中(1,"审批中"),
    已拒绝(2,"已拒绝"),
    未完成(3,"未完成"),
    /*执行中(4,"执行中"),*/
    已完成(5,"已完成"),
    ;

    private Integer code;
    private String name;

    BondStatusEnum(Integer code, String name) {
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
