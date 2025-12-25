package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.entity.Bill;

/**
 * 调整监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public interface OnAdjustListener<B extends Bill>{

    /**
     * 退款
     * @param bill     账单信息
     * @param amount   退款金额
     */
    default void onRefund(B bill, Long amount){

    }

    /**
     * 转应收
     * @param bill     账单信息
     * @param amount   退款金额
     */
    default void onReceive(B bill, Long amount){

    }
}