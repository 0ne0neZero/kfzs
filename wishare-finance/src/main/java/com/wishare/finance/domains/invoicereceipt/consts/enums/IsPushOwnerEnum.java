package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author dp
 * @date 2024/1/12
 * @Description:  是否推送业主手机：0,不推送 1,推送
 */
public enum IsPushOwnerEnum {

    不推送(0, "不推送"),
    推送(1, "推送"),


    ;

    private Integer code;

    private String des;

    public static IsPushOwnerEnum valueOfByCode(Integer code) {
        IsPushOwnerEnum e = null;
        for (IsPushOwnerEnum ee : IsPushOwnerEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    IsPushOwnerEnum(Integer code, String des) {
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
