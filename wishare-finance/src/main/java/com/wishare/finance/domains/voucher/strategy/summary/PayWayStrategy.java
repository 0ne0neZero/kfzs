package com.wishare.finance.domains.voucher.strategy.summary;

import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateSummaryTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntrySummary;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author fxl
 * @describe
 * @date 2024/3/21
 */
@Component
public class PayWayStrategy implements SummaryStrategy{

    @Override
    public VoucherTemplateSummaryTypeEnum summaryType() {
        return VoucherTemplateSummaryTypeEnum.结算方式;
    }

    @Override
    public String summary(VoucherTemplateEntrySummary entrySummary, VoucherBusinessBill businessBill) {
        String payWay = SettleWayEnum.valueNameOfByCode(businessBill.getPayWay());
        String payChannel = SettleChannelEnum.valueNameOfByCode(businessBill.getPayChannel());
        return StringUtils.isBlank(payWay) ? payChannel : payWay + "-" + payChannel;
    }
}
