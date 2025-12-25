package com.wishare.finance.domains.bill.aggregate.approve;

import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.ContentOption;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.operator.Operator;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillApproveE;
import com.wishare.finance.domains.bill.repository.BillRepositoryFactory;
import com.wishare.finance.domains.bill.support.OnBillApproveListener;
import com.wishare.finance.infrastructure.bizlog.LogAction;
import com.wishare.finance.infrastructure.bizlog.LogContext;
import com.wishare.finance.infrastructure.bizlog.LogObject;

/**
 * 生成审核监听器
 *
 * @Author dxclay
 * @Date 2022/9/22
 * @Version 1.0
 */
public class CreateApproveListener<B extends Bill> implements OnBillApproveListener<B> {

    @Override
    public void onAgree(B bill, BillApproveE billApprove) {
        bill.setInit(false);
        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核通过,
                new Content().option(new ContentOption(new PlainTextDataItem("生成审核", false))));
    }

    @Override
    public void onRefuse(B bill, BillApproveE billApprove, String reason) {
        bill.delete();
        //日志记录
        BizLog.normal(String.valueOf(bill.getId()), LogContext.getOperator(), LogObject.账单, LogAction.审核拒绝,
                new Content().option(new ContentOption(new PlainTextDataItem("生成审核拒绝", false))));
    }
}
