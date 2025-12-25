package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;


import com.wishare.finance.apps.model.yuanyang.fo.*;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.BusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BpmOrgAssisteItemStrategy implements BpmVoucherAssisteItemStrategy {
    @Override
    public VoucherTemplateBpmAssisteItemEnum type() {
        return VoucherTemplateBpmAssisteItemEnum.部门;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        AssisteItemOBV itemOBV = new AssisteItemOBV();
        itemOBV.setAscName(entryAssisteItem.getAscName());
        itemOBV.setAscCode(entryAssisteItem.getAscCode());
        itemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(entryAssisteItem.getAscCode()).getCode());

        String name ;String code;
        if (BusinessBillTypeEnum.WAGE_SET.contains(businessProcessHandleF.getBillTypeCode())){
            WageTableF f = accountBook.getWageTableInfo().get(index);
            code = f.getOrgCode();
            name = f.getOrgName();
        }else if (BusinessBillTypeEnum.SOCIAL_SET.contains(businessProcessHandleF.getBillTypeCode())){
            SocialSecurityTableInfoF f = accountBook.getSocialSecurityTableInfo().get(index);
            code = f.getOrgCode();
            name = f.getOrgName();
        }else {
            List<ProcessChargeDetailF> chargeDetails = accountBook.getChargeDetails();
            ProcessChargeDetailF chargeDetailF = chargeDetails.get(index);
            code = (chargeDetailF.getOrgCode());
            name = (chargeDetailF.getOrgName());
        }
        itemOBV.setCode(code);
        itemOBV.setName(name);

        return itemOBV;
    }
}
