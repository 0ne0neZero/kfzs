package com.wishare.finance.domains.voucher.support.fangyuan.strategy;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.ManualBillStrategyCommand;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.PushBusinessBill;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收款结转（临时转应收）
 */
@Service
public class CollectionTransferBillStrategy extends ManualBillStrategy<ManualBillStrategyCommand> {
    public CollectionTransferBillStrategy() {
        super(TriggerEventBillTypeEnum.收款结转);
    }

    @Override
    public List<PushBusinessBill> businessBills(ManualBillStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillFacade.getCollectionTransferBillList(conditions, communityIds);
    }

//    @Override
//    protected void modifyInferenceStatus(List<PushBusinessBill> businessBills) {
//        if (CollectionUtils.isNotEmpty(businessBills)) {
//            pushBillFacade.getModifyInferenceStatus(businessBills);
//        }
//    }
}
