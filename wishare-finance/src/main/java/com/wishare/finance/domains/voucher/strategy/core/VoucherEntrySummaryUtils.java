package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;

import java.util.List;
import java.util.Objects;

/**
 * 摘要解析工具类
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
public class VoucherEntrySummaryUtils {

    public static String format(List<VoucherTemplateEntrySummary> summary, VoucherBusinessBill businessBill){
        if (summary == null || summary.isEmpty()) {
            return "";
        }
        StringBuilder summaryBuilder = new StringBuilder();
        for (VoucherTemplateEntrySummary entrySummary : summary) {
            switch (VoucherTemplateSummaryTypeEnum.valueOfByCode(entrySummary.getType())){
                case 普通文本:
                    summaryBuilder.append(entrySummary);
                    break;
                case 归属月:
                    summaryBuilder.append(DateTimeUtil.formatCNDate(businessBill.getAccountDate()));
                    break;
                case 操作日期:
                    summaryBuilder.append(DateTimeUtil.nowCNDate());
                    break;
                case 成本中心:
                    summaryBuilder.append(Objects.isNull(businessBill.getCostCenterName()) ? "" : businessBill.getCostCenterName());
                    break;
                case 法定单位:
                    summaryBuilder.append(Objects.isNull(businessBill.getStatutoryBodyName()) ? "" : businessBill.getStatutoryBodyName());
                    break;
            }
        }
        return summaryBuilder.toString();
    }

}
