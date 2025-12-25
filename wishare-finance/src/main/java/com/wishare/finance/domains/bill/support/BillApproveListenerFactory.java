package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.aggregate.approve.*;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.entity.Bill;

/**
 * 审核监听器工厂
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class BillApproveListenerFactory {

    /**
     * 根据操作类型获取审核监听器
     * @param approveOperateType
     * @return
     * @param <B>
     */
    public static <B extends Bill> OnBillApproveListener<B> getListener(BillApproveOperateTypeEnum approveOperateType){
        switch (approveOperateType){
            case 调整:
                return new AdjustApproveListener<B>();
            case 减免:
                return new DeductionApproveListener<B>();
            case 结转:
                return new CarryoverApproveListener<B>();
            case 作废:
                return new InvalidApproveListener<B>();
            case 退款:
                return new RefundApproveListener<B>();
            case 生成审核:
                return new CreateApproveListener<B>();
            case 冲销:
                return new ReverseApproveListener<B>();
            case 跳收:
                return new JumpApproveListener<>();
            default: return null;
        }
    }

}
