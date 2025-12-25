package com.wishare.finance.domains.voucher.strategy;

import java.util.List;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;

public class DefaultVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public DefaultVoucherStrategy(
            VoucherEventTypeEnum eventType) {
        super(eventType);
    }

    public DefaultVoucherStrategy() {
        super();
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command,
            List<VoucherRuleConditionOBV> conditions) {
        return null;
    }
}
