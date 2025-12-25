package com.wishare.finance.domains.voucher.strategy;

import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收费系统收款-银行转账、汇款、支票
 * 即流水认领场景
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/28
 */
@Service
public class ClaimManualVoucherStrategy extends ManualVoucherStrategy<ManualVoucherStrategyCommand> {


    public ClaimManualVoucherStrategy() {
        super(VoucherEventTypeEnum.结算认领);
    }

    @Override
    public List<VoucherBusinessBill> businessBills(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        return voucherFacade.getClaimBillList(command,conditions);
    }


}
