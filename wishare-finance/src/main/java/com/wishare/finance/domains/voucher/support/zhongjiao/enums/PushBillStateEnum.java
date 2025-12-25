package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum PushBillStateEnum {

    待推送(1, "待推送"),
    已推送(2, "已推送"),

    推送失败(3, "推送失败"),
    已驳回(4, "已驳回"),
    推送中(5, "推送中"),
 ;
    private int code;
    private String value;

    PushBillStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static PushBillStateEnum valueOfByCode(int code){
        for (PushBillStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_BUSINESS_BILL_TYPE_NOT_SUPPORT.msg());
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
