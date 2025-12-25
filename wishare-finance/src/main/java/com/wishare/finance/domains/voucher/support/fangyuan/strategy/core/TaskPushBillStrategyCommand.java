package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;

import com.wishare.finance.domains.voucher.strategy.core.VoucherStrategyCommand;
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
public class TaskPushBillStrategyCommand implements BillStrategyCommand {

    /**
     * 凭证规则id
     */
    private Long billRuleId;

    public TaskPushBillStrategyCommand(Long billRuleIdId) {
        this.billRuleId = billRuleIdId;
    }
}
