package com.wishare.finance.domains.voucher.support.fangyuan.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import com.wishare.finance.apps.model.reconciliation.fo.ListUserInfoF;
import com.wishare.finance.apps.model.reconciliation.fo.PhoneParamF;
import com.wishare.finance.apps.model.reconciliation.fo.UserOrgRoleF;
import com.wishare.finance.apps.model.reconciliation.vo.PhoneThirdPartyIdV;
import com.wishare.finance.apps.model.reconciliation.vo.UserMobileV;
import com.wishare.finance.apps.model.reconciliation.vo.UserOrgRoleV;
import com.wishare.finance.apps.pushbill.fo.BillRuleF;
import com.wishare.finance.apps.pushbill.fo.FundReceiptsBillZJF;
import com.wishare.finance.apps.pushbill.fo.RemindRuleDetailF;
import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.apps.pushbill.vo.RemindRuleDetailV;
import com.wishare.finance.apps.pushbill.vo.RuleRemindConfigDetailV;
import com.wishare.finance.apps.pushbill.vo.TargetInfoV;
import com.wishare.finance.apps.scheduler.mdm.vo.Mdm63Response;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.apps.service.pushbill.VoucherBillDetailAppService;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.GatherDetailRepository;
import com.wishare.finance.domains.bill.repository.mapper.BillReconciliationMapper;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.repository.CashFlowRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.entity.FinancialTaxInfo;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.mapper.FinancialTaxInfoMapper;
import com.wishare.finance.domains.configure.subject.consts.enums.SubjectRuleMapTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.entity.SubjectMapUnitDetailE;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapUnitDetailRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectRepository;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ChangingObjectTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.message.VoucherBillRuleMessageSendService;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimDetailE;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimRecordApproveStateEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimGatherDetailRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBusinessBillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherPayWayEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleFlowTypeEnum;
import com.wishare.finance.domains.voucher.entity.PushBillConditionOptionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.enums.VoucherBillTypeEnums;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.VoucherBillRuleRemindConfigE;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.FyVoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushBillTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.BillRuleConditionZJTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.OverdueStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.RuleSourceEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillDetailZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBillForSettlement;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.RemindTargetTypeEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.finance.infrastructure.remote.fo.space.CommunityIdF;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityOrgV;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 推单规则
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillRuleDomainService {

    private final BillRuleRepository billRuleRepository;

    private final TransactionTemplate transactionTemplate;
    private final PlatformTransactionManager transactionManager;

    private final VoucherPushBillZJRepository voucherPushBillZJRepository;

    private final VoucherBillDetailZJRepository voucherBillDetailZJRepository;

    private final GatherDetailRepository gatherDetailRepository;

    private final FlowClaimRecordRepository flowClaimRecordRepository;

    private final FlowClaimDetailRepository flowClaimDetailRepository;

    private final FlowDetailRepository flowDetailRepository;

    private final SubjectMapUnitDetailRepository subjectMapUnitDetailRepository;

    private final SubjectRepository subjectRepository;
    private final CashFlowRepository cashFlowRepository;
    private final SharedBillAppService sharedBillAppService;

    private final BillReconciliationMapper billReconciliationMapper;

    private final GatherBillRepository gatherBillRepository;

    private final VoucherBillRuleRemindConfigService remindConfigService;

    private final VoucherBillRuleMessageSendService voucherBillRuleMessageSendService;

    private final SpaceClient spaceClient;

    private final UserClient userClient;

    private final ExternalClient externalClient;

    private final ChargeItemRepository chargeItemRepository;
    private final FlowClaimGatherDetailRepository flowClaimGatherDetailRepository;

    private final FinancialTaxInfoMapper financialTaxInfoMapper;
    @Lazy
    @Autowired
    private VoucherBillDetailAppService voucherBillDetailAppService;

    public boolean manualExecute(Long voucherRuleId) {
        BillRule billRule = getRuleCheck(voucherRuleId);
        if (RuleSourceEnum.方圆.getCode() == billRule.getRuleSource())
            billRule.manualExecute();
        if (RuleSourceEnum.中交.getCode() == billRule.getRuleSource())
            billRule.manualZJExecute();
        return true;
    }

    public List<PushZJBusinessBillForSettlement>  autoExecute(VoucherBillGenerateForContractSettlementContext context) {
        // 收入确认-实签
        BillRule billRule = new BillRule();
        context.setCommunityIdList(context.getCommunityIdList());
        context.setBillIdList(context.getBillIdList());
        context.setEventType(context.getEventType());
        billRule.setVoucherBillGenerateForContractSettlementContext(context);
        billRule.setEventType(context.getEventType());
        return billRule.autoTriggerZJExecute(context);
    }

    /**
     * 定時运行规则
     *
     * @param voucherRuleId
     * @return
     */
    public boolean taskExecute(Long voucherRuleId) {
        BillRule voucherRule = getRuleCheck(voucherRuleId);
        voucherRule.taskExecute();
        return true;
    }


    public BillRule getRuleCheck(Long voucherRuleId) {
        BillRule billRule = billRuleRepository.getOne(new LambdaQueryWrapper<BillRule>()
                .eq(BillRule::getId, voucherRuleId)
                .eq(BillRule::getDisabled, 0)
                .eq(BillRule::getDeleted, 0));
        ErrorAssertUtil.notNullThrow403(billRule, ErrorMessage.BILL_RULE_NOT_EXIST);
        return billRule;
    }

    /**
     * 新增推单规则
     *
     * @param addBillRuleF
     * @return
     */
    public Long addRule(BillRuleF addBillRuleF) {
        BillRule billRule = Global.mapperFacade.map(addBillRuleF, BillRule.class);
        billRule.checkParam();
        //校验名称是否存在相同的数据
        BillRule sameRule = billRuleRepository.getByName(billRule.getRuleName());
        ErrorAssertUtil.isNullThrow301(sameRule, ErrorMessage.BILL_RULE_NAME_EXIST);
        billRule.setId(IdentifierFactory.getInstance().generateLongIdentifier("id"));
        boolean saveRes = billRuleRepository.save(billRule);
        if (saveRes && billRule.registerSchedule()) {
            billRuleRepository.updateById(billRule);
        }
        //处理规则
        this.remindRuleSave(billRule.getId(), addBillRuleF.getRuleDetails());
        return billRule.getId();
    }

    /**
     * 修改推单规则
     *
     * @param updateBillRuleF
     * @return
     */
    public boolean updateRule(BillRuleF updateBillRuleF) {
        BillRule rule = Global.mapperFacade.map(updateBillRuleF, BillRule.class);
        rule.checkParam();
        BillRule byIdWithCheck = getByIdWithCheck(rule.getId());
        byIdWithCheck.checkExecuteState();
        //校验名称是否存在相同的数据
        BillRule byName = billRuleRepository.getByName(rule.getRuleName());
        ErrorAssertUtil.isTrueThrow402(Objects.isNull(byName) || byName.getId().equals(rule.getId()), ErrorMessage.BILL_RULE_NAME_EXIST);
        rule.setTenantId(byIdWithCheck.getTenantId());
        rule.updateSchedule(byIdWithCheck);
        billRuleRepository.updateById(rule);
        //处理规则
        this.remindRuleSave(rule.getId(), updateBillRuleF.getRuleDetails());
        return Boolean.TRUE;
    }

    public BillRule getByIdWithCheck(Long billRuleId) {
        BillRule billRule = billRuleRepository.getById(billRuleId);
        ErrorAssertUtil.notNullThrow403(billRule, ErrorMessage.BILL_RULE_NOT_EXIST);
        return billRule;
    }

    /**
     * 保存-报账汇总规则-消息提醒规则配置
     *
     * @param ruleId
     * @param ruleDetails
     * @return
     */
    public void remindRuleSave(Long ruleId, List<RemindRuleDetailF> ruleDetails) {
        if (ObjectUtil.isEmpty(ruleId) || org.springframework.util.CollectionUtils.isEmpty(ruleDetails)) {
            return;
        }
        //先删除已存在的
        remindConfigService.remove(Wrappers.<VoucherBillRuleRemindConfigE>lambdaQuery()
                .eq(VoucherBillRuleRemindConfigE::getRuleId, ruleId));
        //组装新的
        List<VoucherBillRuleRemindConfigE> configs = ruleDetails.stream().map(detail -> {
            VoucherBillRuleRemindConfigE configE = new VoucherBillRuleRemindConfigE();
            configE.setRuleId(ruleId);
            configE.setRemindDay(detail.getRemindDay());
            configE.setTargetType(detail.getTargetType());
            configE.setTargetInfo(JSON.toJSONString(detail.getTargetInfos()));
            configE.setId(UidHelper.nextId("voucher_bill_rule_remind_config"));
            return configE;
        }).collect(Collectors.toList());
        //保存
        remindConfigService.saveBatch(configs);
    }

    /**
     * 推单规则的禁用
     *
     * @param billRuleId
     * @param disabled
     * @return
     */
    public boolean enable(Long billRuleId, int disabled) {
        ErrorAssertUtil.notNullThrow403(DataDisabledEnum.valueOfByCode(disabled), ErrorMessage.DISABLE_PARAM_ERROR);
        BillRule billRule = getByIdWithCheck(billRuleId);
        billRule.enableOrDisabled(disabled);
        return billRuleRepository.updateById(billRule);
    }

    /**
     * 删除推单规则
     *
     * @param billRuleId 推单规则id
     * @return
     */
    public boolean delete(Long billRuleId) {
        BillRule billRule = getByIdWithCheck(billRuleId);
        billRule.delete();
        return billRuleRepository.removeById(billRule.getId());
    }

    public List<PushBillConditionOptionOBV> getConditionOptions(BillRuleConditionZJTypeEnum conditionType) {
        switch (conditionType) {
            case 费项:
                break;
            case 结算方式:
                return Arrays.stream(VoucherPayWayEnum.values()).map(i -> new PushBillConditionOptionOBV(i.getCode(), i.getCode(), i.getValue())).collect(Collectors.toList());
            case 单据类型:
                return Arrays.stream(VoucherBusinessBillTypeEnum.values()).map(i -> new PushBillConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue())).collect(Collectors.toList());
            case 单据来源:
                return Arrays.stream(SysSourceEnum.values()).map(i -> new PushBillConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getDes())).collect(Collectors.toList());
            case 违约金标识:
                return Arrays.stream(OverdueStateEnum.values()).map(i -> new PushBillConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue())).collect(Collectors.toList());
            case 结算时间:
                break;
            case 归属月:
                break;
            case 收费对象类型:
                return Arrays.stream(ChangingObjectTypeEnum.values()).map(i -> new PushBillConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getDes())).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public List<VoucherRuleConditionOptionOBV> getFYConditionOptions(FyVoucherRuleConditionTypeEnum conditionType) {
        switch (conditionType) {
            case 费项:
            case 结算时间:
            case 违约金标识:
            case 归属月:
            case 减免形式:
            case 费用类型:
                break;
//            case 账单类型:
//                return Arrays.stream(VoucherRuleBillTypeEnum.values())
//                        .map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()),
//                                String.valueOf(i.getCode()), i.getValue()))
//                        .collect(Collectors.toList());
            case 结算方式:
                return Arrays.stream(VoucherPayWayEnum.values())
                        .map(i -> new VoucherRuleConditionOptionOBV(i.getCode(), i.getCode(), i.getValue()))
                        .collect(Collectors.toList());
            case 单据来源:
                return Arrays.stream(VoucherRuleFlowTypeEnum.values())
                        .map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getType()),
                                String.valueOf(i.getType()), i.getDesc()))
                        .collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }


    /**
     * 中交特殊定制 根据流水认领记录生成资金收款单
     * @param receiptsBillZJF
     * @return
     */
    public Long executeZjFundReceiptsBill(FundReceiptsBillZJF receiptsBillZJF, List<String> collect) {
        log.info("根据流水认领记录生成资金收款单-receiptsBillZJF：{},collect：{}", JSONArray.toJSON(receiptsBillZJF), JSONArray.toJSON(collect));
        // 根据流水认领记录生成资金收款单
        // 根据流水认领记录id 查找流水认领明细中的收款单id
        List<Long> ids = receiptsBillZJF.getIds();
        if (CollectionUtils.isNotEmpty(ids)){
            Long id = ids.get(0);
            VoucherBillZJ exist = voucherPushBillZJRepository.getBaseMapper().selectByRecordId(id);
            if (exist != null){
                throw BizException.throw400("生成失败，报账单不允许重复生成");
            }
        }
        List<FlowClaimRecordE> flowClaimRecordES1 = flowClaimRecordRepository.listByIds(ids);
        List<Integer> list1 = flowClaimRecordES1.stream().map(FlowClaimRecordE::getPayChannelType).distinct().collect(Collectors.toList());
        if (list1.size() > 1){
            throw  BizException.throw400("生成失败，请选择收款方式同为现金或非现金的认领记录重新生成!");
        }
        List<Long> list = flowClaimGatherDetailRepository.queryIdByRecordIds(ids);
        // 根据收款单id 查找资金收款单数据
        QueryWrapper<?> wrapper = new QueryWrapper<>();
        wrapper.eq("gd.sup_cp_unit_id", collect.get(0));
        wrapper.eq("rb.sup_cp_unit_id", collect.get(0));
        wrapper.in("gd.id", list);
        wrapper.eq("gd.inference_state", 0);
        String gatherBillTableName = sharedBillAppService.getShareTableName(collect.get(0), BillSharedingColumn.收款账单.getTableName());
        String gatherDetailTableName = sharedBillAppService.getShareTableName(collect.get(0), BillSharedingColumn.收款明细.getTableName());
        String receivableBillTableName = sharedBillAppService.getShareTableName(collect.get(0), BillSharedingColumn.应收账单.getTableName());
        List<PushZJBusinessBill> pushZJBusinessBillList = gatherDetailRepository.getFundReceiptsBillZJList(wrapper,  gatherBillTableName,
                 gatherDetailTableName, receivableBillTableName);
        log.info("根据流水认领记录生成资金收款单-pushZJBusinessBillList：{}", JSONArray.toJSON(pushZJBusinessBillList));
        if(StringUtils.isEmpty(pushZJBusinessBillList.get(0).getContractId())){
            Set<Long> chargeItemIdSet = pushZJBusinessBillList.stream()
                    .map(PushZJBusinessBill::getChargeItemId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            /* 查询对应费项的 代收代付属性费项映射 其他应付款是否配置 */
            Long count = subjectMapUnitDetailRepository.countSubjectLevelLastId(
                    163618387334001L, 162056281171106L, chargeItemIdSet
            );
            if (count > 0) {
                log.info("根据流水认领记录生成资金收款单-非合同数据，且费项有其他应付款配置，拉取核销代码数据");
                List<Mdm63Response> mdm63List = voucherBillDetailAppService.getProjectIdRefresh(collect.get(0));
                if(CollectionUtils.isEmpty(mdm63List)){
                    log.info("根据流水认领记录生成资金收款单-未获取到对应mdm63数据，直接跳出");
                    return null;
                }
            }
        }
        // 检测税率
        checkTaxRate(pushZJBusinessBillList);
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(ids);
        long sum = flowClaimRecordES.stream().filter(s -> s.getDifferenceFlag().equals(0)).mapToLong(FlowClaimRecordE::compareAmount).sum();
        log.info("根据流水认领记录生成资金收款单-判断是否有设置业务标识的费项，sum：{}", sum);
        // 判断是否 有差额认领
        // 若有差额认领 判断是否有设置业务标识的费项
        PushZJBusinessBill pushZJBusinessBill = new PushZJBusinessBill();
        if (sum < 0){
            ChargeItemE chargeItemBusinessFlag = chargeItemRepository.getChargeItemBusinessFlag();
            if (null == chargeItemBusinessFlag) {
                throw new BizException(400, "差额认领生成收款单，未设置业务标识费项");
            }
            SubjectE subjectByCode = subjectRepository.getSubjectByCode("02023");
            if (Objects.nonNull(subjectByCode)) {
                SubjectE subject = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode.getId()), chargeItemBusinessFlag.getId(), 1);
                if (Objects.nonNull(subject)){
                    pushZJBusinessBill.setSubjectId(subject.getId());
                    pushZJBusinessBill.setSubjectName(subject.getSubjectName());
                    pushZJBusinessBill.setSubjectExtId(subject.getIdExt());
                }
            }
            SubjectMapUnitDetailE byUnitId = subjectMapUnitDetailRepository.getByUnitId(chargeItemBusinessFlag.getId(), SubjectRuleMapTypeEnum.现金流量.getCode());
            if (Objects.nonNull(byUnitId)) {
                pushZJBusinessBill.setCashFlowItem(byUnitId.getSubjectLevelLastName());
                CashFlowE byId = cashFlowRepository.getById(byUnitId.getSubjectLevelLastId());
                pushZJBusinessBill.setCashFlowItemExtId(byId.getIdExt());
            }
            pushZJBusinessBill.setTaxIncludAmount(sum);
            pushZJBusinessBill.setTaxExcludAmount(sum);
            pushZJBusinessBill.setTaxRate(new BigDecimal("0"));
            pushZJBusinessBill.setTaxAmount(0L);
            pushZJBusinessBill.setBillEventType(2);
            pushZJBusinessBill.setChargeItemId(chargeItemBusinessFlag.getId());
            pushZJBusinessBill.setChargeItemName(chargeItemBusinessFlag.getName());
            pushZJBusinessBill.setCommunityId(pushZJBusinessBillList.get(0).getCommunityId());
            pushZJBusinessBill.setCommunityName(pushZJBusinessBillList.get(0).getCommunityName());
            pushZJBusinessBill.setBillType(VoucherBillTypeEnums.手续费.getCode());
            pushZJBusinessBillList.add(pushZJBusinessBill);

            log.info("根据流水认领记录生成资金收款单-pushZJBusinessBill：{}", JSONArray.toJSON(pushZJBusinessBill));
        }
        Map<String, List<PushZJBusinessBill>> stringListMap = new HashMap<>();
        stringListMap.put("0", pushZJBusinessBillList);
        Long id = null;
        if (pushZJBusinessBillList != null && pushZJBusinessBillList.size() > 0) {
            try {
                id = (Long) transactionTemplate.execute(new TransactionCallback() {
                    public Object doInTransaction(TransactionStatus status) {
                        VoucherBillZJ voucherBillZJ = null;
                        try {
                            for (String key : stringListMap.keySet()) {
                                List<PushZJBusinessBill> list = stringListMap.get(key);
                                voucherBillZJ = voucherPushBill(new BillRule(), list);
                                voucherBillZJ.setRecordIdList(JSONObject.toJSONString(ids));
                                Set<Long> longs = new HashSet<>();
                                for (PushZJBusinessBill pushZJBusinessBill : list) {
                                    longs.add(pushZJBusinessBill.getChargeItemId());
                                }
                                HashMap<Long, SubjectE> longSubjectEHashMap = new HashMap<>();
                                HashMap<Long, SubjectMapUnitDetailE> longSubjectMapUnitDetailEHashMap = new HashMap<>();
                                if (com.alibaba.nacos.common.utils.StringUtils.isBlank(pushZJBusinessBill.getInnerContractNo())){
                                    //小业主匹配业务科目
                                    extractedXyz(longs, longSubjectEHashMap, longSubjectMapUnitDetailEHashMap, list);
                                }else{
                                    //合同匹配业务科目
                                    extractedContract(longs, longSubjectEHashMap, longSubjectMapUnitDetailEHashMap, list);
                                }
                                //flowClaimRecordES1这个列表只会有一条数据，进行批量拆分了
                                if (CollectionUtils.isNotEmpty(flowClaimRecordES1)) {
                                    String receiptRemark = flowClaimRecordES1.get(0).getReceiptRemark();
                                    LocalDateTime gmtExpire = flowClaimRecordES1.get(0).getGmtExpire();
                                    voucherBillZJ.setReceiptRemark(receiptRemark);
                                    voucherBillZJ.setGmtExpire(gmtExpire);
                                }
                                //对接审批流审核状态
                                voucherBillZJ.setApproveState(PushBillApproveStateEnum.待发起.getCode());
                                //外部部门
                                voucherBillZJ.setExternalDepartmentCode(flowClaimRecordES1.get(0).getExternalDepartmentCode());
                                //再执行原来的入库操作，将单据备注和到期日期存储
                                voucherPushBillZJRepository.save(voucherBillZJ);
                                log.info("voucherPushBillZJRepository.save(voucherBillZJ):" + voucherBillZJ.getId());
                                voucherBillDetailZJRepository.saveBatch(Global.mapperFacade.mapAsList(list, VoucherPushBillDetailZJ.class));
                            }
                        } catch (Exception e) {
                            // 抛出异常，回滚事务
                            status.setRollbackOnly();
                            throw e;
                        }
                        return voucherBillZJ.getId();
                    }
                });
            } catch (Exception e) {
                log.error("报账单生成失败 异常：", e);
            }
        }
        return id;
    }

    private void extractedXyz(Set<Long> longs, HashMap<Long, SubjectE> longSubjectEHashMap, HashMap<Long, SubjectMapUnitDetailE> longSubjectMapUnitDetailEHashMap, List<PushZJBusinessBill> list) {
        log.info("根据流水认领记录生成资金收款单-小业主匹配业务科目");
        // 预收款
        SubjectE subjectByCode = subjectRepository.getSubjectByCode("03013");
        SubjectE subjectByCode1 = subjectRepository.getSubjectByCode("03011");
        SubjectE subjectByCode2 = subjectRepository.getSubjectByCode("03005");
        for (Long aLong : longs) {
            SubjectE subject = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode.getId()), aLong, 1);
            SubjectE subject1 = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode1.getId()), aLong, 1);
            SubjectE subject2 = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode2.getId()), aLong, 1);
            if (Objects.nonNull(subject)) {
                longSubjectEHashMap.put(aLong, subject);
            } else if (Objects.nonNull(subject1)){
                longSubjectEHashMap.put(aLong, subject1);
            } else {
                longSubjectEHashMap.put(aLong, subject2);
            }
            SubjectMapUnitDetailE byUnitId = subjectMapUnitDetailRepository.getByUnitId(aLong, SubjectRuleMapTypeEnum.现金流量.getCode());
            if (Objects.nonNull(byUnitId)) {
                longSubjectMapUnitDetailEHashMap.put(aLong, byUnitId);
            }
        }
        for (PushZJBusinessBill pushZJBusinessBill : list) {
            SubjectE subjectE = longSubjectEHashMap.get(pushZJBusinessBill.getChargeItemId());
            if (Objects.nonNull(subjectE) &&  pushZJBusinessBill.getTaxIncludAmount() > 0){
                pushZJBusinessBill.setSubjectId(subjectE.getId());
                pushZJBusinessBill.setSubjectName(subjectE.getSubjectName());
                pushZJBusinessBill.setSubjectExtId(subjectE.getIdExt());
            }
            SubjectMapUnitDetailE subjectMapUnitDetailE = longSubjectMapUnitDetailEHashMap.get(pushZJBusinessBill.getChargeItemId());
            if (Objects.nonNull(subjectMapUnitDetailE) && pushZJBusinessBill.getTaxIncludAmount() > 0) {
                pushZJBusinessBill.setCashFlowItem(subjectMapUnitDetailE.getSubjectLevelLastName());
                CashFlowE byId = cashFlowRepository.getById(subjectMapUnitDetailE.getSubjectLevelLastId());
                if(Objects.isNull(byId)){
                    log.info("根据流水认领记录生成资金收款单-获取现金流项目来源id失败：当前科目映射数据：{}",JSONArray.toJSON(subjectMapUnitDetailE));
                }
                pushZJBusinessBill.setCashFlowItemExtId(byId.getIdExt());
            }
        }
    }

    //合同匹配业务科目
    private void extractedContract(Set<Long> longs, HashMap<Long, SubjectE> longSubjectEHashMap, HashMap<Long, SubjectMapUnitDetailE> longSubjectMapUnitDetailEHashMap, List<PushZJBusinessBill> list) {
        //预收款
        SubjectE subjectByCode = subjectRepository.getSubjectByCode("03013");
        //应收款
        SubjectE subjectByCode3 = subjectRepository.getSubjectByCode("03001");
        //其他应付款
        SubjectE subjectByCode1 = subjectRepository.getSubjectByCode("03011");
        //其他应收款
        SubjectE subjectByCode2 = subjectRepository.getSubjectByCode("03005");
        Map<Long, List<SubjectE>> stringListMap = new HashMap<>();
        List<SubjectE> srList = new ArrayList<>();
        for (Long aLong : longs) {
            SubjectE subjectYs = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode.getId()), aLong, 1);
            SubjectE subjectYiS = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode3.getId()), aLong, 1);
            SubjectE subjectQtyf = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode1.getId()), aLong, 1);
            SubjectE subjectQWtys = subjectMapUnitDetailRepository.getSubject(Long.valueOf(subjectByCode2.getId()), aLong, 1);
            if(Objects.nonNull(subjectYs)){
                subjectYs.setCreator("预收款");
                srList.add(subjectYs);
            }
            if(Objects.nonNull(subjectYiS)){
                subjectYiS.setCreator("应收款");
                srList.add(subjectYiS);
            } else if (Objects.nonNull(subjectQtyf)){
                longSubjectEHashMap.put(aLong, subjectQtyf);
            } else {
                longSubjectEHashMap.put(aLong, subjectQWtys);
            }
            stringListMap.put(aLong,srList);
            SubjectMapUnitDetailE byUnitId = subjectMapUnitDetailRepository.getByUnitId(aLong, SubjectRuleMapTypeEnum.现金流量.getCode());
            if (Objects.nonNull(byUnitId)) {
                longSubjectMapUnitDetailEHashMap.put(aLong, byUnitId);
            }
        }
        for (PushZJBusinessBill pushZJBusinessBill : list) {
            //收费时间
            LocalDateTime payTime = pushZJBusinessBill.getPayTime();
            //归属月
            LocalDate accountDate = pushZJBusinessBill.getAccountDate();
            YearMonth payTimeYearMonth = YearMonth.from(payTime);
            YearMonth accountDateYearMonth = YearMonth.from(accountDate);
            List<SubjectE> subjectList = stringListMap.get(pushZJBusinessBill.getChargeItemId());
            if(CollectionUtils.isNotEmpty(subjectList) && pushZJBusinessBill.getTaxIncludAmount() > 0 ){
                SubjectE subjectE = new SubjectE();
                //两个时间按年月比较大小
                if(payTimeYearMonth.compareTo(accountDateYearMonth)<=0){
                    subjectE = subjectList.stream().filter(x->"预收款".equals(x.getCreator())).findFirst().orElse(new SubjectE());
                }else{
                    subjectE = subjectList.stream().filter(x->"应收款".equals(x.getCreator())).findFirst().orElse(new SubjectE());
                }
                pushZJBusinessBill.setSubjectId(subjectE.getId());
                pushZJBusinessBill.setSubjectName(subjectE.getSubjectName());
                pushZJBusinessBill.setSubjectExtId(subjectE.getIdExt());
            }
            SubjectE subjectE = longSubjectEHashMap.get(pushZJBusinessBill.getChargeItemId());
            if (Objects.nonNull(subjectE) &&  pushZJBusinessBill.getTaxIncludAmount() > 0){
                pushZJBusinessBill.setSubjectId(subjectE.getId());
                pushZJBusinessBill.setSubjectName(subjectE.getSubjectName());
                pushZJBusinessBill.setSubjectExtId(subjectE.getIdExt());
            }
            SubjectMapUnitDetailE subjectMapUnitDetailE = longSubjectMapUnitDetailEHashMap.get(pushZJBusinessBill.getChargeItemId());
            if (Objects.nonNull(subjectMapUnitDetailE) && pushZJBusinessBill.getTaxIncludAmount() > 0) {
                pushZJBusinessBill.setCashFlowItem(subjectMapUnitDetailE.getSubjectLevelLastName());
                CashFlowE byId = cashFlowRepository.getById(subjectMapUnitDetailE.getSubjectLevelLastId());
                pushZJBusinessBill.setCashFlowItemExtId(byId.getIdExt());
            }
        }
    }

    private void checkTaxRate(List<PushZJBusinessBill> pushZJBusinessBillList) {
        if (CollectionUtils.isEmpty(pushZJBusinessBillList)){
            return;
        }
        PushZJBusinessBill noTaxRateObj = pushZJBusinessBillList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getTaxRateStr())).findFirst().orElse(null);
        if (Objects.nonNull(noTaxRateObj)){
            log.error("生成失败，不允许含有 差额纳税 的账单参与生成资金收款单");
            throw BizException.throw400("生成失败，不允许含有 差额纳税 的账单参与生成资金收款单");
        }
        Set<BigDecimal> taxRateSet = pushZJBusinessBillList.stream()
                .map(PushZJBusinessBill::getTaxRate).collect(Collectors.toSet());
        String taxTypeId = "62f62cde-1396-68be-ac2b-083a01b170bf";
        List<FinancialTaxInfo> taxInfoList = financialTaxInfoMapper.getListByTaxRateAndTaxTypeId(taxRateSet, taxTypeId);
        Map<String, String> rateToIdMap = taxInfoList.stream()
                .collect(Collectors.toMap(e -> e.getApplicableTaxRate().stripTrailingZeros().toPlainString(), FinancialTaxInfo::getId, (v1, v2) -> v1));
        for (PushZJBusinessBill businessBill : pushZJBusinessBillList) {
            String financialTaxId = rateToIdMap.get(businessBill.getTaxRate().stripTrailingZeros().toPlainString());
            if (StringUtils.isBlank(financialTaxId)){
                log.error("生成失败，未维护对应增值税id,无法生成报账单,请联系系统管理员");
                throw BizException.throw400("税率"+businessBill.getTaxRate()+"未维护对应增值税id,无法生成报账单,请联系系统管理员");
            }
        }
    }

    public VoucherBillZJ voucherPushBill(BillRule rule, List<PushZJBusinessBill> businessBills) {
        VoucherBillZJ voucherBill = new VoucherBillZJ();
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        voucherBill.setRuleId(rule.getId());
        voucherBill.setRuleName(rule.getRuleName());
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(businessBills.get(0).getCostCenterId());
        voucherBill.setCostCenterName(businessBills.get(0).getCostCenterName());
        voucherBill.setBillEventType(2);
        voucherBill.setRuleName("资金收款单");
        voucherBill.setApproveState(PushBillApproveStateEnum.审核中.getCode());
        Set<Long> set = new HashSet<>();
        for (PushZJBusinessBill businessBill : businessBills) {
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            if (null == businessBill.getTaxIncludAmount()){
                countAmount(businessBill, businessBill.getSettleAmount());
            }
            businessBill.setBillEventType(2);
            set.add(businessBill.getBusinessUnitId());
        }
        voucherBill.setTotalAmount(businessBills.stream().collect(Collectors.summingLong(PushZJBusinessBill::getTaxIncludAmount)));
        return voucherBill;

    }


    protected void countAmount(PushZJBusinessBill businessBill, Long settleAmount) {
        //(1+税率)
        BigDecimal decimal = businessBill.getTaxRate().add(new BigDecimal("1"));
        if (Objects.isNull(settleAmount)) {
            settleAmount = 0l;
        }
        BigDecimal taxAmount = businessBill.getTaxRate()
                .multiply(new BigDecimal(settleAmount))
                .divide(decimal, 0, RoundingMode.HALF_UP);
        //不含税金额
        Long taxExcludAmount = settleAmount - taxAmount.longValue();
        businessBill.setTaxIncludAmount(settleAmount);
        businessBill.setTaxExcludAmount(taxExcludAmount);
        businessBill.setTaxAmount(taxAmount.longValue());
    }

    public boolean updateFlow(List<Long> ids,
            FlowClaimRecordApproveStateEnum flowClaimRecordApproveStateEnum, FlowClaimStatusEnum flowClaimStatusEnum, boolean approve) {
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(ids);
        for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordES) {
            flowClaimRecordE.setApproveState(flowClaimRecordApproveStateEnum.getCode());
            if (FlowClaimRecordApproveStateEnum.已审核.equalsByCode(flowClaimRecordApproveStateEnum.getCode()) && approve) {
                flowClaimRecordE.setPushState(1);
            } else if (FlowClaimRecordApproveStateEnum.已审核.equalsByCode(flowClaimRecordApproveStateEnum.getCode()) && !approve) {
                flowClaimRecordE.setPushState(4);
            }
        }
        flowClaimRecordRepository.updateBatchById(flowClaimRecordES);

        List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(ids);
        List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getFlowId).collect(Collectors.toList());
        List<FlowDetailE> flowDetailES = flowDetailRepository.listByIds(collect);
        for (FlowDetailE flowDetailE : flowDetailES) {
            flowDetailE.setClaimStatus(flowClaimStatusEnum.getCode());
        }
        flowDetailRepository.updateBatchById(flowDetailES);
        updateReconcileStateAfterApprove(ids, true);
        return true;
    }

    /**
     * 推单之后更新流水认领记录
     * @return
     */
    public boolean updateFlowAfterPush(List<Long> ids, Boolean flag){
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(ids);
        for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordES) {
            if (flag) {
                flowClaimRecordE.setPushState(2);
            } else {
                flowClaimRecordE.setPushState(3);
            }
        }
        flowClaimRecordRepository.updateBatchById(flowClaimRecordES);
        if (flag) {
            // 更新收款单状态
            List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(ids);
            List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            billReconciliationMapper.updateGather(collect,2,0, flowClaimRecordES.get(0).getSupCpUnitId());
            List<GatherDetail> gatherBillIds = gatherDetailRepository.getByGatherBillIdsForReconciliation(collect, flowClaimRecordES.get(0).getSupCpUnitId());
            ArrayList<Long> longs = new ArrayList<>();
            for (GatherDetail gatherBillId : gatherBillIds) {
                longs.add(gatherBillId.getRecBillId());
            }
            if (null != longs && longs.size() > 0){
                billReconciliationMapper.updateReceivableBill(longs,2,
                        0,flowClaimRecordES.get(0).getSupCpUnitId());
            }
        }
        return true;
    }
    public boolean updateFlowAfterDel(List<Long> ids){
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(ids);
        if(CollectionUtils.isEmpty(flowClaimRecordES)){
            return true;
        }
        for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordES) {
            flowClaimRecordE.setPushState(0);
            flowClaimRecordE.setVoucherBillNo("");
            flowClaimRecordE.setVoucherBillId(null);
        }
        List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(ids);
        List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
        billReconciliationMapper.updateGather(collect,0,0, flowClaimRecordES.get(0).getSupCpUnitId());
        flowClaimRecordRepository.updateBatchById(flowClaimRecordES);
        List<GatherDetail> gatherBillIds = gatherDetailRepository.getByGatherBillIdsForReconciliation(collect, flowClaimRecordES.get(0).getSupCpUnitId());
        ArrayList<Long> longs = new ArrayList<>();
        for (GatherDetail gatherBillId : gatherBillIds) {
            longs.add(gatherBillId.getRecBillId());
        }
        if (null != longs && longs.size() > 0){
            for (Long aLong : longs) {
                List<GatherDetail> byRecBillId = gatherDetailRepository.getByRecBillId(aLong, flowClaimRecordES.get(0).getSupCpUnitId(), null);
                List<String> collect1 = byRecBillId.stream().map(GatherDetail::getGatherBillNo).distinct().collect(Collectors.toList());
                List<GatherBill> gatherBillByBillNo = gatherBillRepository.getGatherBillByBillNo(collect1, flowClaimRecordES.get(0).getSupCpUnitId());
                List<Integer> list = gatherBillByBillNo.stream().map(GatherBill::getReconcileState).distinct().collect(Collectors.toList());
                if (list.contains(2)){
                    billReconciliationMapper.updateReceivableBill(Collections.singletonList(aLong),1,
                            0,flowClaimRecordES.get(0).getSupCpUnitId());
                } else {
                    billReconciliationMapper.updateReceivableBill(Collections.singletonList(aLong),0,
                            0,flowClaimRecordES.get(0).getSupCpUnitId());
                }
            }


        }
        return true;
    }

    /**
     *
     * @param ids 流水认领记录id
     * @param flag
     * @return
     */

    // 生成资金收款单后 对账状态更新
    public boolean  updateReconcileStateAfterApprove(List<Long> ids, Boolean flag){
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(ids);
        // 无需审核、 审核中、 审核通过都是更新为 已对账
        if (flag) {
            List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(ids);
            List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            billReconciliationMapper.updateGather(collect,2,0, flowClaimRecordES.get(0).getSupCpUnitId());
            List<GatherDetail> gatherBillIds = gatherDetailRepository.getByGatherBillIdsForReconciliation(collect, flowClaimRecordES.get(0).getSupCpUnitId());
            ArrayList<Long> longs = new ArrayList<>();
            for (GatherDetail gatherBillId : gatherBillIds) {
                longs.add(gatherBillId.getRecBillId());
            }
            if (null != longs && longs.size() > 0){
                billReconciliationMapper.updateReceivableBill(longs,2,
                        0,flowClaimRecordES.get(0).getSupCpUnitId());
            }
        } else {
            List<FlowClaimDetailE> flowClaimDetailES = flowClaimDetailRepository.queryByFlowClaimRecordId(ids);
            List<Long> collect = flowClaimDetailES.stream().map(FlowClaimDetailE::getInvoiceId).collect(Collectors.toList());
            billReconciliationMapper.updateGather(collect,0,0, flowClaimRecordES.get(0).getSupCpUnitId());
            List<GatherDetail> gatherBillIds = gatherDetailRepository.getByGatherBillIdsForReconciliation(collect, flowClaimRecordES.get(0).getSupCpUnitId());
            ArrayList<Long> longs = new ArrayList<>();
            for (GatherDetail gatherBillId : gatherBillIds) {
                longs.add(gatherBillId.getRecBillId());
            }
            //
            if (null != longs && longs.size() > 0){
                for (Long aLong : longs) {
                    List<GatherDetail> byRecBillId = gatherDetailRepository.getByRecBillId(aLong, flowClaimRecordES.get(0).getSupCpUnitId(), null);
                    List<String> collect1 = byRecBillId.stream().map(GatherDetail::getGatherBillNo).distinct().collect(Collectors.toList());
                    List<GatherBill> gatherBillByBillNo = gatherBillRepository.getGatherBillByBillNo(collect1, flowClaimRecordES.get(0).getSupCpUnitId());
                    List<Integer> list = gatherBillByBillNo.stream().map(GatherBill::getReconcileState).distinct().collect(Collectors.toList());
                    if (list.contains(2)){
                        billReconciliationMapper.updateReceivableBill(Collections.singletonList(aLong),1,
                                0,flowClaimRecordES.get(0).getSupCpUnitId());
                    } else {
                        billReconciliationMapper.updateReceivableBill(Collections.singletonList(aLong),0,
                                0,flowClaimRecordES.get(0).getSupCpUnitId());
                    }
                }


            }
        }
    return true;
    }

    /**
     * 查询报账汇总规则提醒配置
     *
     * @param id
     * @return
     */
    public RuleRemindConfigDetailV remindRuleDetail(Long id) {
        RuleRemindConfigDetailV detailV = new RuleRemindConfigDetailV();
        detailV.setRuleId(id);
        detailV.setRuleDetails(new ArrayList<>());
        List<VoucherBillRuleRemindConfigE> configs = remindConfigService.list(Wrappers.<VoucherBillRuleRemindConfigE>lambdaQuery()
                .eq(VoucherBillRuleRemindConfigE::getRuleId, id)
                .eq(VoucherBillRuleRemindConfigE::getDeleted, 0));
        if (CollectionUtils.isEmpty(configs)) {
            return detailV;
        }
        List<RemindRuleDetailV> ruleDetails = configs.stream().map(config -> {
            RemindRuleDetailV ruleDetailV = new RemindRuleDetailV();
            ruleDetailV.setRemindDay(config.getRemindDay());
            ruleDetailV.setTargetType(config.getTargetType());
            ruleDetailV.setTargetInfos(JSON.parseArray(config.getTargetInfo(), TargetInfoV.class));
            return ruleDetailV;
        }).collect(Collectors.toList());
        detailV.setRuleDetails(ruleDetails);
        return detailV;
    }

    public Boolean testSend() {
        Set<String> user4ACodes = new HashSet<>();
        user4ACodes.add("L20143704");
        voucherBillRuleMessageSendService.send2JJ(user4ACodes, "测试项目-测试规则1");
        return Boolean.TRUE;
    }

    public void send() {
        //汇总所有配置
        List<VoucherBillRuleRemindConfigE> configs = gatherConfigs();
        if (CollectionUtils.isEmpty(configs)) {
            log.info("当前没有满足时间条件的规则配置");
            return;
        }
        log.info("当前满足条件的规则配置:{}", JSON.toJSONString(configs));
        //汇总所有报账汇总规则
        List<BillRule> billRules = gatherBillRule(configs);
        Map<Long, BillRule> billRuleMap = billRules.stream().collect(Collectors.toMap(BillRule::getId, Function.identity(), (v1, v2) -> v1));
        //汇总所有的项目id
        List<CommunityOrgV> communityOrgs = gatherCommunityOrg(billRules);
        log.info("报账汇总规则项目涉及到的全部组织信息:{}", JSON.toJSONString(communityOrgs));
        Map<String, CommunityOrgV> communityOrgMap = communityOrgs.stream().collect(Collectors.toMap(CommunityOrgV::getCommunityId, Function.identity(), (v1, v2) -> v1));
        //汇总所有角色类下的人员id和角色、组织信息
        List<UserOrgRoleV> userOrgRoleVS = gatherUserIdByRole(configs, communityOrgs);
        log.info("角色类涉及到全部人员信息:{}",JSON.toJSONString(userOrgRoleVS));
        //汇总所有涉及到的人手机号
        List<UserMobileV> userMobileVS = gatherUserPhone(configs, userOrgRoleVS);
        Map<String, UserMobileV> userMobileMap = userMobileVS.stream().collect(Collectors.toMap(UserMobileV::getId, Function.identity(), (v1, v2) -> v1));
        //汇总所有涉及到的人4A编号
        List<PhoneThirdPartyIdV> user4ACodeVS = gatherUser4ACode(userMobileVS);
        Map<String, PhoneThirdPartyIdV> user4ACodeMap = user4ACodeVS.stream().collect(Collectors.toMap(PhoneThirdPartyIdV::getPhone, Function.identity(), (v1, v2) -> v1));

        for (VoucherBillRuleRemindConfigE config : configs) {
            if (RemindTargetTypeEnum.PERSON.getCode().equals(config.getTargetType()) && StringUtils.isNotBlank(config.getTargetInfo())) {
                String title = getTitle(billRuleMap, config);
                Set<String> targetUserIds = JSON.parseArray(config.getTargetInfo(), TargetInfoV.class).stream().map(TargetInfoV::getTargetId).collect(Collectors.toSet());
                sendMsg(targetUserIds, title, userMobileMap, user4ACodeMap);
            } else {
                String title = getTitle(billRuleMap, config);
                Set<String> targetUserIds = getTargetUserIdsInRole(billRuleMap, communityOrgMap, userOrgRoleVS, config);
                sendMsg(targetUserIds, title, userMobileMap, user4ACodeMap);
            }
        }
    }

    /**
     * 规则-消息标题
     *
     * @param billRuleMap
     * @param config
     * @return
     */
    private String getTitle(Map<Long, BillRule> billRuleMap, VoucherBillRuleRemindConfigE config) {
        BillRule billRule = billRuleMap.getOrDefault(config.getRuleId(), new BillRule());
        String scopeName = "";
        if (CollectionUtils.isNotEmpty(billRule.getScopeApplication())) {
            scopeName = billRule.getScopeApplication().stream().findAny().orElseGet(ScopeF::new).getName();
        }
        return scopeName + "-" + billRule.getRuleName();
    }

    /**
     * 规则-角色类下的人员id
     *
     * @param billRuleMap
     * @param communityOrgMap
     * @param userOrgRoleVS
     * @param config
     * @return
     */
    private Set<String> getTargetUserIdsInRole(Map<Long, BillRule> billRuleMap, Map<String, CommunityOrgV> communityOrgMap, List<UserOrgRoleV> userOrgRoleVS, VoucherBillRuleRemindConfigE config) {
        BillRule billRule = billRuleMap.getOrDefault(config.getRuleId(), new BillRule());
        Set<String> communityIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(billRule.getScopeApplication())) {
            communityIds = billRule.getScopeApplication().stream().map(ScopeF::getId).collect(Collectors.toSet());
        }
        //汇总所有组织
        Set<String> configOrgIds = communityIds.stream()
                .filter(communityOrgMap::containsKey)
                .map(id -> communityOrgMap.get(id).getOrgIds())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        //汇总所有角色
        Set<String> configRoleIds = JSON.parseArray(config.getTargetInfo(), TargetInfoV.class).stream().map(TargetInfoV::getTargetId).collect(Collectors.toSet());
        return userOrgRoleVS.stream().filter(item -> {
            List<String> res1 = configOrgIds.stream().filter(item.getOrgIds()::contains).collect(Collectors.toList());
            List<String> res2 = configRoleIds.stream().filter(item.getRoleIds()::contains).collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(res1) && CollectionUtils.isNotEmpty(res2);
        }).map(UserOrgRoleV::getUserId).collect(Collectors.toSet());
    }

    /**
     * 发送消息
     *
     * @param targetUserIds
     * @param title
     * @param userMobileMap
     * @param user4ACodeMap
     */
    private void sendMsg(Set<String> targetUserIds, String title, Map<String, UserMobileV> userMobileMap, Map<String, PhoneThirdPartyIdV> user4ACodeMap) {
        log.info("当前targetUserIds:{}", targetUserIds);
        voucherBillRuleMessageSendService.send2PC(targetUserIds, title);
        Set<String> user4ACodes = targetUserIds.stream()
                .filter(userId -> userMobileMap.containsKey(userId) && user4ACodeMap.containsKey(userMobileMap.get(userId).getMobileNum()))
                .map(userId -> user4ACodeMap.get(userMobileMap.get(userId).getMobileNum()).getThirdPartyId())
                .collect(Collectors.toSet());
        log.info("对用得到的4A编号:{}", user4ACodes);
        voucherBillRuleMessageSendService.send2JJ(user4ACodes, title);
    }

    /**
     * 规则
     *
     * @return
     */
    private List<VoucherBillRuleRemindConfigE> gatherConfigs() {
        return remindConfigService.list(Wrappers.<VoucherBillRuleRemindConfigE>lambdaQuery()
                .eq(VoucherBillRuleRemindConfigE::getRemindDay, LocalDate.now().getDayOfMonth())
                .eq(VoucherBillRuleRemindConfigE::getDeleted, 0));
    }

    /**
     * 报账汇总规则
     *
     * @param configs
     * @return
     */
    private List<BillRule> gatherBillRule(List<VoucherBillRuleRemindConfigE> configs) {
        Set<Long> ruleIds = configs.stream().map(VoucherBillRuleRemindConfigE::getRuleId).collect(Collectors.toSet());
        return billRuleRepository.listByIds(ruleIds);
    }

    /**
     * 获取项目所属组织
     *
     * @param billRules
     * @return
     */
    private List<CommunityOrgV> gatherCommunityOrg(List<BillRule> billRules) {
        List<String> communityIds = billRules.stream()
                .map(BillRule::getScopeApplication)
                .flatMap(Collection::stream)
                .map(ScopeF::getId).distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(communityIds)) {
            return new ArrayList<>();
        }
        //查communityIds关联的orgIds
        CommunityIdF communityIdF = new CommunityIdF(communityIds);
        List<CommunityOrgV> orgIdsByCommunityIds = spaceClient.getOrgIdsByCommunityIds(communityIdF);
        if (CollectionUtils.isEmpty(orgIdsByCommunityIds)) {
            return new ArrayList<>();
        }
        return orgIdsByCommunityIds;
    }

    /**
     * 获取指定角色和组织下的人员
     *
     * @param configs
     * @param communityOrgs
     * @return
     */
    private List<UserOrgRoleV> gatherUserIdByRole(List<VoucherBillRuleRemindConfigE> configs, List<CommunityOrgV> communityOrgs) {
        List<UserOrgRoleV> res = new ArrayList<>();
        Set<String> roleIds = configs.stream()
                .filter(config -> RemindTargetTypeEnum.ROLE.getCode().equals(config.getTargetType())
                        && StringUtils.isNotBlank(config.getTargetInfo()))
                .map(config -> JSON.parseArray(config.getTargetInfo(), TargetInfoV.class))
                .flatMap(Collection::stream)
                .map(TargetInfoV::getTargetId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(roleIds)) {
            return res;
        }

        Set<String> orgIds = communityOrgs.stream()
                .map(CommunityOrgV::getOrgIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(orgIds)) {
            return res;
        }
        //查询roleIds+orgIds下的人员信息
        UserOrgRoleF userOrgRoleF = new UserOrgRoleF(roleIds, orgIds);
        log.info("获取指定角色和组织下的人员参数:{}", JSON.toJSONString(userOrgRoleF));
        res = userClient.getUserOrgRole(userOrgRoleF);
        return res;
    }

    /**
     * 获取人员手机号
     *
     * @param configs
     * @param userOrgRoleVS
     * @return
     */
    private List<UserMobileV> gatherUserPhone(List<VoucherBillRuleRemindConfigE> configs, List<UserOrgRoleV> userOrgRoleVS) {
        Set<String> userIds = configs.stream()
                .filter(config -> RemindTargetTypeEnum.PERSON.getCode().equals(config.getTargetType())
                        && StringUtils.isNotBlank(config.getTargetInfo()))
                .map(config -> JSON.parseArray(config.getTargetInfo(), TargetInfoV.class))
                .flatMap(Collection::stream)
                .map(TargetInfoV::getTargetId)
                .collect(Collectors.toSet());
        Set<String> swapUserIds = userOrgRoleVS.stream().map(UserOrgRoleV::getUserId).collect(Collectors.toSet());
        userIds.addAll(swapUserIds);
        if (CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }

        //查用户手机号
        ListUserInfoF listUserInfoF = new ListUserInfoF();
        listUserInfoF.setUserIds(new ArrayList<>(userIds));
        listUserInfoF.setGatewaySymbol("saas");
        List<UserMobileV> userMobileVS = userClient.listUserInfoBy(listUserInfoF);
        if (CollectionUtils.isEmpty(userMobileVS)) {
            return new ArrayList<>();
        }
        return userMobileVS;
    }

    /**
     * 获取人员4A编号
     *
     * @param userMobileVS
     * @return
     */
    private List<PhoneThirdPartyIdV> gatherUser4ACode(List<UserMobileV> userMobileVS) {
        if (CollectionUtils.isEmpty(userMobileVS)) {
            return new ArrayList<>();
        }
        List<String> phones = userMobileVS.stream().map(UserMobileV::getMobileNum).distinct().collect(Collectors.toList());
        PhoneParamF phoneParamF = new PhoneParamF();
        phoneParamF.setPhones(phones);
        List<PhoneThirdPartyIdV> userThirdPartyIdByPhone = externalClient.getUserThirdPartyIdByPhone(phoneParamF);
        if (CollectionUtils.isEmpty(userThirdPartyIdByPhone)) {
            return new ArrayList<>();
        }
        return userThirdPartyIdByPhone;
    }
}
