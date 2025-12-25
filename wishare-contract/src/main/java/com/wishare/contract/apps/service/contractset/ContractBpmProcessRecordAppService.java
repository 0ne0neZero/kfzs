package com.wishare.contract.apps.service.contractset;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractBpmProcessRecordE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordPageF;
import com.wishare.contract.domains.service.contractset.ContractBpmProcessRecordService;
import com.wishare.contract.domains.vo.contractset.ContractBpmProcessRecordV;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordSaveF;
import com.wishare.contract.apps.fo.contractset.ContractBpmProcessRecordUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 
* </p>
*
* @author jinhui
* @since 2023-02-24
*/
@Service
@Slf4j
public class ContractBpmProcessRecordAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractBpmProcessRecordService contractBpmProcessRecordService;

    public ContractBpmProcessRecordV get(ContractBpmProcessRecordF contractBpmProcessRecordF){
        return contractBpmProcessRecordService.get(contractBpmProcessRecordF).orElse(null);
    }

    public List<ContractBpmProcessRecordV> list(ContractBpmProcessRecordF contractBpmProcessRecordF,Integer limit){
        return contractBpmProcessRecordService.list(contractBpmProcessRecordF,limit);
    }

    public Long save(ContractBpmProcessRecordSaveF contractBpmProcessRecordF){
        return contractBpmProcessRecordService.save(contractBpmProcessRecordF);
    }

    public void update(ContractBpmProcessRecordUpdateF contractBpmProcessRecordF){
        contractBpmProcessRecordService.update(contractBpmProcessRecordF);
    }

    public boolean remove(ContractBpmProcessRecordF contractBpmProcessRecordF){
        return contractBpmProcessRecordService.remove(contractBpmProcessRecordF);
    }

    public PageV<ContractBpmProcessRecordV> page(PageF<ContractBpmProcessRecordPageF> request) {
        return contractBpmProcessRecordService.page(request);
    }

    public PageV<ContractBpmProcessRecordV> frontPage(PageF<SearchF<ContractBpmProcessRecordE>> request) {
        return contractBpmProcessRecordService.frontPage(request);
    }
}
