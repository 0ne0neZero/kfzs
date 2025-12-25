package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;

/**
 * 手动推凭模式策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/8
 */
public abstract class ManualVoucherStrategy<C extends ManualVoucherStrategyCommand> extends AbstractVoucherStrategy<C> {

    public ManualVoucherStrategy(VoucherEventTypeEnum eventType) {
        super(PushMode.手动, eventType);
    }

    public ManualVoucherStrategy() {
        super(PushMode.手动);
    }

    @Override
    public VoucherRuleRecord execute(C command) {
        return doExecute(command, voucherRuleRepository.getById(command.getVoucherRuleId()));
    }
}
