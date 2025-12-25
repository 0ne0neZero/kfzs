package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 费项摘要
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class ChargeItemSummaryStrategy implements SummaryStrategy {

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.费项;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill) {
        return Objects.isNull(businessBill.getChargeItemName()) ? "" : businessBill.getChargeItemName();
    }
}
