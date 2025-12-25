package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 推凭状态
 * @author dxclay
 * @since  2023/3/6
 * @version 1.0
 */
public enum BillTransactVoucherStateEnum {

    未推凭(0, "收款"),
    推凭中(1, "推凭中"),
    已推凭(2, "已推凭"),
    推凭失败(3, "推凭失败"),
    ;

    private int code;
    private String value;

    BillTransactVoucherStateEnum(int code, String value) {
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

    public static BillTransactVoucherStateEnum valueOfByCode(int code){
        for (BillTransactVoucherStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.PAYMENT_TYPE_NOT_SUPPORT.msg());
    }

}
