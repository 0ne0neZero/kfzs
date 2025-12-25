package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.apps.model.yuanyang.fo.*;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BpmPayeeAccountAssisteItemStrategy implements BpmVoucherAssisteItemStrategy {
    @Override
    public VoucherTemplateBpmAssisteItemEnum type() {
        return VoucherTemplateBpmAssisteItemEnum.收款账号;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        AssisteItemOBV itemOBV = new AssisteItemOBV();
        itemOBV.setAscName(entryAssisteItem.getAscName());
        itemOBV.setAscCode(entryAssisteItem.getAscCode());
        List<ProcessPayeeF> payees = accountBook.getPayees();
        ProcessPayeeF processPayeeF = payees.get(index);
        itemOBV.setCode(processPayeeF.getPayeeAccount());
        itemOBV.setName(processPayeeF.getPayeeAccount());
        itemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(entryAssisteItem.getAscCode()).getCode());
        return itemOBV;
    }
}
