package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收入退款场景
 * @author dongpeng
 * @version 1.0
 * @since 2023/5/18
 */
@Service
public class RefundManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public RefundManualVoucherStrategy() {
        super(VoucherEventTypeEnum.收入退款);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getRefundBillList(command,conditions);
    }
}
