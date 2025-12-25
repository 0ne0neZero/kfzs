package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.apps.model.yuanyang.fo.*;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BpmPayerAccountAssisteItemStrategy implements BpmVoucherAssisteItemStrategy {
    @Override
    public VoucherTemplateBpmAssisteItemEnum type() {
        return VoucherTemplateBpmAssisteItemEnum.付款账号;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        AssisteItemOBV itemOBV = new AssisteItemOBV();
        itemOBV.setAscName(entryAssisteItem.getAscName());
        itemOBV.setAscCode(entryAssisteItem.getAscCode());
        List<ProcessBankPayInfoF> payBankInfos = accountBook.getPayBankInfos();
        ProcessBankPayInfoF processBankPayInfoF = payBankInfos.get(index);
        itemOBV.setCode(processBankPayInfoF.getPayerAccount());
        itemOBV.setName(processBankPayInfoF.getPayerAccount());
        itemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(entryAssisteItem.getAscCode()).getCode());
        return itemOBV;
    }
}
