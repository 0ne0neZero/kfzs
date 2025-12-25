package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRepository;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手动触发付款结算即时触发策略
 * @author dongpeng
 * @date 2023/5/26 15:32
 */
@Service
public class PayManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public PayManualVoucherStrategy(VoucherRuleRepository voucherRuleRepository) {
        super(VoucherEventTypeEnum.付款结算);
        this.voucherRuleRepository = voucherRuleRepository;
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getPayBillList(command,conditions);
    }
}
