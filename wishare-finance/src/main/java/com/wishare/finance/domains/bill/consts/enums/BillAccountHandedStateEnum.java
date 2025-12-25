package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 交账
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillAccountHandedStateEnum {

    未交账(0, "未交账"),
    部分交账(1, "部分交账"),
    已交账(2, "已交账"),
    ;

    private int code;
    private String value;

    BillAccountHandedStateEnum(int code, String value) {
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

    public static BillAccountHandedStateEnum valueOfByCode(int code){
        for (BillAccountHandedStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_ON_ACCOUNT_STATE_NOT_SUPPORT.msg());
    }

}
