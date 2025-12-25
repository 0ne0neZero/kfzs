package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 归属月文本摘要
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class AccountMonthSummaryStrategy implements SummaryStrategy {

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.归属月;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill) {
        return Optional.ofNullable(DateTimeUtil.formatCNMouth(businessBill.getAccountDate())).orElse("");
    }
}
