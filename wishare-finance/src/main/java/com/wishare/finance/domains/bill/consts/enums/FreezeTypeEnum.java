package com.wishare.finance.domains.bill.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

import java.util.List;
import java.util.Set;

/**
 * 冻结类型（0，1）
 * @Author zyj
 * @Date 2023/08/08
 * @Version 1.0
 */
public enum FreezeTypeEnum {

    无类型(0, "无类型"),
    通联银行代扣(1, "通联银行代扣"),

    托收报盘(2, "托收报盘"),

    产权人变更(3, "产权人变更"),

    BPM减免流程(4, "BPM减免流程"),

    BPM跳收流程(5, "BPM跳收流程"),
    BPM开发代付流程(6, "BPM开发代付流程"),
    收款单退款流程(7, "收款单退款流程"),
    收款单冲销流程(8, "收款单冲销流程"),
    ;

    private int code;
    private String value;

    FreezeTypeEnum(int code, String value) {
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

    public static FreezeTypeEnum valueOfByCode(int code){
        for (FreezeTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BILL_FREEZE_TYPE_NOT_SUPPORT.msg());
    }

    public final static List<Integer> LOCK_TYPE =
            List.of(BPM减免流程.getCode(), BPM跳收流程.getCode(), BPM开发代付流程.getCode(), 收款单退款流程.getCode(),收款单冲销流程.getCode());

}
