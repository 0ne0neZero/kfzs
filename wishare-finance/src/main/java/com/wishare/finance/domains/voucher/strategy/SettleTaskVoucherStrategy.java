package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.TaskVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.TaskVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/25
 */
@Service
public class SettleTaskVoucherStrategy extends TaskVoucherStrategy<TaskVoucherStrategyCommand> {

    public SettleTaskVoucherStrategy() {
        super(VoucherEventTypeEnum.收款结算);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(TaskVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getSettleBillList(command, conditions);
    }

    @Override
    protected void modifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        voucherFacade.settleModifyInferenceStatus(voucherBusinessDetails);
    }
}
