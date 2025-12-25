package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证规则筛选条件类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherRuleConditionMethodEnum {

    包含(1, "包含"),
    不包含(2, "不包含"),
    等于(3, "小于等于"),
    小于等于(4, "小于等于"),
    大于等于(5, "大于等于"),
    小于(6, "小于"),
    大于(7, "大于"),

    ;

    private int code;
    private String value;

    VoucherRuleConditionMethodEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherRuleConditionMethodEnum valueOfByCode(int code){
        for (VoucherRuleConditionMethodEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_CONDITION_METHOD_NOT_SUPPORT.msg());
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
