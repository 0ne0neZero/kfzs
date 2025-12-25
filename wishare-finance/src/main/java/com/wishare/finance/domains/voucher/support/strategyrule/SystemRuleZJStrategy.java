package com.wishare.finance.domains.voucher.support.strategyrule;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.strategyrule.core.AbstractSystemRuleStrategy;
import com.wishare.finance.domains.voucher.support.strategyrule.core.SystemMethodEnum;
import com.wishare.finance.domains.voucher.support.strategyrule.core.SystemMethodStrategyCommand;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ManualBillZJStrategyCommand;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushBillZJStrategyContext;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ZJPushMethod;
import org.springframework.stereotype.Service;


@Service
public class SystemRuleZJStrategy<C extends SystemMethodStrategyCommand>extends AbstractSystemRuleStrategy<C> {
    public SystemRuleZJStrategy() {
        super(SystemMethodEnum.中交);
    }

    @Override
    public void manualExecute(SystemMethodStrategyCommand command, BillRule billRule) {
        PushBillZJStrategyContext.getStrategy(billRule.getEventType(), ZJPushMethod.手动推送).execute(new ManualBillZJStrategyCommand(billRule.getId()));
    }
}
