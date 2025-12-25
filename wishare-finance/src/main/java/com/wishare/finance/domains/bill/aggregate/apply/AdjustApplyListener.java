package com.wishare.finance.domains.bill.aggregate.apply;

import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.finance.domains.bill.aggregate.BillAdjustA;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.support.OnBillApplyListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.starter.Global;

/**
 * 调整申请监听器
 * @author yancao
 */
public class AdjustApplyListener<B extends Bill> implements OnBillApplyListener<B> {

    @Override
    public void after(B bill, BillApproveE billApprove, Object applyInfo){
        //预调整
        B b = Global.mapperFacade.map(bill, (Class<B>)bill.getClass());
        BillAdjustA<B> billAdjustA = new BillAdjustA<>(b, billApprove, (BillAdjustE)applyInfo);
        billAdjustA.adjust(1);

        //账单日志记录
        BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.调整申请, new Content());
    }
}
