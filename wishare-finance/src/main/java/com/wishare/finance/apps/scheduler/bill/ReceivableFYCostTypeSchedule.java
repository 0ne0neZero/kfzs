package com.wishare.finance.apps.scheduler.bill;


import com.wishare.finance.apps.service.bill.ReceivableBillAppService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 每个月第一天去查归属月包含在对应季度内的无费用分类的未结算、已审核的应收账单打上标签
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReceivableFYCostTypeSchedule {

    private final ReceivableBillAppService receivableBillAppService;
    private final String FANG_YUAN = "fangyuan";

    @XxlJob("receivableFYCostTypeHandler")
    public void receivableFYCostTypeHandler() {
        if (FANG_YUAN.equals(EnvData.config)) {
            String jobId = String.valueOf(XxlJobHelper.getJobId());
            log.info("================== 定时更新方圆费用标签-开始： 租户任务id=" + jobId + "=======================");
            receivableBillAppService.getBillCostType();
            log.info("================== 定时更新方圆费用标签-结束： 租户任务id=" + jobId + "=======================");
        }
    }
}
