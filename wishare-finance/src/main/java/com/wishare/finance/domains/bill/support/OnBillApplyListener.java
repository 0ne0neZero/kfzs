package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;

/**
 * 审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public interface OnBillApplyListener<B extends Bill> {

    /**
     * 前置操作
     *
     * @param bill      账单
     * @param applyInfo 申请信息
     */
    default void before(B bill, BillApproveE billApprove, Object applyInfo) {

    }

    /**
     * 后置操作
     *
     * @param bill      账单
     * @param applyInfo 申请信息
     */
    default void after(B bill, BillApproveE billApprove, Object applyInfo) {

    }

}
