package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 款项调整场景
 */
@Service
public class PaymentAdjustmentStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {

    public PaymentAdjustmentStrategy() {
        super(TriggerEventBillTypeEnum.款项调整);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillFacade.getPaymentAdjustmentBusinessBills(conditions, communityIds);
    }
}
