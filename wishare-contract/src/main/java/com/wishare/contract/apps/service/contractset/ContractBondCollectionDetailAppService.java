package com.wishare.contract.apps.service.contractset;

import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.interfaces.ApiBase;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractBondCollectionDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailPageF;
import com.wishare.contract.domains.service.contractset.ContractBondCollectionDetailService;
import com.wishare.contract.domains.vo.contractset.ContractBondCollectionDetailV;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailF;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBondCollectionDetailUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 保证金计划收款明细
* </p>
*
* @author ljx
* @since 2022-10-25
*/
@Service
@Slf4j
public class ContractBondCollectionDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondCollectionDetailService contractBondCollectionDetailService;

    public ContractBondCollectionDetailE saveBondCollectionDetail(ContractBondCollectionDetailF collectionDetailF) {
        return contractBondCollectionDetailService.saveBondCollectionDetail(collectionDetailF, curIdentityInfo());
    }

    public List<ContractBondCollectionDetailV> listByBondPlanId(Long bondPlanId) {
        return contractBondCollectionDetailService.listByBondPlanId(bondPlanId);
    }
}
