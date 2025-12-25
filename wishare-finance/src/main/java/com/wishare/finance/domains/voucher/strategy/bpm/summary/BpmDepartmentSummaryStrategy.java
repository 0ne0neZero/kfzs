package com.wishare.finance.domains.voucher.strategy.bpm.summary;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessChargeDetailF;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BpmDepartmentSummaryStrategy implements BpmSummaryStrategy {
    @Override
    public VoucherTemplateBpmSummaryTypeEnum summaryType() {
        return VoucherTemplateBpmSummaryTypeEnum.部门;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        List<ProcessChargeDetailF> chargeDetails = accountBook.getChargeDetails();
        ProcessChargeDetailF chargeDetailF = chargeDetails.get(index);
        return chargeDetailF.getOrgName();
    }
}
