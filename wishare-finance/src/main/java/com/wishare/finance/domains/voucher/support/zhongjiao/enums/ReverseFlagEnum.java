package com.wishare.finance.domains.voucher.support.zhongjiao.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 *收入确认单 逆向标识字段枚举
 */
public enum ReverseFlagEnum {

    默认(0, "默认"),
    冲销(1, "冲销"),
    作废(2, "作废"),
    调整(3, "调整"),
    减免(4, "减免"),
    ;
    private int code;
    private String value;

    ReverseFlagEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ReverseFlagEnum valueOfByCode(int code){
        for (ReverseFlagEnum value : values()) {
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
