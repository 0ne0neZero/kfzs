package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证规则筛选条件类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherRuleConditionTypeEnum {

    费项(1, "费项"),
    结算方式(2, "结算方式"),
    票据类型(3, "票据类型"),
    单据来源(4, "单据来源"),
    税率(5, "税率"),
    客商(6, "客商"),
    银行流水(7, "银行流水"),
    收款银行账户(8, "收款银行账户"),
    付款银行账户(9, "付款银行账户"),
    支付渠道(10, "支付渠道"),
    计费周期(11, "计费周期"),
    结算时间(12, "结算时间"),
    归属月(13, "归属月"),
    业务场景(14, "业务场景"),
    调整方式(15, "调整方式"),
    成本中心(16, "成本中心"),
    应收类型(17, "应收类型"),
    法定单位(18, "法定单位"),
    费项属性(19, "费项属性"),
    减免形式(20, "减免类型"),
    业务类型(21, "业务类型"),
    调整类型(22, "调整类型"),
    业务单元(23, "业务单元"),
    结算状态(24, "结算状态"),
    结转时间(25, "结转时间"),
    ;

    private int code;
    private String value;

    VoucherRuleConditionTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherRuleConditionTypeEnum valueOfByCode(int code){
        for (VoucherRuleConditionTypeEnum value : values()) {
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
