package com.wishare.contract.domains.enums;

import com.wishare.bizlog.action.Action;
import com.wishare.bizlog.action.ActionType;

/**
 * 默认行为
 *
 * @author wangrui
 * @since 2023/1/10
 * @version 1.0
 */
public enum LogActionEnum implements Action {

    新增("CREATE", "新增", ActionType.NORMAL),
    修改("CREATE", "修改", ActionType.NORMAL),
    删除("DELETE", "删除", ActionType.NORMAL),
    提交("SUBMIT", "提交", ActionType.NORMAL),
    发起("LAUNCH", "发起", ActionType.NORMAL),

    ;

    private String code;
    private String value;
    private ActionType type;

    LogActionEnum(String code, String value, ActionType type) {
        this.code = code;
        this.value = value;
        this.type = type;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public ActionType getType() {
        return type;
    }
}
