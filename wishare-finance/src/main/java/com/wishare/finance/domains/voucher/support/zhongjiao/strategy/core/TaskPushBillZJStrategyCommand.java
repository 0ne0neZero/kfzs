package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;

import lombok.Getter;
import lombok.Setter;

/**
 * 定时任务凭证规则运行命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Getter
@Setter
public class TaskPushBillZJStrategyCommand implements BillZJStrategyCommand {

    /**
     * 凭证规则id
     */
    private Long billRuleId;

    public TaskPushBillZJStrategyCommand(Long billRuleIdId) {
        this.billRuleId = billRuleIdId;
    }
}
