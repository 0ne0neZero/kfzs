package com.wishare.finance.domains.voucher.consts.enums;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;
import lombok.Getter;

/**
 * 关账记录关账状态
 */
@Getter
public enum AccountCloseEnum {

    未关账(0, "未关账"),
    已关账(1, "已关账"),
    ;
    private final int code;
    private final String value;

    AccountCloseEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static AccountCloseEnum valueOfByCode(int code){
        for (AccountCloseEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.ACCOUNT_CLOSE_STATE_NOT_SUPPORT.msg());
    }

    public boolean equalsByCode(int code){
        return code == this.code;
    }

}
