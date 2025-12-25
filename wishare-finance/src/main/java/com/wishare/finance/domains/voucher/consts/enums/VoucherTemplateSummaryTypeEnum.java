package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证模板分录摘要类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherTemplateSummaryTypeEnum {

    普通文本("Text", "普通文本"),
    归属月("AccountMonth", "归属月"),
    操作日期("GmtModify", "操作日期"),
    成本中心("CostCenter", "成本中心"),
    法定单位("StatutoryBody", "法定单位"),
    费项("ChargeItem", "费项"),
    结算方式("PayWay", "结算方式"),
    结算日期("SettleTime", "结算日期"),

    ;

    private String code;
    private String value;

    VoucherTemplateSummaryTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateSummaryTypeEnum valueOfByCode(String code){
        for (VoucherTemplateSummaryTypeEnum value : values()) {
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
