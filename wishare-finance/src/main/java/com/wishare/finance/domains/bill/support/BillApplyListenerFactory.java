package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.aggregate.apply.*;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.entity.Bill;

/**
 * 申请监听器工厂
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class BillApplyListenerFactory {

    /**
     * 根据操作类型获取申请监听器
     *
     * @param approveOperateType 操作类型
     * @return Bill
     */
    public static <B extends Bill> OnBillApplyListener<B> getListener(BillApproveOperateTypeEnum approveOperateType) {
        switch (approveOperateType) {
            case 调整:
                return new AdjustApplyListener<>();
            case 减免:
                return new DeductionApplyListener<>();
            case 结转:
                return new CarryoverApplyListener<>();
            case 作废:
                return new InvalidApplyListener<>();
            case 退款:
                return new RefundApplyListener<>();
            case 生成审核:
                return new CreateApplyListener<>();
            case 跳收:
                return new JumpApplyListener<>();
            default:
                return null;
        }
    }

}
