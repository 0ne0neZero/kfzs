package com.wishare.finance.domains.voucher.strategy.bpm.logic;

import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessChargeDetailF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessPayeeF;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BpmPayeeAmountLogicStrategy implements BpmLogicStrategy {
    @Override
    public VoucherTemplateBpmLogicCodeEnum logicCode() {
        return VoucherTemplateBpmLogicCodeEnum.收款金额;
    }

    @Override
    public BigDecimal logicValue(ProcessAccountBookF accountBook, int index,String bpmBillTypeCode) {
        List<ProcessPayeeF> payees = accountBook.getPayees();
        if (CollectionUtils.isNotEmpty(payees)) {
            ProcessPayeeF processPayeeF = payees.get(index);
            return AmountUtils.longToDecimal(processPayeeF.getReceivedAmount());
        } else {
            return BigDecimal.ZERO;
        }
    }
}
