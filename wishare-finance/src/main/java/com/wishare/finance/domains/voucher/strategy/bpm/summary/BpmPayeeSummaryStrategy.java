package com.wishare.finance.domains.voucher.strategy.bpm.summary;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankPrivateF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankPublicF;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BpmPayeeSummaryStrategy implements BpmSummaryStrategy {
    @Override
    public VoucherTemplateBpmSummaryTypeEnum summaryType() {
        return VoucherTemplateBpmSummaryTypeEnum.收款人;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        List<ProcessBankPublicF> publicPayees = accountBook.getPublicPayees();
        List<ProcessBankPrivateF> privatePayees = accountBook.getPrivatePayees();
        if (CollectionUtils.isNotEmpty(publicPayees)) {
            return publicPayees.get(index).getPayee();
        } else if (CollectionUtils.isNotEmpty(privatePayees)) {
            return privatePayees.get(index).getPayee();
        }
        return "";
    }
}
