package com.wishare.contract.apps.service.revision.invoice;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillDetailsE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsPageF;
import com.wishare.contract.domains.service.revision.invoice.ContractSettlementsBillDetailsService;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillDetailsV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsListF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsUpdateF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillDetailsListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收票明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-10
*/
@Service
@Slf4j
public class ContractSettlementsBillDetailsAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillDetailsService contractSettlementsBillDetailsService;

    public ContractSettlementsBillDetailsV get(ContractSettlementsBillDetailsF contractSettlementsBillDetailsF){
        return contractSettlementsBillDetailsService.get(contractSettlementsBillDetailsF).orElse(null);
    }

    public ContractSettlementsBillDetailsListV list(ContractSettlementsBillDetailsListF contractSettlementsBillDetailsListF){
        return contractSettlementsBillDetailsService.list(contractSettlementsBillDetailsListF);
    }

    public String save(ContractSettlementsBillDetailsSaveF contractSettlementsBillDetailsF){
        return contractSettlementsBillDetailsService.save(contractSettlementsBillDetailsF);
    }

    public void update(ContractSettlementsBillDetailsUpdateF contractSettlementsBillDetailsF){
        contractSettlementsBillDetailsService.update(contractSettlementsBillDetailsF);
    }

    public boolean removeById(String id){
        return contractSettlementsBillDetailsService.removeById(id);
    }

    public PageV<ContractSettlementsBillDetailsV> page(PageF<ContractSettlementsBillDetailsPageF> request) {
        return contractSettlementsBillDetailsService.page(request);
    }

    public PageV<ContractSettlementsBillDetailsV> frontPage(PageF<SearchF<ContractSettlementsBillDetailsE>> request) {
        return contractSettlementsBillDetailsService.frontPage(request);
    }
}
