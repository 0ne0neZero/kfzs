package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;



import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRuleRecord;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.utils.ErrorAssertUtil;

/**
 * 手动推凭
 * @param <C>
 */
public abstract class ManualBillStrategy<C extends ManualBillStrategyCommand> extends AbstractPushBillStrategy<C> {

    public ManualBillStrategy(TriggerEventBillTypeEnum eventType) {
        super(PushMethod.手动推送, eventType);
    }

    @Override
    public void execute(C command) {
        doExecute(command, billRuleRepository.getById(command.getBillRuleId()));
    }
}
