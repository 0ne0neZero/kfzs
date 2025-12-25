package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum PayStatusEnum {

    收入确认(1, "未支付"),
    资金收款(2, "已支付");
    private int code;
    private String value;

    PayStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static PayStatusEnum valueOfByCode(int code){
        for (PayStatusEnum value : values()) {
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
