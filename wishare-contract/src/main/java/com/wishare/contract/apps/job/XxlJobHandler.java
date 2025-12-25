package com.wishare.contract.apps.job;

import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.vo.InvoiceInfoRv;
import com.wishare.contract.apps.service.revision.income.ContractIncomeBusinessService;
import com.wishare.contract.apps.service.revision.income.ContractIncomePullService;
import com.wishare.contract.apps.service.revision.pay.ContractPayBusinessService;
import com.wishare.contract.apps.service.revision.remind.ContractRemindMessageConfigService;
import com.wishare.contract.domains.entity.contractset.ContractInvoiceDetailE;
import com.wishare.contract.domains.mapper.contractset.ContractInvoiceDetailMapper;
import com.wishare.contract.domains.service.revision.income.ContractIncomePlanConcludeService;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.consts.Const;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 永遇乐 yeoman <76164451@.qq.com>
 * @line --------------------------------
 * @date 2023/02/25
 */
@Slf4j
@Component
public class XxlJobHandler {

    @Setter(onMethod_ = {@Autowired})
    private ContractInvoiceDetailMapper contractInvoiceDetailMapper;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractRemindMessageConfigService contractRemindMessageConfigService;
    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeBusinessService contractIncomeBusinessService;
    @Setter(onMethod_ = {@Autowired})
    private ContractPayBusinessService contractPayBusinessService;
    @Setter(onMethod_ = {@Autowired})
    private ContractIncomePlanConcludeService contractIncomePlanConcludeService;


    /**
     * 查询并更新合同收款计划发票状态定时任务
     */
    @XxlJob("contractCollectionPlanInvoiceHandler")
    public ReturnT<String> contractCollectionPlanInvoiceHandler() {
        String jobParam = XxlJobHelper.getJobParam();
        Integer days = Integer.valueOf(jobParam);
        log.info("查询并更新合同收款计划发票状态定时任务...开始执行。。。");
        List<ContractInvoiceDetailE> list = contractInvoiceDetailMapper.selectInvoiceStatus1ByYesterday(LocalDateTime.now().minusDays(days), 1000);
        Map<String, List<ContractInvoiceDetailE>> map = list.stream().collect(Collectors.groupingBy(ContractInvoiceDetailE::getTenantId));
        map.forEach((k, v) -> checkAndUpdate(k, v));
        log.info("查询并更新合同收款计划发票状态定时任务...执行结束。。。");
        return ReturnT.SUCCESS;
    }

    /**
     * 发送合同/收款的提醒/预警消息
     *
     * @return
     */
    @XxlJob("contractRemindMessageSend")
    public ReturnT<String> contractRemindMessageSend() {
        log.info("开始发送合同收款的提醒预警消息");
        contractRemindMessageConfigService.send();
        log.info("合同收款的提醒预警消息发送完毕");
        return ReturnT.SUCCESS;
    }

    @XxlJob("contractPullZjHandler")
    public ReturnT<String> contractPullZjHandler(){
        log.info("开始自动推送合同到中交");
        contractIncomeBusinessService.autoPullContract();
        contractPayBusinessService.autoPullContract();
        log.info("自动推送合同到中交结束");
        return ReturnT.SUCCESS;
    }

    @XxlJob("updateContractInUse")
    public ReturnT<String> updateContractInUse() {
        log.info("开始执行updateContractInUse");
        LocalDate targetDate = LocalDate.now();
        contractIncomeBusinessService.updateContractInUse(targetDate);
        contractPayBusinessService.updateContractInUse(targetDate);
        log.info("updateContractInUse执行完毕");
        return ReturnT.SUCCESS;
    }

    /**
     * 重新推送失败收款计划数据到枫行梦
     *
     * @return
     */
    @XxlJob("rePushFxmFailedData")
    public ReturnT<String> rePushFxmFailedData() {
        log.info("开始执行rePushFxmFailedData");
        contractIncomePlanConcludeService.rePushFxmFailedData();
        log.info("rePushFxmFailedData执行结束");
        return ReturnT.SUCCESS;
    }

    private void checkAndUpdate(String tenantId, List<ContractInvoiceDetailE> list) {
        List<Long> invoiceIds = list.stream().map(ContractInvoiceDetailE::getInvoiceId).collect(Collectors.toList());
        List<InvoiceInfoRv> rvs = financeFeignClient.invoices(tenantId, invoiceIds);
        list.forEach(e -> {
            for (int i = 0; i < rvs.size(); i++) {
                if (!e.getInvoiceId().equals(rvs.get(i).getId())) {
                    continue;
                }
                switch (rvs.get(i).getState()) {
                    case Const.State._2:
                        e.setInvoiceStatus(Const.State._0);
                        contractInvoiceDetailMapper.updateInvoiceStateSuccess(e.getId());
                        log.info("合同收款计划ID：{}，发票ID：{}，开票成功", e.getCollectionPlanId(), e.getInvoiceId());
                        break;
                    case Const.State._3:
                        e.setInvoiceStatus(Const.State._2);
                        contractInvoiceDetailMapper.updateInvoiceStateFail(e.getId(), rvs.get(i).getFailReason());
                        log.error("合同收款计划ID：{}，发票ID：{}，开票失败，原因是：{}", e.getCollectionPlanId(), e.getInvoiceId(), rvs.get(i).getFailReason());
                        break;
                    default:
                }
                break;
            }
        });
    }
}
