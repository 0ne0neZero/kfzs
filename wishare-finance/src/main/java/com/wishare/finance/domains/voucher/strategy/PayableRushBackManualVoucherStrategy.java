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
 * 手动触发应付计提冲回即时触发策略
 * @author dongpeng
 * @date 2023/7/14 15:11
 */
@Service
public class PayableRushBackManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public PayableRushBackManualVoucherStrategy(VoucherRuleRepository voucherRuleRepository) {
        super(VoucherEventTypeEnum.应付计提冲回);
        this.voucherRuleRepository = voucherRuleRepository;
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getPayableBillList(command,conditions,VoucherEventTypeEnum.应付计提冲回.getCode());
    }

    @Override
    protected void modifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        voucherFacade.payableModifyInferenceStatus(voucherBusinessDetails);
    }
}
