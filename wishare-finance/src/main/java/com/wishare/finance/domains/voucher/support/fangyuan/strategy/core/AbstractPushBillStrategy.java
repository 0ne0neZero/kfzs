package com.wishare.finance.domains.voucher.support.fangyuan.strategy.core;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitRepository;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.repository.mapper.StatutoryBodyAccountMapper;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationDetailE;
import com.wishare.finance.domains.reconciliation.entity.ReconciliationE;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.ReconciliationRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleExecuteStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleStateEnum;
import com.wishare.finance.domains.voucher.strategy.core.AbstractVoucherStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.*;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.*;
import com.wishare.finance.domains.voucher.support.fangyuan.facade.PushBillFacade;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRecordRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherBillDetailRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.VoucherPushBillRepository;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.wishare.finance.domains.voucher.support.fangyuan.enums.SceneTypeEnum.冲销;
import static com.wishare.finance.domains.voucher.support.fangyuan.enums.TriggerEventBillTypeEnum.对账核销;

/**
 * 方圆推单策略抽象
 */
public abstract class AbstractPushBillStrategy<C extends BillStrategyCommand> implements BillStrategy<C> {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoucherStrategy.class);
    @Autowired
    protected BillRuleRepository billRuleRepository;
    @Autowired
    protected SpaceClient spaceClient;
    @Autowired
    protected PushBillFacade pushBillFacade;
    @Autowired
    protected BillRuleRecordRepository billRuleRecordRepository;
    @Autowired
    protected VoucherPushBillRepository voucherPushBillRepository;
    @Autowired
    protected VoucherBillDetailRepository voucherBillDetailRepository;
    @Autowired
    protected BusinessUnitRepository businessUnitRepository;
    @Autowired
    protected ReceivableBillRepository receivableBillRepository;

    @Autowired
    protected StatutoryBodyAccountMapper statutoryBodyAccountMapper;
    @Autowired
    protected ReconciliationDetailRepository reconciliationDetailRepository;
    @Autowired
    protected ReconciliationRepository reconciliationRepository;
    @Autowired
    protected TransactionTemplate transactionTemplate;
    private final Integer LIST_SIZE = 1000;
    /**
     * 方圆推单模式
     */
    protected PushMethod pushMethod;
    /**
     * 事件类型
     */
    protected TriggerEventBillTypeEnum eventType;

    public AbstractPushBillStrategy(PushMethod pushMethod, TriggerEventBillTypeEnum eventType) {
        this.pushMethod = pushMethod;
        this.eventType = eventType;
    }

    @Override
    public PushMethod pushMethod() {
        return pushMethod;
    }

    @Override
    public TriggerEventBillTypeEnum eventType() {
        return eventType;
    }

    /**
     * 获取业务单据列表
     *
     * @param command
     * @param conditions   过滤条件信息
     * @param communityIds
     * @return 业务单据列表
     */
    public abstract List<PushBusinessBill> businessBills(C command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds);


    /**
     * 执行推单录制
     *
     * @param command 推单规则运行命令
     * @param rule    推单规则
     */

    public void doExecute(C command, BillRule rule) {
        BillRuleRecord billRuleRecord = null;
        try {
            ErrorAssertUtil.notNullThrow403(rule, ErrorMessage.BILL_RULE_NOT_EXIST);
            rule.checkRun();
            //预执行
            billRuleRecord = preExecute(rule);
            List<String> communityIds = this.communityId(rule.getScopeApplication());
            long start = System.currentTimeMillis();
            log.info("开始执行推单录制: 推单规则id:{}, 规则名称:{}", rule.getId(), rule.getRuleName());
            List<PushBusinessBill> businessBills = businessBills(command, rule.getConditions(), communityIds);
            long timeConsume = System.currentTimeMillis() - start;
            log.info("推单规则id：{} 耗时{}", rule.getId(), timeConsume);
            log.info("推单规则id：{} 单据详情:{}", rule.getId(), JSON.toJSONString(businessBills));
            Map<String, List<PushBusinessBill>> stringListMap = this.groupPushBusinessBill(businessBills, rule);
            BillRuleRecord finalBillRuleRecord = billRuleRecord;
            List<VoucherBill> voucherBillList = Lists.newArrayList();
            List<VoucherPushBillDetail> voucherPushBillDetailList = Lists.newArrayList();
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(@NotNull TransactionStatus transactionStatus) {
                    try {
                        if (stringListMap != null) {
                            for (String key : stringListMap.keySet()) {
                                List<PushBusinessBill> list = stringListMap.get(key);
                                calculateAmount(list, rule);
                                //因对方接口限制，方圆每个明细限制1000条左右  对list进行切分 保存凭证信息
                                List<List<PushBusinessBill>> partition = grouping(list, LIST_SIZE);
                                middleHandle(partition);
                                for (List<PushBusinessBill> pushBusinessBills : partition) {
                                    VoucherBill voucherBill = voucherPushBill(rule, pushBusinessBills);
                                    List<VoucherPushBillDetail> voucherPushBillDetails = Global.mapperFacade.mapAsList(pushBusinessBills, VoucherPushBillDetail.class);
                                    voucherBillList.add(voucherBill);
                                    voucherPushBillDetailList.addAll(voucherPushBillDetails);
                                    voucherPushBillRepository.save(voucherBill);
                                    voucherBillDetailRepository.saveBatch(voucherPushBillDetails);
                                }
                            }
                        }
                        //后置处理
                        afterExecute(rule, finalBillRuleRecord);
                        // 推单成功的账单，需要修改状态为已推凭
                        modifyInferenceStatus(businessBills);
                        Integer billEventType = rule.getEventType();
                        if (对账核销.equalsByCode(billEventType)) {
                            log.info("进入对账单号反写方法:{}", JSON.toJSONString(voucherBillList));
                            Reconciliation(voucherBillList, voucherPushBillDetailList);
                        }
                    } catch (Exception e) {
                        //回滚
                        transactionStatus.setRollbackOnly();
                        throw e;
                    }
                }
            });

        } catch (Exception e) {
            log.error("运行凭证规则出错：", e);
            failExecute(rule, billRuleRecord);
        }
    }

    public void middleHandle(List<List<PushBusinessBill>> partition) {

    }

    public void Reconciliation(List<VoucherBill> voucherBillList, List<VoucherPushBillDetail> voucherPushBillDetailList) {
        log.info("执行账单号反写方法");
        Map<Long, List<VoucherBill>> reconciliationMap = voucherBillList.stream().collect(Collectors.groupingBy(VoucherBill::getReconciliationId));
        List<ReconciliationE> reconciliationEList = Lists.newArrayList();
        for (Long reconciliationId : reconciliationMap.keySet()) {
            ReconciliationE reconciliationE = reconciliationRepository.getById(reconciliationId);
            if (Objects.nonNull(reconciliationE)) {
                List<VoucherBill> voucherBills = reconciliationMap.get(reconciliationId);
                String billNo = voucherBills.stream().map(VoucherBill::getVoucherBillNo).collect(Collectors.joining(","));
                List<Long> billIds = voucherBills.stream().map(VoucherBill::getId).collect(Collectors.toList());
                String billId = billIds.stream()
                        .map(n -> String.valueOf(n))
                        .collect(Collectors.joining(","));
                reconciliationE.setId(reconciliationId);
                if (StringUtils.isNotBlank(reconciliationE.getVoucherBillNo())) {
                    reconciliationE.setVoucherBillNo(reconciliationE.getVoucherBillNo() + "," + billNo);
                } else {
                    reconciliationE.setVoucherBillNo(billNo);
                }
                if (StringUtils.isNotBlank(reconciliationE.getVoucherBillId())) {
                    reconciliationE.setVoucherBillId(reconciliationE.getVoucherBillId() + "," + billId);
                } else {
                    reconciliationE.setVoucherBillId(billId);
                }
                reconciliationEList.add(reconciliationE);
            }
        }
        List<ReconciliationDetailE> reconciliationDetailES = Lists.newArrayList();
        Map<String, List<VoucherPushBillDetail>> GatherBillNoMap = voucherPushBillDetailList.stream().collect(Collectors.groupingBy(VoucherPushBillDetail::getGatherBillNo));
        for (String gatherBillNo : GatherBillNoMap.keySet()) {
            List<VoucherPushBillDetail> voucherPushBillDetails = GatherBillNoMap.get(gatherBillNo);
            String PushBillDetailNo = voucherPushBillDetails.stream().map(VoucherPushBillDetail::getVoucherBillNo).distinct().collect(Collectors.joining(","));
            ReconciliationDetailE reconciliationDetailE = reconciliationDetailRepository.getById(voucherPushBillDetails.get(0).getReconciliationDetailId());
            if (Objects.nonNull(reconciliationDetailE)) {
                if (StringUtils.isNotBlank(reconciliationDetailE.getVoucherBillNo())) {
                    reconciliationDetailE.setVoucherBillNo(reconciliationDetailE.getVoucherBillNo() + "," + PushBillDetailNo);
                } else {
                    reconciliationDetailE.setVoucherBillNo(PushBillDetailNo);
                }
                reconciliationDetailE.setId(voucherPushBillDetails.get(0).getReconciliationDetailId());
                reconciliationDetailES.add(reconciliationDetailE);
            }
        }
        reconciliationRepository.updateBatchById(reconciliationEList);
        reconciliationDetailRepository.updateBatchById(reconciliationDetailES);
    }


    /**
     * 对List按groupSize的数量进行分组
     *
     * @param list
     * @param groupSize
     * @return
     */
    public List<List<PushBusinessBill>> grouping(List<PushBusinessBill> list, int groupSize) {
        List<List<PushBusinessBill>> groups = new ArrayList<>();
        List<List<PushBusinessBill>> partitions = Lists.partition(list, groupSize);
        for (List<PushBusinessBill> partition : partitions) {
            List<PushBusinessBill> group = new ArrayList<>(partition);
            groups.add(group);
        }
        return groups;
    }

    public void calculateAmount(List<PushBusinessBill> businessBills, BillRule rule) {
        Iterator<PushBusinessBill> iterator = businessBills.iterator();
        while (iterator.hasNext()) {
            PushBusinessBill businessBill = iterator.next();
            this.amount(businessBill, rule.getEventType());
            if (Objects.isNull(businessBill.getTaxIncludAmount()) || businessBill.getTaxIncludAmount() == 0) {
                iterator.remove();
            }
        }
    }

    private Map<String, List<PushBusinessBill>> groupPushBusinessBill(List<PushBusinessBill> businessBills, BillRule rule) {
        Map<String, List<PushBusinessBill>> stringListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(businessBills)) {
            return null;
        }
        if (StringUtils.isBlank(rule.getSummaryRequirements())) {
            if (rule.getEventType() == 对账核销.getCode()) {
                stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> item.getReconciliationId().toString()));
            } else {
                //汇总要求为空的清空下，默认不进行指定分组
                stringListMap.put("0", businessBills);
            }
        } else {
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> serchKey(item, rule)));
        }
        return stringListMap;
    }

    private String serchKey(PushBusinessBill pushBusinessBill, BillRule rule) {
        List<String> list = Arrays.asList(rule.getSummaryRequirements().split(","));
        List<String> groupSerch = Lists.newArrayList();
        for (String s : list) {
            if (Objects.equals(SummaryConditionsEnum.费项.getCode(), Integer.valueOf(s))) {
                groupSerch.add(pushBusinessBill.getChargeItemId().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.归属月.getCode(), Integer.valueOf(s))) {
                groupSerch.add(pushBusinessBill.getAccountDate().toString());
            }
            // if (Objects.equals(SummaryConditionsEnum.合同.getCode(), Integer.valueOf(s)) && StrUtil.isNotBlank(pushBusinessBill.getContractId())) {
            //     groupSerch.add(pushBusinessBill.getContractId().toString());
            // }
            if (Objects.equals(SummaryConditionsEnum.收费对象类型.getCode(), Integer.valueOf(s)) && Objects.nonNull(pushBusinessBill.getPayerType())) {
                groupSerch.add(pushBusinessBill.getPayerType().toString());
            }
            if (rule.getEventType() == 对账核销.getCode()) {
                groupSerch.add(pushBusinessBill.getReconciliationId().toString());
            }
        }
        return String.join("_", groupSerch);
    }

    /**
     * 规则运行后置处理
     *
     * @param rule           凭证规则
     * @param billRuleRecord 推单规则运行记录
     */
    public void afterExecute(BillRule rule, BillRuleRecord billRuleRecord) {
        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
        //更新运行记录
        billRuleRecordRepository.updateById(billRuleRecord);
        rule.setExecuteState(VoucherRuleExecuteStateEnum.空闲.getCode());
        //更新规则运行状态
        billRuleRepository.updateById(rule);
    }

    /**
     * 规则运行失败后置处理
     *
     * @param rule
     * @param billRuleRecord
     */
    public void failExecute(BillRule rule, BillRuleRecord billRuleRecord) {
        if (Objects.nonNull(billRuleRecord)) {
            billRuleRecord.setState(VoucherRuleStateEnum.处理失败.getCode());
            //更新运行记录
            billRuleRecordRepository.updateById(billRuleRecord);
        }
        rule.setExecuteState(VoucherRuleExecuteStateEnum.空闲.getCode());
        //更新规则运行状态
        billRuleRepository.updateById(rule);
    }

    /**
     * 账单变为以推凭
     *
     * @param businessBills
     */
    protected void modifyInferenceStatus(List<PushBusinessBill> businessBills) {
    }

    /**
     * 汇总单据初始化
     *
     * @param rule
     * @param businessBills
     * @return
     */
    public VoucherBill voucherPushBill(BillRule rule, List<PushBusinessBill> businessBills) {
        VoucherBill voucherBill = new VoucherBill();
        voucherBill.init(rule, businessBills.get(0).getCostCenterId(), businessBills.get(0).getCostCenterName());
        Set<Long> set = new HashSet<>();
        for (PushBusinessBill businessBill : businessBills) {
            StatutoryBodyAccountE statutoryBodyAccountE = statutoryBodyAccountMapper.selectById(businessBill.getSbAccountId());
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setBankAccount(statutoryBodyAccountE == null ? "" : statutoryBodyAccountE.getBankAccount());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbill", "hzdj", 20));
            if (StringUtils.isNotBlank(businessBill.getPayInfos())) {
                List<PayInfoV> payInfoVS = JSON.parseArray(businessBill.getPayInfos(), PayInfoV.class);
                businessBill.setPayChannel(payInfoVS.get(0).getPayChannel());
            }
            businessBill.setBillEventType(rule.getEventType());
            set.add(businessBill.getBusinessUnitId());
        }
        voucherBill.setReconciliationId(businessBills.get(0).getReconciliationId());
        voucherBill.setTotalAmount(businessBills.stream().filter(bill -> bill.getVisible() == 0).mapToLong(PushBusinessBill::getTaxIncludAmount).sum());
        if (CollectionUtils.isNotEmpty(set)) {
            List<BusinessUnitE> businessUnitES = businessUnitRepository.listByIds(set);
            List<String> unitNames = businessUnitES.stream().map(BusinessUnitE::getName).collect(Collectors.toList());
            voucherBill.setBusinessUnitName(String.join(",", unitNames));
        }
        if (rule.getEventType() == TriggerEventBillTypeEnum.未收款开票.getCode()) {
            List<Long> billIds = businessBills.stream().filter(pushBusinessBill -> List.of(InvoiceReceiptStateEnum.已作废.getCode(),
                            InvoiceReceiptStateEnum.已红冲.getCode(), InvoiceReceiptStateEnum.部分红冲).contains(pushBusinessBill.getState()))
                    .map(PushBusinessBill::getBillId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(billIds)) {
                voucherBillDetailRepository.update(new LambdaUpdateWrapper<VoucherPushBillDetail>()
                        .in(VoucherPushBillDetail::getBillId, billIds).set(VoucherPushBillDetail::getInvoiceRedType, 1));
            }
        }
        return voucherBill;
    }


    /**
     * 金额换算
     *
     * @param businessBill
     * @param eventType
     */
    public void amount(PushBusinessBill businessBill, int eventType) {
        if (businessBill.getTaxRate() == null) {
            return;
        }
        Long settleAmount = 0L;
        if (eventType == 对账核销.getCode() || eventType == TriggerEventBillTypeEnum.坏账确认.getCode()) {
            //收款明细的实收金额
            settleAmount = businessBill.getTaxIncludAmount();
        } else if (eventType == TriggerEventBillTypeEnum.预收结转.getCode() || eventType == TriggerEventBillTypeEnum.收款结转.getCode()) {
            //结转金额
            settleAmount = businessBill.getCarriedAmount();
        } else if (eventType == TriggerEventBillTypeEnum.未收款开票.getCode()
                && List.of(InvoiceReceiptStateEnum.已作废.getCode(), InvoiceReceiptStateEnum.已红冲.getCode(), InvoiceReceiptStateEnum.部分红冲).contains(businessBill.getState())) {
            settleAmount = businessBill.getTaxIncludAmount() > 0 ? -businessBill.getTaxIncludAmount() : businessBill.getTaxIncludAmount();
            businessBill.setInvoiceRedType(1);
        } else if (eventType == TriggerEventBillTypeEnum.款项调整.getCode()) {
            if (Objects.equals(businessBill.getSceneType(), 冲销.getCode())) {
                settleAmount = businessBill.getTaxIncludAmount();
            } else {
                settleAmount = -businessBill.getRemainingSettleAmount();
            }
        } else {
            //可支付金额  //含税金额*税率/(1+税率)
            settleAmount = businessBill.getRemainingSettleAmount();
        }
        //(1+税率)
        BigDecimal decimal = businessBill.getTaxRate().add(new BigDecimal("1"));
        BigDecimal taxAmount = businessBill.getTaxRate()
                .multiply(new BigDecimal(settleAmount))
                .divide(decimal, 0, RoundingMode.HALF_UP);
        //不含税金额
        Long taxExcludeAmount = settleAmount - taxAmount.longValue();
        businessBill.setTaxIncludAmount(settleAmount);
        businessBill.setTaxExcludAmount(taxExcludeAmount);
        businessBill.setTaxAmount(taxAmount.longValue());
    }


    /**
     * 前置执行
     *
     * @param rule 凭证规则
     * @return 推单规则运行记录
     */
    public BillRuleRecord preExecute(BillRule rule) {
        BillRuleRecord billRuleRecord = new BillRuleRecord();
        billRuleRecord.setRuleId(rule.getId());
        billRuleRecord.setRuleName(rule.getRuleName());
        billRuleRecord.setVoucherSystem(PushBillSysEnum.方圆系统.getCode());
        billRuleRecord.setEventType(eventType.getCode());
        billRuleRecord.setState(VoucherRuleStateEnum.处理中.getCode());
        //新增运行记录
        billRuleRecordRepository.save(billRuleRecord);
        rule.setExecuteState(VoucherRuleExecuteStateEnum.运行中.getCode());
        //更新规则为运行中状态
        billRuleRepository.updateById(rule);
        return billRuleRecord;
    }


    public List<String> communityId(List<ScopeF> scopeApplication) {
        List<String> communityIds = Lists.newArrayList();
        if (CollectionUtils.isEmpty(scopeApplication)) {
            throw BizException.throw400("推单规则未选择项目或期区 请检查");
        }
        for (ScopeF scopeF : scopeApplication) {
            List<Long> areas = Lists.newArrayList();
            if (scopeF.getType() == 1) {
                areas.add(Long.valueOf(scopeF.getId()));
                List<SpaceDetails> details = spaceClient.getDetails(areas);
                List<String> collect = details.stream().map(SpaceDetails::getCommunityId).collect(Collectors.toList());
                communityIds.addAll(collect);
            }

            if (scopeF.getType() == 0) {
                communityIds.add(scopeF.getId());
            }
        }
        return communityIds;
    }
}
