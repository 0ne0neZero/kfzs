package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;

/**
 * 摘要计算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
public interface SummaryStrategy {

    /**
     * 摘要类型
     * @return
     */
    VoucherTemplateSummaryTypeEnum summaryType();

    /**
     * 摘要描述
     * @param entrySummary 凭证模板分录摘要信息
     * @param businessBill 凭证业务单据信息
     * @return 摘要描述
     */
    String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill);

}
