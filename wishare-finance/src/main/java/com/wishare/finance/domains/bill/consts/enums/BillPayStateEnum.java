package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 支付状态
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillPayStateEnum {

    未支付(0, "未支付"),
    支付中(1, "支付中"),
    支付成功(2, "支付成功"),
    支付取消(3, "支付取消"),
    支付失败(4, "支付失败"),
    支付超时(5, "支付超时"),
    ;

    private int code;
    private String value;

    BillPayStateEnum(int code, String value) {
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

    public static BillPayStateEnum valueOfByCode(int code){
        for (BillPayStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_PAY_STATE_NOT_SUPPORT.msg());
    }

}
