package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;


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
public class ManualBillZJStrategyCommand implements BillZJStrategyCommand {

    /**
     * 推单规则id
     */
    private Long billRuleId;

    public ManualBillZJStrategyCommand(Long billRuleId) {
        this.billRuleId = billRuleId;
    }
}
