package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;



import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;

import java.util.List;

/**
 * 手动推凭
 * @param <C>
 */
public abstract class AutoZJBillStrategy<C extends AutoBillZJStrategyCommand> extends AbstractPushBillZJStrategy<C> {

    public AutoZJBillStrategy(ZJTriggerEventBillTypeEnum eventType) {
        super(ZJPushMethod.自动推送, eventType);
    }

    @Override
    public List<PushZJBusinessBillForSettlement> autoExecute(AutoBillZJStrategyCommand command) {
        return doAutoExecute(command);
    }
}
