package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 普通文本摘要
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Component
public class TextSummaryStrategy implements SummaryStrategy {

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.普通文本;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill) {
        // 去除${}
        String value = entrySummary.getValue();
        if (StringUtils.isNotBlank(value) && value.startsWith("${") && value.endsWith("}")) {
            value = value.substring(2, value.length() - 1);
        }
        return value;
    }
}
