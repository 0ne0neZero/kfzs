package com.wishare.finance.domains.configure.chargeitem.consts.enums;

/**
 * @author xujian
 * @date 2022/12/20
 * @Description: 费项属性： 1 收入,2 支出 3 代收代付及其他
 */
public enum ChargeItemAttributeEnum {

    收入(1, "收入"),
    支出(2, "支出"),
    代收代付及其他(3, "代收代付及其他");

    ;

    private Integer code;
    private String des;

    ChargeItemAttributeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static ChargeItemAttributeEnum valueOfByCode(Integer code) {
        ChargeItemAttributeEnum e = null;
        for (ChargeItemAttributeEnum ee : ChargeItemAttributeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }
}
