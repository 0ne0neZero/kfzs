package com.wishare.contract.domains.consts;

/**
 * @author xujian
 * @date 2023/2/7
 * @Description: 合同对接bpm各业务类型流程，枚举
 */
public enum ContractBusTypeEnum {

    基础物业("基础物业"),
    商管服务("商管服务"),
    工程服务("工程服务"),
    增值业务("增值业务"),
    除增值外("除增值外"),
    不适用("不适用"),
    ;

    private String code;

    public static ContractBusTypeEnum getEnum(String code) {
        ContractBusTypeEnum e = null;
        for (ContractBusTypeEnum ee : ContractBusTypeEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ContractBusTypeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
