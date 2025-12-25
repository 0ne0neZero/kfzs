package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 是否挂账
 * @Author dxclay
 * @Date 2022/9/13
 * @Version 1.0
 */
public enum BillOnAccountEnum {

    未挂账(0, "未挂账"),
    已挂账(1, "已挂账"),
    已销账(2, "已销账"),
    ;

    private int code;
    private String value;

    BillOnAccountEnum(int code, String value) {
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

    public static BillOnAccountEnum valueOfByCode(int code){
        for (BillOnAccountEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_HAND_STATE_NOT_SUPPORT.msg());
    }

}
