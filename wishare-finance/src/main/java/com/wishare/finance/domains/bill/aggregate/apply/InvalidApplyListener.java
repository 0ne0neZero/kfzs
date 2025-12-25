package com.wishare.finance.domains.bill.aggregate.apply;

import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.support.OnBillApplyListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;

/**
 * 作废审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class InvalidApplyListener<B extends Bill> implements OnBillApplyListener<B> {

    @Override
    public void after(B bill, BillApproveE billApprove, Object applyInfo) {
        //账单日志记录
        BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.作废申请, new Content());
    }
}
