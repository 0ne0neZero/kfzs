package com.wishare.finance.apps.scheduler.bill;

import com.wishare.finance.apps.service.reconciliation.ReconciliationAppService;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.starter.exception.BizException;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 对账定时任务规则
 *
 * @Author dxclay
 * @Date 2022/10/27
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconciliationScheduler {

    public static final String RECONCILIATION_HANDLER = "reconciliationHandler";
    private final ReconciliationAppService reconciliationAppService;

    @XxlJob(value = RECONCILIATION_HANDLER)
    public void reconciliationHandler(){
        try {
            String jobId = String.valueOf(XxlJobHelper.getJobId());
            log.info("================== 定时对账-开始： 租户任务id=" + jobId + "=======================");
            reconciliationAppService.reconcile(jobId, ReconcileModeEnum.账票流水对账,null);
            log.info("================== 定时对账-结束： 租户任务id=" + jobId + "=======================");
        }catch (BizException e){
            log.error(e.getMessage());
        }
    }

}
