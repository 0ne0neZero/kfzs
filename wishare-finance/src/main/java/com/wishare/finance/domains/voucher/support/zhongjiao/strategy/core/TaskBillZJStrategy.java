package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;



import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;

/**
 * 手动推凭
 * @param <C>
 */
public abstract class TaskBillZJStrategy<C extends TaskBillZJStrategyCommand> extends AbstractPushBillZJStrategy<C> {

    public TaskBillZJStrategy(ZJTriggerEventBillTypeEnum eventType) {
        super(ZJPushMethod.定时推送, eventType);
    }

    @Override
    public void execute(C command) {
        doExecute(command, billRuleRepository.getById(command.getBillRuleId()));
    }
}
