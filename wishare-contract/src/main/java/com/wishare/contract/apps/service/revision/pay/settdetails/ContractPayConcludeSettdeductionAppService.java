package com.wishare.contract.apps.service.revision.pay.settdetails;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPayConcludeSettdeductionE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionPageF;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPayConcludeSettdeductionService;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionV;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionListF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionUpdateF;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 结算单扣款明细表信息
* </p>
*
* @author zhangfy
* @since 2024-05-20
*/
@Service
@Slf4j
public class ContractPayConcludeSettdeductionAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeSettdeductionService contractPayConcludeSettdeductionService;

    public ContractPayConcludeSettdeductionV get(ContractPayConcludeSettdeductionF contractPayConcludeSettdeductionF){
        return contractPayConcludeSettdeductionService.get(contractPayConcludeSettdeductionF).orElse(null);
    }

    public ContractPayConcludeSettdeductionListV list(ContractPayConcludeSettdeductionListF contractPayConcludeSettdeductionListF){
        return contractPayConcludeSettdeductionService.list(contractPayConcludeSettdeductionListF);
    }

    public String save(ContractPayConcludeSettdeductionSaveF contractPayConcludeSettdeductionF){
        return contractPayConcludeSettdeductionService.save(contractPayConcludeSettdeductionF);
    }

    public void update(ContractPayConcludeSettdeductionUpdateF contractPayConcludeSettdeductionF){
        contractPayConcludeSettdeductionService.update(contractPayConcludeSettdeductionF);
    }

    public boolean removeById(String id){
        return contractPayConcludeSettdeductionService.removeById(id);
    }

    public PageV<ContractPayConcludeSettdeductionV> page(PageF<ContractPayConcludeSettdeductionPageF> request) {
        return contractPayConcludeSettdeductionService.page(request);
    }

    public PageV<ContractPayConcludeSettdeductionV> frontPage(PageF<SearchF<ContractPayConcludeSettdeductionE>> request) {
        return contractPayConcludeSettdeductionService.frontPage(request);
    }
}
