package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessPayerF;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BpmPayerAssisteItemStrategy implements BpmVoucherAssisteItemStrategy {
    @Override
    public VoucherTemplateBpmAssisteItemEnum type() {
        return VoucherTemplateBpmAssisteItemEnum.付款方;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        AssisteItemOBV itemOBV = new AssisteItemOBV();
        itemOBV.setAscName(entryAssisteItem.getAscName());
        itemOBV.setAscCode(entryAssisteItem.getAscCode());
        List<ProcessPayerF> payers = accountBook.getPayers();
        if (CollectionUtils.isNotEmpty(payers)) {
            ProcessPayerF processPayerF = payers.get(index);
            itemOBV.setCode(getPayeeCode(processPayerF.getPayerCode()));
            itemOBV.setName(processPayerF.getPayerAccount());
            handleCustomerInfo(entryAssisteItem, processPayerF.getCreditCode(), itemOBV, businessProcessHandleF.getBillTypeCode());
        }
        itemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(entryAssisteItem.getAscCode()).getCode());
        return itemOBV;
    }

    public String getPayeeCode(String payeeCode) {
        if (StringUtils.isNotBlank(payeeCode)) {
            if (payeeCode.lastIndexOf("-") != -1) {
                return payeeCode.substring(0, payeeCode.lastIndexOf("-"));
            }
        }
        return payeeCode;
    }
}
