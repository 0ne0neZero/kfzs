package com.wishare.finance.domains.voucher.consts.enums.bpm;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum VoucherTemplateBpmLogicCodeEnum {

    原币金额("originalAmount", "原币金额"),
    原币金额_不含税("excTaxAmount", "原币金额（不含税）"),
    原币税额("taxAmount", "原币税额"),
    税率("taxRate", "税率"),
    收款金额("payeeAmount", "收款金额"),

    // 扣缴养老("withholdingPension","扣缴养老"),
    // 扣缴失业("unemployment","扣缴失业"),
    // 扣缴医疗("medical","扣缴医疗"),
    // 扣缴住房("lodging","扣缴住房"),
    // 扣缴其它("withholdOther","扣缴其它"),
    // 代扣个税("personalIncomeTax","代扣个税"),
    // 个税补退("incomeTaxRefund","个税补退"),
    // 扣缴合计("withholdingTotal","扣缴合计"),
    // 代扣工会会费("unionDues","代扣工会会费"),
    //
    //
    // 实发工资("takeHomePay","实发工资"),
    // 应发工资("wagesPayable","应发工资"),
    // 激励合计("excitationTotal","激励合计"),
    //
    // 公司承担养老保险("corporationProvide","公司承担养老保险"),
    // 公司承担失业保险("corporationUnemployment","公司承担失业保险"),
    // 公司承担医疗保险("corporationMedical","公司承担医疗保险"),
    // 公司承担生育保险("corporationBirth","公司承担生育保险"),
    // 公司承担工伤保险("corporationInjury","公司承担工伤保险"),
    // 公司承担补充医疗保险("corporationComplementaryMedical","公司承担补充医疗保险"),


    ;

    private String code;
    private String value;

    VoucherTemplateBpmLogicCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateBpmLogicCodeEnum valueOfByCode(String code){
        for (VoucherTemplateBpmLogicCodeEnum value : values()) {
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
