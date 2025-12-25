package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description: 推送方式：-1 不推送,0 邮箱;1 手机 2 站内信
 */
public enum PushModeEnum {

    不推送(-1, "不推送"),
    邮箱(0, "邮箱"),
    手机(1, "手机"),
    站内信(2, "站内信"),


    ;

    private Integer code;

    private String des;

    public static PushModeEnum valueOfByCode(Integer code) {
        PushModeEnum e = null;
        for (PushModeEnum ee : PushModeEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    PushModeEnum(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public boolean equalsByCode(int code){
        return this.code == code;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
