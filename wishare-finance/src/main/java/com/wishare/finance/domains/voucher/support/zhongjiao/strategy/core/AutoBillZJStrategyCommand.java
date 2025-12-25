package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;


import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import lombok.Getter;
import lombok.Setter;

/**
 * 手动凭证规则运行命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Getter
@Setter
public class AutoBillZJStrategyCommand implements BillZJStrategyCommand {

    /**
     * 推单规则id
     */
    private VoucherBillGenerateForContractSettlementContext voucherBillGenerateForContractSettlementContext;

    public AutoBillZJStrategyCommand(VoucherBillGenerateForContractSettlementContext voucherBillGenerateForContractSettlementContext) {
        this.voucherBillGenerateForContractSettlementContext = voucherBillGenerateForContractSettlementContext;
    }
}
