package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证规则凭证逻辑类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherTemplateLogicCodeEnum {

    手续费("fee", "手续费"),
    账单金额("amount", "账单金额"),
    增值税税率("vatRate", "增值税税率"),
    实收付金额("actualPay", "实收/付金额"),
    实收减免金额("actualDiscount", "实收减免金额"),
    应收付金额("receivablePayable", "应收/付金额"),
    应收减免金额("receivableDiscount", "应收减免金额"),
    实收付金额不含手续费("actualAmountWithoutFee", "实收付金额（不含手续费）"),
    退款金额("refundAmount", "退款金额"),
    差额("difference", "差额"),
    账单调整金额("adjustAmount", "账单调整金额"),
    款项结转金额("carryoverAmount","结转金额"),
    ;

    private String code;
    private String value;

    VoucherTemplateLogicCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateLogicCodeEnum valueOfByCode(String code){
        for (VoucherTemplateLogicCodeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_RULE_LOGIC_TYPE_NOT_SUPPORT.msg());
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public boolean equalsByCode(String code){
        return this.code.equals(code);
    }

}
