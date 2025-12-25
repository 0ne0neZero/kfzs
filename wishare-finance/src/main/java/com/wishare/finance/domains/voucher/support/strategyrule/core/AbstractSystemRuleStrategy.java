package com.wishare.finance.domains.voucher.support.strategyrule.core;

import com.wishare.finance.domains.voucher.strategy.core.AbstractVoucherStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushMethod;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ZJPushMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


public abstract class AbstractSystemRuleStrategy<C extends SystemMethodStrategyCommand> implements SystemRuleStrategy<C> {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoucherStrategy.class);

    /**
     * 事件类型
     */
    protected SystemMethodEnum eventType;

    public AbstractSystemRuleStrategy(SystemMethodEnum eventType) {
        this.eventType = eventType;
    }

    @Override
    public SystemMethodEnum eventType() {
        return eventType;
    }


}
