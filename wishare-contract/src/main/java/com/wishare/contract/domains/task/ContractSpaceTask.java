package com.wishare.contract.domains.task;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.fo.ContractPlanRf;
import com.wishare.contract.apps.remote.vo.ContractPlanRv;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractPlanV;
import com.wishare.starter.Global;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * @Author :wangrui
 * @Date: 2023/1/05
 */
@Slf4j
@Component
@EnableScheduling
@Setter(onMethod_ = {@Autowired})
public class ContractSpaceTask implements ApplicationListener<ApplicationReadyEvent> {

    private ContractConcludeService contractConcludeService;

    private ExternalFeignClient externalFeignClient;

    /**
     * 每天23点50分执行 推送今天变更的合同收付款计划
     */
    @Scheduled(cron = "0 50 23 * * ?")
    public void contractPlan() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start("推送合同账单");
        List<ContractPlanV> contractPlanV = contractConcludeService.selectContractTimeList("108314314140208");
        if (!CollectionUtils.isEmpty(contractPlanV)) {
            List<ContractPlanRf> contractPlanRf = Global.mapperFacade.mapAsList(contractPlanV, ContractPlanRf.class);
            ContractPlanRv contractPlanRv = externalFeignClient.contractPlan(contractPlanRf);
            log.info("请求空间资源接口返回数据----->contractPlanRv:{}", JSON.toJSONString(contractPlanRv));
            if (contractPlanRv.getCode().equals(10001)) {
                log.info("请求空间资源接口失败");
            }
            if (contractPlanRv.getCode().equals(10000)) {
                log.info("请求空间资源接口成功");
            }
        }
        stopWatch.stop();
        log.info("推送合同账单任务结束=================\n{}", stopWatch.prettyPrint());
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
//        this.contractPlan();
    }
}
