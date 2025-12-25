package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 对账状态
 * @Author dxclay
 * @Date 2022/8/24
 * @Version 1.0
 */
@Deprecated
public enum BillReconcileStateEnum {

    未对账(0, "未对账"),
    未对平(1, "未对平"),
    部分对平(2, "部分对平"),
    已对平(3, "已对平"),
    ;

    private int code;
    private String value;

    BillReconcileStateEnum(int code, String value) {
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

    public static BillReconcileStateEnum valueOfByCode(int code){
        for (BillReconcileStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_RECONCILE_STATE_NOT_SUPPORT.msg());
    }

}
