package com.wishare.contract.apps.service.contractset;

import com.alibaba.fastjson.JSONObject;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.InvoiceBillAmount;
import com.wishare.contract.apps.remote.fo.ReceiptBatchRf;
import com.wishare.contract.apps.remote.fo.ReceiptDetailRf;
import com.wishare.contract.apps.remote.vo.ReceiptDetailRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.interfaces.ApiBase;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractBondReceiptDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractBondReceiptDetailPageF;
import com.wishare.contract.domains.service.contractset.ContractBondReceiptDetailService;
import com.wishare.contract.domains.vo.contractset.ContractBondReceiptDetailV;
import com.wishare.contract.apps.fo.contractset.ContractBondReceiptDetailF;
import com.wishare.contract.apps.fo.contractset.ContractBondReceiptDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBondReceiptDetailUpdateF;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

/**
* <p>
* 保证金收据明细表
* </p>
*
* @author ljx
* @since 2022-12-12
*/
@Service
@Slf4j
public class ContractBondReceiptDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondReceiptDetailService contractBondReceiptDetailService;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    public ContractBondReceiptDetailE saveBondReceiptDetail(ContractBondReceiptDetailF receiptDetailF) {
        return contractBondReceiptDetailService.saveBondReceiptDetail(receiptDetailF, curIdentityInfo());
    }

    public List<ContractBondReceiptDetailV> listByBondPlanId(Long bondPlanId) {
        return contractBondReceiptDetailService.listByBondPlanId(bondPlanId);
    }
    public Long receiptBatch(Long billId, BigDecimal invoiceAmount, String subUnitId) {
        long invoiceAmountLong = invoiceAmount.multiply(new BigDecimal("100")).longValue();
        ReceiptBatchRf receiptBatchRf = new ReceiptBatchRf();
        receiptBatchRf.setType(6);
        receiptBatchRf.setClerk(curIdentityInfo().getUserName());
        receiptBatchRf.setSysSource(2);
        receiptBatchRf.setInvSource(1);
        receiptBatchRf.setPriceTaxAmount(invoiceAmountLong);
        receiptBatchRf.setBillIds(Collections.singletonList(billId));
        InvoiceBillAmount invoiceBillAmount = new InvoiceBillAmount();
        invoiceBillAmount.setBillId(billId);
        invoiceBillAmount.setInvoiceAmount(invoiceAmountLong);
        receiptBatchRf.setInvoiceBillAmounts(Collections.singletonList(invoiceBillAmount));
        receiptBatchRf.setBillType(3);
        receiptBatchRf.setPushMode(Collections.singletonList(-1));
        receiptBatchRf.setSupCpUnitId(subUnitId);
        log.info("开收据：" + JSONObject.toJSONString(receiptBatchRf));
        return financeFeignClient.receiptBatch(receiptBatchRf);
    }

    public ReceiptDetailRv receiptDetail(ReceiptDetailRf from) {
        ReceiptDetailRv receiptDetailRv = financeFeignClient.receiptDetail(from);
        if (Objects.nonNull(receiptDetailRv) && !receiptDetailRv.getInvoiceReceiptDetail().isEmpty()) {
            ContractBondReceiptDetailE contractBondReceiptDetailE = contractBondReceiptDetailService.getByInvoiceNumber(receiptDetailRv.getReceiptNo());
            if (Objects.nonNull(contractBondReceiptDetailE)) {
                receiptDetailRv.getInvoiceReceiptDetail().get(0).setRemark(contractBondReceiptDetailE.getRemark());
            }
        }
        return receiptDetailRv;
    }

    public List<Boolean> updateReceiptDetail(List<ContractBondReceiptDetailF> from) {
        List<Boolean> result = new ArrayList<>();
        if (!from.isEmpty()) {
            from.forEach(item -> {
                ContractBondReceiptDetailE map = Global.mapperFacade.map(item, ContractBondReceiptDetailE.class);
                result.add(contractBondReceiptDetailService.updateById(map));
            });
        }
        return result;
    }
}
