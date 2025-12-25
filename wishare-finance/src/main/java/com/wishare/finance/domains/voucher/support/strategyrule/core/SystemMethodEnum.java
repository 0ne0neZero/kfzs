package com.wishare.finance.domains.voucher.support.strategyrule.core;

import com.wishare.starter.exception.BizException;

/**
 * 规则运行
 *
 * @author ZMH
 * @version 1.0
 * @since 2023/12/2
 */
public enum SystemMethodEnum {

    方圆(0, "方圆"),
    中交(1, "中交"),
    临港(2, "临港");
    private int code;
    private String value;

    SystemMethodEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SystemMethodEnum valueOfByCode(int code){
        for (SystemMethodEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400("系统不支持的项目 请排查");
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
