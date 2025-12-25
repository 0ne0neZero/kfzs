package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum ApplyStatusEnum {

    未支付(1, "未支付"),
    支付中(2, "支付中"),
    已支付(3, "已支付");
    private int code;
    private String value;

    ApplyStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ApplyStatusEnum valueOfByCode(int code){
        for (ApplyStatusEnum value : values()) {
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
