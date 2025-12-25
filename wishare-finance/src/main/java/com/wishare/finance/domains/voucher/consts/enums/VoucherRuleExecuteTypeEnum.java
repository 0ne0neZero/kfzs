package com.wishare.finance.domains.voucher.consts.enums;

/**
 * @description: 凭证规则推送方式
 * @author: pgq
 * @since: 2022/11/1 11:19
 * @version: 1.0.0
 */
public enum VoucherRuleExecuteTypeEnum {

    定时推送(0, "定时推送"),
    即时推送(1, "即时推送"),
    手动推送(2, "手动推送"),
    ;

    private int type;

    private String desc;

    VoucherRuleExecuteTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
