package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 推凭模式
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherRulePushModeEnum {

    定时推送(1, "定时推送"),
    即时推送(2, "即时推送"),
    手动推凭(3, "手动推凭"),
    ;

    private int code;
    private String value;

    VoucherRulePushModeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherRulePushModeEnum valueOfByCode(int code){
        for (VoucherRulePushModeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_PUSH_MODE_NOT_SUPPORT.msg());
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
