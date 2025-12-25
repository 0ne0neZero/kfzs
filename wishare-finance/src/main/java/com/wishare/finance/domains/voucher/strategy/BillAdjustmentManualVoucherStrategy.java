package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账单调整-单价面积调整
 * @author zmh
 * @version 1.0
 * @since 2023/5/24
 */
@Service
public class BillAdjustmentManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {


    public BillAdjustmentManualVoucherStrategy() {
        super(VoucherEventTypeEnum.账单调整);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {

        return voucherFacade.getReceivableBillList(command,conditions);
    }


}
