package com.wishare.finance.apps.service.configure.cashflow;

import com.wishare.finance.apps.model.configure.cashflow.fo.CreateCashFlowF;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.service.CashFlowDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 现金流量应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/14
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CashFlowAppService {

    private final CashFlowDomainService cashFlowDomainService;

    /**
     * 同步现金流量
     * @param cashFlowFS
     * @return
     */
    @Transactional
    public boolean syncCashFlow(List<CreateCashFlowF> cashFlowFS) {
        return cashFlowDomainService.syncCashFlow(Global.mapperFacade.mapAsList(cashFlowFS, CashFlowE.class));
    }
}
