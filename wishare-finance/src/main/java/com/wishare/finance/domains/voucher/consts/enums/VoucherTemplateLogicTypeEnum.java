package com.wishare.finance.domains.voucher.consts.enums;


import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.exception.BizException;

/**
 * 凭证规则凭证逻辑类型
 * @Author dxclay
 * @Date 2022/8/23
 * @Version 1.0
 */
public enum VoucherTemplateLogicTypeEnum {

    运算实体("code", "运算实体"),
    运算方式("method", "运算方式"),
    括号("bracket", "括号"),
    数字("number", "数字"),
    小数点("point", "小数点"),
    分录金额("entryAmount", "分录金额"),
    ;

    private String code;
    private String value;

    VoucherTemplateLogicTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static VoucherTemplateLogicTypeEnum valueOfByCode(String code){
        for (VoucherTemplateLogicTypeEnum value : values()) {
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
