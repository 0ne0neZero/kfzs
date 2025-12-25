package com.wishare.contract.apps.service.revision.bond.pay;

import com.wishare.contract.domains.vo.revision.bond.pay.BondPayDetailV;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayPageF;
import com.wishare.contract.domains.service.revision.bond.pay.RevisionBondPayService;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayV;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayListF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPaySaveF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayUpdateF;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 保证金改版-缴纳类保证金
* </p>
*
* @author chenglong
* @since 2023-07-28
*/
@Service
@Slf4j
public class RevisionBondPayAppService {

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondPayService revisionBondPayService;

    public RevisionBondPayV get(RevisionBondPayF revisionBondPayF){
        return revisionBondPayService.get(revisionBondPayF).orElse(null);
    }

    public RevisionBondPayListV list(RevisionBondPayListF revisionBondPayListF){
        return revisionBondPayService.list(revisionBondPayListF);
    }

    public String save(RevisionBondPaySaveF revisionBondPayF){
        return revisionBondPayService.save(revisionBondPayF);
    }

    public void update(RevisionBondPayUpdateF revisionBondPayF){
        revisionBondPayService.update(revisionBondPayF);
    }

    public boolean removeById(String id){
        return revisionBondPayService.removeById(id);
    }

    public PageV<RevisionBondPayV> page(PageF<RevisionBondPayPageF> request) {
        return revisionBondPayService.page(request);
    }

    public PageV<BondPayDetailV> frontPage(PageF<SearchF<RevisionBondPayE>> request) {
        PageV<BondPayDetailV> pageV = revisionBondPayService.frontPage(request);
        for (BondPayDetailV record : pageV.getRecords()) {

            record.setResidueAmount((record.getPayAmount().subtract(record.getSettleTransferAmount())).subtract(record.getCollectAmount()));

            revisionBondPayService.dealBtnShow(record);
        }
        return pageV;
    }
}
