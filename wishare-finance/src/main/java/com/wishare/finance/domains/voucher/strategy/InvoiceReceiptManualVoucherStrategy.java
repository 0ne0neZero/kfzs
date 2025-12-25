package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/5/24 13:45
 */
@Service
public class InvoiceReceiptManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public InvoiceReceiptManualVoucherStrategy() {
        super(VoucherEventTypeEnum.账单开票);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getInvoiceReceiptBillList(command,conditions);
    }
}
