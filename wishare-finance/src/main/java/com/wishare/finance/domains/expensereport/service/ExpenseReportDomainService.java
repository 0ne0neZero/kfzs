package com.wishare.finance.domains.expensereport.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.wishare.finance.domains.configure.chargeitem.entity.BusinessSegmentE;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.BusinessSegmentRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.expensereport.aggregate.ExpenseReportA;
import com.wishare.finance.domains.expensereport.entity.ExpenseReportDetailE;
import com.wishare.finance.domains.expensereport.entity.ExpenseReportE;
import com.wishare.finance.domains.expensereport.enums.KingDeePushSceneTypeEnum;
import com.wishare.finance.domains.expensereport.repository.ExpenseReportDetailRepository;
import com.wishare.finance.domains.expensereport.repository.ExpenseReportRepository;
import com.wishare.finance.domains.invoicereceipt.aggregate.InvoiceA;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.infrastructure.remote.clients.base.ConfigClient;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.fo.external.kingdee.AddKingDeeRecBillF;
import com.wishare.finance.infrastructure.remote.fo.external.kingdee.KingDeeRecBillEntry;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbQueryRF;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.external.kingdee.KingDeeResultRV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.MapperFacadeUtil;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ExpenseReportDomainService {

    private final ExpenseReportRepository expenseReportRepository;
    private final ExpenseReportDetailRepository expenseReportDetailRepository;
    private final BusinessSegmentRepository businessSegmentRepository;
    private final ChargeItemRepository chargeItemRepository;

    private final ExternalClient externalClient;
    private final ConfigClient configClient;

    @Value("${kingdee.defaultOrg:LSZB0303}")
    private String defaultOrg;
    @Value("${kingdee.defaultCustomer:LSZB001}")
    private String defaultCustomer;

    @Value("${kingdee.defaultSettleType:电汇}")
    private String defaultSettleType;

    /**
     * 开票计提
     * @return
     */
    public ExpenseReportA generateExpenseReport(InvoiceA invoiceA) {
        InvoiceE invoice = invoiceA.getInvoice();
        InvoiceReceiptE invoiceReceipt = invoiceA.getInvoiceReceipt();
        List<InvoiceReceiptDetailE> invoiceReceiptDetails = invoiceA.getInvoiceReceiptDetails();
        ExpenseReportE expenseReportE = new ExpenseReportE();
        expenseReportE.setSceneId(invoiceReceipt.getId());
        expenseReportE.setSceneType(KingDeePushSceneTypeEnum.开票计提.getCode());
        expenseReportE.setBusinessBillId(invoiceReceipt.getId());
        expenseReportE.setBusinessBillNo(invoice.getInvoiceNo());
        expenseReportE.setBizDate(LocalDateTimeUtil.format(invoiceReceipt.getBillingTime(),
                DatePattern.NORM_DATE_PATTERN));
        expenseReportE.setCustomer(defaultCustomer);   // 默认值
        // 默认拈花湾在金蝶的默认组织编码
        expenseReportE.setOrg(defaultOrg);  //默认值
        // 分录明细（以票面上为一行）
        List<ExpenseReportDetailE> expenseReportDetailEList = new ArrayList<>();
        Map<Integer, List<InvoiceReceiptDetailE>> lineDetails = invoiceReceiptDetails.stream()
                .collect(Collectors.groupingBy(InvoiceReceiptDetailE::getLineNo));
        lineDetails.forEach((lineNo, details) -> {
            InvoiceReceiptDetailE invoiceReceiptDetailE = details.get(0);
            ExpenseReportDetailE expenseReportDetailE = new ExpenseReportDetailE();
            expenseReportDetailE.setExpenseReportId(expenseReportE.getId());
            expenseReportDetailE.setTotalAmount(invoiceReceiptDetailE.getFaceTaxAmount() + invoiceReceiptDetailE.getFaceTaxExcludedAmount());
            expenseReportDetailE.setAmount(invoiceReceiptDetailE.getFaceTaxExcludedAmount());
            BigDecimal taxRate = new BigDecimal(invoiceReceiptDetailE.getTaxRate()).multiply(new BigDecimal("100"));
            expenseReportDetailE.setTaxRate(taxRate.stripTrailingZeros().toPlainString());
            expenseReportDetailE.setTaxAmount(invoiceReceiptDetailE.getFaceTaxAmount());
            List<CfgExternalDataV> externalDataList = configClient.getExternalMapByCode("community",
                    invoiceReceipt.getCommunityId());
            if (CollectionUtils.isEmpty(externalDataList)) {
                throw BizException.throw400(invoiceReceipt.getCommunityName() + "项目未配置外部数据映射");
            }
            List<CfgExternalDataV> communities = externalDataList.stream()
                    .filter(node -> StringUtils.equals(node.getExternalDataType(), "community"))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(communities)) {
                throw BizException.throw400(invoiceReceipt.getCommunityName() + "项目未配置项目外部数据映射");
            }
            expenseReportDetailE.setItem(communities.get(0).getDataCode());
            Long chargeItemId = invoiceReceiptDetailE.getChargeItemId();
            BusinessSegmentE businessSegmentE = businessSegmentRepository.getById(chargeItemId);
            if (Objects.isNull(businessSegmentE)) {
                throw BizException.throw400(invoiceReceiptDetailE.getChargeItemName() + "费项未设置业务板块");
            }
            expenseReportDetailE.setBusinessSegmentCode(businessSegmentE.getBusinessSegmentCode());
            ChargeItemE chargeItemE = chargeItemRepository.getById(chargeItemId);
            expenseReportDetailE.setChargeItemCode(chargeItemE.getCode());
            expenseReportDetailEList.add(expenseReportDetailE);
        });

        return new ExpenseReportA(expenseReportE, expenseReportDetailEList);
    }

    public Boolean save(ExpenseReportA expenseReportA) {
        expenseReportRepository.save(expenseReportA.getExpenseReportE());
        return expenseReportDetailRepository.saveBatch(expenseReportA.getExpenseReportDetails());
    }

    /**
     * 推送应收单
     * @param expenseReportA
     * @return
     */
    public void pushRecBill(ExpenseReportA expenseReportA) {
        ExpenseReportE expenseReportE = expenseReportA.getExpenseReportE();
        AddKingDeeRecBillF addKingDeeRecBillF = MapperFacadeUtil.defaultMapperFacade()
                .map(expenseReportE, AddKingDeeRecBillF.class);
        List<KingDeeRecBillEntry> kingDeeRecBillEntries = MapperFacadeUtil.defaultMapperFacade()
                .mapAsList(expenseReportA.getExpenseReportDetails(), KingDeeRecBillEntry.class);
        String today = DateUtil.today();
        addKingDeeRecBillF.setBilldate(today);
        addKingDeeRecBillF.setJzdate(today);
        addKingDeeRecBillF.setMybillno(expenseReportE.getBusinessBillNo());
        addKingDeeRecBillF.setBizdate(expenseReportE.getBizDate());
        addKingDeeRecBillF.setDescription("");
        addKingDeeRecBillF.setSettletype(defaultSettleType);
        addKingDeeRecBillF.setEntrys(kingDeeRecBillEntries);
        KingDeeResultRV kingDeeResultRV = externalClient.addKingDeeRecBill(addKingDeeRecBillF);
        if ("0000".equals(kingDeeResultRV.getCode())) {
            expenseReportE.setExternalBillNo(kingDeeResultRV.getBillno());
        } else {
            throw BizException.throw400("推送金蝶失败：" + kingDeeResultRV.getMess());
        }
    }





}
