package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.entity.BillHandAccountRule;
import com.wishare.finance.domains.bill.repository.mapper.BillAccountHandRuleMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交账规则表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-01-11
 */
@Service
public class BillAccountHandRuleRepository extends ServiceImpl<BillAccountHandRuleMapper, BillHandAccountRule> {


    /**
     * 根据自租户id获取规则
     * @param tenantId
     * @return
     */
    public BillHandAccountRule getByTenant(String tenantId) {
        return getOne(new LambdaQueryWrapper<BillHandAccountRule>().eq(BillHandAccountRule::getTenantId, tenantId));
    }
}
