package com.wishare.finance.domains.reconciliation.facade;

import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/8/22
 */
@Service
@Slf4j
public class ReconciliationOrgFacade {

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;

    /**
     * 根据id获取租户详情
     *
     * @param id id
     * @return OrgTenantRv
     */
    public OrgTenantRv tenantGetById(String id) {
        return orgClient.tenantGetById(id);
    }
}
