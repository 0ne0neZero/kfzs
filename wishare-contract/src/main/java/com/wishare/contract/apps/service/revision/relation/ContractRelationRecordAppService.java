package com.wishare.contract.apps.service.revision.relation;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.relation.ContractRelationRecordE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordPageF;
import com.wishare.contract.domains.service.revision.relation.ContractRelationRecordService;
import com.wishare.contract.domains.vo.revision.relation.ContractRelationRecordV;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordListF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordSaveF;
import com.wishare.contract.apps.fo.revision.relation.ContractRelationRecordUpdateF;
import com.wishare.contract.domains.vo.revision.relation.ContractRelationRecordListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Service
@Slf4j
public class ContractRelationRecordAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractRelationRecordService contractRelationRecordService;

    public ContractRelationRecordV get(ContractRelationRecordF contractRelationRecordF){
        return contractRelationRecordService.get(contractRelationRecordF).orElse(null);
    }

    public ContractRelationRecordListV list(ContractRelationRecordListF contractRelationRecordListF){
        return contractRelationRecordService.list(contractRelationRecordListF);
    }

    public String save(ContractRelationRecordSaveF contractRelationRecordF){
        return contractRelationRecordService.save(contractRelationRecordF);
    }

    public void update(ContractRelationRecordUpdateF contractRelationRecordF){
        contractRelationRecordService.update(contractRelationRecordF);
    }

    public boolean removeById(String id){
        return contractRelationRecordService.removeById(id);
    }

    public PageV<ContractRelationRecordV> page(PageF<ContractRelationRecordPageF> request) {
        return contractRelationRecordService.page(request);
    }

    public PageV<ContractRelationRecordV> frontPage(PageF<SearchF<ContractRelationRecordE>> request) {
        return contractRelationRecordService.frontPage(request);
    }
}
