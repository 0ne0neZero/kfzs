package com.wishare.contract.domains.consts;

/**
 * @author xujian
 * @date 2023/2/7
 * @Description: 合同对接bpm各区域流程，枚举
 */
public enum ContractAreaSonEnum {

    华中华西("华中华西"),
    北京( "北京"),
    华南( "华南"),
    华东( "华东"),
    环渤海( "环渤海"),
    ;

    private String code;

    public static ContractAreaSonEnum getEnum(String code) {
        ContractAreaSonEnum e = null;
        for (ContractAreaSonEnum ee : ContractAreaSonEnum.values()) {
            if (ee.getCode().equals(code)) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ContractAreaSonEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
