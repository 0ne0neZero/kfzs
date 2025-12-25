package com.wishare.contract.apps.service.contractset;

import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.interfaces.ApiBase;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.CollectionPlanDerateDetailE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.CollectionPlanDerateDetailPageF;
import com.wishare.contract.domains.service.contractset.CollectionPlanDerateDetailService;
import com.wishare.contract.domains.vo.contractset.CollectionPlanDerateDetailV;
import com.wishare.contract.apps.fo.contractset.CollectionPlanDerateDetailF;
import com.wishare.contract.apps.fo.contractset.CollectionPlanDerateDetailSaveF;
import com.wishare.contract.apps.fo.contractset.CollectionPlanDerateDetailUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收款计划减免明细
* </p>
*
* @author ljx
* @since 2022-11-07
*/
@Service
@Slf4j
public class CollectionPlanDerateDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private CollectionPlanDerateDetailService collectionPlanDerateDetailService;

    public CollectionPlanDerateDetailE saveBondCollectionDetail(CollectionPlanDerateDetailF collectionDetailF) {
        return collectionPlanDerateDetailService.saveBondCollectionDetail(collectionDetailF, curIdentityInfo());
    }

    public List<CollectionPlanDerateDetailV> listByCollectionPlanId(Long collectionPlanId) {
        return collectionPlanDerateDetailService.listByCollectionPlanId(collectionPlanId);
    }
}
