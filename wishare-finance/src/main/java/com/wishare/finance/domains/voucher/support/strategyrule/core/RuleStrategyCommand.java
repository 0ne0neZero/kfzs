package com.wishare.finance.domains.voucher.support.strategyrule.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleStrategyCommand implements SystemMethodStrategyCommand {

    /**
     * 规则id
     */
    private Long billRuleId;

    public RuleStrategyCommand(Long billRuleId) {
        this.billRuleId = billRuleId;
    }
}