package com.wishare.finance.infrastructure.remote.model;

/**
 * 支付状态
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum PaymentState {

    待支付(0, "待支付"),
    支付中(1, "支付中"),
    支付成功(2, "支付成功"),
    支付失败(3, "支付失败"),
    已撤销(4, "已撤销"),
    退款中(5, "退款中"),
    部分退款(6, "部分退款"),
    已退款(7, "已退款"),
    已关闭(8, "已关闭"),
    ;

    private int code;
    private String value;

    PaymentState(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

    public static PaymentState valueOfByCode(int code){
        for (PaymentState value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        return null;
    }

}
