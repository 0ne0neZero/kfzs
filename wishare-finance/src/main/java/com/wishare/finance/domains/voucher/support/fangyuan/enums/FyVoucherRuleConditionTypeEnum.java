package com.wishare.finance.domains.voucher.support.fangyuan.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 方圆凭证规则筛选条件类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum FyVoucherRuleConditionTypeEnum {

    费项(1, "费项"),
    费用类型(2, "费用类型"),
    减免形式(3, "减免形式"),
    结算方式(4, "结算方式"),
    账单类型(5, "账单类型"),
    单据来源(6, "单据来源"),
    付款方类型(7, "付款方类型"),
    违约金标识(8, "违约金标识"),
    归属月(9, "归属月"),
    结算时间(10, "结算时间"),
    收款时间(11, "收款时间"),
    ;

    private int code;
    private String value;

    FyVoucherRuleConditionTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FyVoucherRuleConditionTypeEnum valueOfByCode(int code){
        for (FyVoucherRuleConditionTypeEnum value : values()) {
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
