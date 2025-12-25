package com.wishare.finance.domains.bill.event;

/**
 * 账单行为
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
public enum BillAction {

    /**
     * 新建
     */
    CREATED,
    /**
     * 调整
     */
    ADJUSTED,
    /**
     * 减免
     */
    DEDUCTION,
    /**
     * 结算
     */
    SETTLED,
    /**
     * 结转
     */
    CARRYOVER,
    /**
     * 开票
     */
    INVOICED,
    /**
     * 推凭
     */
    VOUCHER,
    /**
     * 作废
     */
    INVALIDED,
    /**
     * 冻结
     */
    FREEZE,
    /**
     * 冲销
     */
    REVERSED,
    /**
     * 交账
     */
    HANDED,
    /**
     * 核销
     */
    VERIFY,
    /**
     * 退款
     */
    REFUND,
    /**
     * 挂账
     */
    ON_ACCOUNT,
    /**
     * 审核(计提)
     */
    APPROVED,

    /**
     * 批量新建
     */
    CREATED_BATCH,
    /**
     * 批量结算
     */
    SETTLED_BATCH,
    /**
     * 批量调整
     */
    ADJUSTED_BATCH,
    /**
     * 批量开票
     */
    INVOICED_BATCH,
    /**
     * 批量作废
     */
    INVALIDED_BATCH,
    /**
     * 批量冲销
     */
    REVERSED_BATCH,
    /**
     * 批量核销
     */
    VERIFY_BATCH,
    /**
     * 批量退款
     */
    REFUND_BATCH,
    /**
     * 批量审核(计提)
     */
    APPROVED_BATCH,
    /**
     * 跳收
     */
    JUMP,
    /**
     * 流水认领
     */
    FLOW_CLAIM,
    /**
     * 反审核
     */
    REVERSE_APPROVED,
    /**
     * 收款单冲销
     */
    GATHER_BILL_REVERSED
}
