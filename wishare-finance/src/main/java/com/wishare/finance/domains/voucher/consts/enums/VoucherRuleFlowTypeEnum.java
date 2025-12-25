package com.wishare.finance.domains.voucher.consts.enums;

/**
 * 流水类型
 * @author dxclay
 * @since  2023/4/5
 * @version 1.0
 */
public enum VoucherRuleFlowTypeEnum {

    全部(0, "全部"),
    已认领流水(1, "已认领流水"),
    未认领流水(2, "未认领流水"),
    ;

    private int type;

    private String desc;

    VoucherRuleFlowTypeEnum(int type, String desc) {
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
