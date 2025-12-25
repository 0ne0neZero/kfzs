package com.wishare.contract.apps.service.contractset;

import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.interfaces.ApiBase;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractReceiveInvoiceDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailPageF;
import com.wishare.contract.domains.service.contractset.ContractReceiveInvoiceDetailService;
import com.wishare.contract.domains.vo.contractset.ContractReceiveInvoiceDetailV;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailF;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractReceiveInvoiceDetailUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 付款计划收票明细
* </p>
*
* @author ljx
* @since 2022-11-29
*/
@Service
@Slf4j
public class ContractReceiveInvoiceDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractReceiveInvoiceDetailService contractReceiveInvoiceDetailService;

    public ContractReceiveInvoiceDetailE saveReceiveInvoiceDetail(ContractReceiveInvoiceDetailF from) {
        return contractReceiveInvoiceDetailService.saveReceiveInvoiceDetail(from, curIdentityInfo());
    }

    public List<ContractReceiveInvoiceDetailV> contractReceiveInvoiceDetailList(Long contractId, Long collectionPlanId) {
        return contractReceiveInvoiceDetailService.contractReceiveInvoiceDetailList(contractId, collectionPlanId);
    }

}
