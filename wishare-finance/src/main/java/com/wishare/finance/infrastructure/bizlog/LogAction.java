package com.wishare.finance.infrastructure.bizlog;

import com.wishare.bizlog.action.Action;
import com.wishare.bizlog.action.ActionType;

/**
 * 日志行为
 *
 * @author dxclay
 * @since 2023/1/6
 * @version 1.0
 */
public enum LogAction implements Action {

    创建("CREATE", "创建", ActionType.NORMAL),
    修改("UPDATE", "修改", ActionType.NORMAL),
    删除("DELETE", "删除", ActionType.NORMAL),
    生成("CREATE", "生成", ActionType.NORMAL),
    导入("IMPORT", "导入", ActionType.NORMAL),
    补录导入("RECORD_IMPORT", "补录导入", ActionType.NORMAL),
    审核通过("APPROVE_PASS", "审核通过", ActionType.NORMAL),
    审核拒绝("APPROVE_REFUSE", "审核拒绝", ActionType.NORMAL),
    生成审核("APPROVE_CREATE", "生成审核", ActionType.NORMAL),
    作废("INVALID", "作废", ActionType.NORMAL),
    退款("REFUND", "退款", ActionType.INITIATE),
    付款("PAY", "付款", ActionType.INITIATE),
    收款("GATHER", "收款", ActionType.INITIATE),
    结转("CARRYOVER", "结转", ActionType.INITIATE),
    冲销("REVERSE", "冲销", ActionType.INITIATE),
    拆分("SPLIT", "拆分", ActionType.NORMAL),
    调整申请("APPLY_ADJUST", "调整申请", ActionType.INITIATE),
    减免申请("APPLY_DEDUCTION", "减免申请", ActionType.INITIATE),
    结转申请("APPLY_CARRYOVER", "结转申请", ActionType.INITIATE),
    退款申请("APPLY_REFUND", "退款申请", ActionType.INITIATE),
    冲销申请("APPLY_REVERSE", "冲销申请", ActionType.INITIATE),
    作废申请("APPLY_INVALID", "作废申请", ActionType.INITIATE),

    跳收申请("APPLY_JUMP", "跳收申请", ActionType.INITIATE),
    生成申请("APPLY_CREATE", "生成申请", ActionType.INITIATE),
    反审核("DEAPPROVE", "反审核", ActionType.INITIATE),
    冻结("FREEZE", "冻结", ActionType.INITIATE),
    解冻("UNFREEZE", "解冻", ActionType.INITIATE),
    挂账("ON_ACCOUNT", "挂账", ActionType.INITIATE),
    销账("WRITE_OFF", "销账", ActionType.INITIATE),
    回滚冲销("ROBACK_REVERSE", "回滚冲销", ActionType.INITIATE),
    开票("INVOICE", "开发票", ActionType.INITIATE),
    开收据("RECEIPT", "开收据", ActionType.INITIATE),
    交账("HAND_ACCOUNT", "交账", ActionType.INITIATE),
    反交账("HAND_ACCOUNT_REVERSAL", "交账", ActionType.INITIATE),
    扣款("Deduction", "扣款", ActionType.INITIATE),

    ;

    private String code;
    private String value;
    private ActionType type;

    LogAction(String code, String value, ActionType type) {
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
