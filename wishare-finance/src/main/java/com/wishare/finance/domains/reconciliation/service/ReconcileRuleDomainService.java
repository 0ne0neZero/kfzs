package com.wishare.finance.domains.reconciliation.service;

import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import com.wishare.finance.domains.reconciliation.enums.ReconcileExecuteStateEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.repository.ReconcileRuleRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 对账规则领域服务
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconcileRuleDomainService {

    private final ReconcileRuleRepository reconcileRuleRepository;

    /**
     * 新增对账规则
     * @param reconcileRule
     * @return
     */
    public boolean add(ReconcileRuleE reconcileRule){
        ErrorAssertUtil.isNullThrow300(getOneRule(), ErrorMessage.RECONCILE_RULE_EXIST);
        reconcileRule.checkDimension();
        return reconcileRuleRepository.save(reconcileRule);
    }

    /**
     * 更新对账规则
     * @param reconcileRule 规则
     * @param operateType 操作类型
     * @return 结果
     */
    public ReconcileRuleE update(ReconcileRuleE reconcileRule, int operateType){
        ReconcileRuleE reconcileRuleE = reconcileRuleRepository.getById(reconcileRule.getId());
        ErrorAssertUtil.notNullThrow300(reconcileRuleE, ErrorMessage.RECONCILE_RULE_NOT_EXIST);
        ErrorAssertUtil.isFalseThrow402(ReconcileExecuteStateEnum.运行中.equalsByCode(reconcileRule.getExecuteState()), ErrorMessage.RECONCILE_RULE_IS_RUNNING);
        if (operateType== 0) {
            reconcileRule.checkDimension();
        }
        boolean res = reconcileRuleRepository.updateById(reconcileRule);
        if (res){
            Global.mapperFacade.map(reconcileRule, reconcileRuleE);
        }
        return reconcileRuleE;
    }

    /**
     * 更新对账规则，不需要校验对账状态
     * @param reconcileRule
     * @return
     */
    public boolean updateNonCheckState(ReconcileRuleE reconcileRule){
        reconcileRule.checkDimension();
        return reconcileRuleRepository.updateById(reconcileRule);
    }

    /**
     * 获取当前租户的一个对账规则（一个租户只有一个规则）
     * @return
     */
    public ReconcileRuleE getOneRule(){
        return reconcileRuleRepository.getOneRule();
    }

    /**
     * 根据任务id获取运行你规则
     * @param scheduleId
     */
    public ReconcileRuleE getByScheduleId(String scheduleId) {
       return reconcileRuleRepository.getByScheduleId(scheduleId);
    }

    /**
     * 根据id获取对账规则
     * @param reconcileId
     * @return
     */
    public ReconcileRuleE getById(Long reconcileId) {
        return reconcileRuleRepository.getById(reconcileId);
    }

    /**
     * 根据对账模式获取对账规则
     * @param reconcileMode 对账模式
     * @return 对账规则
     */
    public ReconcileRuleE getOrAddRuleByMode(Integer reconcileMode) {
        ReconcileRuleE reconcileRule = reconcileRuleRepository.getByMode(reconcileMode);
        if (Objects.isNull(reconcileRule)){
            reconcileRule = new ReconcileRuleE(ReconcileModeEnum.valueOfByCode(reconcileMode));
            reconcileRuleRepository.save(reconcileRule);
        }
        return reconcileRule;
    }

    /**
     * 根据租户获取对账规则树
     * @return 对账规则树
     */
    public List<ReconcileRuleE> getTreeByTenant() {
        List<ReconcileRuleE> ruleList = reconcileRuleRepository.getListByTenant();
        Map<Integer, List<ReconcileRuleE>> ruleMap;
        if (CollectionUtils.isNotEmpty(ruleList)){
            ruleMap = ruleList.stream().collect(Collectors.groupingBy(ReconcileRuleE::getReconcileMode));
        }else {
            ruleMap = new HashMap<>();
        }
        List<ReconcileRuleE> undbRules = new ArrayList<>();
        for (ReconcileModeEnum reconcileModeEnum : ReconcileModeEnum.values()) {
            if (!ruleMap.containsKey(reconcileModeEnum.getCode())){
                undbRules.add(new ReconcileRuleE(ReconcileModeEnum.valueOfByCode(reconcileModeEnum.getCode())));
            }
        }
        if (!undbRules.isEmpty()){
            reconcileRuleRepository.saveBatch(undbRules);
            ruleList.addAll(undbRules);
        }
        return ruleList;
    }

    public ReconcileRuleE getByMode(int reconcileMode) {
        return reconcileRuleRepository.getByMode(reconcileMode);
    }
}
