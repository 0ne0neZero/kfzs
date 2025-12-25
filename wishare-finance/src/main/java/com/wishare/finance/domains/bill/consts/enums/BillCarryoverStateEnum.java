package com.wishare.finance.domains.bill.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 结转状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BillCarryoverStateEnum {

    未结转(0, "未结转"),
    待结转(1, "待结转"),
    部分结转(2, "部分结转"),
    已结转(3, "已结转"),
    ;

    private int code;
    private String value;

    BillCarryoverStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static BillCarryoverStateEnum valueOfByCode(int code){
        for (BillCarryoverStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_SETTLE_STATE_NOT_SUPPORT.msg());
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
