package com.wishare.finance.domains.voucher.strategy.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.mapper.GatherDetailMapper;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectLeafStatusEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBookModeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionMethodEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleExecuteStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherSourceEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.entity.CashFlowOBV;
import com.wishare.finance.domains.voucher.entity.CloseAccount;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.entity.VoucherAccountBook;
import com.wishare.finance.domains.voucher.entity.VoucherBook;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherChargeItemOBV;
import com.wishare.finance.domains.voucher.entity.VoucherCostCenterOBV;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import com.wishare.finance.domains.voucher.entity.VoucherMakeError;
import com.wishare.finance.domains.voucher.entity.VoucherRule;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleRecord;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrg;
import com.wishare.finance.domains.voucher.entity.VoucherStatutoryBody;
import com.wishare.finance.domains.voucher.entity.VoucherTemplate;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryOBV;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.repository.CloseAccountRepository;
import com.wishare.finance.domains.voucher.repository.VoucherBusinessDetailRepository;
import com.wishare.finance.domains.voucher.repository.VoucherInfoRepository;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRecordRepository;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRepository;
import com.wishare.finance.domains.voucher.repository.VoucherRuleTemplateRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeOrgRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeRuleRepository;
import com.wishare.finance.domains.voucher.repository.VoucherTemplateRepository;
import com.wishare.finance.domains.voucher.service.VoucherDomainService;
import com.wishare.finance.domains.voucher.strategy.assisteitem.VoucherAssisteItemContext;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.VoucherSysEnum;
import com.wishare.finance.infrastructure.remote.enums.OrgFinanceTypeEnum;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 凭证策略抽象
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/22
 */
public abstract class AbstractVoucherStrategy<C extends VoucherStrategyCommand> implements VoucherStrategy<C> {

    @Autowired
    protected VoucherFacade voucherFacade;
    @Autowired
    protected VoucherDomainService voucherDomainService;
    @Autowired
    protected VoucherInfoRepository voucherInfoRepository;
    @Autowired
    protected VoucherRuleRepository voucherRuleRepository;
    @Autowired
    protected VoucherTemplateRepository voucherTemplateRepository;
    @Autowired
    protected VoucherRuleRecordRepository voucherRuleRecordRepository;
    @Autowired
    protected VoucherSchemeRuleRepository voucherSchemeRuleRepository;
    @Autowired
    protected VoucherSchemeOrgRepository voucherSchemeOrgRepository;

    @Autowired
    protected VoucherBusinessDetailRepository voucherBusinessDetailRepository;

    @Autowired
    protected VoucherRuleTemplateRepository voucherRuleTemplateRepository;

    @Autowired
    protected GatherDetailRepository gatherDetailRepository;
    @Autowired
    protected GatherDetailMapper gatherDetailMapper;

    @Autowired
    protected CloseAccountRepository closeAccountRepository;

    @Autowired
    protected BusinessUnitRepository businessUnitRepository;


    private static final Logger log = LoggerFactory.getLogger(AbstractVoucherStrategy.class);
    /**
     * 推凭模式
     */
    protected PushMode mode;

    /**
     * 事件类型
     */
    protected VoucherEventTypeEnum eventType;

    public AbstractVoucherStrategy(PushMode mode, VoucherEventTypeEnum eventType) {
        this.mode = mode;
        this.eventType = eventType;
    }

    public AbstractVoucherStrategy(PushMode mode) {
        this.mode = mode;
    }

    @Override
    public PushMode mode() {
        return mode;
    }

    @Override
    public VoucherEventTypeEnum eventType() {
        return eventType;
    }

    /**
     * 获取业务单据列表
     *
     * @param command
     * @param conditions 过滤条件信息
     * @return 业务单据列表
     */
    public abstract List<VoucherBusinessBill> businessBills(C command, List<VoucherRuleConditionOBV> conditions);

    /**
     * 前置执行
     *
     * @param rule 凭证规则
     * @return 凭证规则运行记录
     */
    public VoucherRuleRecord preExecute(VoucherRule rule) {
        VoucherRuleRecord voucherRuleRecord = new VoucherRuleRecord();
        voucherRuleRecord.setVoucherRuleId(rule.getId());
        voucherRuleRecord.setVoucherRuleName(rule.getRuleName());
        voucherRuleRecord.setVoucherSystem(VoucherSysEnum.用友Ncc系统.getCode());
        voucherRuleRecord.setEventType(eventType.getCode());
        voucherRuleRecord.setState(VoucherRuleStateEnum.处理中.getCode());
        //新增运行记录
        voucherRuleRecordRepository.save(voucherRuleRecord);
        //更新规则为运行中状态
        voucherRuleRepository.updateExecuteStateById(rule.getId(), VoucherRuleExecuteStateEnum.运行中.getCode());
        return voucherRuleRecord;
    }



