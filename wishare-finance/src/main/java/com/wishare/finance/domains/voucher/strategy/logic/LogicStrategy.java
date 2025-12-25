package com.wishare.finance.domains.voucher.strategy.logic;

import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;

import java.math.BigDecimal;

/**
 * 逻辑策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/12
 */
public interface LogicStrategy {

    /**
     * 凭证逻辑类型
     * @return
     */
    VoucherTemplateLogicCodeEnum logicCode();

    /**
     * 逻辑值
     *
     * @param businessBill 凭证业务单据信息
     * @return 逻辑值 (金额单位：分）
     */
    BigDecimal logicValue(VoucherBusinessBill businessBill);


}
