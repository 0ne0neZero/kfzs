package com.wishare.finance.domains.voucher.strategy.bpm.summary;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import org.springframework.stereotype.Component;

@Component
public class BpmApplicantSummaryStrategy implements BpmSummaryStrategy {
    @Override
    public VoucherTemplateBpmSummaryTypeEnum summaryType() {
        return VoucherTemplateBpmSummaryTypeEnum.申请人;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        return businessProcessHandleF.getApplicant();
    }
}
