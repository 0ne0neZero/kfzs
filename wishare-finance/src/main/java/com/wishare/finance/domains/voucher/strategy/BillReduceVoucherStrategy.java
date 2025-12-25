package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/5/18 13:39
 * @descrption: 账单减免场景
 */
@Service
public class BillReduceVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public BillReduceVoucherStrategy() {
        super(VoucherEventTypeEnum.账单减免);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getReducedBillList(command,conditions);
    }
}
