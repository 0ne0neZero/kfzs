package com.wishare.contract.apps.service.revision.income;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeExpandMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.owl.enhance.IOwlApiBase;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandPageF;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeExpandService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandV;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandListF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandSaveF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeConcludeExpandUpdateF;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeExpandListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收入合同订立信息拓展表
* </p>
*
* @author chenglong
* @since 2023-09-22
*/
@Service
@Slf4j
public class ContractIncomeConcludeExpandAppService extends ServiceImpl<ContractIncomeConcludeExpandMapper, ContractIncomeConcludeExpandE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeExpandService contractIncomeConcludeExpandService;

    public ContractIncomeConcludeExpandV get(ContractIncomeConcludeExpandF contractIncomeConcludeExpandF){
        return contractIncomeConcludeExpandService.get(contractIncomeConcludeExpandF).orElse(null);
    }

    public ContractIncomeConcludeExpandListV list(ContractIncomeConcludeExpandListF contractIncomeConcludeExpandListF){
        return contractIncomeConcludeExpandService.list(contractIncomeConcludeExpandListF);
    }

    public Long save(ContractIncomeConcludeExpandSaveF contractIncomeConcludeExpandF){
        return contractIncomeConcludeExpandService.save(contractIncomeConcludeExpandF);
    }

    public void update(ContractIncomeConcludeExpandUpdateF contractIncomeConcludeExpandF){
        contractIncomeConcludeExpandService.update(contractIncomeConcludeExpandF);
    }

    public void updateByContractId(ContractIncomeConcludeExpandUpdateF contractIncomeConcludeExpandF){
        contractIncomeConcludeExpandService.updateByContractId(contractIncomeConcludeExpandF);
    }

    public boolean removeById(Long id){
        return contractIncomeConcludeExpandService.removeById(id);
    }

    public PageV<ContractIncomeConcludeExpandV> page(PageF<ContractIncomeConcludeExpandPageF> request) {
        return contractIncomeConcludeExpandService.page(request);
    }

    public PageV<ContractIncomeConcludeExpandV> frontPage(PageF<SearchF<ContractIncomeConcludeExpandE>> request) {
        return contractIncomeConcludeExpandService.frontPage(request);
    }
}
