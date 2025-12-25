package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 法定单位摘要
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class StatutoryBodySummaryStrategy implements SummaryStrategy {

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.法定单位;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill) {
        return Objects.isNull(businessBill.getStatutoryBodyName()) ? "" : businessBill.getStatutoryBodyName();
    }
}
