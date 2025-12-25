package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.domains.consts.contractset.ContractInvoiceDetailFieldConst;
import com.wishare.contract.domains.consts.contractset.ContractProfitLossBillFieldConst;
import com.wishare.contract.domains.entity.contractset.ContractInvoiceDetailE;
import com.wishare.starter.Global;
import com.wishare.starter.helpers.UidHelper;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.contractset.ContractProfitLossBillE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillPageF;
import com.wishare.contract.domains.service.contractset.ContractProfitLossBillService;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossBillV;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillSaveF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossBillUpdateF;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 损益账单关联表
* </p>
*
* @author ljx
* @since 2022-10-17
*/
@Service
@Slf4j
public class ContractProfitLossBillAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossBillService contractProfitLossBillService;

    public Long save(ContractProfitLossBillF contractProfitLossBillF) {
        contractProfitLossBillF.setId(UidHelper.nextId(ContractProfitLossBillFieldConst.CONTRACT_PROFIT_LOSS_BILL));
        ContractProfitLossBillE map = Global.mapperFacade.map(contractProfitLossBillF, ContractProfitLossBillE.class);
        contractProfitLossBillService.save(map);
        return map.getId();
    }
}
