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
public interface OnBillApproveListener<B extends Bill> {

    /**
     * 前置操作
     *
     * @param bill         账单
     * @param billApprove  审核信息
     */
    default void beforeOperation(B bill, BillApproveE billApprove){

    }

    /**
     * 同意审核
     * @param bill         账单
     * @param billApprove  审核信息
     */
    default void onAgree(B bill, BillApproveE billApprove){

    }

    /**
     * 拒绝
     * @param bill         账单
     * @param billApprove  审核信息
     * @param reason       原因
     */
    default void onRefuse(B bill, BillApproveE billApprove, String reason){

    }

    /**
     * 后置操作
     *
     * @param bill         账单
     * @param billApprove  审核信息
     * @param applyInfo    申请信息
     */
    default void afterOperation(B bill, BillApproveE billApprove, Object applyInfo){

    }

}
