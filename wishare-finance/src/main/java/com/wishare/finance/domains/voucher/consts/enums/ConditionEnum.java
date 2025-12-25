package com.wishare.finance.domains.voucher.consts.enums;

/**
 * @description: 推凭规则过滤条件
 * @author: pgq
 * @since: 2022/10/11 14:52
 * @version: 1.0.0
 */
public enum ConditionEnum {

    法定单位(1, "法定单位", "statutory_body_id"),
    成本中心(2, "成本中心", "community_id"),
    结算方式(3, "结算方式", ""),
    票据类型(4, "票据类型", ""),
    商客(5, "商客", ""),
    账单来源(6, "账单来源", ""),
    账单类型(7, "账单类型", ""),
    员工档案(8, "员工档案", ""),
    收款银行账户(9, "收款银行账户", ""),
    付款银行账户(10, "付款银行账户", ""),
    税率(11, "税率", ""),
    部门(12, "部门", ""),
    ;

    private int code;

    private String name;

    private String column;

    ConditionEnum(int code, String name, String column) {
        this.code = code;
        this.name = name;
        this.column = column;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getColumn() {
        return column;
    }

    public static ConditionEnum valueOfByCodeByCode(Integer code) {
        for (ConditionEnum value : ConditionEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