    /**
     * 执行凭证录制
     *
     * @param command 凭证规则运行命令
     * @param rule 凭证规则
     */
    public VoucherRuleRecord doExecute(C command, VoucherRule rule) {
        Long recordId = 0L;
        try {
            ErrorAssertUtil.notNullThrow403(rule, ErrorMessage.VOUCHER_RULE_NOT_EXIST);
            VoucherTemplate template = new VoucherTemplate();
            if (!rule.getEventType().equals(VoucherEventTypeEnum.作废.getCode())) {
                template = voucherTemplateRepository.getById(rule.getVoucherTemplateId());
                ErrorAssertUtil.notNullThrow403(template, ErrorMessage.VOUCHER_TEMPLATE_NOT_EXIST);
                ErrorAssertUtil.notNullThrow403(template.getEntries(), ErrorMessage.VOUCHER_RUN_TEMPLATE_ENTRIES_ERROR);
            }
            rule.checkRun();
            //预执行
            VoucherRuleRecord voucherRuleRecord = preExecute(rule);
            recordId = voucherRuleRecord.getId();
            VoucherMakeError voucherMakeError = new VoucherMakeError();
            // 添加核算方案关联查询

            List<VoucherScheme> schemes = voucherSchemeRuleRepository.getSchemeByRuleId(rule.getId());
            List<Long> schemeIds = schemes.stream().map(VoucherScheme::getId).collect(Collectors.toList());
            ErrorAssertUtil.notEmptyThrow400(schemeIds, ErrorMessage.VOUCHER_RULE_NOT_LINK_SCHEME);
            List<VoucherSchemeOrg> voucherSchemeOrgs = voucherSchemeOrgRepository.listBySchemeIds(schemeIds);
            final List<Long> statutoryBodyIds = voucherSchemeOrgs.stream().filter(org -> OrgFinanceTypeEnum.法定单位.getCode().equals(org.getOrgType())).map(VoucherSchemeOrg::getOrgId).collect(Collectors.toList());
            final List<Long> costCenterIds = voucherSchemeOrgs.stream().filter(org -> OrgFinanceTypeEnum.成本中心.getCode().equals(org.getOrgType())).map(VoucherSchemeOrg::getOrgId).collect(Collectors.toList());

            for (Long statutoryBodyId : statutoryBodyIds) {
                VoucherRuleConditionOBV condition = new VoucherRuleConditionOBV();
                condition.setType(VoucherRuleConditionTypeEnum.法定单位.getCode());
                setIntoRule(rule, Collections.singletonList(statutoryBodyId), condition);
                makeVoucherSplit(command, rule, voucherMakeError, template, voucherRuleRecord);
            }
            for (Long costId : costCenterIds) {
                VoucherRuleConditionOBV condition = new VoucherRuleConditionOBV();
                condition.setType(VoucherRuleConditionTypeEnum.成本中心.getCode());
                setIntoRule(rule, Collections.singletonList(costId), condition);
                makeVoucherSplit(command, rule, voucherMakeError, template, voucherRuleRecord);
            }

            //后置处理
            afterExecute(rule, voucherRuleRecord,voucherMakeError);
            return voucherRuleRecord;
        } catch (Exception e) {
            log.error("凭证规则运行失败:{}", e.getMessage(), e);
            voucherRuleRepository.updateExecuteStateById(rule.getId(), VoucherRuleExecuteStateEnum.空闲.getCode());
            // 运行中的推凭记录改为运行失败
            voucherRuleRecordRepository.setFail(recordId, e.getMessage());
        }
        return null;
    }

