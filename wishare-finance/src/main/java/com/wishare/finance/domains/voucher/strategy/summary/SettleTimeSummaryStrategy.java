package com.wishare.finance.domains.voucher.strategy.summary;

import java.util.Objects;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Component;

@Component
public class SettleTimeSummaryStrategy implements SummaryStrategy {

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.结算日期;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary,
            VoucherBusinessBill businessBill) {
        if (Objects.isNull(businessBill.getPayTime())) {
            return "";
        }
        return LocalDateTimeUtil.format(businessBill.getPayTime(), DatePattern.CHINESE_DATE_PATTERN);
    }
}
