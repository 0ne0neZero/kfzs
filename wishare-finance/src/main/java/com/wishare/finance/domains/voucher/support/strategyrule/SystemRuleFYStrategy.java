package com.wishare.finance.domains.voucher.support.strategyrule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBillStrategyContext;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushMethod;
import com.wishare.finance.domains.voucher.support.strategyrule.core.AbstractSystemRuleStrategy;
import com.wishare.finance.domains.voucher.support.strategyrule.core.SystemMethodEnum;
import com.wishare.finance.domains.voucher.support.strategyrule.core.SystemMethodStrategyCommand;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ManualBillZJStrategyCommand;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushBillZJStrategyContext;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ZJPushMethod;
import org.springframework.stereotype.Service;


@Service
public class SystemRuleFYStrategy<C extends SystemMethodStrategyCommand>extends AbstractSystemRuleStrategy<C> {
    public SystemRuleFYStrategy() {
        super(SystemMethodEnum.方圆);
    }

    @Override
    public void manualExecute(SystemMethodStrategyCommand command, BillRule billRule) {
        PushBillStrategyContext.getStrategy(billRule.getEventType(), PushMethod.手动推送).execute(new ManualBillStrategyCommand(billRule.getId()));
    }
}
