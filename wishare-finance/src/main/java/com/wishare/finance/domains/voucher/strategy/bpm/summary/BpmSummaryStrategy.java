package com.wishare.finance.domains.voucher.strategy.bpm.summary;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;

public interface BpmSummaryStrategy {

    /**
     * 摘要类型
     * @return
     */
    VoucherTemplateBpmSummaryTypeEnum summaryType();

    /**
     * 摘要描述
     * @param entrySummary 凭证模板分录摘要信息
     * @param accountBook 凭证业务单据信息
     * @return 摘要描述
     */
    String summary(VoucherTemplateEntrySummary entrySummary, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index);

}
