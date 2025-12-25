package com.wishare.contract.apps.service.contractset;

import com.wishare.starter.interfaces.ApiBase;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractBondPaymentDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractBondPaymentDetailPageF;
import com.wishare.contract.domains.service.contractset.ContractBondPaymentDetailService;
import com.wishare.contract.domains.vo.contractset.ContractBondPaymentDetailV;
import com.wishare.contract.apps.fo.contractset.ContractBondPaymentDetailF;
import com.wishare.contract.apps.fo.contractset.ContractBondPaymentDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBondPaymentDetailUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 保证金计划付/退款明细
* </p>
*
* @author ljx
* @since 2022-10-25
*/
@Service
@Slf4j
public class ContractBondPaymentDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondPaymentDetailService contractBondPaymentDetailService;

    public ContractBondPaymentDetailE saveBondPaymentDetail(ContractBondPaymentDetailF collectionDetailF) {
        return contractBondPaymentDetailService.saveBondPaymentDetail(collectionDetailF, curIdentityInfo());
    }

    public List<ContractBondPaymentDetailV> listByBondPlanId(Long bondPlanId) {
        return contractBondPaymentDetailService.listByBondPlanId(bondPlanId);
    }

}
