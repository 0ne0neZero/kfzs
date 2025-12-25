package com.wishare.finance.domains.voucher.strategy.bpm.logic;

import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessChargeDetailF;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BpmOriginalAmountLogicStrategy implements BpmLogicStrategy {

    @Override
    public VoucherTemplateBpmLogicCodeEnum logicCode() {
        return VoucherTemplateBpmLogicCodeEnum.原币金额;
    }

    @Override
    public BigDecimal logicValue(ProcessAccountBookF accountBook, int index,String bpmBillTypeCode) {
        List<ProcessChargeDetailF> chargeDetails = accountBook.getChargeDetails();
        ProcessChargeDetailF chargeDetailF = chargeDetails.get(index);
        return AmountUtils.longToDecimal(chargeDetailF.getAmount());
    }
}
