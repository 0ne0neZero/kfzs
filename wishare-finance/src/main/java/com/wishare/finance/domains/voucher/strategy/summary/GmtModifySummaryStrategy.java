package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

/**
 * 操作日期文本摘要
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class GmtModifySummaryStrategy implements SummaryStrategy {

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.操作日期;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill) {
        return DateTimeUtil.nowCNDate();
    }
}
