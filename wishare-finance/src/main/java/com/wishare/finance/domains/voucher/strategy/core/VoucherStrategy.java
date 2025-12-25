package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;

/**
 * 凭证策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/22
 */
public interface VoucherStrategy<C extends VoucherStrategyCommand> {

    /**
     * 事件类型
     *
     * @return 事件类型
     */
    VoucherEventTypeEnum eventType();

    /**
     * 推凭模式
     *
     * @return 推凭模式
     */
    PushMode mode();

    /**
     * 执行策略
     */
    VoucherRuleRecord execute(C command);

}
