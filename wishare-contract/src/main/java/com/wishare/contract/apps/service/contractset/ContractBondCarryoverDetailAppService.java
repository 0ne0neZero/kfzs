package com.wishare.contract.apps.service.contractset;

import com.wishare.starter.interfaces.ApiBase;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.contract.domains.service.contractset.ContractBondCarryoverDetailService;
import com.wishare.contract.domains.vo.contractset.ContractBondCarryoverDetailV;
import com.wishare.contract.apps.fo.contractset.ContractBondCarryoverDetailF;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 保证金结转明细表
* </p>
*
* @author ljx
* @since 2022-11-21
*/
@Service
@Slf4j
public class ContractBondCarryoverDetailAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractBondCarryoverDetailService contractBondCarryoverDetailService;

    public Long saveBondPlanCarryoverDetail(ContractBondCarryoverDetailF carryoverDetailF) {
        return contractBondCarryoverDetailService.saveBondPlanCarryoverDetail(carryoverDetailF, curIdentityInfo());
    }

    public List<ContractBondCarryoverDetailV> listByBondPlanId(Long bondPlanId) {
        return contractBondCarryoverDetailService.listByBondPlanId(bondPlanId);
    }
}
