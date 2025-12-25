package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 退款状态
 * @Author dxclay
 * @Date 2022/8/24
 * @Version 1.0
 */
public enum BillRefundStateEnum {

    未退款(0, "未退款"),
    退款中(1, "退款中"),
    部分退款(2, "部分退款"),
    已退款(3, "已退款"),
    ;

    private int code;
    private String value;

    BillRefundStateEnum(int code, String value) {
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

    public static BillRefundStateEnum valueOfByCode(int code){
        for (BillRefundStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_APPROVE_RECORD_NOT_EXIST.msg());
    }

}
