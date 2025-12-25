package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证规则筛选条件业务场景
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherRuleBusinessScenarioEnum {

    空置房收款确收(1, "空置房收款确收"),
    临时性收款(2, "临时性收款"),
    ;

    private int code;
    private String value;

    VoucherRuleBusinessScenarioEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherRuleBusinessScenarioEnum valueOfByCode(int code){
        for (VoucherRuleBusinessScenarioEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CONDITION_TYPE_NOT_SUPPORT.msg());
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
