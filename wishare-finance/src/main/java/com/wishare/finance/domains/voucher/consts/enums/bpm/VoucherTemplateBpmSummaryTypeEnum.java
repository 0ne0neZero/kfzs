package com.wishare.finance.domains.voucher.consts.enums.bpm;

import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

public enum VoucherTemplateBpmSummaryTypeEnum {

    普通文本("Text", "普通文本"),
    部门("Department", "部门"),
    费项("ChargeItem", "费项"),
    申请人("Applicant", "申请人"),
    申请日期("ApplyTime", "申请日期"),
    实际付款日期("PayTime", "实际付款日期"),
    成本中心("CostCenter", "成本中心"),
    收款人("Payee", "收款人"),

    ;

    private String code;
    private String value;

    VoucherTemplateBpmSummaryTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateBpmSummaryTypeEnum valueOfByCode(String code){
        for (VoucherTemplateBpmSummaryTypeEnum value : values()) {
            if (value.equalsByCode(code)){
                return value;
            }
        }
        throw BizException.throw400(ErrorMessage.VOUCHER_TEMPLATE_SUMMARY_TYPE_NOT_SUPPORT.msg());
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
