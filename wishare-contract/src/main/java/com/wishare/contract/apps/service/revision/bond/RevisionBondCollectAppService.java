package com.wishare.contract.apps.service.revision.bond;

import com.wishare.contract.domains.vo.revision.bond.BondCollectDetailV;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectPageF;
import com.wishare.contract.domains.service.revision.bond.RevisionBondCollectService;
import com.wishare.contract.domains.vo.revision.bond.RevisionBondCollectV;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectListF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectSaveF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectUpdateF;
import com.wishare.contract.domains.vo.revision.bond.RevisionBondCollectListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 保证金改版-收取类保证金
* </p>
*
* @author chenglong
* @since 2023-07-26
*/
@Service
@Slf4j
public class RevisionBondCollectAppService {

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondCollectService revisionBondCollectService;

    public RevisionBondCollectV get(RevisionBondCollectF revisionBondCollectF){
        return revisionBondCollectService.get(revisionBondCollectF).orElse(null);
    }

    public RevisionBondCollectListV list(RevisionBondCollectListF revisionBondCollectListF){
        return revisionBondCollectService.list(revisionBondCollectListF);
    }

    public String save(RevisionBondCollectSaveF revisionBondCollectF){
        return revisionBondCollectService.save(revisionBondCollectF);
    }

    public void update(RevisionBondCollectUpdateF revisionBondCollectF){
        revisionBondCollectService.update(revisionBondCollectF);
    }

    public boolean removeById(String id){
        return revisionBondCollectService.removeById(id);
    }

    public PageV<RevisionBondCollectV> page(PageF<RevisionBondCollectPageF> request) {
        return revisionBondCollectService.page(request);
    }

    public PageV<BondCollectDetailV> frontPage(PageF<SearchF<RevisionBondCollectE>> request) {
        PageV<BondCollectDetailV> page = revisionBondCollectService.frontPage(request);
        for (BondCollectDetailV record : page.getRecords()) {

            //-- 计算保证金余额
            record.setResidueAmount((record.getCollectAmount().subtract(record.getSettleTransferAmount())).subtract(record.getRefundAmount()));

            revisionBondCollectService.dealBtnShow(record);
        }
        return page;
    }
}
