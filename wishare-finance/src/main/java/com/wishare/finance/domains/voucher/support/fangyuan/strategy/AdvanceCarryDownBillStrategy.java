package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**\
 * 预收结转
 */
@Service
public class AdvanceCarryDownBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {


    public AdvanceCarryDownBillStrategy() {
        super(TriggerEventBillTypeEnum.预收结转);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillFacade.getAdvanceCarryDownBillList(conditions,communityIds);
    }

}
