package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 交易状态
 * @author dxclay
 * @since  2023/3/6
 * @version 1.0
 */
public enum BillTransactStateEnum {

    待交易(0, "待交易"),
    交易中(1, "交易中"),
    交易成功(2, "交易成功"),
    交易失败(3, "交易失败"),
    交易已取消(4, "交易已取消"),
    交易已关闭(5, "交易已关闭")
    ;

    private int code;
    private String value;

    BillTransactStateEnum(int code, String value) {
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

    public static BillTransactStateEnum valueOfByCode(int code){
        for (BillTransactStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.PAYMENT_STATE_NOT_SUPPORT.msg());
    }

}
