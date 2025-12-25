package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;



import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;

/**
 * 手动推凭
 * @param <C>
 */
public abstract class ManualZJBillStrategy<C extends ManualBillZJStrategyCommand> extends AbstractPushBillZJStrategy<C> {

    public ManualZJBillStrategy(ZJTriggerEventBillTypeEnum eventType) {
        super(ZJPushMethod.手动推送, eventType);
    }

    @Override
    public void execute(C command) {
        doExecute(command, billRuleRepository.getById(command.getBillRuleId()));
    }
}
