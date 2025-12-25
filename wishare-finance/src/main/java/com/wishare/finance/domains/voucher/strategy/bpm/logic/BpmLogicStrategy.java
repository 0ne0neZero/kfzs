package com.wishare.finance.domains.voucher.strategy.bpm.logic;

import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;

import java.math.BigDecimal;

public interface BpmLogicStrategy {

    /**
     * 凭证逻辑类型
     * @return
     */
    VoucherTemplateBpmLogicCodeEnum logicCode();

    /**
     * 逻辑值
     *
     * @param accountBook 凭证账套信息
     * @return 逻辑值 (金额单位：分）
     */
    BigDecimal logicValue(ProcessAccountBookF accountBook, int index,String bpmBillTypeCode);

}
