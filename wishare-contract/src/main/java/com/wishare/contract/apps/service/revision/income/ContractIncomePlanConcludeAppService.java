package com.wishare.contract.apps.service.revision.income;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.contract.apps.fo.revision.income.ContractIncomePlanAddF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomePlanConcludePageF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomePlanConcludeUpdateF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanAddF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludePageF;
import com.wishare.contract.apps.fo.revision.pay.ContractPayPlanConcludeUpdateF;
import com.wishare.contract.domains.service.revision.income.ContractIncomePlanConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayPlanConcludeService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeSumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanDetailsV;
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
 * @description:合同收款计划
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:49
 */
@Service
@Slf4j
public class ContractIncomePlanConcludeAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomePlanConcludeService contractPayPlanConcludeService;


    public ContractIncomePlanDetailsV getDetailsById(String id){
        return contractPayPlanConcludeService.getDetailsById(id);
    }

    public PageV<ContractIncomePlanConcludeV> page(PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        PageV<ContractIncomePlanConcludeV> pageList = contractPayPlanConcludeService.page(request);
        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    public ContractIncomePlanConcludeSumV accountAmountSum(PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        return contractPayPlanConcludeService.accountAmountSum(request);
    }

    public String save(ContractIncomePlanAddF addF){
        return contractPayPlanConcludeService.save(addF);
    }


    public void update(ContractIncomePlanConcludeUpdateF contractPayConcludeF){
        contractPayPlanConcludeService.update(contractPayConcludeF);
    }


    public boolean removeById(String id){
        return contractPayPlanConcludeService.removeById(id);
    }

    public void sumbitId(String id){
        contractPayPlanConcludeService.sumbitId(id);
    }

}
