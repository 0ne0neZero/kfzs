package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankPrivateF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessBankPublicF;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.BusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.CustomerExcludeBIllType;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BpmPayeeAssisteItemStrategy implements BpmVoucherAssisteItemStrategy {
    @Override
    public VoucherTemplateBpmAssisteItemEnum type() {
        return VoucherTemplateBpmAssisteItemEnum.收款人;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        AssisteItemOBV itemOBV = new AssisteItemOBV();
        itemOBV.setAscName(entryAssisteItem.getAscName());
        itemOBV.setAscCode(entryAssisteItem.getAscCode());
        List<ProcessBankPublicF> publicPayees = accountBook.getPublicPayees();
        List<ProcessBankPrivateF> privatePayees = accountBook.getPrivatePayees();
        if (CollectionUtils.isNotEmpty(publicPayees)) {
            ProcessBankPublicF processBankPublicF = publicPayees.get(index);
            itemOBV.setCode(getPayeeCode(processBankPublicF.getPayeeCode()));
            itemOBV.setName(processBankPublicF.getPayee());
            handleCustomerInfo(entryAssisteItem, processBankPublicF.getCreditCode(), itemOBV, businessProcessHandleF.getBillTypeCode());
        } else if (CollectionUtils.isNotEmpty(privatePayees)) {
            ProcessBankPrivateF processBankPrivateF = privatePayees.get(index);
            itemOBV.setCode(getPayeeCode(processBankPrivateF.getPayeeCode()));
            itemOBV.setName(processBankPrivateF.getPayee());
            handleCustomerInfo(entryAssisteItem, processBankPrivateF.getCreditCode(), itemOBV, businessProcessHandleF.getBillTypeCode());
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
