package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 未收款开票
 */
@Service
public class UncollectedInvoicBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {

    public UncollectedInvoicBillStrategy() {
        super(TriggerEventBillTypeEnum.未收款开票);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillFacade.getReceivableBillList(conditions,communityIds);
    }
}
