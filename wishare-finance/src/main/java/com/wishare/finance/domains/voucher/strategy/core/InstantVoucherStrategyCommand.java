package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.voucher.consts.enums.VoucherBusinessBillTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 即时凭证规则运行命令
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
@Getter
@Setter
public class InstantVoucherStrategyCommand implements VoucherStrategyCommand {

    /**
     * 业务单据id
     */
    private Long businessBillId;

    /**
     * 业务单据类型
     */
    private VoucherBusinessBillTypeEnum businessBillType;

    @Override
    public Long getRuleId() {
        return null;
    }
}
