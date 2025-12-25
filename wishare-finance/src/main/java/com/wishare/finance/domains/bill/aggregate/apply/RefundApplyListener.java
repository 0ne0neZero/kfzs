package com.wishare.finance.domains.bill.aggregate.apply;

import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.OptionStyle;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.support.OnBillApplyListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;
import com.wishare.finance.infrastructure.utils.AmountUtils;

/**
 * 退款审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class RefundApplyListener<B extends Bill> implements OnBillApplyListener<B> {

    @Override
    public void after(B bill, BillApproveE billApprove, Object applyInfo) {
        if (applyInfo instanceof BillRefundE) {
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.退款申请,
                    new Content().option(new ContentOption(new PlainTextDataItem("审核内容： 账单退款", true)))
                            .option(new ContentOption(new PlainTextDataItem("退款金额为：", false)))
                            .option(new ContentOption(new PlainTextDataItem(AmountUtils.toStringAmount( ((BillRefundE) applyInfo).getRefundAmount()), false), OptionStyle.normal()))
                            .option(new ContentOption(new PlainTextDataItem("元", false))));
        }else {
            BizLog.initiate(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.退款申请, new Content());
        }
    }
}
