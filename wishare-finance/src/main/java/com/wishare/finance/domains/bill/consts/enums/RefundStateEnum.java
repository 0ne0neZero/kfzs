package com.wishare.finance.domains.bill.consts.enums;

/**
 * @author xujian
 * @date 2022/9/9
 * @Description: 退款状态：0 待退款，1 退款中，2 已退款，3 未生效
 */
public enum RefundStateEnum {
    待退款(0,"待退款"),
    退款中(1,"退款中"),
    已退款(2,"已退款"),
    未生效(3,"未生效"),

    ;

    private Integer code;

    private String des;

    RefundStateEnum(Integer code, String des) {
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
