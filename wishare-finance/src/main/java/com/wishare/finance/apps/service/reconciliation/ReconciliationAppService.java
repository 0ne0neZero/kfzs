package com.wishare.finance.apps.service.reconciliation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.apps.model.reconciliation.fo.*;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationClearStatisticsV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationDetailV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationPageV;
import com.wishare.finance.apps.model.reconciliation.vo.ReconciliationStatisticsV;
import com.wishare.finance.apps.service.bill.HandAccountDomainService;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.bill.command.*;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleWayEnum;
import com.wishare.finance.domains.bill.entity.BillRefundE;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.*;
import com.wishare.finance.domains.invoicereceipt.command.invocing.GatherInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.PayInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.command.invocing.ReceivableInvoiceQuery;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.reconciliation.ReconciliationA;
import com.wishare.finance.domains.reconciliation.ReconciliationClearA;
import com.wishare.finance.domains.reconciliation.command.FlowsInvoiceQuery;
import com.wishare.finance.domains.reconciliation.dto.FlowReconciliationDetailDto;
import com.wishare.finance.domains.reconciliation.entity.*;
import com.wishare.finance.domains.reconciliation.enums.*;
import com.wishare.finance.domains.reconciliation.repository.*;
import com.wishare.finance.domains.reconciliation.service.FlowDetailDomainService;
import com.wishare.finance.domains.reconciliation.service.ReconcileRuleDomainService;
import com.wishare.finance.domains.reconciliation.service.ReconciliationDomainService;
import com.wishare.finance.domains.reconciliation.service.ReconciliationYinlianDomainService;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherPushBillDetail;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherBillDetailRepository;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.bill.ReconciliationGroupRV;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.finance.infrastructure.support.mutiltable.MutilTableParam;
import com.wishare.finance.infrastructure.support.thread.AppRunnable;
import com.wishare.finance.infrastructure.support.thread.AppThreadManager;
import com.wishare.finance.infrastructure.utils.ListPageUtils;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对账应用服务
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReconciliationAppService {

    private final FlowClaimAppService flowClaimAppService;
    private final InvoiceDomainService invoiceDomainService;
    private final FlowDetailDomainService flowDetailDomainService;
    private final HandAccountDomainService handAccountDomainService;
    private final ReconcileRuleDomainService reconcileRuleDomainService;
    private final ReconciliationDomainService reconciliationDomainService;
    private final ReceivableBillRepository receivableBillRepository;
    private final BillRefundRepository billRefundRepository;
    private final VoucherBillDetailRepository voucherBillDetailRepository;
    private final BillReconciliationRepository billReconciliationRepository;
    private final ReconciliationRepository reconciliationRepository;
    private final SpaceClient spaceClient;
    private final ReconciliationYinlianDomainService reconciliationYinlianDomainService;
    private final ReconciliationYinlianRepository reconciliationYinlianRepository;
    private final FlowClaimRecordRepository flowClaimRecordRepository;
    private final FlowClaimDetailRepository flowClaimDetailRepository;
    private final ReconciliationDetailRepository reconciliationDetailRepository;
    private final GatherBillRepository gatherBillRepository;
    private final GatherDetailRepository gatherDetailRepository;
    /**
     * 分页查询对账单列表
     *
     * @param pageF
     * @return
     */
    public PageV<ReconciliationPageV> reconciliationPage(PageF<SearchF<ReconciliationE>> pageF) {
        PageV<ReconciliationPageV> page = RepositoryUtil.convertMoneyPage(reconciliationDomainService.reconciliationPage(pageF), ReconciliationPageV.class);
        List<ReconciliationPageV> reconciliationPageVS = reconciliationPageVList(page);
        return PageV.of(page.getPageNum(), page.getPageSize(), page.getTotal(), reconciliationPageVS);
    }

    /**
     * 分页查询对账单列表
     *
     * @param pageF
     * @return
     */
    public PageV<ReconciliationPageV> reconciliationPageClear(PageF<SearchF<ReconciliationE>> pageF) {
        PageV<ReconciliationPageV> page =  RepositoryUtil.convertMoneyPage(reconciliationDomainService.reconciliationPageClear(pageF),ReconciliationPageV.class);
        List<ReconciliationPageV> reconciliationPageVS = reconciliationPageVList(page);
        return PageV.of(page.getPageNum(), page.getPageSize(), page.getTotal(), reconciliationPageVS);
    }

    /**
     * 分页查询对账详情列表
     *
     * @param pageF
     * @return
     */
    public Page<ReconciliationDetailE> reconciliationDetailPage(PageF<SearchF<ReconciliationDetailE>> pageF) {

        String reconciliationId = (String)pageF.getConditions().getFields().get(0).getValue();
        ReconciliationE e = reconciliationRepository.getById(reconciliationId);
        if (ObjectUtil.isNull(e)){
            Page<ReconciliationDetailE> page = new Page<>();
            page.setRecords(Collections.emptyList());
            page.setSize(pageF.getPageSize());
            return page;
        }
        if (ObjectUtil.isNotNull(e.getReconcileMode()) &&
                ReconcileModeEnum.账票流水对账.getCode() == e.getReconcileMode()){
            return reconciliationDomainService.reconciliationDetailPage(pageF);
        }

        List<ReconciliationDetailE> list = reconciliationDetailRepository.list(Wrappers.<ReconciliationDetailE>lambdaQuery()
                .eq(ReconciliationDetailE::getReconciliationId, reconciliationId));

        List<ReconciliationDetailE> result = new ArrayList<>(list.size());

        if (CollUtil.isNotEmpty(list)) {
            Map<String, List<ReconciliationDetailE>> collect = list.stream().filter(v -> StrUtil.isNotBlank(v.getChannelSeqId()))
                    .collect(Collectors.groupingBy(ReconciliationDetailE::getChannelSeqId));
            collect.forEach((k,ll)->{
                String s = StrUtil.join(StrPool.COMMA, ll.stream().map(ReconciliationDetailE::getBillNo).collect(Collectors.toSet()));
                ReconciliationDetailE a = ll.get(0);
                a.setBillNo(s);
                result.add(a);
            });

        }
        for (ReconciliationDetailE d : list) {
            if (StrUtil.isBlank(d.getChannelSeqId())) {
                result.add(d);
            }
        }

        Page<ReconciliationDetailE> page = new Page<>();
        page.setCurrent(pageF.getPageNum());
        page.setSize(pageF.getPageSize());
        page.setRecords(ListPageUtils.page(result,(int) pageF.getPageNum(),(int)pageF.getPageSize()));
        page.setTotal(result.size());
        return page;




//        List<ReconciliationDetailPageV> reconciliationDetailPageVS = reconciliationDetailPageVList(page);
//        // 查找收款单号
//        for (ReconciliationDetailPageV reconciliationDetailPageV : reconciliationDetailPageVS) {
//            ReconciliationE byId = reconciliationRepository.getById(reconciliationDetailPageV.getReconciliationId());
//            List<ReconciliationRecBillDetailOBV> recBillDetailsById = reconciliationDomainService.getRecBillDetailsById(reconciliationDetailPageV.getBillId(), byId.getCommunityId());
//            List<Long> idList = recBillDetailsById.stream().map(ReconciliationRecBillDetailOBV::getBillId).collect(Collectors.toList());
//            List<String> noList = recBillDetailsById.stream().map(ReconciliationRecBillDetailOBV::getBillNo).collect(Collectors.toList());
//            reconciliationDetailPageV.setRecBillIdList(idList);
//            reconciliationDetailPageV.setRecBillNoList(noList);
//            reconciliationDetailPageV.setRecBillDetails(recBillDetailsById);
//        }
//        return page;
    }

    /**
     * 根据账单id和对账单id获取对账详情
     *
     * @param billId
     * @return
     */
    public ReconciliationDetailV reconciliationDetail(Long billId, Long reconciliationId, String supCpUnitId) {
        ReconciliationDetailE reconciliationDetail = reconciliationDomainService.getDetailByBillId(billId, reconciliationId);
        ReconciliationDetailV reconciliationDetailV = Global.mapperFacade.map(reconciliationDetail, ReconciliationDetailV.class);
        if (Objects.nonNull(reconciliationDetail)) {
            //流水信息
            if (Objects.nonNull(reconciliationDetail.getFlowIds()) && CollectionUtils.isNotEmpty(reconciliationDetail.getFlowIds())) {
                // 添加过滤 已解除的认领记录不显示
                List<FlowDetailE> flowDetailDomainServiceByIdList = flowDetailDomainService.getByIdList(reconciliationDetail.getFlowIds());
                if (null != flowDetailDomainServiceByIdList && flowDetailDomainServiceByIdList.size() > 0) {
                    List<FlowDetailE> flowDetailES =
                            flowDetailDomainServiceByIdList.stream().filter(s -> s.getClaimStatus() == 1).collect(Collectors.toList());
                    reconciliationDetailV.setFlowDetails(Global.mapperFacade.mapAsList(flowDetailES, ReconciliationFlowDetailOBV.class));
                } else {
                    reconciliationDetailV.setFlowDetails(new ArrayList<>());
                }
            } else {
                reconciliationDetailV.setFlowDetails(new ArrayList<>());
            }
            //账单信息
            if ((BillTypeEnum.收款单.equalsByCode(reconciliationDetailV.getBillType())) || BillTypeEnum.预收账单.equalsByCode(reconciliationDetailV.getBillType())
                    && Objects.nonNull(reconciliationDetail.getInvoiceIds())) {
                List<ReconciliationRecBillDetailOBV> recBillDetailsById = reconciliationDomainService.getRecBillDetailsById(reconciliationDetailV.getBillId(), supCpUnitId);
                List<Long> collect = recBillDetailsById.stream().filter(i -> null != i.getBillId()).map(ReconciliationRecBillDetailOBV::getBillId).collect(Collectors.toList());
                log.info("recBillDetailsById:" + JSON.toJSON(recBillDetailsById));
                log.info("collect:" + JSON.toJSON(collect));
                log.info("CollectionUtils.isNotEmpty(collect)" + CollectionUtils.isNotEmpty(collect));
                log.info("collect.stream().allMatch(Objects::nonNull)" + collect.stream().allMatch(Objects::nonNull));
                if (CollectionUtils.isNotEmpty(collect)   && collect.stream().allMatch(Objects::nonNull)) {
                    List<VoucherPushBillDetail> pushDetails = voucherBillDetailRepository.getPushDetails(collect);
                    Map<Long, List<VoucherPushBillDetail>> pushDetailsMap = pushDetails.stream().collect(Collectors.groupingBy(VoucherPushBillDetail::getBillId));
                    for (ReconciliationRecBillDetailOBV reconciliationRecBillDetailOBV : recBillDetailsById) {
                        Long billId1 = reconciliationRecBillDetailOBV.getBillId();
                        List<VoucherPushBillDetail> voucherPushBillDetails = pushDetailsMap.get(billId1);
                        if (CollectionUtils.isNotEmpty(voucherPushBillDetails)){
                            reconciliationRecBillDetailOBV.setVoucherBillNo(voucherPushBillDetails.get(0).getVoucherBillNo());
                        }
                    }
                }
                reconciliationDetailV.setRecBillDetails(recBillDetailsById);
            } else {
                reconciliationDetailV.setRecBillDetails(new ArrayList<>());
            }
            //票据信息
            if (Objects.nonNull(reconciliationDetail.getInvoiceIds())) {
                reconciliationDetailV.setInvoiceDetails(Global.mapperFacade.mapAsList(invoiceDomainService.getInvoiceReceiptByBillIds(List.of(billId),
                                reconciliationDetailV.getRecBillDetails().stream().map(ReconciliationRecBillDetailOBV::getBillId).collect(Collectors.toList())),
                        ReconciliationInvoiceDetailOBV.class));
            } else {
                reconciliationDetailV.setInvoiceDetails(new ArrayList<>());
            }
            //退款信息
            if (Objects.nonNull(reconciliationDetail.getRefundIds())) {
                reconciliationDetailV.setRefundDetails(reconciliationDomainService.getRefundInfos(new ReconcileRefundQuery(reconciliationDetail.getRefundIds())));
            } else {
                reconciliationDetailV.setRefundDetails(new ArrayList<>());
            }
            //if (Objects.nonNull(reconciliationDetail.getSettleIds())) {
            //    reconciliationDetailV.setSettleDetails(Global.mapperFacade.mapAsList(reconciliationDomainService.getSettleDetailOBVs(reconciliationDetail.getSettleIds()), ReconciliationSettleDetailOBV.class));
            //} else {
            //    reconciliationDetailV.setSettleDetails(new ArrayList<>());
            //}
            // 查询费项信息
            if (Objects.nonNull(reconciliationDetailV.getRecBillDetails())) {
                List<Long> collect = reconciliationDetailV.getRecBillDetails().stream().map(ReconciliationRecBillDetailOBV::getBillId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collect)   && collect.stream().allMatch(Objects::nonNull)) {
                    List<ReceivableBill> receivableBills = receivableBillRepository.getlistByIds(collect, supCpUnitId);
                    if(receivableBills.size() > 0 && receivableBills != null) {
                        reconciliationDetailV.setStartTime(receivableBills.get(0).getStartTime());
                        reconciliationDetailV.setEndTime(receivableBills.get(0).getEndTime());
                        List<ChargeItem> list = new ArrayList<>();
                        receivableBills.forEach( s ->{
                            boolean flag = false;
                            for (ChargeItem chargeItem : list) {
                                if (chargeItem.getChargeItemId().equals(s.getChargeItemId())) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                ChargeItem chargeItem = new ChargeItem();
                                chargeItem.setChargeItemId(s.getChargeItemId());
                                chargeItem.setChargeItemName(s.getChargeItemName());
                                list.add(chargeItem);
                            }
                            if (s.getStartTime().isBefore(reconciliationDetailV.getStartTime())){
                                reconciliationDetailV.setStartTime(s.getStartTime());
                            }
                            if (s.getEndTime().isAfter(reconciliationDetailV.getEndTime())){
                                reconciliationDetailV.setEndTime(s.getEndTime());
                            }
                        });
                        reconciliationDetailV.setChargeItemList(list);
                    }
                }
            }
        }
        return reconciliationDetailV;
    }

    /**
     * 发起对账
     *
     * @return
     */
    public boolean reconcile(String scheduleId, ReconcileModeEnum reconcileMode, CommunityList communityList) {

        //1.获取对账规则

        ReconcileRuleE reconcileRule = getAndCheckReconcileRule(scheduleId, reconcileMode);
        int executeType = 0;
        if (StringUtils.isNotBlank(scheduleId)) { //设置租户隔离
            IdentityInfo identityInfo = new IdentityInfo();
            identityInfo.setTenantId(reconcileRule.getTenantId());
            ThreadLocalUtil.set("IdentityInfo", identityInfo);
//            executeType = 1;
        }
        if (!reconcileRule.checkExecute(executeType)) {
            return true;
        }
        log.info("===================开始对账【tenant_id = " + reconcileRule.getTenantId() + "】=================================");
        if (ReconcileModeEnum.账票流水对账.equalsByCode(reconcileRule.getReconcileMode())) {
            reconcileBillInvoice(reconcileRule, reconcileRule.getTenantId(), communityList);
        } else if (ReconcileModeEnum.商户清分对账.equalsByCode(reconcileRule.getReconcileMode())) {
            reconcileMerchantClearing(reconcileRule, reconcileRule.getTenantId(), null);
        }
        log.info("===================结束对账【tenant_id = " + reconcileRule.getTenantId() + "】=================================");
        return true;
    }

    public boolean reconcileMerchantClearing(String scheduleId, ReconcileModeEnum reconcileMode, ReconcileMerchantClearingF reconcileMerchantClearingF) {

        //1.获取对账规则
        ReconcileRuleE reconcileRule = getAndCheckReconcileRule(scheduleId, reconcileMode);
        int executeType = 0;
        if (StringUtils.isNotBlank(scheduleId)) { //设置租户隔离
            IdentityInfo identityInfo = new IdentityInfo();
            identityInfo.setTenantId(reconcileRule.getTenantId());
            ThreadLocalUtil.set("IdentityInfo", identityInfo);
//            executeType = 1;
        }
        if (!reconcileRule.checkExecute(executeType)) {
            return true;
        }
        log.info("===================开始对账【tenant_id = " + reconcileRule.getTenantId() + "】=================================");
        reconcileMerchantClearing(reconcileRule, reconcileRule.getTenantId(), reconcileMerchantClearingF);
        log.info("===================结束对账【tenant_id = " + reconcileRule.getTenantId() + "】=================================");
        return true;
    }


    /**
     * 商户清分对账
     *
     * @param reconcileRule
     * @return
     */
    public boolean reconcileMerchantClearing(ReconcileRuleE reconcileRule, String tenantId, ReconcileMerchantClearingF reconcileMerchantClearingF) {
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
                try {
                    reconcileRule.execute();
                    reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                    // 获取当前日期
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.now().plusDays(-1);
                    String endDate = localDate.format(fmt);
                    // 定义环境标识
                    // 远洋从2023-12-01 00:00:00开始 判断开始时间小于该时间点，则赋值为2023-12-01 00:00:00
                    String beginDate = null;
                    if (EnvConst.YUANYANG.equals(EnvData.config)){
                       String date = "2024-01-30";
                        if (null != reconcileMerchantClearingF && null != reconcileMerchantClearingF.getReconciliationDate()) {
                            // 判断该时间是否大于开始时间，若是小于，则取开始时间
                            LocalDate parse1 = LocalDate.parse(date, fmt);
                            LocalDate parse2 = LocalDate.parse(reconcileMerchantClearingF.getReconciliationDate().get(0), fmt);
                            beginDate =  parse1.isAfter(parse2) ? date : reconcileMerchantClearingF.getReconciliationDate().get(0);
                            endDate = reconcileMerchantClearingF.getReconciliationDate().get(1);
                        } else {
                            beginDate = date;
                        }
                    } else {
                        if (null != reconcileMerchantClearingF && null != reconcileMerchantClearingF.getReconciliationDate()) {
                            beginDate = reconcileMerchantClearingF.getReconciliationDate().get(0);
                            endDate = reconcileMerchantClearingF.getReconciliationDate().get(1);
                        }
                    }


                    ArrayList<String> list = new ArrayList<>();
                    // 判断是否有传项目id  如果有传项目id 按照传入的项目对账，否则就根据租户id获取所有项目id 进行对账
                    if (null != reconcileMerchantClearingF && CollectionUtils.isNotEmpty(reconcileMerchantClearingF.getCommunityList())) {
                        for (Reconcile reconcile : reconcileMerchantClearingF.getCommunityList()) {
                            list.add(reconcile.getCommunityId());
                        }
                    } else {
                        // 获取所有项目
                        List<CommunityShortRV> communityByTenantId = spaceClient.getCommunityByTenantId(tenantId);
                        for (CommunityShortRV communityShortRV : communityByTenantId) {
                            list.add(communityShortRV.getId());
                        }
                    }
                    log.info("List list size:" + list.size());
                    // 获取所有项目
                    for (String s : list) {
                        log.info("商户清分对账进入项目列表循环:" + s);
                        // 线程添加项目id
                        MutilTableParam.supCpUnitId.set(s);
                        //1.根据维度获取账单分组
                        List<ReconciliationGroupRV> reconcileGroups = reconciliationDomainService.getReconcileGroups(reconcileRule);
                        log.info("商户清分对账查询对账记录分组信息 {}", JSONObject.toJSONString(reconcileGroups));
                        if (!CollectionUtils.isNotEmpty(reconcileGroups)) {
                            log.info("项目id:" + s + "无对账数据");
                            MutilTableParam.supCpUnitId.remove();
                            continue;
                        }
                        //3.根据分组获取每个分组的账单执行分组对账
                        try {
                            ReconcileQuery reconcileQuery = new ReconcileQuery();
                            reconcileQuery.setDimensionRuleQuery(Global.mapperFacade.map(reconcileRule.getDimensionRule(), ReconcileDimensionRuleQuery.class));
                            reconcileQuery.setPreconditionsQueries(Global.mapperFacade.mapAsList(reconcileRule.getPreconditions(), ReconcilePreconditionsQuery.class));
                            reconcileQuery.setPageSize(500);
                            // 添加 对账模式类型:商户清分对账
                            reconcileQuery.setReconcileMode(1);
                            ReconciliationE reconciliation;
                            for (ReconciliationGroupRV rg : reconcileGroups) {
                                //添加对账单
                                reconciliation = ReconciliationE.builder(reconcileRule)
                                        .statutoryBodyId(rg.getStatutoryBodyId())
                                        .statutoryBodyName(rg.getStatutoryBodyName())
                                        .communityId(rg.getCommunityId())
                                        .communityName(rg.getCommunityName())
                                        .reconcileTime(LocalDateTime.now())
                                        .costCenterId(rg.getCostCenterId())
                                        .costCenterName(rg.getCostCenterName())
                                        .sbAccountId(rg.getSbAccountId())
                                        .state(ReconcileRunStateEnum.运行中.getCode())
                                        .result(ReconcileResultEnum.未核对.getCode())
                                        .reconcileRuleId(reconcileRule.getId())
                                        .reconcileMode(ReconcileModeEnum.商户清分对账.getCode())
                                        .build();
                                if (EnvConst.YUANYANG.equals(EnvData.config)){
                                    reconcileQuery.setReconciliationBillQuery(new ReconciliationBillQuery()
                                            .setStatutoryBodyId(rg.getStatutoryBodyId())
                                            .setPayChannel(rg.getPayChannel())
                                            .setCommunityId(rg.getCommunityId())
                                            .setSbAccountId(rg.getSbAccountId())
                                            .setPayWay(SettleWayEnum.银联.getCode())
                                            .setPayChannel(rg.getPayChannel())
                                            .setCostCenterId(rg.getCostCenterId()));
                                } else {
                                    reconcileQuery.setReconciliationBillQuery(new ReconciliationBillQuery()
                                            .setStatutoryBodyId(rg.getStatutoryBodyId())
                                            .setPayChannel(rg.getPayChannel())
                                            .setCommunityId(rg.getCommunityId())
                                            .setSbAccountId(rg.getSbAccountId())
                                            .setPayWay(SettleWayEnum.线上.getCode())
                                            .setPayChannel(rg.getPayChannel())
                                            .setCostCenterId(rg.getCostCenterId()));
                                }
                                List<ReconciliationYinlianE> reconciliationYinlianES = null;
                                PageV<ReconciliationBillOBV> reconciliationBillPage = null;
                                List<ReconciliationBillOBV> records;
                                //构建对账单聚合
                                long pageNum = 0;
                                while (reconciliationBillPage == null || !reconciliationBillPage.isLast()) {
                                    ReconciliationClearA reconciliationClearA = new ReconciliationClearA();
                                    reconcileQuery.setPageNum(++pageNum);
                                    //获取对账账单数据
                                    reconciliationBillPage = reconciliationDomainService.getMerchantClearingReconciliationBillPage(reconcileQuery, endDate, beginDate);
                                    records = reconciliationBillPage.getRecords();
                                    if (CollectionUtils.isEmpty(records)) {
                                        break;
                                    }
                                    log.info("records空记录id信息:" + JSON.toJSON(records.stream().filter(a -> StringUtils.isBlank(a.getTradeNo())).map(ReconciliationBillOBV::getId).collect(Collectors.toList())));
                                    log.info("records记录:" + JSON.toJSON(records.stream().map(ReconciliationBillOBV::getTradeNo).collect(Collectors.toList())));
                                    if (pageNum >= 1) {
                                        reconciliationDomainService.addReconciliation(reconciliation);
                                        Global.mapperFacade.map(reconciliation, reconciliationClearA);
                                    }
                                    // 添加环境标识
                                    // 远洋查询字段不一致
                                    if (EnvConst.YUANYANG.equals(EnvData.config)){
                                        reconciliationYinlianES = reconciliationYinlianDomainService.listYYNotReconcile(records.stream()
                                                .filter(item -> StringUtils.isNotBlank(item.getBankFlowNo())).map(ReconciliationBillOBV::getBankFlowNo).collect(Collectors.toList()));
                                    } else {
                                        reconciliationYinlianES = reconciliationYinlianDomainService.listNotReconcile(records.stream()
                                                .filter(item -> StringUtils.isNotBlank(item.getTradeNo())).map(ReconciliationBillOBV::getTradeNo).collect(Collectors.toList()));
                                    }
                                    Global.mapperFacade.map(reconciliation, reconciliationClearA);
                                    try {
                                        reconciliationClearA.setReconciliationYinlianEList(reconciliationYinlianES);
                                        reconciliationClearA.setReconciliationBillOBVList(records);
                                        reconciliationClearA.reconcile();//执行对账
                                        if (CollectionUtils.isNotEmpty(reconciliationClearA.getReconciliationDetails())) {
                                            //添加对账详情
                                            reconciliationDomainService.reconcileBatch(reconciliationClearA.getReconciliationDetails());
                                            //更新对账单信息
                                            reconciliationDomainService.updateReconciliation(reconciliationClearA);
                                            List<Long> rids = reconciliationYinlianES.stream().map(ReconciliationYinlianE::getId).collect(Collectors.toList());
                                            //更新核对信息
                                            if (rids.size() > 0) {
                                                reconciliationYinlianDomainService.updateStateByIds(rids, 1);
                                            }
                                        }
                                        reconciliationClearA.finish();
                                    } catch (Exception e) {
                                        log.error("对账单[" + reconciliationClearA.getId() + "]对账异常，", e);
                                        reconciliationClearA.error();
                                    } finally {
                                        reconciliationDomainService.updateReconciliation(reconciliationClearA);
                                        reconciliation.setId(null);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.error("商户清分对账对账异常，", e);
                        } finally {
                            //对账完成
                            reconcileRule.finish();
                            reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                        }
                        MutilTableParam.supCpUnitId.remove();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("对账异常，异常信息:" , e);
                } finally {
                    reconcileRule.finish();
                    reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                }
            }
        });
        return true;
    }


    /**
     * 生成需要对账的项目id，封装为一个list
     *
     * @param tenantId
     * @param communityList
     * @return
     */
    private ArrayList<String> setCommunityIdList(String tenantId, CommunityList communityList) {
        ArrayList<String> list = new ArrayList<>();
        // 判断是否有传项目id  如果有传项目id 按照传入的项目对账，否则就根据租户id获取所有项目id 进行对账
        if (null != communityList && CollectionUtils.isNotEmpty(communityList.getCommunityList())) {
            for (Reconcile reconcile : communityList.getCommunityList()) {
                list.add(reconcile.getCommunityId());
            }
        } else {
            // 获取所有项目
            List<CommunityShortRV> communityByTenantId = spaceClient.getCommunityByTenantId(tenantId);
            for (CommunityShortRV communityShortRV : communityByTenantId) {
                list.add(communityShortRV.getId());
            }
        }
        return list;
    }

    /**
     * 封装对账查询条件
     */

    private ReconcileQuery createReconcileQuery(ReconcileRuleE reconcileRule) {
        ReconcileQuery reconcileQuery = new ReconcileQuery();
        reconcileQuery.setDimensionRuleQuery(Global.mapperFacade.map(reconcileRule.getDimensionRule(), ReconcileDimensionRuleQuery.class));
        reconcileQuery.setPreconditionsQueries(Global.mapperFacade.mapAsList(reconcileRule.getPreconditions(), ReconcilePreconditionsQuery.class));
        reconcileQuery.setPageSize(500);
        reconcileQuery.setReconcileMode(0);
        return reconcileQuery;
    }

    /**
     * 账票流水对账
     *
     * @param reconcileRule
     * @return
     */
    public boolean reconcileBillInvoice(ReconcileRuleE reconcileRule, String tenantId, CommunityList communityList) {
        AppThreadManager.execute(new AppRunnable() {
            @Override
            public void execute() {
                reconcileBillInv(reconcileRule,tenantId,communityList);
            }
        });
        return true;
    }

    private void reconcileBillInv(ReconcileRuleE reconcileRule, String tenantId, CommunityList communityList) {
        try {
            reconcileHandle(reconcileRule, tenantId, communityList);
            //对账结束
        }
        catch (Exception e) {
            log.error("对账异常:" + e);
        } finally {
            //对账完成
            reconcileRule.finish();
            reconcileRuleDomainService.updateNonCheckState(reconcileRule);
        }
    }

    private void reconcileHandle(ReconcileRuleE reconcileRule, String tenantId, CommunityList communityList) {
        log.info(ReconcileModeEnum.账票流水对账.getValue()+"开始对账=========");
        if (EnvConst.FANGYUAN.equals(EnvData.config) || EnvConst.LINGANG.equals(EnvData.config)) {
            ArrayList<String> list = new ArrayList<>();
            // 判断是否有传项目id  如果有传项目id 按照传入的项目对账，否则就根据租户id获取所有项目id 进行对账
            if (null != communityList && CollectionUtils.isNotEmpty(communityList.getCommunityList())) {
                for (Reconcile reconcile : communityList.getCommunityList()) {
                    list.add(reconcile.getCommunityId());
                }
            } else {
                // 获取所有项目
                List<CommunityShortRV> communityByTenantId = spaceClient.getCommunityByTenantId(tenantId);
                for (CommunityShortRV communityShortRV : communityByTenantId) {
                    list.add(communityShortRV.getId());
                }
            }
            // 1、根据项目遍历对账
            for (int i = 0; i < list.size(); i++) {
                MutilTableParam.supCpUnitId.set(list.get(i));
                // 2、去获取流水认领记录
                // 2.1 该项目下 所有已认领的数据
                List<FlowReconciliationDetailDto> newReconciliationFlows = reconciliationDomainService.getNewReconciliationFlows(list.get(i));
                if (!CollectionUtils.isNotEmpty(newReconciliationFlows)) {
                    log.info(ReconcileModeEnum.账票流水对账.getValue()+"项目id:" + list.get(i) + "无流水认领数据， 不参与对账");
                    MutilTableParam.supCpUnitId.remove();
                    continue;
                }
                // 2.2  对流水数据根据认领类型进行分组
                // 根据认领id 类型 分成四个list  认领ID类型：1:蓝票;2:红票;3:收款单;4:退款单;
                Map<String, List<FlowReconciliationDetailDto>> claimIdTypeMap = newReconciliationFlows.stream().collect(Collectors.groupingBy(FlowReconciliationDetailDto::getClaimIdType));
                // 蓝票id集合
                List<Long> blueInvoiceIdList = null;
                // 红票id集合
                List<Long> RedInvoiceIdList = null;
                List<Long> gatherBillIdList = null;
                List<Long> payBillIdList = null;
                // 发票id合计
                List<Long> invoiceIdList = new ArrayList<>();
                if (null != claimIdTypeMap.get("1")) {
                    blueInvoiceIdList = claimIdTypeMap.get("1").stream().map(FlowReconciliationDetailDto::getInvoiceId).collect(Collectors.toList());
                    invoiceIdList.addAll(blueInvoiceIdList);
                }
                if (null != claimIdTypeMap.get("2")) {
                    RedInvoiceIdList = claimIdTypeMap.get("2").stream().map(FlowReconciliationDetailDto::getInvoiceId).collect(Collectors.toList());
                    invoiceIdList.addAll(RedInvoiceIdList);
                }
                if (null != claimIdTypeMap.get("3")) {
                    gatherBillIdList = claimIdTypeMap.get("3").stream().map(FlowReconciliationDetailDto::getInvoiceId).collect(Collectors.toList());
                }
                if (null != claimIdTypeMap.get("4")) {
                    payBillIdList = claimIdTypeMap.get("4").stream().map(FlowReconciliationDetailDto::getInvoiceId).collect(Collectors.toList());
                }
                //2.根据维度获取账单分组
                List<ReconciliationGroupRV> reconcileGroups = reconciliationDomainService.getReconcileGroups(reconcileRule);
                log.info(ReconcileModeEnum.账票流水对账.getValue()+"查询对账记录分组信息 {}", JSONObject.toJSONString(reconcileGroups));
                if (!CollectionUtils.isNotEmpty(reconcileGroups)) {
                    log.info(ReconcileModeEnum.账票流水对账.getValue()+"项目id:" + list.get(i) + "无对账数据");
                    MutilTableParam.supCpUnitId.remove();
                    continue;
                }
                reconcileRule.execute();
                reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                //3.根据分组获取每个分组的账单执行分组对账
                try {
                    ReconcileQuery reconcileQuery = new ReconcileQuery();
                    reconcileQuery.setDimensionRuleQuery(Global.mapperFacade.map(reconcileRule.getDimensionRule(), ReconcileDimensionRuleQuery.class));
                    reconcileQuery.setPreconditionsQueries(Global.mapperFacade.mapAsList(reconcileRule.getPreconditions(), ReconcilePreconditionsQuery.class));
                    reconcileQuery.setPageSize(500);
                    reconcileQuery.setReconcileMode(0);
                    reconcileQuery.setInvoiceIdList(invoiceIdList);
                    reconcileQuery.setGatherBillIdList(gatherBillIdList);
                    reconcileQuery.setPayBillIdList(payBillIdList);
                    ReconciliationE reconciliation;
                    for (ReconciliationGroupRV rg : reconcileGroups) {
                        //得到对应分组信息
                        reconciliation = ReconciliationE.builder(reconcileRule)
                                .statutoryBodyId(rg.getStatutoryBodyId())
                                .statutoryBodyName(rg.getStatutoryBodyName())
                                .communityId(rg.getCommunityId())
                                .communityName(rg.getCommunityName())
                                .reconcileTime(LocalDateTime.now())
                                .costCenterId(rg.getCostCenterId())
                                .costCenterName(rg.getCostCenterName())
                                .sbAccountId(rg.getSbAccountId())
                                .reconcileMode(ReconcileModeEnum.账票流水对账.getCode())
                                .state(ReconcileRunStateEnum.运行中.getCode())
                                .result(ReconcileResultEnum.未核对.getCode())
                                .reconcileRuleId(reconcileRule.getId())
                                .build();


                        reconcileQuery.setReconciliationBillQuery(new ReconciliationBillQuery()
                                .setStatutoryBodyId(rg.getStatutoryBodyId())
                                .setPayChannel(rg.getPayChannel())
                                .setCommunityId(rg.getCommunityId())
                                .setSbAccountId(rg.getSbAccountId())
                                .setCostCenterId(rg.getCostCenterId()));

                        //构建对账单聚合
                        ReconciliationA reconciliationA = new ReconciliationA();
                        Global.mapperFacade.map(reconciliation, reconciliationA);
                        try {
                            PageV<ReconciliationBillOBV> reconciliationBillPage = null;
                            PageV<ReconciliationBillOBV> reconciliationPayBillPage = null;
                            PageV<ReconciliationBillOBV> reconciliationGatherBillPage = null;
                            long pageNum = 0;
                            List<ReconciliationBillOBV> records = new ArrayList<>();
                            while (reconciliationBillPage == null || !reconciliationBillPage.isLast()) {
                                reconcileQuery.setPageNum(++pageNum);
                                // 已经开票的账单数据
                                if (null != reconcileQuery.getInvoiceIdList() && reconcileQuery.getInvoiceIdList().size() > 0) {
                                    reconciliationBillPage = reconciliationDomainService.getReconciliationInvoiceBillPage(reconcileQuery);
                                }
                                // 退款单数据
                                if (null != reconcileQuery.getPayBillIdList() && reconcileQuery.getPayBillIdList().size() > 0) {
                                    reconciliationPayBillPage = reconciliationDomainService.getReconciliationPayBillPage(reconcileQuery);
                                }
                                // 查询收款单数据开票或为开票 按照账单认领
                                if (null != reconcileQuery.getGatherBillIdList() && reconcileQuery.getGatherBillIdList().size() > 0) {
                                    reconciliationGatherBillPage = reconciliationDomainService.getFYReconciliationGatherBillPage(reconcileQuery);
                                }
                                log.info(ReconcileModeEnum.账票流水对账.getValue()+"已经开票的账单数据 {}, 退款单数据{}, 收款单数据{}",
                                        JSONObject.toJSONString(reconciliationBillPage),
                                        JSONObject.toJSONString(reconciliationPayBillPage), JSONObject.toJSONString(reconciliationGatherBillPage));

                                if ((null == reconciliationBillPage || CollectionUtils.isEmpty(reconciliationBillPage.getRecords())) && (null == reconciliationPayBillPage || CollectionUtils.isEmpty(reconciliationPayBillPage.getRecords()))
                                        && (null == reconciliationGatherBillPage || CollectionUtils.isEmpty(reconciliationGatherBillPage.getRecords()))) {
                                    break;
                                }
                                if (null != reconciliationBillPage && !CollectionUtils.isEmpty(reconciliationBillPage.getRecords())) {
                                    records.addAll(reconciliationBillPage.getRecords());
                                }
                                if (null != reconciliationPayBillPage && !CollectionUtils.isEmpty(reconciliationPayBillPage.getRecords())) {
                                    records.addAll(reconciliationPayBillPage.getRecords());
                                }
                                if (null != reconciliationGatherBillPage && !CollectionUtils.isEmpty(reconciliationGatherBillPage.getRecords())) {
                                    records.addAll(reconciliationGatherBillPage.getRecords());
                                }

                                Map<Integer, List<ReconciliationBillOBV>> rbMap = records.stream().collect(Collectors.groupingBy(ReconciliationBillOBV::getBillType));

                                List<Long> collect = records.stream().map(ReconciliationBillOBV::getId).collect(Collectors.toList());
                                //获取循环
                                List<ReconciliationInvoiceDetailOBV> invoiceReceipts = new ArrayList<>();
                                if (null != reconciliationBillPage && !CollectionUtils.isEmpty(reconciliationBillPage.getRecords())) {
                                    Map<Integer, List<ReconciliationBillOBV>> collect1 = reconciliationBillPage.getRecords().stream().collect(Collectors.groupingBy(ReconciliationBillOBV::getBillType));
                                    for (Map.Entry<Integer, List<ReconciliationBillOBV>> reBill : collect1.entrySet()) {
                                        List<Long> billIds = reBill.getValue().stream().map(ReconciliationBillOBV::getId).collect(Collectors.toList());
                                        BillTypeEnum billType = BillTypeEnum.valueOfByCode(reBill.getKey());
                                        switch (billType) {
                                            case 预收账单:
                                            case 收款单:
                                            case 应收账单:
                                            case 临时收费账单:
                                                //退款信息
                                                reconciliationA.setRefundDetails(reconciliationDomainService.getReconciliationGatherRefunds(new ReconcileGatherRefundQuery(billIds)));
                                                //

                                                invoiceReceipts.addAll(reconciliationDomainService.getInvoiceByGatherBillIds(new GatherInvoiceQuery(billIds)));
                                                break;
                                            case 付款单:
                                                invoiceReceipts.addAll(reconciliationDomainService.getInvoiceByPayBillIds(new PayInvoiceQuery(billIds)));
                                                break;
                                        }
                                    }
                                }

                                reconciliationA.setInvoiceDetails(invoiceReceipts);
                                if (CollectionUtils.isNotEmpty(invoiceReceipts)) {
                                    for (ReconciliationInvoiceDetailOBV invoiceReceipt : invoiceReceipts) {
                                        collect.add(invoiceReceipt.getId());
                                    }
                                }
                                reconciliationA.setFlowDetails(reconciliationDomainService.getReconciliationFlows(new FlowsInvoiceQuery(collect, FlowClaimDetailStatusEnum.正常)));
                                // 如果没有认领流水则不对账
                                if (CollectionUtils.isEmpty(reconciliationA.getFlowDetails())) {
                                    break;
                                }
                                reconciliationA.setBills(records);

                                //添加对账单
                                reconciliationDomainService.addReconciliation(reconciliation);
                                reconciliationA.setId(reconciliation.getId());
                                reconciliationA.reconcile(); //执行对账
                                if (CollectionUtils.isNotEmpty(reconciliationA.getReconciliationDetails())) {
                                    //添加对账详情
                                    reconciliationDomainService.reconcileBatch(reconciliationA.getReconciliationDetails());
                                    //更新对账单信息
                                    reconciliationDomainService.updateReconciliation(reconciliationA);
                                    // 更新流水认领表 对账状态
                                    // 一个批次流水存在部分对账情况
                                    List<FlowClaimDetailE> flowClaimDetailByInvoiceIds = reconciliationDomainService.getFlowClaimDetailByInvoiceIds(reconciliationA.getFlowDetails());
                                    log.info("查询flowClaimDetailByInvoiceIds:" + JSON.toJSON(flowClaimDetailByInvoiceIds));
                                    Map<Long, List<FlowClaimDetailE>> collect1 = flowClaimDetailByInvoiceIds.stream().collect(Collectors.groupingBy(FlowClaimDetailE::getFlowClaimRecordId));
                                    log.info("查询flowClaimDetailByInvoiceIds之后分组");
                                    for (Long l : collect1.keySet()) {
                                        log.info("循环开始getFlowClaimRecordId:" + l);
                                        // 所有收款单id
                                        List<Long> collect2 = collect1.get(l).stream().map(FlowClaimDetailE::getFlowClaimRecordId).collect(Collectors.toList());
                                        // flow_claim_detail 表中的 flow_claim_record_id
                                        List<FlowClaimDetailE> byFlowClaimRecordId = reconciliationDomainService.getByFlowClaimRecordId(collect2);
                                        List<Long> longList = byFlowClaimRecordId.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
                                        log.info("所有收款单id:" + JSON.toJSON(longList));
                                        // 查找所有收款单 是否全部对账
                                        List<GatherBill> gatherBill = gatherBillRepository.getGatherBill(longList, MutilTableParam.supCpUnitId.get());
                                        log.info("gatherBill:" + JSON.toJSONString(gatherBill));
                                        if (gatherBill.stream().anyMatch(bill -> bill.getReconcileState().equals(0))) {
                                            reconciliationDomainService.updateReconcileFlag(l, 2);
                                        } else {
                                            reconciliationDomainService.updateReconcileFlag(l, 1);
                                        }
                                    }
                                    // reconciliationDomainService.updateFlowClaimRecordFlag(reconciliationA.getFlowDetails());
                                    //清除对账
                                    reconciliationA.clear();
                                }
                            }
                            //对账结束
                            reconciliationA.finish();
                        } catch (Exception e) {
                            log.error("对账单[" + reconciliationA.getId() + "]对账异常，", e);
                            reconciliationA.error();
                        } finally {
                            reconciliationDomainService.updateReconciliation(reconciliationA);
                        }
                    }
                } catch (Exception e) {
                    log.error("账票流水对账异常，", e);
                } finally {
                    //对账完成
                    reconcileRule.finish();
                    reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                }
                MutilTableParam.supCpUnitId.remove();
            }
        }
        else {
            // 生成需要对账的项目id，封装为一个list
            ArrayList<String> list = setCommunityIdList(tenantId, communityList);
            // 1、根据项目遍历进行对账
            for (int i = 0; i < list.size(); i++) {
                MutilTableParam.supCpUnitId.set(list.get(i));
                // 2、去获取流水认领记录
                List<FlowReconciliationDetailDto> newReconciliationFlows = reconciliationDomainService.getNewReconciliationFlows(list.get(i));
                // 2.1 如果没有改项目没有认领流水，则不参与对账 跳过该项目
                if (!CollectionUtils.isNotEmpty(newReconciliationFlows)) {
                    log.info("项目id:" + list.get(i) + "无流水认领数据， 不参与对账");
                    MutilTableParam.supCpUnitId.remove();
                    continue;
                }
                //2.根据对账维度获取账单分组
                List<ReconciliationGroupRV> reconcileGroups = reconciliationDomainService.getReconcileGroups(reconcileRule);
                log.info("查询对账记录分组信息 {}", JSONObject.toJSONString(reconcileGroups));
                if (!CollectionUtils.isNotEmpty(reconcileGroups)) {
                    log.info("项目id:" + list.get(i) + "无对账数据");
                    MutilTableParam.supCpUnitId.remove();
                    continue;
                }
                //3.对账规则更新为对账中
                reconcileRule.execute();
                reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                //4.根据分组查询收款单/预收单数据
                //4.1 构建查询账单的查找条件
                ReconcileQuery reconcileQuery = createReconcileQuery(reconcileRule);
                ReconciliationE reconciliation;
                try {
                    for (ReconciliationGroupRV rg : reconcileGroups) {
                        //得到对应分组信息
                        reconciliation = ReconciliationE.builder(reconcileRule)
                                .statutoryBodyId(rg.getStatutoryBodyId())
                                .statutoryBodyName(rg.getStatutoryBodyName())
                                .communityId(rg.getCommunityId())
                                .communityName(rg.getCommunityName())
                                .reconcileTime(LocalDateTime.now())
                                .costCenterId(rg.getCostCenterId())
                                .costCenterName(rg.getCostCenterName())
                                .sbAccountId(rg.getSbAccountId())
                                .reconcileMode(ReconcileModeEnum.账票流水对账.getCode())
                                .state(ReconcileRunStateEnum.运行中.getCode())
                                .result(ReconcileResultEnum.未核对.getCode())
                                .reconcileRuleId(reconcileRule.getId())
                                .build();

                        reconcileQuery.setReconciliationBillQuery(new ReconciliationBillQuery()
                                .setStatutoryBodyId(rg.getStatutoryBodyId())
                                .setPayChannel(rg.getPayChannel())
                                .setCommunityId(rg.getCommunityId())
                                .setSbAccountId(rg.getSbAccountId())
                                .setCostCenterId(rg.getCostCenterId()));

                        //构建对账单聚合
                        // 根据收款单 查询未对账的数据。
                        PageV<ReconciliationBillOBV> reconciliationGatherBillPage = reconciliationDomainService.getReconciliationGatherBillPage(reconcileQuery);


                        if ((null == reconciliationGatherBillPage || CollectionUtils.isEmpty(reconciliationGatherBillPage.getRecords())) && (null == reconciliationGatherBillPage || CollectionUtils.isEmpty(reconciliationGatherBillPage.getRecords()))
                                && (null == reconciliationGatherBillPage || CollectionUtils.isEmpty(reconciliationGatherBillPage.getRecords()))) {
                            continue;
                        }

                        List<ReconciliationBillOBV> records = reconciliationGatherBillPage.getRecords();

                        reconciliationDomainService.addReconciliation(reconciliation);
                        ReconciliationA reconciliationA = new ReconciliationA();
                        Global.mapperFacade.map(reconciliation, reconciliationA);
                        try{
                            reconciliationA.setBills(records);
                            // 执行对账
                            newReconcile(reconciliationA);
                            if (CollectionUtils.isNotEmpty(reconciliationA.getReconciliationDetails())) {
                                //添加对账详情
                                reconciliationDomainService.reconcileBatch(reconciliationA.getReconciliationDetails());
                                //更新对账单信息
                                reconciliationDomainService.updateReconciliation(reconciliationA);
                                // 更新流水认领表 对账状态
                                // 对账对平之后更新流水认领状态
                                if (null != reconciliationA.getFlowDetails()) {
                                    reconciliationDomainService.updateFlowClaimRecordFlag(reconciliationA.getFlowDetails());
                                }
                                //清除对账
                                reconciliationA.clear();
                            }
                            reconciliationA.finish();
                            reconciliationDomainService.updateReconciliation(reconciliationA);
                        } catch (Exception e) {
                            log.error("对账过程异常:" + e);
                            throw new RuntimeException();
                        } finally {
                            reconciliationA.finish();
                            reconciliationDomainService.updateReconciliation(reconciliationA);
                        }

                    }
                } catch (Exception e) {
                    log.error("对账过程异常:" + e);
                } finally {
                    reconcileRule.finish();
                    reconcileRuleDomainService.updateNonCheckState(reconcileRule);
                }
                MutilTableParam.supCpUnitId.remove();
            }

        }

        log.info(ReconcileModeEnum.账票流水对账.getValue()+"结束对账=========");
    }

    private void newReconcile(ReconciliationA reconciliationA) {
        if (CollectionUtils.isEmpty(reconciliationA.getBills())) {
            return;
        }
        List<ReconciliationBillOBV> bills = reconciliationA.getBills();
        // key 为收款单id
        Map<Long, List<ReconciliationBillOBV>> mapByGatherBillId = bills.stream().collect(Collectors.groupingBy(ReconciliationBillOBV::getId));
        for (Map.Entry<Long, List<ReconciliationBillOBV>> longListEntry : mapByGatherBillId.entrySet()) {
            // 创建一个对账明细对
            ReconciliationDetailE reconciliationDetailE = new ReconciliationDetailE();
            reconciliationA.setBillCount(reconciliationA.getBillCount() + 1L);
            List<ReconciliationBillOBV> value = longListEntry.getValue();
            List<Long> collect = value.stream().map(ReconciliationBillOBV::getRecId).collect(Collectors.toList());
            // 通过应收单id 查询发票
            List<ReconciliationInvoiceDetailOBV> invoiceByRecBillIds = reconciliationDomainService.getInvoiceByRecBillIds(new ReceivableInvoiceQuery(collect));
            // 计算发票金额
            long invoiceAmount = invoiceByRecBillIds.stream().mapToLong(ReconciliationInvoiceDetailOBV::getInvoiceAmount).sum();
            // 计算账单金额
            long actualAmount = value.stream().mapToLong(ReconciliationBillOBV::getActualAmount).sum();
            long refundAmount = value.stream().mapToLong(ReconciliationBillOBV::getRefundAmount).sum();
            // 判断退款金额 是否为0  查询退款记录
            List<BillRefundE> billRefundES = new ArrayList<>();
            if (refundAmount == 0L){
                billRefundES  = billRefundRepository.listByBillId(longListEntry.getKey());
            }
            //  查询发票id 或者收款单id 是否认领
            List<Long> invoiceIds = invoiceByRecBillIds.stream().map(ReconciliationInvoiceDetailOBV::getId).collect(Collectors.toList());
            invoiceIds.add(longListEntry.getKey());
            List<ReconciliationFlowDetailOBV> reconciliationFlows = reconciliationDomainService.getReconciliationFlows(new FlowsInvoiceQuery(invoiceIds, FlowClaimDetailStatusEnum.正常));
            // 校验 发票  流水 账单金额 是否一致  判断是否认领流水
            // 流水金额可以不等于发票金额或者账单金额  即认领流水即可
            if (reconciliationFlows.size() > 0 && actualAmount == invoiceAmount) {
                reconciliationDetailE.setResult(ReconcileResultEnum.已核对.getCode());
                // 若是一致 则对平数据+1
                reconciliationA.setBalanceCount(reconciliationA.getBalanceCount() + 1L);
                reconciliationA.setFlowDetails(reconciliationFlows);
            } else {
                reconciliationDetailE.setResult(ReconcileResultEnum.核对失败.getCode());
            }
            long flowAmount = reconciliationFlows.stream().mapToLong(ReconciliationFlowDetailOBV::getFlowAmount).sum();
            reconciliationDetailE.setBillNo(value.get(0).getBillNo());
            reconciliationDetailE.setBillId(longListEntry.getKey());
            reconciliationDetailE.setCostCenterId(value.get(0).getCostCenterId() == null ? "" : value.get(0).getCostCenterId());
            reconciliationDetailE.setCostCenterName(value.get(0).getCostCenterName() == null ? "" : value.get(0).getCostCenterName());
            reconciliationDetailE.setStatutoryBodyId(value.get(0).getStatutoryBodyId() == null ? null : value.get(0).getStatutoryBodyId());
            reconciliationDetailE.setStatutoryBodyName(value.get(0).getStatutoryBodyName() == null ? "" : value.get(0).getStatutoryBodyName());
            reconciliationDetailE.setBillType(value.get(0).getBillType());
            reconciliationDetailE.setReconciliationId(reconciliationA.getId());
            reconciliationDetailE.setReconcileTime(reconciliationA.getReconcileTime());
            reconciliationDetailE.setReconcileMode(reconciliationA.getReconcileMode());
            // 发票id 去重
            reconciliationDetailE.setInvoiceIds(invoiceByRecBillIds.stream().map(ReconciliationInvoiceDetailOBV::getId).collect(Collectors.toList()).
                    stream().distinct().collect(Collectors.toList()));
            // 流水id 去重
            reconciliationDetailE.setFlowIds(reconciliationFlows.stream().map(ReconciliationFlowDetailOBV::getId).collect(Collectors.toList()).
                    stream().distinct().collect(Collectors.toList()));
            // 退款单id
            reconciliationDetailE.setRefundIds(billRefundES.stream().map(BillRefundE::getId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList()));
            reconciliationDetailE.setActualAmount(actualAmount);
            reconciliationDetailE.setInvoiceAmount(invoiceAmount);
            reconciliationDetailE.setFlowClaimAmount(flowAmount);
            reconciliationDetailE.setRefundAmount(refundAmount);
            reconciliationDetailE.setReceivableAmount(value.stream().mapToLong(ReconciliationBillOBV::getNoReductionAmount).sum());
            reconciliationDetailE.setSysSource(value.get(0).getSysSource() == null ? null : value.get(0).getSysSource());
            reconciliationDetailE.doReconcileResult();
            reconciliationA.setActualTotal(reconciliationA.getActualTotal() + actualAmount);
            reconciliationA.setInvoiceTotal(reconciliationA.getInvoiceTotal() + invoiceAmount);
            reconciliationA.setFlowClaimTotal(reconciliationA.getFlowClaimTotal() + flowAmount);
            // 添加对账明细
            reconciliationA.addReconciliationDetail(reconciliationDetailE);
        }
    }

    /**
     * 获取对账规则
     *
     * @param scheduleId
     * @return
     */
    public ReconcileRuleE getAndCheckReconcileRule(String scheduleId, ReconcileModeEnum reconcileMode) {
        ReconcileRuleE reconcileRule;
        if (StringUtils.isNotBlank(scheduleId)) {
            reconcileRule = reconcileRuleDomainService.getByScheduleId(scheduleId);
        } else {
            reconcileRule = reconcileRuleDomainService.getByMode(reconcileMode.getCode());
        }
        ErrorAssertUtil.notNullThrow300(reconcileRule, ErrorMessage.RECONCILE_RULE_NOT_EXIST);
        ErrorAssertUtil.isFalseThrow402(ReconcileExecuteStateEnum.运行中.equalsByCode(reconcileRule.getExecuteState()), ErrorMessage.RECONCILE_RULE_IS_RUNNING);
        return reconcileRule;
    }


    /**
     * 解除绑定
     *
     * @param unclaimF
     * @return
     */
    @Transactional
    public boolean unclaim(UnclaimF unclaimF) {
        if (CollectionUtils.isEmpty(unclaimF.getReconcileDetailIdList())){
            List<String> list = new ArrayList<>();
            list.add(unclaimF.getReconcileDetailId());
            unclaimF.setReconcileDetailIdList(list);
        }
        for (String reconcileDetailId : unclaimF.getReconcileDetailIdList()) {
            ReconciliationDetailE reconciliationDetail = reconciliationDomainService.getReconciliationDetailById(reconcileDetailId);
            log.info("根据对账详情id查询对账详情:" + JSON.toJSON(reconciliationDetail));
            // ErrorAssertUtil.notNullThrow404(ErrorMessage.RECONCILE_DETAIL_NOT_EXIST);
            if (ObjectUtil.isNull(reconciliationDetail)){
                continue;
            }
            // 根据流水id  获取到当前批次的所有账单id
            List<Long> flowIds = reconciliationDetail.getFlowIds();
            Long flowClaimRecordId = flowClaimRecordRepository.queryIdByFlowId(flowIds.get(0));
            log.info("查询流水认领记录id:" + flowClaimRecordId);
            List<Long> arrayList = new ArrayList<>();
            arrayList.add(flowClaimRecordId);
            List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(arrayList);
            log.info("根据流水认领记录查询流水认领记录:" + JSON.toJSON(flowClaimDetailES));
            // 获取所有账单id 判断该批次下的账单是否有推凭的
            List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            List<ReconciliationDetailE> byBillIds = reconciliationDomainService.getByBillIdsAndResult(collect);
            log.info("查询该批次所有对账明细:" + JSON.toJSON(byBillIds));
            // 如果有，则提示报错
            List<String> voucherBillNoList = byBillIds.stream().map(ReconciliationDetailE::getVoucherBillNo).collect(Collectors.toList());
            log.info("voucherBillNoList.stream().allMatch(StringUtils::isBlank):" + voucherBillNoList.stream().allMatch(StringUtils::isBlank));
            if (voucherBillNoList.stream().allMatch(StringUtils::isBlank)){
                //根据发票获取关联的流水的信息
                List<Long> flowDetailIds = flowDetailDomainService.getFlowDetailIdsByFlowIds(flowIds);
                log.info("flowDetailIds" + JSON.toJSON(flowDetailIds));
                //解除流水认领
                // flowClaimAppService.revokedForRecon(new RevokedFlowF(flowDetailIds));
                //反交账
                //
                // handAccountDomainService.reversal(reconciliationDetail.getBillId(), BillTypeEnum.valueOfByCode(reconciliationDetail.getBillType()), unclaimF.getSupCpUnitId());
                // 更新对账表 ，对账明细表
                // 判断该主表下 是否只有这一个批次的账单
                // 如果是， 则删除对账对账记录
                // 否则更新对账记录
                List<ReconciliationDetailE> byReconciliationId = reconciliationDomainService.getByReconciliationId(reconciliationDetail.getReconciliationId());
                // 判断该批次有几个对账记录
                List<Long> list = byBillIds.stream().map(ReconciliationDetailE::getReconciliationId).distinct().collect(Collectors.toList());

                if (list.size() == 1){
                    if (byReconciliationId.size() ==  collect.size()){
                        // 删除对账记录
                        ReconciliationE reconciliationE = reconciliationRepository.getById(byReconciliationId.get(0).getReconciliationId());
                        reconciliationE.setDeleted(1);
                        log.info("reconciliationE :" + JSON.toJSON(reconciliationE));
                        reconciliationRepository.removeById(reconciliationE);
                        byBillIds.forEach(s -> {
                            s.setDeleted(1);
                        });
                        log.info("byBillIds :" + JSON.toJSON(byBillIds));
                        reconciliationDetailRepository.removeBatchByIds(byBillIds);
                    } else {
                        // 统计三个金额
                        long actualAmount = byBillIds.stream().mapToLong(ReconciliationDetailE::getActualAmount).sum();
                        long invoiceAmount = byBillIds.stream().mapToLong(ReconciliationDetailE::getInvoiceAmount).sum();
                        long flowClaimAmount = byBillIds.stream().mapToLong(ReconciliationDetailE::getFlowClaimAmount).sum();
                        // 更新对账记录
                        ReconciliationE reconciliationE = reconciliationRepository.getById(byReconciliationId.get(0).getReconciliationId());
                        reconciliationE.setBillCount(reconciliationE.getBillCount() - collect.size());
                        List<ReconciliationDetailE> collect1 = byBillIds.stream().filter(i -> i.getResult() == 2).collect(Collectors.toList());
                        reconciliationE.setBalanceCount(reconciliationE.getBalanceCount() - collect1.size());
                        reconciliationE.setActualTotal(reconciliationE.getActualTotal() - actualAmount);
                        reconciliationE.setInvoiceTotal(reconciliationE.getInvoiceTotal() - invoiceAmount);
                        reconciliationE.setFlowClaimTotal(reconciliationE.getFlowClaimTotal() - flowClaimAmount);
                        if(reconciliationE.getActualTotal() < 0){
                            reconciliationRepository.removeById(reconciliationE);
                        } else {
                            reconciliationRepository.updateById(reconciliationE);
                        }
                        byBillIds.forEach(s -> {
                            s.setDeleted(1);
                        });
                        log.info("byBillIds :" + JSON.toJSON(byBillIds));
                        reconciliationDetailRepository.removeBatchByIds(byBillIds);
                    }
                } else {
                    for (Long l : list) {
                        // 跟流水有关的的 对账记录主表
                        ReconciliationE reconciliationE = reconciliationRepository.getById(l);
                        // 判断对账记录下是否有其他的流水
                        List<ReconciliationDetailE> byReconciliationId1 = reconciliationDetailRepository.getByReconciliation(l);
                        List<Long> allIds = new ArrayList<>();
                        for (ReconciliationDetailE reconciliationDetailE : byReconciliationId1) {
                            for (Long flowId : reconciliationDetailE.getFlowIds()) {
                                allIds.add(flowId);
                            }
                        }
                        List<Long> collect1 = allIds.stream().distinct().collect(Collectors.toList());
                        List<Long> list1 = flowClaimRecordRepository.queryIdByFlowIds(collect1);
                        if (list1.size() == 1){
                            reconciliationE.setDeleted(1);
                            log.info("reconciliationE :" + JSON.toJSON(reconciliationE));
                            reconciliationRepository.removeById(reconciliationE);
                            byReconciliationId1.forEach(s -> {
                                s.setDeleted(1);
                            });
                            log.info("byBillIds :" + JSON.toJSON(byReconciliationId1));
                            reconciliationDetailRepository.removeBatchByIds(byReconciliationId1);
                        } else {
                            List<ReconciliationDetailE> byBillIdsAndReconciliationId = reconciliationDomainService.getByBillIdsAndReconciliationId(collect, l);
                            long actualAmount = byBillIdsAndReconciliationId.stream().mapToLong(ReconciliationDetailE::getActualAmount).sum();
                            long invoiceAmount = byBillIdsAndReconciliationId.stream().mapToLong(ReconciliationDetailE::getInvoiceAmount).sum();
                            long flowClaimAmount = byBillIdsAndReconciliationId.stream().mapToLong(ReconciliationDetailE::getFlowClaimAmount).sum();
                            // 更新对账记录
                            reconciliationE.setBillCount(reconciliationE.getBillCount() - byBillIdsAndReconciliationId.size());
                            List<ReconciliationDetailE> collect2 = byBillIdsAndReconciliationId.stream().filter(i -> i.getResult() == 2).collect(Collectors.toList());
                            reconciliationE.setBalanceCount(reconciliationE.getBalanceCount() - collect2.size());
                            reconciliationE.setActualTotal(reconciliationE.getActualTotal() - actualAmount);
                            reconciliationE.setInvoiceTotal(reconciliationE.getInvoiceTotal() - invoiceAmount);
                            reconciliationE.setFlowClaimTotal(reconciliationE.getFlowClaimTotal() - flowClaimAmount);
                            if(reconciliationE.getActualTotal() < 0){
                                reconciliationRepository.removeById(reconciliationE);
                            } else {
                                reconciliationRepository.updateById(reconciliationE);
                            }
                            byBillIdsAndReconciliationId.forEach(s -> {
                                s.setDeleted(1);
                            });
                            log.info("byBillIds :" + JSON.toJSON(byBillIdsAndReconciliationId));
                            reconciliationDetailRepository.removeBatchByIds(byBillIdsAndReconciliationId);
                        }
                    }
                }
                //解除流水认领
                flowClaimAppService.revokedForRecon(new RevokedFlowF(flowDetailIds));
                // 更新收款单 对账记录  跟新收款单对应的 应收账单的对账记录
                // 获取对应收款单id
                List<Long> billIdList = byBillIds.stream().map(ReconciliationDetailE::getBillId).collect(Collectors.toList());
                // 更新商户账票流水对账状态为  未对账
                List<GatherBill> gatherBill = gatherBillRepository.getGatherBill(billIdList, unclaimF.getSupCpUnitId());
                log.info("gatherBill :" + JSON.toJSON(gatherBill));
                List<Long> idList = gatherBill.stream().map(GatherBill::getId).collect(Collectors.toList());
                gatherBillRepository.updateReconcileState(idList, unclaimF.getSupCpUnitId());

                // 查询该收款单下 所有收款明细对应的账单
                // 查询该账单所有的收款明细对应的收款单是否已经对账
                // 若是都未对账则该账单是未对账，否则该账单部分对账
                for (Long id : idList) {
                    List<GatherDetail> byGatherBillId = gatherDetailRepository.getByGatherBillId(id, unclaimF.getSupCpUnitId());
                    for (GatherDetail gatherDetail : byGatherBillId) {
                        List<GatherDetail> byRecBillIds = gatherDetailRepository.getByRecBillIds(Arrays.asList(gatherDetail.getRecBillId()), unclaimF.getSupCpUnitId());
                        List<GatherBill> bill = gatherBillRepository.getGatherBill(byRecBillIds.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList()), unclaimF.getSupCpUnitId());
                        // 判断所有的bill 是否是  已经取消对账
                        for (GatherBill gatherBill1 : bill) {
                            if (gatherBill1.getReconcileState().equals(2) || gatherBill1.getReconcileState().equals(1)){
                                receivableBillRepository.updateReconcileState(Arrays.asList(gatherDetail.getRecBillId()), unclaimF.getSupCpUnitId(), 1);
                                break;
                            }
                        }
                        receivableBillRepository.updateReconcileState(Arrays.asList(gatherDetail.getRecBillId()), unclaimF.getSupCpUnitId(), 0);
                    }
                }
            }
            else {
                throw BizException.throw400(ErrorMessage.RECONCILE_DETAIL_HAVE_VOUCHER_BILL_NO.msg());
            }
        }



        return true;
    }

    /** {@linkplain ChannelFlowClaimAppService#claim} 反操作
     * @param dto dto
     */
    @Transactional(rollbackOn = Exception.class)
    public Boolean unReconciliation(UnclaimF dto) {
        if (StrUtil.isBlank(dto.getBillNo()) && CollectionUtils.isEmpty(dto.getBillNoList())){
            ErrorAssertUtil.throw400(ErrorMessage.RECONCILE_DETAIL_PARAM);
        }
        if (CollectionUtils.isEmpty(dto.getBillNoList())){
            List<String> list = new ArrayList<>();
            list.add(dto.getBillNo());
            dto.setBillNoList(list);
        }
        for (String billNo : dto.getBillNoList()) {
            // 1-当交易对账状态为「已核对」
            List<String> billNoList = StrUtil.split(billNo, ',');
            ReconciliationDetailE reconciliationDetail = reconciliationDetailRepository.getOne(Wrappers.<ReconciliationDetailE>lambdaQuery()
                    .in(ReconciliationDetailE::getBillNo,billNoList).last("limit 1"));
            log.info("根据对账详情id查询对账详情:" + JSON.toJSON(reconciliationDetail));
            ErrorAssertUtil.notNullThrow404(reconciliationDetail, ErrorMessage.RECONCILE_DETAIL_NOT_EXIST);
            ErrorAssertUtil.isTrueThrow400(ReconcileResultEnum.已核对.getCode() == reconciliationDetail.getResult(),
                    ErrorMessage.RECONCILE_DETAIL_RESULT_ERROR);
            if (StrUtil.isNotBlank(reconciliationDetail.getVoucherBillNo()) && EnvConst.FANGYUAN.equals(EnvData.config)) {
                // 点击此按钮提示:解除对账失败，该收款单已生成对账核销报账单并已推送至NC65。
                Integer state = receivableBillRepository.getVoucherStatus(reconciliationDetail.getBillNo());
                if (ObjectUtil.isNotNull(state) && PushBillTypeEnum.已推送.getCode() == state){
                    ErrorAssertUtil.throw400(ErrorMessage.RECONCILE_DETAIL_FLOW_IS_PUSH);
                }else {
                    ErrorAssertUtil.throw400(ErrorMessage.RECONCILE_DETAIL_FLOW_NO_PUSH);
                }
            }

            // --------校验结束---更新状态
            //1：关联的数量，同步减少平账进度(同时金额也减少)
            ReconciliationE e = reconciliationRepository.getById(reconciliationDetail.getReconciliationId());
            ErrorAssertUtil.notNullThrow404(e, ErrorMessage.RECONCILE_DETAIL_NOT_EXIST);
            if (e.getBillCount() == 1) {
                reconciliationRepository.removeById(e.getId());
            } else {
                LambdaUpdateWrapper<ReconciliationE> eq = Wrappers.<ReconciliationE>lambdaUpdate()
                        .set(ReconciliationE::getBillCount, e.getBillCount() - 1)
                        .set(ReconciliationE::getBalanceCount, e.getBalanceCount() - 1)
                        .set(ReconciliationE::getActualTotal, e.getActualTotal() - reconciliationDetail.getActualAmount())
                        .set(ReconciliationE::getFlowClaimTotal, e.getFlowClaimTotal() - reconciliationDetail.getFlowClaimAmount())
                        .eq(ReconciliationE::getId, reconciliationDetail.getReconciliationId());

                // 处理状态
                handleMainResult(e, eq);
                reconciliationRepository.update(eq);
            }

//        2-根据bill_no更新gather_bill#mc_reconcile_state  字段。
            LambdaUpdateWrapper<GatherBill> up = Wrappers.<GatherBill>lambdaUpdate()
                    .set(GatherBill::getMcReconcileState, ReconcileResultEnum.未核对.getCode())
                    .eq(GatherBill::getSupCpUnitId, dto.getSupCpUnitId())
                    .in(GatherBill::getBillNo, billNoList);
            if (EnvConst.YUANYANG.equals(EnvData.config)){
                up.set(GatherBill::getBankFlowNo,"");
            } else {
                // 支付流水页面相关字段清除(关联不到就ok了)
                up.set(GatherBill::getTradeNo, "");
            }
            gatherBillRepository.update(up);



            //3-更新 reconciliation_yinlian#state 字段
            if (EnvConst.YUANYANG.equals(EnvData.config)) {

                reconciliationYinlianRepository.update(Wrappers.<ReconciliationYinlianE>lambdaUpdate()
                        .set(ReconciliationYinlianE::getState, ReconciliationYinlianStateEnum.NO.getCode())
                        .eq(ReconciliationYinlianE::getSearchReferenceNo, reconciliationDetail.getChannelSeqId())
                );
            } else {
                reconciliationYinlianRepository.update(Wrappers.<ReconciliationYinlianE>lambdaUpdate()
                        .set(ReconciliationYinlianE::getState, ReconciliationYinlianStateEnum.NO.getCode())
                        .eq(ReconciliationYinlianE::getSeqId, reconciliationDetail.getChannelSeqId())
                );
            }

            // 4-删除对应明细
            reconciliationDetailRepository.update(Wrappers.<ReconciliationDetailE>lambdaUpdate()
                    .set(ReconciliationDetailE::getDeleted,1).in(ReconciliationDetailE::getBillNo,billNoList));
        }

        return true;
    }

    /**0未核对，1部分核对，2已核对，3核对失败
     * 1 -> 0 -> 修改0
     * 1 -> 0，3 ->0
     * 1-> 0,2 -> 1
     * 1-> 3->3
     *
     * @param e e
     * @param eq update
     */
    private void handleMainResult(ReconciliationE e, LambdaUpdateWrapper<ReconciliationE> eq) {
        if (ObjectUtil.isNotNull(e.getResult()) && e.getResult().equals(ReconcileResultEnum.部分核对.getCode())){
            List<Integer> re = reconciliationDetailRepository.list(Wrappers.<ReconciliationDetailE>lambdaQuery().select(ReconciliationDetailE::getResult)
                    .eq(ReconciliationDetailE::getReconciliationId, e.getId())).stream().map(ReconciliationDetailE::getResult)
                    .filter(ObjectUtil::isNotNull).collect(Collectors.toList());

            if (re.contains(ReconcileResultEnum.未核对.getCode()) && re.contains(ReconcileResultEnum.核对失败.getCode())){
                eq.set(ReconciliationE::getResult,ReconcileResultEnum.未核对.getCode());
            } else if (re.contains(ReconcileResultEnum.未核对.getCode()) && re.contains(ReconcileResultEnum.已核对.getCode())){
                eq.set(ReconciliationE::getResult,ReconcileResultEnum.部分核对.getCode());
            }else if (re.contains(ReconcileResultEnum.未核对.getCode())){
                eq.set(ReconciliationE::getResult,ReconcileResultEnum.未核对.getCode());
            }else if (re.contains(ReconcileResultEnum.核对失败.getCode())){
                eq.set(ReconciliationE::getResult,ReconcileResultEnum.核对失败.getCode());
            }
        }
    }

    /**
     * 商户清分对账计算统计
     *
     * @param pageF
     * @return
     */
    public ReconciliationClearStatisticsF getReconciliationClearStatistics(PageF<SearchF<ReconciliationE>> pageF) {
        ReconciliationClearStatisticsV reconciliationClearStatistics = reconciliationDomainService.getReconciliationClearStatistics(pageF);
        ReconciliationClearStatisticsF reconciliationClearStatisticsF = new ReconciliationClearStatisticsF();
        reconciliationClearStatisticsF.setActualTotal(amountTransaction(reconciliationClearStatistics.getActualTotal()));
        reconciliationClearStatisticsF.setFlowClaimTotal(amountTransaction(reconciliationClearStatistics.getFlowClaimTotal()));
        reconciliationClearStatisticsF.setCommission(amountTransaction(reconciliationClearStatistics.getCommission()));
        return reconciliationClearStatisticsF;
    }

    /**
     * 商户清分对账详情统计
     *
     * @param pageF
     * @return
     */
    public ReconciliationClearStatisticsF reconciliationClearDetailStatistics(PageF<SearchF<ReconciliationDetailE>> pageF) {
        ReconciliationClearStatisticsV reconciliationClearStatisticsV = reconciliationDomainService.reconciliationClearDetailStatistics(pageF);
        ReconciliationClearStatisticsF reconciliationClearStatisticsF = new ReconciliationClearStatisticsF();
        reconciliationClearStatisticsF.setActualTotal(amountTransaction(reconciliationClearStatisticsV.getActualTotal()));
        reconciliationClearStatisticsF.setFlowClaimTotal(amountTransaction(reconciliationClearStatisticsV.getFlowClaimTotal()));
        reconciliationClearStatisticsF.setCommission(amountTransaction(reconciliationClearStatisticsV.getCommission()));
        return reconciliationClearStatisticsF;
    }


    public ReconciliationStatisticsF getReconciliationStatistics(PageF<SearchF<ReconciliationE>> pageF) {
        ReconciliationStatisticsV reconciliationStatisticsv = reconciliationDomainService.getReconciliationStatistics(pageF);
        ReconciliationStatisticsF reconciliationStatisticsF = new ReconciliationStatisticsF();
        reconciliationStatisticsF.setActualTotal(amountTransaction(reconciliationStatisticsv.getActualTotal()));
        reconciliationStatisticsF.setFlowClaimTotal(amountTransaction(reconciliationStatisticsv.getFlowClaimTotal()));
        reconciliationStatisticsF.setInvoiceTotal(amountTransaction(reconciliationStatisticsv.getInvoiceTotal()));
        return reconciliationStatisticsF;
    }


    public ReconciliationStatisticsF reconciliationDetailStatistics(PageF<SearchF<ReconciliationDetailE>> pageF) {
        ReconciliationStatisticsV reconciliationStatisticsV = reconciliationDomainService.reconciliationDetailStatistics(pageF);
        ReconciliationStatisticsF reconciliationStatisticsF = new ReconciliationStatisticsF();
        reconciliationStatisticsF.setActualTotal(amountTransaction(reconciliationStatisticsV.getActualTotal()));
        reconciliationStatisticsF.setFlowClaimTotal(amountTransaction(reconciliationStatisticsV.getFlowClaimTotal()));
        reconciliationStatisticsF.setReceivableAmount(amountTransaction(reconciliationStatisticsV.getReceivableAmount()));
        reconciliationStatisticsF.setCarriedAmount(amountTransaction(reconciliationStatisticsV.getCarriedAmount()));
        reconciliationStatisticsF.setInvoiceTotal(amountTransaction(reconciliationStatisticsV.getInvoiceTotal()));
        return reconciliationStatisticsF;
    }

    /**
     * 分转换为元 计算
     *
     * @param amount
     * @return
     */
    private String amountTransaction(Long amount) {
        double amountValue = amount / 100.0;
        BigDecimal bigDecimal = new BigDecimal(amountValue);
        String format = new DecimalFormat(",###.00").format(bigDecimal);
        if (format.startsWith(".")) {
            format = "0" + format;
        }
        return format;
    }


