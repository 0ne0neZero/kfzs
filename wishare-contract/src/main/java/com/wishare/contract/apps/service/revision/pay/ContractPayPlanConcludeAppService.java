package com.wishare.contract.apps.service.revision.pay;

import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanAddF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludePageF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludeUpdateF;
import com.wishare.contract.domains.service.revision.pay.ContractPayPlanConcludeService;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeSumV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanDetailsV;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:合同付款计划
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:49
 */
@Service
@Slf4j
class ContractPayPlanConcludeAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayPlanConcludeService contractPayPlanConcludeService;


    public ContractPayPlanDetailsV getDetailsById(String id){
        return contractPayPlanConcludeService.getDetailsById(id);
    }

    public PageV<ContractPayPlanConcludeV> page(PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        PageV<ContractPayPlanConcludeV> pageList = contractPayPlanConcludeService.page(request);
        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    public ContractPayPlanConcludeSumV accountAmountSum(PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        return contractPayPlanConcludeService.accountAmountSum(request);
    }

    public String save(ContractPayPlanAddF addF){
        return contractPayPlanConcludeService.save(addF);
    }


    public void update(ContractPayPlanConcludeUpdateF contractPayConcludeF){
        contractPayPlanConcludeService.update(contractPayConcludeF);
    }


    public boolean removeById(String id){
        return contractPayPlanConcludeService.removeById(id);
    }

    public void sumbitId(String id){
        contractPayPlanConcludeService.sumbitId(id);
    }

}
