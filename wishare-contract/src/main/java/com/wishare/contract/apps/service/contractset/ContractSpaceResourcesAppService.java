package com.wishare.contract.apps.service.contractset;

import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractSpaceResourcesE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesPageF;
import com.wishare.contract.domains.service.contractset.ContractSpaceResourcesService;
import com.wishare.contract.domains.vo.contractset.ContractSpaceResourcesV;
import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesF;
import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesSaveF;
import com.wishare.contract.apps.fo.contractset.ContractSpaceResourcesUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同空间资源信息
* </p>
*
* @author wangrui
* @since 2022-12-26
*/
@Service
@Slf4j
public class ContractSpaceResourcesAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractSpaceResourcesService contractSpaceResourcesService;

    public ContractSpaceResourcesV get(Long id){
        return contractSpaceResourcesService.get(id).orElse(null);
    }

    public List<ContractSpaceResourcesV> list(ContractSpaceResourcesF contractSpaceResourcesF){
        return contractSpaceResourcesService.list(contractSpaceResourcesF);
    }

    public void save(List<ContractSpaceResourcesSaveF> contractSpaceResourcesF){
        for (ContractSpaceResourcesSaveF contractSpaceResourcesSaveF : contractSpaceResourcesF) {
            contractSpaceResourcesService.save(contractSpaceResourcesSaveF);
        }
    }

    public void update(ContractSpaceResourcesUpdateF contractSpaceResourcesF){
        contractSpaceResourcesService.update(contractSpaceResourcesF);
    }

    public boolean remove(Long id){
        return contractSpaceResourcesService.remove(id);
    }

    public void deleteByContractId(Long contractId){
        contractSpaceResourcesService.deleteByContractId(contractId);
    }

    public PageV<ContractSpaceResourcesV> page(PageF<SearchF<ContractSpaceResourcesE>> request,String tenantId) {
        return contractSpaceResourcesService.pageList(request,tenantId);
    }

    public void batchSetting(List<ContractSpaceResourcesUpdateF> resourcesUpdateFList) {
        for (ContractSpaceResourcesUpdateF contractSpaceResourcesUpdateF : resourcesUpdateFList) {
            contractSpaceResourcesService.update(contractSpaceResourcesUpdateF);
        }
    }
}