    /**
     * @param command c
     * @param rule 规则
     * @param voucherMakeError 错误信息记录
     * @param template 凭证模板
     * @param voucherRuleRecord 凭证记录
     */
    private void makeVoucherSplit(C command, VoucherRule rule, VoucherMakeError voucherMakeError, VoucherTemplate template, VoucherRuleRecord voucherRuleRecord) {
        voucherFacade.getCommunityIds(rule.getConditions(), voucherMakeError);
        List<VoucherBusinessBill> businessBills = businessBills(command, rule.getConditions());
        printLog(businessBills);
        List<Voucher> vouchers = new ArrayList<>();

        List<VoucherBusinessDetail> voucherBusinessDetails = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(businessBills)) {
            vouchers.addAll(makeVoucher(rule, template, voucherMakeError, businessBills, voucherBusinessDetails, voucherRuleRecord));
        }
        //保存凭证信息
        if (!vouchers.isEmpty()){
            voucherInfoRepository.saveBatch(vouchers);
            voucherBusinessDetailRepository.saveBatch(voucherBusinessDetails);
        }
        //填充凭证运行记录信息
        putVoucherRuleRecord(voucherRuleRecord, vouchers);
        // 部分场景制作凭证成功的账单，需要修改状态为已推凭
        modifyInferenceStatus(voucherBusinessDetails);
    }

    private void printLog(List<VoucherBusinessBill> businessBills){
        if (CollUtil.isEmpty(businessBills)){
            log.info("查询凭证业务单据信息:为空");
        }else {
            ArrayList<VoucherBusinessBillLog> list = new ArrayList<>(businessBills.size());
            businessBills.forEach(v->{
                list.add(VoucherBusinessBillLog.builder()
                        .businessBillId(v.getBusinessBillId())
                                .businessBillNo(v.getBusinessBillNo())
                        .supCpUnitId(v.getSupCpUnitId())
                        .build());
            });
            log.info("查询凭证业务单据信息: {}", JSON.toJSONString(list));
        }
    }
    /**
     * 预制凭证
     *
     * @param rule             凭证规则
     * @param template         凭证模板
     * @param voucherMakeError 凭证预制错误信息
     * @param businessBills    单据信息
     * @return 预制凭证列表
     */
    public List<Voucher> makeVoucher(VoucherRule rule, VoucherTemplate template, VoucherMakeError voucherMakeError,
                                     List<VoucherBusinessBill> businessBills, List<VoucherBusinessDetail> voucherBusinessDetails,
                                     VoucherRuleRecord voucherRuleRecord) {
        //根据法定单位、成本中心、费项、系统来源分组，单个分组为一张凭证
        Map<String, List<VoucherBusinessBill>> businessBillMaps = businessBills.stream()
                .collect(Collectors.groupingBy(item -> String.join("_", Arrays.asList(
                        String.valueOf(item.getBusinessUnitId()),
                        String.valueOf(item.getCostCenterId()),
//                        String.valueOf(item.getChargeItemId()),
                        String.valueOf(item.getSysSource())))));
        //记录成本中心
        List<Voucher> vouchers = new ArrayList<>();
        List<VoucherBook> books;
        VoucherBusinessBill businessBill;
//        VoucherStatutoryBody statutoryBody = null;
        BusinessUnitE businessUnitE = null;
        Map<String, BigDecimal> logicAmountMap;
        Voucher voucher;
        for (List<VoucherBusinessBill> voucherBusinessBills : businessBillMaps.values()) {
            businessBill = voucherBusinessBills.get(0);
            books = findBooks(rule, businessBill.getCostCenterId(), businessBill.getChargeItemId(),businessBill.getStatutoryBodyId(),businessBill.getBusinessUnitId());
            if (books.isEmpty()) {
                //获取账簿，如果存在多个账簿，则推多个账簿信息, 如果为空则不推凭
                voucherMakeError.addMessage("找不到指定的映射账簿信息，成本中心：id=" + businessBill.getCostCenterId() + ", name=" + businessBill.getCostCenterName()
                        + " 费项： id=" + businessBill.getChargeItemId() + ", name=" + businessBill.getChargeItemName()
                        + " 法定单位： id=" + businessBill.getStatutoryBodyId() + ", name=" + businessBill.getStatutoryBodyName());
                continue;
            }
            businessUnitE = findBusinessUnit(businessUnitE, businessBill.getBusinessUnitId());
            log.info("获取业务单元信息:{}", JSON.toJSONString(businessUnitE));
            if(Objects.isNull(businessUnitE)){
                voucherMakeError.addMessage("找不到指定的业务单元信息, id=" + businessBill.getBusinessUnitId());
                continue;
            }
            for (VoucherBook book : books) {
                voucher = doMakeVoucher(book, template, businessUnitE, voucherBusinessBills, voucherMakeError, voucherRuleRecord);
                if (Objects.nonNull(voucher)) {
                    //判断当前账簿是否关账且凭证会计期间在这个内，则不推凭至账簿
                    if (voucher.getAccountBookId() !=  null && voucher.getFiscalPeriod() != null){
                        String str = dateCheck(voucher.getFiscalPeriod());
                        CloseAccount closeAccount = closeAccountRepository.getBookIdBuyAccount(voucher.getAccountBookId(), str);
                        // 说明关账了。
                        if (ObjectUtil.isNotNull(closeAccount)){
                            // 处理会计月
                            LocalDate d = LocalDate.parse(closeAccount.getAccountingPeriod() + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            voucher.setFiscalPeriod(d);
                            voucher.setFiscalYear(d.getYear());
                        }
                    }
                    vouchers.add(voucher);
                    Long voucherId = voucher.getId();
                    voucherBusinessDetails.addAll(voucherBusinessBills.stream().map(item ->
                            new VoucherBusinessDetail(voucherId, item.getBusinessBillId(), item.getBusinessBillNo(),
                                    item.getBusinessBillType(), item.getSceneId(), item.getSceneType(), item,
                                    item.getSupCpUnitId(), item.getSupCpUnitName(),item.getReceivableBillId(),item.getAdvanceId(),book.getBookId(),item.getPayChannel()))
                            .collect(Collectors.toList()));
                }
            }
        }
        // 设置单据来源系统
        vouchers.forEach(v->{
            v.setSysSource(getSysSourceFromRule(rule));
        });
        return vouchers;
    }
    private Integer getSysSourceFromRule(VoucherRule rule){
        if (ObjectUtil.isNull(rule) || CollUtil.isEmpty(rule.getConditions())){
            return SysSourceEnum.收费系统.getCode();
        }
        List<VoucherRuleConditionOBV> list = rule.getConditions();
        for (VoucherRuleConditionOBV obv : list) {
            if (Objects.equals(VoucherRuleConditionTypeEnum.单据来源.getCode(),obv.getType())){
                if (CollUtil.isEmpty(obv.getValues())){
                    return SysSourceEnum.收费系统.getCode();
                }

                return Integer.valueOf(obv.getValues().get(0).getCode());
            }
        }
        return SysSourceEnum.收费系统.getCode();
    }

    private static LocalDate gatMaxCloseDate(String maxCloseDate){

        // 解析输入的年月字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(maxCloseDate + "-01", formatter);
        // 加一个月
        return date.plusMonths(1);
    }

    public static void main(String[] args) {
        System.out.println(gatMaxCloseDate("2023-12"));
        System.out.println(gatMaxCloseDate("2023-02"));
    }
    /**
     * 规则运行后置处理
     *
     * @param rule              凭证规则
     * @param voucherRuleRecord 凭证规则运行记录
     * @param error error
     */
    public void afterExecute(VoucherRule rule, VoucherRuleRecord voucherRuleRecord, VoucherMakeError error) {
        //设置错误描述信息
        if (error.getMessages().size()>500){
            voucherRuleRecord.setRemark(CollUtil.join(error.getMessages().subList(0,500),"\n"));
        }else {
            voucherRuleRecord.setRemark(CollUtil.join(error.getMessages(),"\n"));
        }

        // 过滤重复费项
        TreeSet<VoucherChargeItemOBV> s = new TreeSet<>(Comparator.comparing(VoucherChargeItemOBV::getChargeItemId));
        s.addAll(voucherRuleRecord.getChargeItems());
        voucherRuleRecord.setChargeItems(new ArrayList<>(s));
        // 过滤重复

        TreeSet<VoucherStatutoryBody> a = new TreeSet<>(Comparator.comparing(VoucherStatutoryBody::getStatutoryBodyId));
        a.addAll(voucherRuleRecord.getStatutoryBodys());
        voucherRuleRecord.setStatutoryBodys(new ArrayList<>(a));

        //
        TreeSet<VoucherAccountBook> b = new TreeSet<>(Comparator.comparing(VoucherAccountBook::getAccountBookId));
        b.addAll(voucherRuleRecord.getAccountBooks());
        voucherRuleRecord.setAccountBooks(new ArrayList<>(b));

        //更新运行记录
        voucherRuleRecordRepository.updateById(voucherRuleRecord);
        //更新规则运行状态
        voucherRuleRepository.updateExecuteStateById(rule.getId(), VoucherRuleExecuteStateEnum.空闲.getCode());
    }

    protected void modifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {

    }

    /**
     * 预制凭证
     *
     * @param book             账簿
     * @param template         模板
     * @param businessUnitE    业务单元
     * @param businessBills     业务单据
     * @param voucherMakeError 错误信息
     * @return 凭证信息
     */
    public Voucher doMakeVoucher(VoucherBook book, VoucherTemplate template, BusinessUnitE businessUnitE,
                                 List<VoucherBusinessBill> businessBills, VoucherMakeError voucherMakeError,
                                 VoucherRuleRecord voucherRuleRecord) {
        Voucher voucher = Voucher.create(eventType(), book, template);
        voucher.setVoucherSource(VoucherSourceEnum.系统生成.getCode());
        voucher.setVoucherRuleRecordId(voucherRuleRecord.getId());
        //添加法定单位信息
        voucher.setStatutoryBodyId(businessUnitE.getId());
        voucher.setStatutoryBodyCode(businessUnitE.getCode());
        voucher.setStatutoryBodyName(businessUnitE.getName());
        StringBuilder sb = new StringBuilder();
        businessBills.stream().map(VoucherBusinessBill::getPayChannel).distinct().filter(StringUtils::isNotBlank).forEach(t-> sb.append(SettleChannelEnum.valueNameOfByCode(t)).append(";"));
        if (sb.length() > 0) {
            voucher.setPayChannelValue(sb.substring(0,sb.lastIndexOf(";")));
        }
        // 取第一个，因为已经分组了，所以一些信息通用
        VoucherBusinessBill firstBusinessBill = businessBills.get(0);

        //设置分录信息
        Map<String, SubjectE> subjectMap = new HashMap<>();
        List<VoucherDetailOBV> details = new ArrayList<>();
        List<VoucherDetailOBV> tempDetails = new ArrayList<>();
        printLog(businessBills);
        Map<Long, ChargeItemE> chargeItemEMap = new HashMap<>();
        for (VoucherBusinessBill businessBill : businessBills) {
            ChargeItemE chargeItem = chargeItemEMap.get(businessBill.getChargeItemId());
            if (Objects.isNull(chargeItem)) {
                chargeItem = findChargeItem(businessBill.getChargeItemId());
                if (ObjectUtil.isNull(chargeItem)){
                    continue;
                }
                chargeItemEMap.put(businessBill.getChargeItemId(), chargeItem);
            }
            //业务单据信息转换为指定类型的金额
            Map<String, BigDecimal> logicAmountMap = VoucherEntryLogicUtils.parseAmountMap(businessBill);
            tempDetails = new ArrayList<>();
            for (VoucherTemplateEntryOBV entry : template.getEntries()) {
                //获取映射的科目信息
                // 如果是科目叶子节点，不需要费项有配置对应科目
                SubjectE subject;
                subject = voucherFacade.getSubject(entry.getSubjectId());
                if (Objects.isNull(subject) || SubjectLeafStatusEnum.叶子节点.getCode() != subject.getLeaf()) {
                    subject = voucherFacade.getSubjectByChargeItem(entry.getSubjectId(), businessBill.getChargeItemId());
                    if (Objects.isNull(subject)) {
//                        subject = voucherFacade.getSubjectByCode(entry.getSubjectCode());
//                        if (Objects.isNull(subject)){
//                            voucherMakeError.addMessage("未找到指定科目【" + entry.getSubjectName() + "（" + entry.getSubjectCode()+ "）】");
//                        } else if (subject.getLeaf() != 1) {
//                            voucherMakeError.addMessage("未找到费项【" + businessBill.getChargeItemName() + "】所关联的科目信息");
//                        }
                        continue;
                    }
                }
                subjectMap.put(subject.getSubjectCode(), subject);
                tempDetails.add(createVoucherDetailOBV(entry, subject, chargeItem, businessBill, tempDetails, logicAmountMap,voucherRuleRecord));
            }
            details.addAll(tempDetails);
        }
        voucher.setDetails(mergeDetails(details, template)); //设置分录
        //设置现金流量信息
        log.info("设置现金流量信息: {}   {}", JSON.toJSONString(voucher.getDetails()), JSON.toJSONString(subjectMap));
        voucherDomainService.putVoucherCashFlow(voucher.getDetails(), subjectMap);

        //凭证总金额
        voucher.setAmount(voucher.getDetails().stream().mapToLong(VoucherDetailOBV::getCreditAmount).sum());
        //添加成本中心信息
        if (Objects.nonNull(firstBusinessBill.getCostCenterId())) {
            VoucherCostCenterOBV voucherCostCenterOBV = new VoucherCostCenterOBV(firstBusinessBill.getCostCenterId(), firstBusinessBill.getCostCenterName());
            voucher.setCostCenters(List.of(voucherCostCenterOBV));
            // 增加成本中心的字段
            voucher.setCostCenterId(firstBusinessBill.getCostCenterId());
            voucher.setCostCenterName(firstBusinessBill.getCostCenterName());
        }
        voucher.init();
        log.info("凭证明细: {} ", JSON.toJSONString(voucher));
        return voucher;
    }

    /**
     * 合并分录
     * 借贷方向、科目、辅助核算相同的分录进行合并
     * @param details
     * @return
     */
    public List<VoucherDetailOBV> mergeDetails(List<VoucherDetailOBV> details, VoucherTemplate template) {
        Map<String, VoucherDetailOBV> detailMap = new HashMap<>();
        for (VoucherDetailOBV detail : details) {
            if(!detail.doesTheAmountExist(detail.getOriginalAmount())){
                continue;
            }
            String key = detail.getType() +  detail.getSubjectCode() + detail.findAssisteItemString() + detail.getSummary() + detail.getCashFlowCode();
            if (detailMap.containsKey(key)) {
                VoucherDetailOBV mergedDetail = detailMap.get(key);
                if (mergedDetail.getType().equals(VoucherLoanTypeEnum.借方.getCode())) {
                    mergedDetail.setDebitAmount(mergedDetail.getDebitAmount() + detail.getDebitAmount());
                } else {
                    mergedDetail.setCreditAmount(mergedDetail.getCreditAmount() + detail.getCreditAmount());
                }
                mergedDetail.setOriginalAmount(mergedDetail.getOriginalAmount() + detail.getOriginalAmount());
                mergedDetail.setIncTaxAmount(mergedDetail.getIncTaxAmount() + detail.getIncTaxAmount());
                // 如果有现金流，说明是冲销、作废的反向生成凭证，这个时候已经有现金流，需要合并现金流
                if (StringUtils.isNotBlank(detail.getCashFlowCode())) {
                    List<CashFlowOBV> cashFlows = mergedDetail.getCashFlows();
                    CashFlowOBV cashFlowOBV = cashFlows.get(0);
                    cashFlowOBV.setMoney(cashFlowOBV.getMoney() + detail.getCashFlows().get(0).getMoney());
                }
            }else {
                detailMap.put(key, detail);
            }
        }
        List<VoucherDetailOBV> voucherDetails = new ArrayList<>(detailMap.values());
        voucherDetails.sort((o1, o2) -> {
            if (o1 != null && o2 != null) {
                if (VoucherLoanTypeEnum.借方.getCode().equals(o1.getType()) && VoucherLoanTypeEnum.贷方.getCode().equals(o2.getType())) {
                    return -1;
                } else if (StringUtils.equals(o1.getType(), o2.getType())) {
                    return 0;
                }
            }
            return 1;
        });

        // 如果是红字凭证
        if (template.getRedVoucher() != null && template.getRedVoucher() == 1) {
            voucherDetails.forEach(detail -> {
                //账单冲销事件，对于科目包含库存现金、银行存款的凭证特殊场景处理
                List<String> subjectNameList = Arrays.asList(detail.getSubjectName().split("/"));
                if (subjectNameList.contains("库存现金") || subjectNameList.contains("银行存款") ||
                        subjectNameList.contains("其他货币资金") || subjectNameList.contains("银行存款-过渡")) {
                    //判断如果是红字负数凭证，则做一个反向正数凭证， 把对应金额转变成正数
                    if (detail.getOriginalAmount() < 0) {
                        detail.setOriginalAmount(Math.abs(detail.getOriginalAmount()));
                        //包含库存现金、银行存款的所在的借贷方不变，金额变为正数
                        if (detail.getType().equals(VoucherLoanTypeEnum.借方.getCode())) {
                            detail.setDebitAmount(Math.abs(detail.getDebitAmount()));
                        } else {
                            detail.setCreditAmount(Math.abs(detail.getCreditAmount()));
                        }
                    } else {
                        //反之如果是红字正数凭证，则做一个反向负数凭证
                        //包含库存现金、银行存款的所在的借贷方改变，金额不变
                        if (detail.getType().equals(VoucherLoanTypeEnum.借方.getCode())) {
                            detail.setType(VoucherLoanTypeEnum.贷方.getCode());
                            detail.setCreditAmount(detail.getDebitAmount());
                            detail.setDebitAmount(0L);
                        } else {
                            detail.setType(VoucherLoanTypeEnum.借方.getCode());
                            detail.setDebitAmount(detail.getCreditAmount());
                            detail.setCreditAmount(0L);
                        }
                    }
                } else {
                    //不包含库存现金、银行存款的还是按照之前原逻辑去做凭证
                    if (detail.getOriginalAmount() < 0) {
                        detail.setOriginalAmount(Math.abs(detail.getOriginalAmount()));
                        if (detail.getType().equals(VoucherLoanTypeEnum.借方.getCode())) {
                            detail.setDebitAmount(Math.abs(detail.getDebitAmount()));
                        } else {
                            detail.setCreditAmount(Math.abs(detail.getCreditAmount()));
                        }
                    } else {
                        detail.setOriginalAmount(-detail.getOriginalAmount());
                        if (detail.getType().equals(VoucherLoanTypeEnum.借方.getCode())) {
                            detail.setDebitAmount(-detail.getDebitAmount());
                        } else {
                            detail.setCreditAmount(-detail.getCreditAmount());
                        }
                    }
                }
            });
        }
        return voucherDetails;
    }


    /**
     * 创建凭证详情信息
     *
     * @param entry          凭证规则分录信息
     * @param subject        科目
     * @param chargeItem     费项
     * @param businessBills  业务单据（根据法定单位、成本中心、费项、系统来源分组)故，此list为同一个组
     * @param logicAmountMap 金额映射信息
     * @return 凭证分录详情
     */
    protected VoucherDetailOBV createVoucherDetailOBV(VoucherTemplateEntryOBV entry, SubjectE subject, ChargeItemE chargeItem,
                                                      VoucherBusinessBill businessBill, List<VoucherDetailOBV> details,
                                                      Map<String, BigDecimal> logicAmountMap,VoucherRuleRecord voucherRuleRecord) {
        VoucherDetailOBV voucherDetail = new VoucherDetailOBV();
        voucherDetail.setType(entry.getType());
        voucherDetail.setSubjectId(subject.getId());
        voucherDetail.setSubjectCode(subject.getSubjectCode());
        voucherDetail.setSubjectName(subject.getFullName());
        voucherDetail.putAmount(entry.evalLogic(logicAmountMap, details));
        voucherDetail.setIncTaxAmount(findIncTaxAmount(logicAmountMap,voucherRuleRecord));
        voucherDetail.setSummary(entry.generateSummary(businessBill));
        voucherDetail.setChargeItemId(chargeItem.getId());
        voucherDetail.setChargeItemCode(chargeItem.getCode());
        voucherDetail.setChargeItemName(chargeItem.getName());
        businessBill.setType(entry.getType());
        // 凭证明细中，设置场景业务数据ID
        //获取辅助核算信息
        List<AssisteItemOBV> assisteItems = new ArrayList<>();
        // AssisteItemTypeEnum#code
        List<Integer> assisteItemTypes = subject.convertWithAuxiliaryCount();
        if (CollectionUtils.isNotEmpty(assisteItemTypes)) {
            for (Integer type : assisteItemTypes) {
                businessBill.setOriginalAmount(voucherDetail.getOriginalAmount());
                assisteItems.add(VoucherAssisteItemContext.getStrategy(type).getByBus(businessBill));
            }
        }
        voucherDetail.setAssisteItems(assisteItems);
        return voucherDetail;
    }

    public ChargeItemE findChargeItem(Long chargeItemId) {
        return Objects.nonNull(chargeItemId) ? voucherFacade.getChargeItemById(chargeItemId) : new ChargeItemE();
    }

    public Long findIncTaxAmount(Map<String, BigDecimal> logicAmountMap,VoucherRuleRecord voucherRuleRecord){
        VoucherEventTypeEnum eventTypeEnum = VoucherEventTypeEnum.valueOfByCode(voucherRuleRecord.getEventType());
        switch (eventTypeEnum){
            case 收入退款:
              return logicAmountMap.get(VoucherTemplateLogicCodeEnum.退款金额.getCode()).longValue();
            case 款项结转:
                return logicAmountMap.get(VoucherTemplateLogicCodeEnum.款项结转金额.getCode()).longValue();
        }
              return logicAmountMap.get(VoucherTemplateLogicCodeEnum.实收付金额.getCode()).longValue();
    }

    /**
     * 查找账簿
     *
     * @param rule         规则
     * @param costCenterId 成本中心id
     * @param chargeItemId 费项id
     * @param statutoryBodyId 法定单位id
     * @return 账簿列表
     */
    public List<VoucherBook> findBooks(VoucherRule rule, Long costCenterId, Long chargeItemId,Long statutoryBodyId,Long businessUnitId) {
        if(VoucherBookModeEnum.指定账簿.equalsByCode(rule.getBookMode())){
            return List.of(rule.getBook());
        }else if(Objects.nonNull(businessUnitId) && 0 != businessUnitId){
            List<VoucherBook> voucherBooks = voucherFacade.business_unit_id(businessUnitId);
            if(voucherBooks.size() > 0){
                return voucherBooks;
            }
        }
        return voucherFacade.getBook(costCenterId, chargeItemId,statutoryBodyId);
    }

    /**
     * 添加凭证规则关联的所有核算方案对应的成本中心和法定单位的查询条件
     * @param rule
     */
    public void addOrgCondition(VoucherRule rule) {
        List<VoucherScheme> schemes = voucherSchemeRuleRepository.getSchemeByRuleId(rule.getId());
        List<Long> schemeIds = schemes.stream().map(VoucherScheme::getId).collect(Collectors.toList());
        ErrorAssertUtil.notEmptyThrow400(schemeIds, ErrorMessage.VOUCHER_RULE_NOT_LINK_SCHEME);
        List<VoucherSchemeOrg> voucherSchemeOrgs = voucherSchemeOrgRepository.listBySchemeIds(schemeIds);
        List<Long> statutoryBodyIds = voucherSchemeOrgs.stream().filter(org -> OrgFinanceTypeEnum.法定单位.getCode().equals(org.getOrgType())).map(VoucherSchemeOrg::getOrgId).collect(Collectors.toList());
        List<Long> costCenterIds = voucherSchemeOrgs.stream().filter(org -> OrgFinanceTypeEnum.成本中心.getCode().equals(org.getOrgType())).map(VoucherSchemeOrg::getOrgId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(statutoryBodyIds)) {
            VoucherRuleConditionOBV condition = new VoucherRuleConditionOBV();
            condition.setType(VoucherRuleConditionTypeEnum.法定单位.getCode());
            setIntoRule(rule, statutoryBodyIds, condition);
        }
        if (CollectionUtils.isNotEmpty(costCenterIds)) {
            VoucherRuleConditionOBV condition = new VoucherRuleConditionOBV();
            condition.setType(VoucherRuleConditionTypeEnum.成本中心.getCode());
            setIntoRule(rule, costCenterIds, condition);
        }
    }

    private void setIntoRule(VoucherRule rule, List<Long> costCenterIds, VoucherRuleConditionOBV condition) {
        condition.setMethod(VoucherRuleConditionMethodEnum.包含.getCode());
        List<VoucherRuleConditionOptionOBV> options = costCenterIds.stream().map(id -> {
            VoucherRuleConditionOptionOBV option = new VoucherRuleConditionOptionOBV();
            option.setId(String.valueOf(id));
            return option;
        }).collect(Collectors.toList());
        condition.setValues(options);
        rule.getConditions().removeIf(existCondition ->
                VoucherRuleConditionTypeEnum.法定单位.equalsByCode(existCondition.getType())
                        || VoucherRuleConditionTypeEnum.成本中心.equalsByCode(existCondition.getType()));
        rule.getConditions().add(condition);
    }

    /**
     * 查询法定单位信息
     *
     * @param statutoryBody 法定单位
     * @param statutoryBodyId 法定单位id
     * @return 法定单位信息
     */
    public VoucherStatutoryBody findStatutoryBody(VoucherStatutoryBody statutoryBody, Long statutoryBodyId) {
        if (Objects.nonNull(statutoryBody) && statutoryBody.getStatutoryBodyId().equals(statutoryBodyId)) {
            return statutoryBody;
        }
        return voucherFacade.getVoucherStatutoryBody(statutoryBodyId);
    }

    /**
     * 查询业务单元信息
     *
     * @param businessUnit 业务单元
     * @param businessUnitId 业务单元id
     * @return 业务单元信息
     */
    public BusinessUnitE findBusinessUnit(BusinessUnitE businessUnit, Long businessUnitId) {
        if (Objects.nonNull(businessUnit) && businessUnit.getId().equals(businessUnitId)) {
            return businessUnit;
        }
        return businessUnitRepository.getById(businessUnitId);
    }

    /**
     * 根据预制凭证设置法定单位、成本中心、费项信息
     *
     * @param voucherRuleRecord 推凭记录
     * @param vouchers          预制凭证列表
     */
    protected void putVoucherRuleRecord(VoucherRuleRecord voucherRuleRecord, List<Voucher> vouchers) {
        Map<Long, VoucherStatutoryBody> statutoryBodyOBVMap = new HashMap<>();
        Map<Long, VoucherChargeItemOBV> chargeItemOBVMap = new HashMap<>();
        Map<Long, VoucherCostCenterOBV> costCenterOBVMap = new HashMap<>();
        Map<Long, VoucherAccountBook> accountBookOBVMap = new HashMap<>();
        List<VoucherCostCenterOBV> costCenters;
        if (Objects.isNull(voucherRuleRecord.getDebitAmount())){
            voucherRuleRecord.setDebitAmount(0L);
        }
        if (Objects.isNull(voucherRuleRecord.getCreditAmount())){
            voucherRuleRecord.setCreditAmount(0L);
        }
        for (Voucher voucher : vouchers) {
            if (!accountBookOBVMap.containsKey(voucher.getAccountBookId())) {
                accountBookOBVMap.put(voucher.getAccountBookId(), new VoucherAccountBook(voucher.getAccountBookId(),
                        voucher.getAccountBookCode(), voucher.getAccountBookName()));
            }
            if (!statutoryBodyOBVMap.containsKey(voucher.getStatutoryBodyId())) {
                statutoryBodyOBVMap.put(voucher.getStatutoryBodyId(), new VoucherStatutoryBody(voucher.getStatutoryBodyId(),
                        voucher.getStatutoryBodyCode(), voucher.getStatutoryBodyName()));
            }
            costCenters = voucher.getCostCenters();
            if (CollectionUtils.isNotEmpty(costCenters)) {
                for (VoucherCostCenterOBV costCenter : costCenters) {
                    if (!costCenterOBVMap.containsKey(costCenter.getCostCenterId())) {
                        costCenterOBVMap.put(costCenter.getCostCenterId(),
                                new VoucherCostCenterOBV(costCenter.getCostCenterId(), costCenter.getCostCenterName()));
                    }
                }
            }
            for (VoucherDetailOBV detail : voucher.getDetails()) {
                if (!chargeItemOBVMap.containsKey(detail.getChargeItemId())) {
                    chargeItemOBVMap.put(detail.getChargeItemId(), new VoucherChargeItemOBV(detail.getChargeItemId(), detail.getChargeItemName()));
                }
                voucherRuleRecord.setDebitAmount(voucherRuleRecord.getDebitAmount() + detail.getDebitAmount());
                voucherRuleRecord.setCreditAmount(voucherRuleRecord.getCreditAmount() + detail.getCreditAmount());
            }

        }
        if (CollUtil.isNotEmpty(voucherRuleRecord.getAccountBooks())){
            voucherRuleRecord.getAccountBooks().addAll(accountBookOBVMap.values());
        }else {
            voucherRuleRecord.setAccountBooks( new ArrayList<>(accountBookOBVMap.values()));
        }

        if (CollUtil.isNotEmpty(voucherRuleRecord.getStatutoryBodys())){
            voucherRuleRecord.getStatutoryBodys().addAll(statutoryBodyOBVMap.values());
        }else {
            voucherRuleRecord.setStatutoryBodys( new ArrayList<>(statutoryBodyOBVMap.values()));
        }

        if (CollUtil.isNotEmpty(voucherRuleRecord.getCostCenters())){
            voucherRuleRecord.getCostCenters().addAll(costCenterOBVMap.values());
        }else {
            voucherRuleRecord.setCostCenters( new ArrayList<>(costCenterOBVMap.values()));
        }

        if (CollUtil.isNotEmpty(voucherRuleRecord.getChargeItems())){
            voucherRuleRecord.getChargeItems().addAll(chargeItemOBVMap.values());
        }else {
            voucherRuleRecord.setChargeItems( new ArrayList<>(chargeItemOBVMap.values()));
        }


        voucherRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());

    }

    protected boolean checkSourceSystem(List<VoucherRuleConditionOBV> conditions) {
        if (CollectionUtils.isEmpty(conditions)) {
            return false;
        }
        // 先过滤出来是 单据来源 查询条件的conditions
        List<VoucherRuleConditionOBV> sourceConditions = conditions.stream().filter(e -> Integer.valueOf(VoucherRuleConditionTypeEnum.单据来源.getCode()).equals(e.getType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sourceConditions)) {
            return false;
        }
        // 不考虑规则乱配置的情况，目前只拿第一个即可
        VoucherRuleConditionOBV sourceCondition = sourceConditions.get(0);
        long sourceChargeCount = 0L;
        switch (VoucherRuleConditionMethodEnum.valueOfByCode(sourceCondition.getMethod())){
            case 包含:
                // 包含条件，过滤出有收费系统来源的过滤值，说明是包含收费系统来源过滤条件，直接返回true
                sourceChargeCount = sourceCondition.getValues().stream().filter(e -> SysSourceEnum.收费系统.getCode().equals(Integer.valueOf(e.getId()))).count();
                return sourceChargeCount > 0;
            case 不包含:
                // 不包含条件，没有收费系统来源的过滤值，说明包含收费系统来源过滤条件，直接返回true
                sourceChargeCount = sourceCondition.getValues().stream().filter(e -> SysSourceEnum.收费系统.getCode().equals(Integer.valueOf(e.getId()))).count();
                return sourceChargeCount <= 0;
            default:
                return false;
        }
    }

    protected String dateCheck(LocalDate date){
        LocalDate fiscalPeriod = date;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM");
        String dateStr = fiscalPeriod.format(df);
        return dateStr;
    }

    protected List<Voucher> mergeVoucher(List<Voucher> sourceVouchers, List<VoucherBusinessDetail> voucherBusinessDetails) {
        List<Voucher> resultVouchers = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sourceVouchers)) {
            Map<Long, List<VoucherBusinessDetail>> detailMaps = voucherBusinessDetails.stream()
                    .collect(Collectors.groupingBy(
                            VoucherBusinessDetail::getVoucherId));
            Map<String, List<Voucher>> voucherMap = sourceVouchers.stream().collect(Collectors.groupingBy(
                    item -> item.getAccountBookCode() + item.getCostCenterIdByCostCenters()));
            voucherMap.forEach((key, vouchers) -> {
                Iterator<Voucher> iterator = vouchers.iterator();
                Voucher firstVoucher = iterator.next();
                List<VoucherDetailOBV> details = firstVoucher.getDetails();
                Voucher voucher;
                while (iterator.hasNext()) {
                    voucher = iterator.next();
                    details.addAll(voucher.getDetails());
                    List<VoucherBusinessDetail> businessDetails = detailMaps.get(
                            voucher.getId());
                    businessDetails.forEach(detail -> {
                        detail.setVoucherId(firstVoucher.getId());
                    });
                }
                firstVoucher.setDetails(mergeDetails(details, new VoucherTemplate()));
                //凭证总金额
                firstVoucher.setAmount(firstVoucher.getDetails().stream().mapToLong(VoucherDetailOBV::getCreditAmount).sum());
                resultVouchers.add(firstVoucher);
            });
        }
        return resultVouchers;
    }
}
