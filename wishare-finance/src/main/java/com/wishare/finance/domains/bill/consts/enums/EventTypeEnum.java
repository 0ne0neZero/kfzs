package com.wishare.finance.domains.bill.consts.enums;

/**
 * @description: 触发的事件类型
 * @author: pgq
 * @since: 2022/10/11 13:52
 * @version: 1.0.0
 */
public enum EventTypeEnum {
    应收计提(1, 0, "INFER_VOUCHER_EVENT_TYPE", "应收计提", ""),
    收款结算(2, 0, "INFER_VOUCHER_EVENT_TYPE", "收款结算", ""),
    预收应收核销(3, 0, "INFER_VOUCHER_EVENT_TYPE", "预收应收核销（结账销账）", ""),
    账单调整(4, 0, "INFER_VOUCHER_EVENT_TYPE", "账单调整", ""),
    账单开票(5, 0, "INFER_VOUCHER_EVENT_TYPE", "账单开票", ""),
    冲销作废(6, 0, "INFER_VOUCHER_EVENT_TYPE", "冲销作废", ""),
    未认领暂收款(7, 0, "INFER_VOUCHER_EVENT_TYPE", "未认领暂收款", ""),
    应付计提(8, 0, "INFER_VOUCHER_EVENT_TYPE", "应付计提", ""),
    付款结算(9, 0, "INFER_VOUCHER_EVENT_TYPE", "付款结算", ""),
    收票结算(10, 0, "INFER_VOUCHER_EVENT_TYPE", "收票结算", ""),
    ;

    private int event;

    private int type;

    private String dictionaryType;

    private String action;

    private String desc;

    EventTypeEnum(int event, int type, String dictionaryType, String action,
        String desc) {
        this.event = event;
        this.type = type;
        this.dictionaryType = dictionaryType;
        this.action = action;
        this.desc = desc;
    }

    public int getEvent() {
        return event;
    }

    public int getType() {
        return type;
    }

    public String getDictionaryType() {
        return dictionaryType;
    }

    public String getAction() {
        return action;
    }

    public String getDesc() {
        return desc;
    }

}
