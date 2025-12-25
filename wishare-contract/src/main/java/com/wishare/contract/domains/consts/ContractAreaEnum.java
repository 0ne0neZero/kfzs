package com.wishare.contract.domains.consts;

/**
 * @author xujian
 * @date 2023/2/7
 * @Description: 合同对接bpm各类型流程，枚举
 */
public enum ContractAreaEnum {

    总部("总部"),
    楼宇("楼宇"),
    应维("应维"),
    远龙("远龙"),
    不动产("不动产"),
    区域("区域"),
    ;

    private String code;

    public static ContractAreaEnum getEnum(String code) {
        ContractAreaEnum e = null;
        for (ContractAreaEnum ee : ContractAreaEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ContractAreaEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
