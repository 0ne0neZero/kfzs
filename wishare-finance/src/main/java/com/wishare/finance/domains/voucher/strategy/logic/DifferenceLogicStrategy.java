package com.wishare.finance.domains.voucher.strategy.logic;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 差额逻辑策略
 *
 * @author zmh
 * @version 1.0
 * @since 2023/6/16
 */
@Component
public class DifferenceLogicStrategy implements LogicStrategy{
    @Override
    public VoucherTemplateLogicCodeEnum logicCode() {
        return VoucherTemplateLogicCodeEnum.差额;
    }

    @Override
    public BigDecimal logicValue(VoucherBusinessBill businessBill) {
        return AmountUtils.longToDecimal(businessBill.getActualPayAmount());
    }
}
