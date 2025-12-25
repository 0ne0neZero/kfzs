package com.wishare.finance.domains.voucher.strategy.logic;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author szh
 * @date 2024/7/4 9:56
 */

@Component
public class AdjustAmountLogicStrategy implements LogicStrategy {

    @Override
    public VoucherTemplateLogicCodeEnum logicCode() {
        return VoucherTemplateLogicCodeEnum.账单调整金额;
    }

    @Override
    public BigDecimal logicValue(VoucherBusinessBill businessBill) {
        return AmountUtils.longToDecimal(businessBill.getAdjustAmount());
    }

}

