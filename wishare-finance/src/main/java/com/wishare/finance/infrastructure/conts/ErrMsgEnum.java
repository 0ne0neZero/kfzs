package com.wishare.finance.infrastructure.conts;

/**
 * 异常枚举常量
 */
public enum ErrMsgEnum {

    DISABLE_STATE_EXCEPTION("启用禁用状态异常"),

    TAX_RATE_NO_EXISTS("该税率不存在"),
    TAX_RATE_USED_BY_CHARGE_ITEM("该税率已被以下费项引用不可删除："),

    STATUTORY_BODY_CODE_EXISTS("该法定单位编码已存在"),
    STATUTORY_BODY_NAME_EXISTS("该法定单位名称已存在"),
    STATUTORY_BODY_NO_EXISTS("该法定单位不存在"),

    STATUTORY_BODY_ACCOUNT_NO_EXISTS("该银行账户不存在"),
    STATUTORY_BODY_ACCOUNT_EXISTS("该银行账户已存在"),
    STATUTORY_BODY_ACCOUNT_NAME_EXISTS("该银行账户名称已存在"),
    STATUTORY_BODY_ACCOUNT_BASIC_TYPE_EXISTS("该法定单位下已存在一个基本户"),

    COST_CENTER_CODE_EXISTS("成本中心编码已存在"),
    COST_CENTER_NAME_EXISTS("成本中心名称已存在"),
    COST_CENTER_NO_EXISTS("成本中心不存在"),

    ACCOUNT_BOOK_CODE_EXISTS("该账簿编码已存在"),
    ACCOUNT_BOOK_NAME_EXISTS("该账簿编号已存在"),
    ACCOUNT_BOOK_NO_EXISTS("该账簿不存在"),
    ACCOUNT_BOOK_EXISTS("该账簿已存在"),

    //发票相关异常提示
    INVOICE_BOOK_NO_EXISTS("该票本不存在"),
    INVOICE_BOOK_HAS_RECEIVE("当前票本内的票据已被领用,无法删除"),

    INVOICE_RECEIVE_NO_EXISTS("该发票领用不存在"),

    INVOICE_QUANTIT_MORE("发票领用数量超额异常"),

    FLOW_DETAIL_NOT_FOUNT("流水不存在"),
    FLOW_NUMBER_EXIST("流水号已存在"),
    FLOW_OUR_ACCOUNT_NOT_SAME("流水本方账户信息不一致"),
    FLOW_OPPOSITE_ACCOUNT_NOT_SAME("流水对方账户信息不一致"),
    FLOW_INCOME_ACCOUNT_NOT_SAME("收入流水本方账户信息不一致"),
    FLOW_REFUND_ACCOUNT_NOT_SAME("退款流水本方账户信息不一致"),
    FLOW_ACCOUNT_NOT_SAME("流水账户信息不一致"),
    FLOW_OPPOSITE_ACCOUNT_IS_EMPTY("对方账户不能为空"),
    FLOW_OPPOSITE_NAME_IS_EMPTY("对方名称不能为空"),
    FLOW_OPPOSITE_BANK_IS_EMPTY("对方开户行不能为空"),
    FLOW_OUR_ACCOUNT_IS_EMPTY("本方账户不能为空"),
    FLOW_OUR_NAME_IS_EMPTY("本方名称不能为空"),
    FLOW_TRADING_PLATFORM_IS_EMPTY("交易平台不能为空"),
    FLOW_OUR_BANK_IS_EMPTY("本方开户行不能为空"),
    FLOW_SETTLE_AMOUNT_IS_EMPTY("交易金额不能为空"),
    FLOW_SETTLE_AMOUNT_NOT_LEGAL("交易金额不合法，请保证小数点后最多两位小数"),
    FLOW_CLAIM_AMOUNT_NOT_SAME("流水金额与票据金额不一致"),
    FLOW_BILL_AMOUNT_CLAIM_AMOUNT_NOT_SAME("流水金额与账单金额不一致"),
    FLOW_RED_AMOUNT_NOT_SAME("流水退款金额与票据红冲金额不一致"),
    FLOW_REFUND_AMOUNT_NOT_SAME("流水退款金额与账单退款金额不一致"),
    FLOW_CLAIM_RECORDS_NOT_EXIST("流水认领记录不存在"),
    FLOW_CLAIM_REVOKED_FAIL("流水认领撤销失败"),
    FLOW_TYPE_FAIL("流水类型错误"),
    AMOUNT_FAIL("金额类型有误，请输入数值类型的金额"),
    SERIAL_NUMBER_EXISTS("流水号已存在"),
    REMIT_AMOUNT_CLAIM_NOT_SAME("汇款金额和流水金额不一致"),

    FLOW_CLAIM_IS_RECONCILE("账单已对账核对成功，无法解除认领"),
    FLOW_CLAIM_IS_APPROVE("已驳回资金收款单不能直接接触认领"),
    FLOW_CLAIM_IS_PUSH("已生成资金收款单，无法解除认领"),
    BILL_INVOICED("该批次账单存在已开票状态，请重新选择"),
    BILL_INVOICING("该批次账单存在开票中状态，请重新选择"),
    STATUTORYBODY_NO_SAME("该批次存在不同法定单位账单，不允许同时开票"),

    STATUTORYINVOICECONF_NOT_FOUND("未查询到相关电子发票配置"),

    BILL_NOT_FOUND("账单不存在"),
    SETTLE_AMOUNT_ERR("结算金额异常"),
    ;

    private String errMsg;

    ErrMsgEnum(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
