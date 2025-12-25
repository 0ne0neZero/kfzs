package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;

/**
 * 即时触发推凭模式策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/8
 */
public abstract class InstantVoucherStrategy<C extends InstantVoucherStrategyCommand> extends AbstractVoucherStrategy<C> {

    public InstantVoucherStrategy(VoucherEventTypeEnum eventType) {
        super(PushMode.即时, eventType);
    }

}
