package com.wishare.contract.apps.service.revision.invoice;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillCalculateE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculatePageF;
import com.wishare.contract.domains.service.revision.invoice.ContractSettlementsBillCalculateService;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillCalculateV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateListF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateUpdateF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillCalculateListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 结算单计量明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Service
@Slf4j
public class ContractSettlementsBillCalculateAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillCalculateService contractSettlementsBillCalculateService;

    public ContractSettlementsBillCalculateV get(ContractSettlementsBillCalculateF contractSettlementsBillCalculateF){
        return contractSettlementsBillCalculateService.get(contractSettlementsBillCalculateF).orElse(null);
    }

    public ContractSettlementsBillCalculateListV list(ContractSettlementsBillCalculateListF contractSettlementsBillCalculateListF){
        return contractSettlementsBillCalculateService.list(contractSettlementsBillCalculateListF);
    }

    public String save(ContractSettlementsBillCalculateSaveF contractSettlementsBillCalculateF){
        return contractSettlementsBillCalculateService.save(contractSettlementsBillCalculateF);
    }

    public void update(ContractSettlementsBillCalculateUpdateF contractSettlementsBillCalculateF){
        contractSettlementsBillCalculateService.update(contractSettlementsBillCalculateF);
    }

    public boolean removeById(String id){
        return contractSettlementsBillCalculateService.removeById(id);
    }

    public PageV<ContractSettlementsBillCalculateV> page(PageF<ContractSettlementsBillCalculatePageF> request) {
        return contractSettlementsBillCalculateService.page(request);
    }

    public PageV<ContractSettlementsBillCalculateV> frontPage(PageF<SearchF<ContractSettlementsBillCalculateE>> request) {
        return contractSettlementsBillCalculateService.frontPage(request);
    }
}
