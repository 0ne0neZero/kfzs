package com.wishare.contract.apps.service.revision.pay.settdetails;

import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsUpdateF;
import com.wishare.contract.domains.service.revision.pay.settdetails.ContractPaySettDetailsService;
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
public class ContractPaySettDetailsAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPaySettDetailsService contractPaySettDetailsService;

    public ContractPaySettDetailsV getDetailsById(String id){
        return contractPaySettDetailsService.getDetailsById(id);
    }

    public String save(ContractPaySettDetailsSaveF saveF){
        return contractPaySettDetailsService.save(saveF);
    }

    public void update(ContractPaySettDetailsUpdateF contractPayFundF){
        contractPaySettDetailsService.update(contractPayFundF);
    }

    public boolean removeById(String id){
        return contractPaySettDetailsService.removeById(id);
    }

    public List<ContractPaySettDetailsV> getDetailsBySettlementId(String id){
        return contractPaySettDetailsService.getDetailsBySettlementId(id);
    }
}
