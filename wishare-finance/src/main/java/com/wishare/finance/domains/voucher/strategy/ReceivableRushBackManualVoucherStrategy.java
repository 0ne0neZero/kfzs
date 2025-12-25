package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRepository;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手动触发应收计提冲回即时触发策略
 *
 * @author dongpeng
 * @version 1.0
 * @since 2023/7/14 15:35
 */
@Service
public class ReceivableRushBackManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public ReceivableRushBackManualVoucherStrategy(VoucherRuleRepository voucherRuleRepository) {
        super(VoucherEventTypeEnum.应收计提冲回);
        this.voucherRuleRepository = voucherRuleRepository;
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getReceivableList(conditions,command,VoucherEventTypeEnum.应收计提冲回.getCode());
    }

    @Override
    protected void modifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        voucherFacade.receivableModifyInferenceStatus(voucherBusinessDetails);
    }
}
