package com.wishare.finance.domains.voucher.strategy.logic;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 账单金额逻辑策略
 *
 * @author dongpeng
 * @version 1.0
 * @since 2023/5/20
 */
@Component
public class RefundLogicStrategy implements LogicStrategy{
    @Override
    public VoucherTemplateLogicCodeEnum logicCode() {
        return VoucherTemplateLogicCodeEnum.退款金额;
    }

    @Override
    public BigDecimal logicValue(VoucherBusinessBill businessBill) {
        return AmountUtils.longToDecimal(businessBill.getRefundAmount());
    }
}
