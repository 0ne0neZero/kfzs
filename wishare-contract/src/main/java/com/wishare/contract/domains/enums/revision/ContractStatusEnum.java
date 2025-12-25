package com.wishare.contract.domains.enums.revision;

import java.util.Objects;

/**
 * @author longhuadmin
 */

public enum ContractStatusEnum {

    /**
     * 合同数据库status原生枚举
     **/
     WAIT_SUBMIT(0, "待提交"),
     APPROVE_ING(1, "审批中"),
     REFUSE(2, "已拒绝"),
     NOT_START(3, "未开始"),
     EXECUTING(4, "执行中"),
     EXPIRED(5, "已到期"),
     TERMINATED(6, "已终止");

    private Integer code;
    private String name;

    ContractStatusEnum(Integer code, String name) {
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
        if (Objects.isNull(code)) {
            return null;
        }
        for (ContractStatusEnum value : ContractStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }

}