//    private List<ReconciliationDetailE> reconciliationDetailPageVList(Page<ReconciliationDetailE> page) {
//        List<ReconciliationDetailE> records = page.getRecords();
//        List<String> idList = new ArrayList<>();
//        List<String> noList = new ArrayList<>();
//        for (ReconciliationDetailE record : records) {
//            String voucherBillId = record.getVoucherBillId();
//            String VoucherBillNo = record.getVoucherBillNo();
//            if (null != voucherBillId){
//                String[] idSplit = voucherBillId.split(",");
//                for (String s : idSplit) {
//                    idList.add(s);
//                }
//            }
//           if (null != VoucherBillNo){
//               String[] noSplit = VoucherBillNo.split(",");
//               for (String s : noSplit) {
//                   noList.add(s);
//               }
//           }
//            record.setVoucherBillNoList(noList);
//            record.setVoucherBillIdList(idList);
//        }
//        return reconciliationDetailPageVS;
//    }


    private List<ReconciliationPageV> reconciliationPageVList(PageV<ReconciliationPageV> page ) {
        List<ReconciliationPageV> reconciliationPageVS = Global.mapperFacade.mapAsList(page.getRecords(), ReconciliationPageV.class);
        for (ReconciliationPageV record : reconciliationPageVS) {
            List<String> idList = new ArrayList<>();
            List<String> noList = new ArrayList<>();
            String voucherBillId = record.getVoucherBillId();
            String VoucherBillNo = record.getVoucherBillNo();
            if (null != voucherBillId) {
                String[] idSplit = voucherBillId.split(",");
                for (String s : idSplit) {
                    if (StringUtils.isNotBlank(s)){
                        idList.add(s);
                    }
                }
            }
           if (null != VoucherBillNo){
               String[] noSplit = VoucherBillNo.split(",");
               for (String s : noSplit) {
                   if (StringUtils.isNotBlank(s)) {
                       noList.add(s);
                   }
               }
           }
            record.setVoucherBillNoList(noList);
            record.setVoucherBillIdList(idList);
        }
        return reconciliationPageVS;
    }
}
