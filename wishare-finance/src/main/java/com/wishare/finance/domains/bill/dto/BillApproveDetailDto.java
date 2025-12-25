package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单详情信息
 *
 * @Author dxclay
 * @Date 2022/9/21
 * @Version 1.0
 */
@Getter
@Setter
public class BillApproveDetailDto<B extends Bill> {

    /**
     * 账单信息
     */
    private B bill;
    /**
     * 调整信息记录
     */
    private BillAdjustE billAdjust;
    /**
     * 结算信息记录
     */
    private BillSettleE billSettle;
    /**
     * 结转信息记录
     */
    private BillCarryoverE billCarryover;
    /**
     * 申请信息记录
     */
    private BillApproveE billApprove;
    /**
     * 退款信息记录
     */
    private BillRefundE billRefund;

    /**
     * 跳收信息记录
     */
    private BillJumpE billJump;

}
