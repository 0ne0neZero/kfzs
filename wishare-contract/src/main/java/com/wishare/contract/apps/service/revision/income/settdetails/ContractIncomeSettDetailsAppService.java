package com.wishare.contract.apps.service.revision.income.settdetails;

import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsUpdateF;
import com.wishare.contract.domains.service.revision.income.settdetails.ContractIncomeSettDetailsService;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.owl.enhance.IOwlApiBase;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
* <p>
* 支出合同-款项表
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Service
@Slf4j
public class ContractIncomeSettDetailsAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeSettDetailsService contractPaySettDetailsService;

    public ContractIncomeSettDetailsV getDetailsById(String id){
        return contractPaySettDetailsService.getDetailsById(id);
    }

    public String save(ContractIncomeSettDetailsSaveF saveF){
        return contractPaySettDetailsService.save(saveF);
    }

    public void update(ContractIncomeSettDetailsUpdateF contractPayFundF){
        contractPaySettDetailsService.update(contractPayFundF);
    }

    public boolean removeById(String id){
        return contractPaySettDetailsService.removeById(id);
    }

    public List<ContractIncomeSettDetailsV> getDetailsBySettlementId(String id){
        return contractPaySettDetailsService.getDetailsBySettlementId(id);
    }
}
