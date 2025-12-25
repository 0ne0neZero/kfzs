package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.apps.fo.contractset.*;
import com.wishare.contract.domains.entity.contractset.ContractSpaceResourcesE;
import com.wishare.contract.domains.service.contractset.ContractMaterialListService;
import com.wishare.contract.domains.service.contractset.ContractSpaceResourcesService;
import com.wishare.contract.domains.vo.contractset.ContractMaterialListV;
import com.wishare.contract.domains.vo.contractset.ContractSpaceResourcesV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* <p>
* 合同采购物资清单
* </p>
*
* @author wangrui
* @since 2022-12-26
*/
@Service
@Slf4j
public class ContractMaterialListAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractMaterialListService contractMaterialListService;

    public void save(List<ContractMaterialListSaveF> fList){
        for (ContractMaterialListSaveF contractSpaceResourcesSaveF : fList) {
            contractMaterialListService.save(contractSpaceResourcesSaveF);
        }
    }

    public void update(ContractMaterialListSaveF fList){
        contractMaterialListService.update(fList);
    }

    public void deleteByContractId(Long contractId){
        contractMaterialListService.deleteByContractId(contractId);
    }

    public List<ContractMaterialListV> list(ContractMaterialListF f) {
        return contractMaterialListService.list(f);
    }
}
