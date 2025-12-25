package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证来源
 * @Author dp
 * @Date 2023/12/8
 * @Version 1.0
 */
public enum VoucherSourceEnum {

    系统生成(0, "系统生成"),
    BPM推送(1, "BPM推送"),


    ;

    private int code;
    private String value;

    VoucherSourceEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherSourceEnum valueOfByCode(int code){
        for (VoucherSourceEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_MAKE_TYPE_NOT_SUPPORT.msg());
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
