package com.wishare.finance.domains.voucher.support.zhongjiao.strategy;

import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleConditionOBV;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.AutoBillZJStrategyCommand;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.AutoZJBillStrategy;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对下结算实签
 */
@Service
public class PayCostConfirmBilDxlZJStrategy extends AutoZJBillStrategy<AutoBillZJStrategyCommand> {

    public PayCostConfirmBilDxlZJStrategy() {
        super(ZJTriggerEventBillTypeEnum.对下结算实签);
    }

    @Override
    public List<PushZJBusinessBill> businessZJBills(AutoBillZJStrategyCommand command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return List.of();
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return List.of();
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForPayIncome(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds) {
        return List.of();
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
        if (businessBill.getTaxRate() == null && StringUtils.isBlank(businessBill.getTaxRateStr())) {
            return;
        }
        Long settleAmount = businessBill.getReceivableAmount();
        countAmountForSettlement(businessBill, settleAmount);
    }

    @Override
    public void execute(AutoBillZJStrategyCommand command) {

    }


    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> billIdList) {
        return pushBillZJFacade.getReceivableBillZJListForSettlementDx(conditions,communityIds,billIdList);
    }

    @Override
    public void updateBillRelatedStatus(List<Long> billIdList, List<String> communityIds) {
        if (CollectionUtils.isEmpty(billIdList) || CollectionUtils.isEmpty(communityIds)){
            return;
        }
        pushBillZJFacade.updateRelatedBillStatusOnPayCost(billIdList,communityIds);
    }

    @Override
    protected List<PushZJBusinessBillForSettlement> businessZJBillsForSettlementForReverse(List<String> communityIds,
                                                                                           List<String> billIdList) {
        List<PushZJBusinessBillForSettlement> detailList =
                pushBillZJFacade.getReceivableBillZJListForSettlementOnReverse(null, communityIds, billIdList, 3);
        if (CollectionUtils.isEmpty(detailList)){
            return null;
        }
        return detailList;
    }

    @Override
    public List<PushZJBusinessBillForSettlement> businessZJBillsForPaymentApplicationForm(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> settlementIdList) {
        return List.of();
    }
}
