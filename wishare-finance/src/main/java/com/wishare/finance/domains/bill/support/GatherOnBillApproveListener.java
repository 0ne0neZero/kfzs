package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.entity.BillApproveE;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author xujian
 * @date 2023/1/5
 * @Description:
 */
public interface GatherOnBillApproveListener<B> {

    /**
     * 同意审核
     * @param bill         账单
     * @param billApprove  审核信息
     */
     default void onAgree(B bill, BillApproveE billApprove){};

    /**
     * 拒绝
     * @param bill         账单
     * @param billApprove  审核信息
     * @param reason       原因
     */
    default void onRefuse(B bill, BillApproveE billApprove, String reason){};
}
