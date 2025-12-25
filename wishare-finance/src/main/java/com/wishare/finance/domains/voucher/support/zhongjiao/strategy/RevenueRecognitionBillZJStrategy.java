package com.wishare.finance.domains.voucher.support.zhongjiao.strategy;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ManualBillZJStrategyCommand;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.ManualZJBillStrategy;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收入确认
 */
@Service
public class RevenueRecognitionBillZJStrategy extends ManualZJBillStrategy<ManualBillZJStrategyCommand> {

    public RevenueRecognitionBillZJStrategy() {
        super(ZJTriggerEventBillTypeEnum.收入确认);
    }

    @Override
    public List<PushZJBusinessBill> businessZJBills(ManualBillZJStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillZJFacade.getReceivableBillZJList(conditions,communityIds);
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return pushBillZJFacade.getReceivableBillZJListForSettlement(conditions,communityIds,null,3);
    }

    @Override
    public void updateBillRelatedStatus(List<Long> billIdList, List<String> communityIds) {
        // 不用处理
    }

    @Override
    protected List<PushZJBusinessBillForSettlement> businessZJBillsForSettlementForReverse(List<String> newArrayList, List<String> billIdList) {
        return null;
    }

    @Override
    public void amount(PushZJBusinessBill businessBill) {
        if (businessBill.getTaxRate() == null) {
            return;
        }
        Long settleAmount = businessBill.getReceivableAmount();
        countAmount(businessBill, settleAmount);
    }


    @Override
    public void amountForSettlement(PushZJBusinessBillForSettlement businessBill) {
        // 不用处理 - 老收入确认单
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> billIdList) {
        return List.of();
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForPayIncome(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return List.of();
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForPaymentApplicationForm(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> settlementIdList) {
        return List.of();
    }
}
