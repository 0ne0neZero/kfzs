package com.wishare.finance.domains.configure.organization.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.organization.dto.BankAccountCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.BankAccountCostOrgE;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.mapper.BankAccountCostOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Service
public class BankAccountCostOrgRepository extends ServiceImpl<BankAccountCostOrgMapper, BankAccountCostOrgE> {

    @Autowired
    private BankAccountCostOrgMapper bankAccountCostOrgMapper;

    /**
     * 获取成本中心与银行账户关联关系
     * @param costOrdId 成本中心id
     * @param tenantId 租户id
     * @return
     */
    public List<BankAccountCostOrgDto> getRelation(Long costOrdId, String tenantId) {
        return bankAccountCostOrgMapper.getRelation(costOrdId, tenantId);
    }

    public List<StatutoryBodyAccountE> queryStaIdByCostIds(List<Long> ids) {
        return bankAccountCostOrgMapper.queryStaIdByCostIds(ids);
    }
}
