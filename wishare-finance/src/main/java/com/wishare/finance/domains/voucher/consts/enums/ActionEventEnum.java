package com.wishare.finance.domains.voucher.consts.enums;

/**
 * @description: 触发动作枚举（不分收入支出）
 * @author: pgq
 * @since: 2022/10/24 20:17
 * @version: 1.0.0
 */
public enum ActionEventEnum {
    计提(1, "计提"),
    冲销(2, "冲销"),
    结算(3, "结算"),
    调整(4, "调整"),
    销账(5, "销账"),
    开票(6, "开票"),
    收票(7, "收票"),
    流水(8, "流水"),
    ;

    private int code;

    private String event;

    ActionEventEnum(int code, String event) {
        this.code = code;
        this.event = event;
    }

    public int getCode() {
        return code;
    }

    public String getEvent() {
        return event;
    }

    public static ActionEventEnum valueOfByCode(int code) {
        for (ActionEventEnum value : ActionEventEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }
}
