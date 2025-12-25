package com.wishare.contract.domains.consts;

/**
 * @author xujian
 * @date 2023/2/7
 * @Description: 合同性质 1 收入 2 支出 3 其他
 */
public enum ContractNatureEnum {

    收入(1, "收入"),
    支出(2, "支出"),
    其他(3, "其他"),
    ;

    private Integer code;

    private String des;

    public static ContractNatureEnum getEnum(Integer code) {
        ContractNatureEnum e = null;
        for (ContractNatureEnum ee : ContractNatureEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    ContractNatureEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
