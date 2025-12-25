package com.wishare.contract.apps.service.revision.invoice;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillItemE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemPageF;
import com.wishare.contract.domains.service.revision.invoice.ContractSettlementsBillItemService;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillItemV;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemListF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemUpdateF;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillItemListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收票款项明细表
* </p>
*
* @author zhangfuyu
* @since 2024-05-07
*/
@Service
@Slf4j
public class ContractSettlementsBillItemAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractSettlementsBillItemService contractSettlementsBillItemService;

    public ContractSettlementsBillItemV get(ContractSettlementsBillItemF contractSettlementsBillItemF){
        return contractSettlementsBillItemService.get(contractSettlementsBillItemF).orElse(null);
    }

    public ContractSettlementsBillItemListV list(ContractSettlementsBillItemListF contractSettlementsBillItemListF){
        return contractSettlementsBillItemService.list(contractSettlementsBillItemListF);
    }

    public String save(ContractSettlementsBillItemSaveF contractSettlementsBillItemF){
        return contractSettlementsBillItemService.save(contractSettlementsBillItemF);
    }

    public void update(ContractSettlementsBillItemUpdateF contractSettlementsBillItemF){
        contractSettlementsBillItemService.update(contractSettlementsBillItemF);
    }

    public boolean removeById(String id){
        return contractSettlementsBillItemService.removeById(id);
    }

    public PageV<ContractSettlementsBillItemV> page(PageF<ContractSettlementsBillItemPageF> request) {
        return contractSettlementsBillItemService.page(request);
    }

    public PageV<ContractSettlementsBillItemV> frontPage(PageF<SearchF<ContractSettlementsBillItemE>> request) {
        return contractSettlementsBillItemService.frontPage(request);
    }
}
