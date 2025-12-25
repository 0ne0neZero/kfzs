package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 银行到账场景
 * 以银行对账单为维度
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/27
 */
@Service
public class BankManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public BankManualVoucherStrategy() {
        super(VoucherEventTypeEnum.银行到账);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getBankSettleBillList(command,conditions);
    }
}
