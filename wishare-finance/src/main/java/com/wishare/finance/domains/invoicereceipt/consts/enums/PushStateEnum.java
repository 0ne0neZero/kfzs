package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description: 推送状态 0和空 未推送，1 已推送
 */
public enum PushStateEnum {

    未推送(0, "未推送"),
    已推送(1, "已推送"),
    推送失败(2, "推送失败"),

    ;

    private Integer code;

    private String des;

    public static PushStateEnum valueOfByCode(Integer code) {
        PushStateEnum e = null;
        for (PushStateEnum ee : PushStateEnum.values()) {
            if (ee.getCode() == code) {
                e = ee;
                break;
            }
        }
        return e;
    }

    PushStateEnum(Integer code, String des) {
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
