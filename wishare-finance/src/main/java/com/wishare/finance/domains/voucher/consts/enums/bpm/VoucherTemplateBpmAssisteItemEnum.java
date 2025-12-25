package com.wishare.finance.domains.voucher.consts.enums.bpm;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum VoucherTemplateBpmAssisteItemEnum {

    普通文本("Text", "普通文本"),
    收款账号("PayeeAccount", "收款账号"),
    付款账号("PayerAccount", "付款账号"),
    收款人("Payee", "收款人"),
    付款方("Payer", "付款方"),
    成本中心("CostCenter", "成本中心"),
    申请人("Applicant", "申请人"),
    增值税税率("TaxRate", "增值税税率"),
    部门("Org", "部门"),
    业务类型("BizType", "业务类型"),

    ;

    private String code;
    private String value;

    VoucherTemplateBpmAssisteItemEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateBpmAssisteItemEnum valueOfByCode(String code){
        for (VoucherTemplateBpmAssisteItemEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.BPM_VOUCHER_ASSISTE_ITEM_NOT_SUPPORT.msg());
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
