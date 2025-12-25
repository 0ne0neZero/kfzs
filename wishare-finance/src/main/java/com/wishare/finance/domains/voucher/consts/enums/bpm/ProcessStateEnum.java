package com.wishare.finance.domains.voucher.consts.enums.bpm;

import org.apache.commons.lang3.StringUtils;

public enum ProcessStateEnum {

    成功("0", "成功"),
    失败("1", "失败"),
    ;

    private String code;
    private String value;

    ProcessStateEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return StringUtils.equals(code, this.code);
    }

}
