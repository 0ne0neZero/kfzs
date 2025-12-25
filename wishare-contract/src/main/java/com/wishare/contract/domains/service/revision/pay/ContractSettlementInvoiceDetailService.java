package com.wishare.contract.domains.service.revision.pay;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceDetailF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementInvoiceSaveF;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementInvoiceDetailE;
import com.wishare.contract.domains.enums.revision.VerifyStatusEnum;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementInvoiceDetailMapper;
import com.wishare.starter.Global;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContractSettlementInvoiceDetailService extends ServiceImpl<ContractSettlementInvoiceDetailMapper, ContractSettlementInvoiceDetailE> {

    public void batchSaveDetails(ContractSettlementInvoiceSaveF contractSettlementInvoiceSaveF) {
        List<ContractSettlementInvoiceDetailF> invoiceDetails = contractSettlementInvoiceSaveF.getInvoiceDetails();
        if (CollectionUtils.isEmpty(invoiceDetails)) {
            return;
        }
        //先批量删除
        this.remove(Wrappers.<ContractSettlementInvoiceDetailE>lambdaQuery()
                .eq(ContractSettlementInvoiceDetailE::getSettlementId, contractSettlementInvoiceSaveF.getSettlementId()));
        //再保存
        List<ContractSettlementInvoiceDetailE> results = invoiceDetails.stream()
                .map(detail -> {
                    ContractSettlementInvoiceDetailE res = Global.mapperFacade.map(detail, ContractSettlementInvoiceDetailE.class);
                    res.setSettlementId(contractSettlementInvoiceSaveF.getSettlementId());
                    res.setVerifyStatus(ObjectUtils.isEmpty(detail.getVerifyStatus()) ? VerifyStatusEnum.PASS.getCode() : detail.getVerifyStatus());
                    res.setTaxRate(this.calculateTaxRatio(detail.getInvoiceTaxAmount(), detail.getTaxAmount()));
                    return res;
                }).collect(Collectors.toList());
        this.saveBatch(results);
    }

    /**
     * 计算税率公式：含税金额/(含税金额-税额)-1
     * @param invoiceTaxAmount 收票含税金额
     * @param taxAmount 税额
     * @return 计算结果
     */
    public static BigDecimal calculateTaxRatio(BigDecimal invoiceTaxAmount,
                                               BigDecimal taxAmount) {
        BigDecimal result = BigDecimal.ZERO;
        if (invoiceTaxAmount == null || taxAmount == null) {
            return result;
        }
        // 计算分母 (含税金额 - 税额)
        BigDecimal denominator = invoiceTaxAmount.subtract(taxAmount);
        // 计算比率 [含税金额/(含税金额-税额) - 1]
        return invoiceTaxAmount
                .divide(invoiceTaxAmount.subtract(taxAmount), 6, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE)
                .setScale(6, RoundingMode.HALF_UP); // 最终结果四舍五入
    }

}
