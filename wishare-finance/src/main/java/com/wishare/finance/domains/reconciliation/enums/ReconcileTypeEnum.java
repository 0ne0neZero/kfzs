package com.wishare.finance.domains.reconciliation.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 对账类型
 * @author yancao
 */
public enum ReconcileTypeEnum {

    自动对账(0, "自动对账"),
    手动对账(1, "手动对账"),
    ;

    private int code;
    private String value;

    ReconcileTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ReconcileTypeEnum valueOfByCode(int code){
        for (ReconcileTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw402(ErrorMessage.RECONCILE_TYPE_NOT_SUPPORT.getErrMsg());
    }
    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
