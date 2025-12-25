package com.wishare.finance.domains.voucher.support.zhongjiao.strategy;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收入确认实签
 */
@Service
public class PaymentApplicationFormStrategy extends  AutoZJBillStrategy<AutoBillZJStrategyCommand>  {

    public PaymentApplicationFormStrategy() {
        super(ZJTriggerEventBillTypeEnum.支付申请);
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return List.of();
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForPayIncome(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
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
        if (businessBill.getTaxRate() == null) {
            return;
        }
        Long settleAmount = businessBill.getReceivableAmount();
        countAmountForSettlement(businessBill, settleAmount);
    }



    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> billIdList) {
        return List.of();
    }

    @Override
    public void updateBillRelatedStatus(List<Long> billIdList, List<String> communityIds) {
        if (CollectionUtils.isEmpty(billIdList) || CollectionUtils.isEmpty(communityIds)){
            return;
        }
        pushBillZJFacade.updatePayAppVoucherPushingStatus(billIdList,communityIds);
    }

    @Override
    protected List<PushZJBusinessBillForSettlement> businessZJBillsForSettlementForReverse(List<String> newArrayList, List<String> billIdList) {
        return null;
    }


    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForPaymentApplicationForm(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> settlementIdList) {
        return pushBillZJFacade.businessZJBillsForPaymentApplicationForm(conditions,communityIds,settlementIdList);
    }

    @Override
    public List<PushZJBusinessBill> businessZJBills(AutoBillZJStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return List.of();
    }

    @Override
    public void execute(AutoBillZJStrategyCommand command) {

    }
}
