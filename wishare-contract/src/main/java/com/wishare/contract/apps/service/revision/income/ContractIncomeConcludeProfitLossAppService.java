package com.wishare.contract.apps.service.revision.income;

import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeProfitLossE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeProfitLossService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeProfitLossV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeProfitLossListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同收入损益表
* </p>
*
* @author chenglong
* @since 2023-10-24
*/
@Service
@Slf4j
public class ContractIncomeConcludeProfitLossAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeProfitLossService contractIncomeConcludeProfitLossService;

    public ContractIncomeConcludeProfitLossV get(ContractIncomeConcludeProfitLossF contractIncomeConcludeProfitLossF){
        return contractIncomeConcludeProfitLossService.get(contractIncomeConcludeProfitLossF).orElse(null);
    }

    public List<ContractIncomePlanConcludeV> list(ContractIncomeConcludeProfitLossListF contractIncomeConcludeProfitLossListF){
        return contractIncomeConcludeProfitLossService.list(contractIncomeConcludeProfitLossListF);
    }

    public Boolean save(List<ContractIncomePlanAddF> addF){
        return contractIncomeConcludeProfitLossService.save(addF);
    }

    public void update(List<ContractIncomePlanConcludeUpdateF> contractPayConcludeF){
        contractIncomeConcludeProfitLossService.update(contractPayConcludeF);
    }

    public boolean removeById(String id){
        return contractIncomeConcludeProfitLossService.removeById(id);
    }

    public PageV<ContractIncomePlanConcludeV> page(PageF<SearchF<ContractIncomePlanConcludePageF>> request) {
        return contractIncomeConcludeProfitLossService.page(request);
    }

    public PageV<ContractIncomeConcludeProfitLossV> frontPage(PageF<SearchF<ContractIncomeConcludeProfitLossE>> request) {
        return contractIncomeConcludeProfitLossService.frontPage(request);
    }

    public List<ContractPlanDateV> calculate(ContractPlanDateF planDateF) {
        return contractIncomeConcludeProfitLossService.calculate(planDateF);
    }

    public void sumbitId(String id) {
        contractIncomeConcludeProfitLossService.sumbitId(id);
    }
}
