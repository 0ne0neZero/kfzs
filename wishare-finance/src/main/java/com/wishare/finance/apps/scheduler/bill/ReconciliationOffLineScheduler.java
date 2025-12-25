package com.wishare.finance.apps.scheduler.bill;

import com.wishare.finance.apps.model.reconciliation.fo.ChannelClaimF;
import com.wishare.finance.apps.service.reconciliation.ChannelFlowClaimAppService;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.fo.OrgFinanceF;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReconciliationOffLineScheduler {

    @Autowired
    private OrgClient orgClient;

    @Autowired
    private ChannelFlowClaimAppService channelFlowClaimAppService;
    /**
     * 远洋租户id
     */
    @Value("${reconciliation.tenantId:108314314140208}")
    private String tenantId;
    @XxlJob(value = "ReconciliationOffLineScheduler")
    public void reconciliationHandler(){
        try {
            String jobId = String.valueOf(XxlJobHelper.getJobId());
            log.info("================== 定时对账-开始： 租户任务id=" + jobId + "=======================");
            // 定义租户id
            IdentityInfo identityInfo = new IdentityInfo();
            identityInfo.setTenantId(tenantId);
            ThreadLocalUtil.set("IdentityInfo", identityInfo);
            OrgFinanceF orgFinanceF = new OrgFinanceF();
            orgFinanceF.setFinanceType("COST");
            orgFinanceF.setType("2");
            List<StatutoryBodyRv> orgFinanceList = orgClient.getOrgFinanceList(orgFinanceF);
            List<Long> costIdList = orgFinanceList.stream().filter(s -> s.getDisabled() == 0).map(StatutoryBodyRv::getId).collect(Collectors.toList());
            ChannelClaimF channelClaimF = new ChannelClaimF();
            channelClaimF.setCostCenterIdList(costIdList);
            channelClaimF.setEndTime(LocalDate.now().atStartOfDay());
            channelFlowClaimAppService.channelClaimOffLine(channelClaimF);
            log.info("================== 定时对账-结束： 租户任务id=" + jobId + "=======================");
        }catch (BizException e){
            log.error(e.getMessage());
        }
    }
}
