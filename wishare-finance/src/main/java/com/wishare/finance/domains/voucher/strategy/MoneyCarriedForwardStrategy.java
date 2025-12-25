package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/25
 */
@Service
public class MoneyCarriedForwardStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public MoneyCarriedForwardStrategy() {
        super(VoucherEventTypeEnum.款项结转);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.moneyCarriedForwardList(command, conditions);
    }


}
