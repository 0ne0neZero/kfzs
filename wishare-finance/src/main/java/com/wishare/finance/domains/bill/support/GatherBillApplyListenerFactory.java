package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.aggregate.apply.GatherBillRefundApplyListener;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;

/**
 * 申请监听器工厂
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class GatherBillApplyListenerFactory {

    /**
     * 根据操作类型获取申请监听器
     *
     * @param approveOperateType 操作类型
     * @return Bill
     */
    public static  GatherOnBillApproveListener getListener(BillApproveOperateTypeEnum approveOperateType) {
        switch (approveOperateType) {
            case 收款单退款:
                return new GatherBillRefundApplyListener();
            default:
                return null;
        }
    }

}
