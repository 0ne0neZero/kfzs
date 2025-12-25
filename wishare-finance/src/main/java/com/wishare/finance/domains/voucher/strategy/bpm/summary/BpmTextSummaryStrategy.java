package com.wishare.finance.domains.voucher.strategy.bpm.summary;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class BpmTextSummaryStrategy implements BpmSummaryStrategy {
    @Override
    public VoucherTemplateBpmSummaryTypeEnum summaryType() {
        return VoucherTemplateBpmSummaryTypeEnum.普通文本;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        // 去除${}
        String value = entrySummary.getValue();
        if (StringUtils.isNotBlank(value) && value.startsWith("${") && value.endsWith("}")) {
            value = value.substring(2, value.length() - 1);
        }
        return value;
    }
}
