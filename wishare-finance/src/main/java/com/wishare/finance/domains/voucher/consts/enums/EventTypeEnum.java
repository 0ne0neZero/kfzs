package com.wishare.finance.domains.voucher.consts.enums;

/**
 * @description: 触发的事件类型
 * @author: pgq
 * @since: 2022/10/11 13:52
 * @version: 1.0.0
 */
public enum EventTypeEnum {
    收入_应收计提(1, 1, "INCOME_VOUCHER_EVENT_TYPE", "应收计提", "1、收款账单，日期到达应收日期（晚上23点）2、收款账单，手动计提"),
    收入_收款结算(2, 1, "INCOME_VOUCHER_EVENT_TYPE", "收款结算", "1、收款账单，完成收费，状态变更为“已结算”"),
    收入_账单调整(3, 1, "INCOME_VOUCHER_EVENT_TYPE", "账单调整", "1、收款账单，完成对账核销"),
    收入_冲销作废(4, 1, "INCOME_VOUCHER_EVENT_TYPE", "冲销作废", "1、未结算收款账单，冲销应收 2、已结算未核销收款账单，冲销预收 3、已核销收款账单，冲销收入"),
    收入_坏账准备(5, 1, "INCOME_VOUCHER_EVENT_TYPE", "坏账准备", "1、收款账单，标注为坏账准备"),
    收入_坏账核销(6, 1, "INCOME_VOUCHER_EVENT_TYPE", "坏账核销", "1、被标注为坏账准备的收款账单，款项收回时"),
    收入_结账销账(7, 1, "INCOME_VOUCHER_EVENT_TYPE", "结账销账", ""),
    收入_账单开票(8, 1, "INCOME_VOUCHER_EVENT_TYPE", "账单开票", ""),
    支出_进销开票(122, 2, "PAY_VOUCHER_EVENT_TYPE", "进销开票", ""),
    支出_账单调整(123, 2, "PAY_VOUCHER_EVENT_TYPE", "账单调整", ""),
    支出_冲销作废(124, 2, "PAY_VOUCHER_EVENT_TYPE", "冲销作废", "1、未结算收款账单，冲销应支 2、已结算未核销收款账单，冲销预支 3、已核销收款账单，冲销成本"),
    支出_结账核销(125, 2, "PAY_VOUCHER_EVENT_TYPE", "结账核销", "1、付款账单，完成核销（当日自动核销）"),
    支出_付款结算(126, 2, "PAY_VOUCHER_EVENT_TYPE", "付款结算", "1、付款账单，完成付款，状态变更为“已结算”"),
    支出_应付计提(127, 2, "PAY_VOUCHER_EVENT_TYPE", "应付计提", "1、付款账单，日期到达应付日期 2、付款账单，手动计提"),
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
    手动生成(127, 0, "INFER_VOUCHER_EVENT_TYPE", "手动生成", ""),
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

    public static EventTypeEnum valueOfByCodeByEvent(int code) {
        for (EventTypeEnum value : EventTypeEnum.values()) {
            if (value.getEvent() == code && value.getType() == 0) {
                return value;
            }
        }
        return null;
    }

}
