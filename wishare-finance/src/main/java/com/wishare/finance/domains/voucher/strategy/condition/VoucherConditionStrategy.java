package com.wishare.finance.domains.voucher.strategy.condition;

import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;

/**
 * 凭证规则条件策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/13
 */
public interface VoucherConditionStrategy {


    void select(VoucherRuleConditionOBV condition);

}
