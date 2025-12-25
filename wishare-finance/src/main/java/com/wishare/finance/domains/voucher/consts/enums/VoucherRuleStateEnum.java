package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 执行状态
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherRuleStateEnum {

    待处理(0, "待处理"),
    处理中(1, "处理中"),
    处理完成(2, "处理完成"),
    处理失败(3, "处理失败")
    ;

    private int code;
    private String value;

    VoucherRuleStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherRuleStateEnum valueOfByCode(int code){
        for (VoucherRuleStateEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_STATE_NOT_SUPPORT.msg());
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
