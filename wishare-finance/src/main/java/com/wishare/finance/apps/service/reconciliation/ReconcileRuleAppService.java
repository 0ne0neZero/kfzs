package com.wishare.finance.apps.service.reconciliation;

import com.wishare.finance.apps.model.reconciliation.fo.AddReconcileRuleF;
import com.wishare.finance.apps.model.reconciliation.fo.UpdateReconcileRuleF;
import com.wishare.finance.apps.scheduler.bill.ReconciliationScheduler;
import com.wishare.finance.domains.reconciliation.entity.ReconcileRuleE;
import com.wishare.finance.domains.reconciliation.enums.ReconcileTypeEnum;
import com.wishare.finance.domains.reconciliation.service.ReconcileRuleDomainService;
import com.wishare.finance.infrastructure.remote.clients.xxljob.OldXxlJobClient;
import com.wishare.finance.infrastructure.remote.clients.xxljob.XxlJobManageDto;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 对账规则应用服务
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconcileRuleAppService {

    private final ReconcileRuleDomainService reconcileRuleDomainService;

    private final OldXxlJobClient oldXxlJobClient;

    /**
     * 新增对账规则
     * @param reconcileRuleF
     * @return
     */
    @Transactional
    public Long add(AddReconcileRuleF reconcileRuleF){
        //1.新增规则
        ReconcileRuleE reconcileRule = Global.mapperFacade.map(reconcileRuleF, ReconcileRuleE.class);
        boolean addResult = reconcileRuleDomainService.add(reconcileRule);
        //2.注册定时对账任务
        if (addResult){
            registerOrUpdateScheduler(reconcileRule);
        }
        return reconcileRule.getId();
    }

    /**
     * 更新对账规则
     * @param updateReconcileRuleF
     * @return
     */
    @Transactional
    public boolean update(UpdateReconcileRuleF updateReconcileRuleF) {
        ReconcileRuleE reconcileRule = Global.mapperFacade.map(updateReconcileRuleF, ReconcileRuleE.class);
        //2.注册或更新定时对账任务
        reconcileRule = reconcileRuleDomainService.update(reconcileRule, updateReconcileRuleF.getOperateType());
        registerOrUpdateScheduler(reconcileRule);
        return Objects.nonNull(reconcileRule);
    }

    /**
     * 注册定时对账任务
     * @param reconcileRule
     */
    public boolean registerOrUpdateScheduler(ReconcileRuleE reconcileRule){
        //2.注册定时任务
        if (ReconcileTypeEnum.自动对账.equalsByCode(reconcileRule.getReconcileType()) && Objects.nonNull(reconcileRule.getScheduleRule())){ //是否启用自动对账
            String cron = reconcileRule.getScheduleRule().cron();
            if (Objects.nonNull(cron)){
                //如果未注册了定时任务，则新增定时任务，反之则更新
                if (StringUtils.isBlank(reconcileRule.getScheduleId())){
                    String jobId = oldXxlJobClient.addJob(new XxlJobManageDto()
                            .setJobDesc("财务对账")
                            .setJobHandler(ReconciliationScheduler.RECONCILIATION_HANDLER)
                            .setCron(cron)
                            .setAuthor("finance-admin"));
                    reconcileRule.setScheduleId(jobId);
                    //更新任务
                    reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                }else{
                    oldXxlJobClient.updateJob(new XxlJobManageDto()
                            .setTaskId(Integer.valueOf(reconcileRule.getScheduleId()))
                            .setJobHandler(ReconciliationScheduler.RECONCILIATION_HANDLER)
                            .setCron(cron)
                            .setJobDesc("财务对账")
                            .setAuthor("finance-admin"));
                }
                //启用禁用
                if (reconcileRule.enabled()){
                    oldXxlJobClient.startJob(reconcileRule.getScheduleId());
                }else{
                    oldXxlJobClient.stopJob(reconcileRule.getScheduleId());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 查询单个租户下的单个规则
     * @return
     */
    public ReconcileRuleE getOrAddRuleByMode(Integer reconcileMode) {
        return reconcileRuleDomainService.getOrAddRuleByMode(reconcileMode);
    }

    /**
     * 根据租户获取对账规则树
     * @return 对账规则树
     */
    public List<ReconcileRuleE> getTreeByTenant() {
        return reconcileRuleDomainService.getTreeByTenant();
    }
}
