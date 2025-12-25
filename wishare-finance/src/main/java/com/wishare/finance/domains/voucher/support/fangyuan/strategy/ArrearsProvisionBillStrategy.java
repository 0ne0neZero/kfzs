package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.apache.commons.collections4.CollectionUtils;
import org.jaxen.function.IdFunction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 欠费计提触发事件
 */
@Service
public class ArrearsProvisionBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {
    public ArrearsProvisionBillStrategy() {
        super(TriggerEventBillTypeEnum.欠费计提);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions,List<String> communityIds) {
        return pushBillFacade.getArrearsProvisionBillList(conditions,communityIds);
    }
    @Override
    protected void modifyInferenceStatus(List<PushBusinessBill> businessBills) {
        if (CollectionUtils.isNotEmpty(businessBills)){
            pushBillFacade.updateReceivableBillInferenceStater(businessBills);
        }
    }
}
