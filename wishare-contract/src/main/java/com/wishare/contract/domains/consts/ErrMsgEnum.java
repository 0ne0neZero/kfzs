package com.wishare.contract.domains.consts;

/**
 * 异常枚举常量
 * @author yancao
 */
public enum ErrMsgEnum {

    MERCHANT_NAME_EXISTS("客商名称已存在"),
    MERCHANT_NOT_EXISTS("客商不存在"),

    CONTRACT_CATEGORY_NAME_EXIST("合同分类名称已存在"),
    CONTRACT_CATEGORY_NOT_EXIST("合同分类不存在"),
    CONTRACT_CATEGORY_EXCEED_MAX_LEVEL("合同分类层级超过最大层级"),
    CONTRACT_CATEGORY_IN_USE("当前分类已被引用，不可删除"),

    CONTRACT_TEMPLATE_NAME_EXIST("合同范本名称已存在，修改后可再次导入"),
    CONTRACT_TEMPLATE_IN_USE("当前合同范本已被引用，不可删除"),
    CONTRACT_TEMPLATE_CHANGING("该范本存在草稿状态的变更，请先完成草稿状态的范本变更后方可继续操作"),

    COLLECTION_JUMP_PERIOD("必须按照收款计划升序时间执行收款，不允许跳收"),
    INVOICE_JUMP_PERIOD("必须按照收款计划升序时间执行开票，不允许跳期开票"),
    PAYMENT_JUMP_PERIOD("必须按照收款计划升序时间执行付款，不允许跳期付款"),

    DELETE_BILLS("删除应收账单失败"),
    NOT_BILLS("获取账单id失败"),
    EXAMINE_BILLS("账单审核失败"),
    ADD_BILLS("新增应收账单失败"),
    SETTLE_BILLS("应收账单结算失败"),
    PAY_BILLS("应付账单结算失败"),
    ADD_PAY_BILLS("新增应付账单失败"),
    ADD_BILLS_NOT_EXIST("账单不存在"),
    COLLECTION_PLAN("补充协议，终止协议，框架合同，虚拟合同均不可以进行收款操作"),

    CONTRACT_BILL_NOT_EXIST("合同账单不存在，无法开票"),

    FUNCTION_NOT_EXIST("不存在此功能")
    ;

    private String errMsg;

    ErrMsgEnum(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
