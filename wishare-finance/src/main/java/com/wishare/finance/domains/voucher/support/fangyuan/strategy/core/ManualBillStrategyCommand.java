package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;


import lombok.Getter;
import lombok.Setter;

/**
 * 手动凭证规则运行命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Getter
@Setter
public class ManualBillStrategyCommand implements BillStrategyCommand {

    /**
     * 推单规则id
     */
    private Long billRuleId;

    public ManualBillStrategyCommand(Long billRuleId) {
        this.billRuleId = billRuleId;
    }
}
