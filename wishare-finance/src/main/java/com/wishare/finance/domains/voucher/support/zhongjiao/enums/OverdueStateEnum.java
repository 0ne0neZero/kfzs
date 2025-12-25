package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum OverdueStateEnum {

    有违约金(1, "是"),
    无违约金(0, "否"),
 ;
    private int code;
    private String value;

    OverdueStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static OverdueStateEnum valueOfByCode(int code){
        for (OverdueStateEnum value : values()) {
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
