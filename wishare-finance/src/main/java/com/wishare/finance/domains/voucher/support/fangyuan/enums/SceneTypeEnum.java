package com.wishare.finance.domains.voucher.support.fangyuan.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum SceneTypeEnum {

    冲销(1, "冲销"),
    作废(2, "作废");
    private final int code;
    private final String value;

    SceneTypeEnum(int code, String value) {
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

}
