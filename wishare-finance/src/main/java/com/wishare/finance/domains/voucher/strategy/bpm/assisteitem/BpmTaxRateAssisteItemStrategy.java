package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessChargeDetailF;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxRateE;
import com.wishare.finance.domains.configure.chargeitem.service.TaxRateDomainService;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.support.yuanyang.YuanYangTaxRateProperties;
import com.wishare.finance.infrastructure.utils.NumberUtil;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BpmTaxRateAssisteItemStrategy implements BpmVoucherAssisteItemStrategy {

    private final TaxRateDomainService taxRateDomainService;
    private final YuanYangTaxRateProperties yuanYangTaxRateProperties;
    @Override
    public VoucherTemplateBpmAssisteItemEnum type() {
        return VoucherTemplateBpmAssisteItemEnum.增值税税率;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        AssisteItemOBV itemOBV = new AssisteItemOBV();
        itemOBV.setAscName(entryAssisteItem.getAscName());
        itemOBV.setAscCode(entryAssisteItem.getAscCode());
        List<ProcessChargeDetailF> chargeDetails = accountBook.getChargeDetails();
        ProcessChargeDetailF chargeDetailF = chargeDetails.get(index);
        String taxRateCode;
        //获取增值税税率
        TaxRateE taxRate = taxRateDomainService.getByCategoryRate(yuanYangTaxRateProperties.getTaxCategoryCode(), chargeDetailF.getTaxRate());
        ErrorAssertUtil.notNullThrow403(taxRate, ErrorMessage.TAX_RATE_NO_EXISTS);
        taxRateCode = taxRate.getCode();
        itemOBV.setCode(taxRateCode);
        itemOBV.setName(chargeDetailF.getTaxRate().stripTrailingZeros().toEngineeringString() + "%");
        itemOBV.setType(AssisteItemTypeEnum.valueOfByAscCode(entryAssisteItem.getAscCode()).getCode());
        return itemOBV;
    }

}
