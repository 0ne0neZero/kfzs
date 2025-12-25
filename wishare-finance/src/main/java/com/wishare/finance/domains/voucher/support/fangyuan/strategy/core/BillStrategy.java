package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;


import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;


public interface BillStrategy<C extends BillStrategyCommand> {

    /**
     * 事件类型
     *
     * @return 事件类型
     */
    TriggerEventBillTypeEnum eventType();

    /**
     *  推单模式
     *
     * @return 推单模式
     */
    PushMethod pushMethod();

    /**
     * 执行策略
     */
    void execute(C command);

}
