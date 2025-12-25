package com.wishare.finance.domains.voucher.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 报账审批流程申请结果枚举
 */
public enum VoucherApproveStateEnum {

    审批中(0, "审批中"),
    无需审批(1, "无需审批"),
    ;
    private int code;
    private String value;

    VoucherApproveStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherApproveStateEnum valueOfByCode(int code){
        for (VoucherApproveStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_APPROVE_STATE_NOT_SUPPORT.msg());
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

}
