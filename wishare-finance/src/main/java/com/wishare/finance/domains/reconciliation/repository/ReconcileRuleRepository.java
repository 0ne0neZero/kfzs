package com.wishare.finance.domains.reconciliation.repository;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import com.wishare.finance.domains.reconciliation.repository.mapper.ReconcileRuleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对象单资源库
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Service
public class ReconcileRuleRepository extends ServiceImpl<ReconcileRuleMapper, ReconcileRuleE> {

    /**
     * 获取当前租户的一个对账规则（一个租户只有一个规则）
     * @return
     */
    public ReconcileRuleE getOneRule(){
        return getOne(null, false);
    }

    /**
     * 获取当前租户的一个对账规则（一个租户只有一个规则）
     * @return
     */
    public ReconcileRuleE getByMode(Integer reconcileMode){
        return getOne(new LambdaQueryWrapper<ReconcileRuleE>().eq(ReconcileRuleE::getReconcileMode, reconcileMode), false);
    }

    /**
     * 获取当前租户的所有对账规则
     * @return
     */
    public List<ReconcileRuleE> getListByTenant(){
        return list();
    }


    public ReconcileRuleE getByScheduleId(String scheduleId) {
        return baseMapper.getByScheduleId(scheduleId);
    }

}
