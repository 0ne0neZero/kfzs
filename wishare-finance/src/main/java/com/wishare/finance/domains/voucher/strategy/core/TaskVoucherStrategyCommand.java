package com.wishare.finance.domains.voucher.strategy.core;

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
public class TaskVoucherStrategyCommand implements VoucherStrategyCommand {

    /**
     * 凭证规则id
     */
    private Long voucherRuleId;

    public TaskVoucherStrategyCommand(Long voucherRuleId) {
        this.voucherRuleId = voucherRuleId;
    }

    @Override
    public Long getRuleId() {
        return getVoucherRuleId();
    }
}
