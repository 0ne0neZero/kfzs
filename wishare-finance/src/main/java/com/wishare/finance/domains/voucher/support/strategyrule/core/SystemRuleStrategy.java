package com.wishare.finance.domains.voucher.support.strategyrule.core;


import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;


public interface SystemRuleStrategy <C extends SystemMethodStrategyCommand>{
    /**
     * 系统类型
     *
     * @return 系统类型
     */
    SystemMethodEnum eventType();

    /**
     * 执行策略
     */
    void manualExecute(C command, BillRule billRule);
}
