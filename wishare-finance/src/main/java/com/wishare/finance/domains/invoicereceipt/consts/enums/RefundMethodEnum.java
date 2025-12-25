package com.wishare.finance.domains.invoicereceipt.consts.enums;

/**
 * @author yyx
 * @date 2023/8/25 11:24
 * 退款方式枚举
 */
public enum RefundMethodEnum {

    汇款("1", "汇款"),
    支票("2", "支票"),
    其他("3", "其他"),
    现金("4", "现金"),
    线下支付宝("5", "线下-支付宝"),
    线下微信("6", "线下-微信"),
    /*
        只有在线支付（微信，支付宝，郑州银行）时才可以选择
     */
    原路退款("7","原路退款")
    ;

    private String code;

    private String des;

    RefundMethodEnum(String code, String des) {
        this.code = code;
        this.des = des;
    }



    public static RefundMethodEnum getBillMethodEnumByCode(String code) {
        for (RefundMethodEnum objectEnum : RefundMethodEnum.values()) {
            if (code.equals(objectEnum.getCode())) {
                return objectEnum;
            }
        }
        return null;
    }
    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
