package com.wishare.finance.apps.scheduler.bill;

import com.wishare.finance.apps.service.reconciliation.ReconciliationAppService;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.IdempotentUtil;
import com.wishare.starter.exception.BizException;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wishare.finance.infrastructure.conts.ErrorMessage.RECONCILE_RULE_IS_RUNNING;
import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.RECONCILIATION_MERCHANT;

/**
 * 商户清分定时任务对账
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconcileMerchantClearingSchedule {
    public static final String RECONCILIATION_MERCHANT_HANDLER = "reconciliationMerchantClearingHandler";
    private final ReconciliationAppService reconciliationAppService;

    @XxlJob(value = RECONCILIATION_MERCHANT_HANDLER)
    public void reconciliationHandler(){
        try {
            String jobId = String.valueOf(XxlJobHelper.getJobId());
            log.info("================== 定时对账-开始： 租户任务id=" + jobId + "=======================");
            IdempotentUtil.setIdempotent(DateTimeUtil.nowDateNoc() + String.valueOf(XxlJobHelper.getJobId()), RECONCILIATION_MERCHANT, 60*60, RECONCILE_RULE_IS_RUNNING);
            reconciliationAppService.reconcile(jobId, ReconcileModeEnum.商户清分对账, null);
            log.info("================== 定时对账-结束： 租户任务id=" + jobId + "=======================");
        }catch (BizException e){
            log.error(e.getMessage());
        }
    }
}
