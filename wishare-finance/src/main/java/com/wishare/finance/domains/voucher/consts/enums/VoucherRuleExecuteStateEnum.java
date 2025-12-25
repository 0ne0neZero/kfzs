package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证运行状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherRuleExecuteStateEnum {

    空闲(0, "空闲"),
    运行中(1, "运行中"),

    ;

    private int code;
    private String value;

    VoucherRuleExecuteStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherRuleExecuteStateEnum valueOfByCode(int code){
        for (VoucherRuleExecuteStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_EXECUTE_STATE_NOT_SUPPORT.msg());
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
