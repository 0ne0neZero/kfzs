package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预收结转特殊场景
 */
@Service
public class AdvanceCarryDownTwoBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {


    public AdvanceCarryDownTwoBillStrategy() {
        super(TriggerEventBillTypeEnum.预收结转按钮触发);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillFacade.getAdvanceCarryDownTwoBillList(conditions,communityIds);
    }
}
