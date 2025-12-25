package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;

/**
 * 定时任务推凭模式策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/8
 */
public abstract class TaskVoucherStrategy<C extends TaskVoucherStrategyCommand> extends AbstractVoucherStrategy<C> {

    public TaskVoucherStrategy(VoucherEventTypeEnum eventType) {
        super(PushMode.定时, eventType);
    }

    @Override
    public VoucherRuleRecord execute(C command) {
        return doExecute(command, voucherRuleRepository.getById(command.getVoucherRuleId()));
    }

}
