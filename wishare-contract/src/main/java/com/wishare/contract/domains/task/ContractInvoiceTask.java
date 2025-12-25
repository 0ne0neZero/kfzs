package com.wishare.contract.domains.task;

import com.alibaba.fastjson.JSON;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.InvoiceInfoF;
import com.wishare.contract.apps.remote.vo.InvoiceInfoRv;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.dto.contractset.ContractProfitLossBillD;
import com.wishare.contract.domains.entity.contractset.ContractCollectionPlanE;
import com.wishare.contract.domains.service.contractset.ContractCollectionPlanService;
import com.wishare.contract.domains.service.contractset.ContractInvoiceDetailService;
import com.wishare.contract.domains.service.contractset.ContractProfitLossBillService;
import com.wishare.contract.domains.vo.contractset.ContractCollectionPlanV;
import com.wishare.contract.domains.vo.contractset.ContractInvoiceDetailV;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author :wangrui
 * @Date: 2022/9/21
 */
@Slf4j
@Component
@EnableScheduling
@Setter(onMethod_ = {@Autowired})
@RequiredArgsConstructor
public class ContractInvoiceTask {

    private final ContractProfitLossBillService contractProfitLossBillService;
    private final ContractInvoiceDetailService contractInvoiceDetailService;
    private final ContractCollectionPlanService contractCollectionPlanService;
    private final FinanceFeignClient financeFeignClient;

    /**
     * 每秒请求中台获取开票信息
     */
    @Scheduled(cron = "0 * * * * ?")
    public void invoiceStatusTask() {
        log.info("执行请求开票信息任务开始=================");
        //取出正常状态租户的到期时间
        List<ContractProfitLossBillD> invoiceBills = contractProfitLossBillService.invoiceGetTask();
        if (CollectionUtils.isNotEmpty(invoiceBills)) {
            invoiceBills.forEach(invoiceBill -> {
                if (Objects.nonNull(invoiceBill.getInvoiceId())) {
                    try {
                        // 调用中台获取开票明细
                        InvoiceInfoF invoiceInfoF = new InvoiceInfoF(null, Collections.singletonList(invoiceBill.getInvoiceId()));
                        //-- TODO supUnitId default
                        invoiceInfoF.setSupCpUnitId("default");
                        IdentityInfo identityInfo = new IdentityInfo();
                        identityInfo.setTenantId(invoiceBill.getTenantId());
                        ThreadLocalUtil.set("IdentityInfo", identityInfo);
                        List<InvoiceInfoRv> invoiceInfoRvs = financeFeignClient.listByBillIds(invoiceInfoF);
                        if (CollectionUtils.isNotEmpty(invoiceInfoRvs)) {
                            InvoiceInfoRv invoiceInfoRv = invoiceInfoRvs.get(0);
                            if (Objects.nonNull(invoiceInfoRv) &&  invoiceInfoRv.getState() != 1) {
                                Integer invoiceState = invoiceInfoRv.getState() == 2 ?
                                        ContractSetConst.INVOICE_SUCCESS : ContractSetConst.INVOICE_FAIL;
                                invoiceInfoRv.setInvoiceStatus(invoiceState);
                                contractInvoiceDetailService.updateInvoiceInfo(invoiceInfoRv);
                                // 更新收款计划
                                if (invoiceInfoRv.getState() == 3) {
                                    List<ContractInvoiceDetailV> contractInvoiceDetailVS =
                                            contractInvoiceDetailService.invoiceDetailListByInvoiceId(invoiceInfoRv.getId());
                                    contractInvoiceDetailVS.forEach(item -> {
                                        Long collectionPlanId = item.getCollectionPlanId();
                                        ContractCollectionPlanE collectionPlanE = contractCollectionPlanService.getById(collectionPlanId);
                                        if (Objects.nonNull(collectionPlanE)) {
                                            BigDecimal invoiceAmount = collectionPlanE.getInvoiceAmount().subtract(item.getInvoiceApplyAmount());
                                            collectionPlanE.setInvoiceAmount(invoiceAmount);
                                            if (invoiceAmount.compareTo(new BigDecimal("0.00")) <= 0) {
                                                collectionPlanE.setInvoiceStatus(ContractSetConst.NOT_INVOICED);
                                            } else {
                                                collectionPlanE.setInvoiceStatus(ContractSetConst.PART_INVOICED);
                                            }
                                            contractCollectionPlanService.updateById(collectionPlanE);
                                        }
                                    });
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("更新开票信息报错, 信息: {}, 错误: {}", JSON.toJSONString(invoiceBill), e);
                    }
                }
            });
        }
        log.info("执行请求开票信息任务结束=================");
    }
}
