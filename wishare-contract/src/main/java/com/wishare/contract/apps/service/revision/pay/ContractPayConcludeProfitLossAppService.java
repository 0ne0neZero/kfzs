package com.wishare.contract.apps.service.revision.pay;

import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeProfitLossE;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeProfitLossService;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeProfitLossV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
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
* 合同成本损益表
* </p>
*
* @author chenglong
* @since 2023-10-26
*/
@Service
@Slf4j
public class ContractPayConcludeProfitLossAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeProfitLossService contractPayConcludeProfitLossService;

    public ContractPayConcludeProfitLossV get(ContractPayConcludeProfitLossF contractPayConcludeProfitLossF){
        return contractPayConcludeProfitLossService.get(contractPayConcludeProfitLossF).orElse(null);
    }

    public List<ContractPayPlanConcludeV>  list(ContractPayConcludeProfitLossListF contractPayConcludeProfitLossListF){
        return contractPayConcludeProfitLossService.list(contractPayConcludeProfitLossListF);
    }

    public Boolean save(List<ContractPayPlanAddF> addF){
        return contractPayConcludeProfitLossService.save(addF);
    }

    public void update(List<ContractPayPlanConcludeUpdateF> contractPayPlanConcludeUpdateF){
        contractPayConcludeProfitLossService.update(contractPayPlanConcludeUpdateF);
    }

    public boolean removeById(String id){
        return contractPayConcludeProfitLossService.removeById(id);
    }

    public PageV<ContractPayPlanConcludeV> page(PageF<SearchF<ContractPayPlanConcludePageF>> request) {
        return contractPayConcludeProfitLossService.page(request);
    }

    public PageV<ContractPayConcludeProfitLossV> frontPage(PageF<SearchF<ContractPayConcludeProfitLossE>> request) {
        return contractPayConcludeProfitLossService.frontPage(request);
    }

    public List<ContractPlanDateV> calculate(ContractPlanDateF planDateF) {
        return contractPayConcludeProfitLossService.calculate(planDateF);
    }

    public void sumbitId(String id) {
        contractPayConcludeProfitLossService.sumbitId(id);

    }
}
