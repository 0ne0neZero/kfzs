package com.wishare.finance.domains.voucher.support.zhongjiao.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证规则筛选条件类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum BillRuleConditionZJTypeEnum {

    费项(1, "费项"),
    归属月(2, "归属月"),
    结算时间(3, "结算时间"),
    结算方式(4, "结算方式"),
    单据类型(5, "单据类型"),
    单据来源(6, "单据来源"),
    违约金标识(7, "违约金标识"),
    收费对象类型(8, "收费对象类型"),
    ;

    private int code;
    private String value;

    BillRuleConditionZJTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static BillRuleConditionZJTypeEnum valueOfByCode(int code){
        for (BillRuleConditionZJTypeEnum value : values()) {
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
