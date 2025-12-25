package com.wishare.finance.domains.bill.support;

import com.wishare.finance.domains.bill.aggregate.approve.GatherRefundApproveListener;
import com.wishare.finance.domains.bill.aggregate.approve.GatherReverseApproveListener;
import com.wishare.finance.domains.bill.consts.enums.BillApproveOperateTypeEnum;
import com.wishare.finance.domains.bill.entity.GatherBill;

/**
 * @author xujian
 * @date 2023/1/5
 * @Description: 收款单审核监听器工厂
 */
public class GatherBillApproveListenerFactory {

    /**
     * 根据操作类型获取审核监听器
     * @param approveOperateType 收款单操作类型
     * @return 收款单不同操作策略处理器
     */
    public static GatherOnBillApproveListener getListener(BillApproveOperateTypeEnum approveOperateType){
        switch (approveOperateType){
            case 收款单退款:
                return new GatherRefundApproveListener();
            case 收款单冲销:
                return new GatherReverseApproveListener();
            default: return null;
        }
    }
}
