package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手动触发收款结算即时触发策略
 * 同时支持银联到账(按结算明细算)
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/25
 */
@Service
public class SettleManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {

    public SettleManualVoucherStrategy() {
        super(VoucherEventTypeEnum.收款结算);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getSettleBillList(command,conditions);
    }

    @Override
    protected void modifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        voucherFacade.settleModifyInferenceStatus(voucherBusinessDetails);
    }
}
