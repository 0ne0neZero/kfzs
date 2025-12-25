package com.wishare.finance.domains.reconciliation.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 对账模式
 * @author yancao
 */
public enum ReconcileModeEnum {

    账票流水对账(0, "账票流水对账"),
    商户清分对账(1, "商户清分对账"),
    ;

    private int code;
    private String value;

    ReconcileModeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static ReconcileModeEnum valueOfByCode(int code){
        for (ReconcileModeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw402(ErrorMessage.RECONCILE_MODE_NOT_SUPPORT.getErrMsg());
    }
    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
