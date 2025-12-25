package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.voucher.consts.enums.InferenceStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleExecuteStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleStateEnum;
import com.wishare.finance.domains.voucher.strategy.core.AbstractVoucherStrategy;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.*;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.*;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRecordRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ApproveStatusEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.facade.PushBillZJFacade;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中交推单策略抽象
 */

public abstract class AbstractPushBillZJStrategy<C extends BillZJStrategyCommand> implements BillZJStrategy<C> {
    private static final Logger log = LoggerFactory.getLogger(AbstractVoucherStrategy.class);
    @Resource
    protected BillRuleRepository billRuleRepository;
    @Resource
    protected BillRuleRecordRepository billRuleRecordRepository;
    @Resource
    protected VoucherPushBillZJRepository voucherPushBillZJRepository;
    @Resource
    protected VoucherBillDetailZJRepository voucherBillDetailZJRepository;

    @Resource
    protected VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    @Resource
    protected VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;

    @Resource
    protected SpaceClient spaceClient;
    @Autowired
    protected PushBillZJFacade pushBillZJFacade;
    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Autowired
    private PushBillZJDomainService pushBillZJDomainService;
    /**
     * 中交推单模式
     */
    protected ZJPushMethod pushMethod;
    /**
     * 事件类型
     */
    protected ZJTriggerEventBillTypeEnum eventType;

    @Autowired
    private FinanceProcessRecordZJRepository financeProcessRecordZJRepository;

    public AbstractPushBillZJStrategy(ZJPushMethod pushMethod, ZJTriggerEventBillTypeEnum eventType) {
        this.pushMethod = pushMethod;
        this.eventType = eventType;
    }

    @Override
    public ZJPushMethod pushMethod() {
        return pushMethod;
    }

    @Override
    public ZJTriggerEventBillTypeEnum eventType() {
        return eventType;
    }

    /**
     * 获取业务单据列表
     *
     * @param command
     * @param conditions 过滤条件信息
     * @return 业务单据列表
     */
    public abstract List<PushZJBusinessBill> businessZJBills(C command, List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds);

    /**
     * 获得业务单据列表-对下结算单-计提
     * @param conditions 过滤条件信息
     * @param communityIds 项目id
     * @return 业务单据列表-对下结算单的
     **/
    public abstract List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds);

    public abstract List<PushZJBusinessBillForSettlement> businessZJBillsForPayIncome(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds);
    /**
     * 获得业务单据列表-对下结算单-实签
     * @param conditions 过滤条件信息
     * @param communityIds 项目id
     * @return 业务单据列表-对下结算单的
     **/
    public abstract List<PushZJBusinessBillForSettlement> businessZJBillsForSettlement(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> billIdList);

    public abstract List<PushZJBusinessBillForSettlement> businessZJBillsForPaymentApplicationForm(List<VoucherBillRuleConditionOBV> conditions, List<String> communityIds, List<String> settlementIdList);
    /**
     * 更新账单相关状态
     * 计提： 计提状态、计提推凭状态
     * 实签: 对应实签状态、对应实签推凭状态
     **/
    public abstract void updateBillRelatedStatus(List<Long> billIdList, List<String> communityIds);



    /**
     * 执行推单录制
     *
     * @param command 推单规则运行命令
     * @param rule    推单规则
     */

    public void doExecute(C command, BillRule rule) {
        // 对下结算-计提
        if (rule.getEventType() == 3){
            doExecuteForSettlement(rule);
            return;
        }
        // 收入确认V2-计提
        if (rule.getEventType() == 5){
            doExecuteForPayIncome(rule);
            return;
        }
        ErrorAssertUtil.notNullThrow403(rule, ErrorMessage.BILL_RULE_NOT_EXIST);
        rule.checkRun();
        //预执行
        BillRuleRecord billRuleRecord = preExecute(rule);
        List<String> communityIds = this.communityId(rule.getScopeApplication());
        List<PushZJBusinessBill> pushZJBusinessBills = businessZJBills(command, rule.getConditions(), communityIds);
        Map<String, List<PushZJBusinessBill>> stringListMap = this.groupPushBusinessBill(pushZJBusinessBills, rule);
        log.info("stringListMap信息:" + JSON.toJSON(stringListMap));
        if (stringListMap != null) {
            try {
                transactionTemplate.execute(new TransactionCallback() {
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            for (String key : stringListMap.keySet()) {
                                List<PushZJBusinessBill> list = stringListMap.get(key);
                                voucherPushBillZJRepository.save(voucherPushBill(rule, list));
                                voucherBillDetailZJRepository.saveBatch(Global.mapperFacade.mapAsList(list, VoucherPushBillDetailZJ.class));
                            }
                        } catch (Exception e) {
                            // 抛出异常，回滚事务
                            status.setRollbackOnly();
                            throw e;
                        }
                        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
                        return null;
                    }
                });
            } catch (Exception e) {
                billRuleRecord.setState(VoucherRuleStateEnum.处理失败.getCode());
                log.error("报账单生成失败 异常：{}", e.getMessage());
            } finally {
                //后置处理
                afterExecute(rule, billRuleRecord);
            }
        }
        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
        afterExecute(rule, billRuleRecord);
    }


    public List<PushZJBusinessBillForSettlement> doAutoExecute(AutoBillZJStrategyCommand command) {
        if (command.getVoucherBillGenerateForContractSettlementContext().getEventType() != 9) {
            List<PushZJBusinessBillForSettlement> pushZJBusinessBillForSettlementList = new ArrayList<>();
            VoucherBillGenerateForContractSettlementContext context =
                    command.getVoucherBillGenerateForContractSettlementContext();

            doExecuteForPayIncomeConfirmXQ(context);
            return pushZJBusinessBillForSettlementList;
        }else{
            VoucherBillGenerateForContractSettlementContext context =
                    command.getVoucherBillGenerateForContractSettlementContext();
            if (!context.getSaveVoucher()) {
                return doExecuteForPaymentApplicationForm(context);
            }else{
                return doExecuteForPaymentApplicationFormSave(context);
            }
        }
    }

    /**
     * 生成对下结算单
     **/
    public void doExecuteForSettlement(BillRule rule){
        ErrorAssertUtil.notNullThrow403(rule, ErrorMessage.BILL_RULE_NOT_EXIST);
        rule.checkRun();
        //预执行
        BillRuleRecord billRuleRecord = preExecute(rule);
        List<String> communityIds = this.communityId(rule.getScopeApplication());
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = businessZJBillsForSettlement(rule.getConditions(), communityIds);
        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap = this.groupPushBusinessBillForSettlement(pushZJBusinessBills, rule);
        log.info("stringListMap信息:" + JSON.toJSON(stringListMap));
        if (stringListMap != null) {
            try {
                transactionTemplate.execute(new TransactionCallback() {
                    @Override
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            for (String key : stringListMap.keySet()) {
                                List<PushZJBusinessBillForSettlement> list = stringListMap.get(key);
                                VoucherBillDxZJ voucherBillDxZJ = voucherPushBillForSettlement(rule, list);
                                voucherPushBillDxZJRepository.save(voucherBillDxZJ);
                                voucherBillDetailDxZJRepository.saveBatch(Global.mapperFacade.mapAsList(list, VoucherPushBillDetailDxZJ.class));
                                // 将指定账单id的计提推凭态更新为已推送
                                List<Long> billIdList = list.stream().map(PushZJBusinessBillForSettlement::getBillId).collect(Collectors.toList());
                                updateBillRelatedStatus(billIdList,communityIds);
                                //发起审批流
                                String communityId = list.get(0).getCommunityId();
                                String communityName = list.get(0).getCommunityName();
                                //屏蔽自动触发审批，改为手动上传附件之后触发
                                //pushBillZJDomainService.initiateApprove(voucherBillDxZJ, communityId, communityName, OperateTypeEnum.对下结算计提单);
                            }
                        } catch (Exception e) {
                            // 抛出异常，回滚事务
                            status.setRollbackOnly();
                            throw e;
                        }
                        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
                        return null;
                    }
                });
            } catch (Exception e) {
                billRuleRecord.setState(VoucherRuleStateEnum.处理失败.getCode());
                log.error("对下结算-计提-报账单生成失败 异常：{}", e.getMessage());
            } finally {
                //后置处理
                afterExecute(rule, billRuleRecord);
            }
        }
        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
        afterExecute(rule, billRuleRecord);
    }

    /**
     * 生成通用收入
     **/
    public void doExecuteForPayIncome(BillRule rule){
        ErrorAssertUtil.notNullThrow403(rule, ErrorMessage.BILL_RULE_NOT_EXIST);
        rule.checkRun();
        //预执行
        BillRuleRecord billRuleRecord = preExecute(rule);
        List<String> communityIds = this.communityId(rule.getScopeApplication());
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = businessZJBillsForPayIncome(rule.getConditions(), communityIds);
        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap = this.groupPushBusinessBillForPayIncome(pushZJBusinessBills, rule);
        log.info("stringListMap信息:" + JSON.toJSON(stringListMap));
        if (stringListMap != null) {
            try {
                transactionTemplate.execute(new TransactionCallback() {
                    @Override
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            for (String key : stringListMap.keySet()) {
                                List<PushZJBusinessBillForSettlement> list = stringListMap.get(key);
                                VoucherBillDxZJ voucherBillDxZJ = voucherPushBillForSettlement(rule, list);
                                voucherPushBillDxZJRepository.save(voucherBillDxZJ);
                                voucherBillDetailDxZJRepository.saveBatch(Global.mapperFacade.mapAsList(list, VoucherPushBillDetailDxZJ.class));
                                // 将指定账单id的计提推凭态更新为已推送
                                List<Long> billIdList = list.stream().map(PushZJBusinessBillForSettlement::getBillId).collect(Collectors.toList());
                                updateBillRelatedStatus(billIdList,communityIds);
                                //发起审批流
                                String communityId = list.get(0).getCommunityId();
                                String communityName = list.get(0).getCommunityName();
                                //屏蔽自动触发审批，改为手动上传附件之后触发
                                //pushBillZJDomainService.initiateApprove(voucherBillDxZJ, communityId, communityName, OperateTypeEnum.收入确认计提单);
                            }
                        } catch (Exception e) {
                            // 抛出异常，回滚事务
                            log.error("doExecuteForPayIncome error,msg is:{} ",e.getMessage());
                            status.setRollbackOnly();
                            throw e;
                        }
                        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
                        return null;
                    }
                });
            } catch (Exception e) {
                billRuleRecord.setState(VoucherRuleStateEnum.处理失败.getCode());
                log.error("对下结算-计提-报账单生成失败 异常：{}", e.getMessage());
            } finally {
                //后置处理
                afterExecute(rule, billRuleRecord);
            }
        }
        billRuleRecord.setState(VoucherRuleStateEnum.处理完成.getCode());
        afterExecute(rule, billRuleRecord);
    }


    private List<PushZJBusinessBillForSettlement> doExecuteForPaymentApplicationForm(VoucherBillGenerateForContractSettlementContext context) {
        List<PushZJBusinessBillForSettlement> businessBillForSettlements = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(context.getBillIdList())){
            List<VoucherBillRuleConditionOBV> conditions = new ArrayList<>();
            VoucherBillRuleConditionOBV bv = new VoucherBillRuleConditionOBV();
            bv.setType(1);
            businessBillForSettlements = businessZJBillsForPaymentApplicationForm(conditions, Lists.newArrayList(context.getCommunityIdList()), context.getBillIdList());
        }else{
            businessBillForSettlements = businessZJBillsForPaymentApplicationForm(null, Lists.newArrayList(context.getCommunityIdList()), context.getSettlementIdList());
        }
        if (CollectionUtils.isEmpty(businessBillForSettlements)) {
            return Collections.emptyList();
        }
        businessBillForSettlements.forEach(b-> this.amountForSettlement(b));
        return businessBillForSettlements;
    }


    private List<PushZJBusinessBillForSettlement> doExecuteForPaymentApplicationFormSave(VoucherBillGenerateForContractSettlementContext context) {
        List<PushZJBusinessBillForSettlement> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(context.getBillIdList())){
            List<VoucherBillRuleConditionOBV> conditions = new ArrayList<>();
            VoucherBillRuleConditionOBV bv = new VoucherBillRuleConditionOBV();
            bv.setType(1);
            conditions.add(bv);
            list = businessZJBillsForPaymentApplicationForm(conditions, Lists.newArrayList(context.getCommunityIdList()), context.getBillIdList());
        }else{
            list = businessZJBillsForPaymentApplicationForm(null, Lists.newArrayList(context.getCommunityIdList()), context.getSettlementIdList());
        }
        log.info("pushZJBusinessBills信息:" + JSON.toJSON(list));
        if (CollectionUtils.isNotEmpty(list)) {
            try {
                List<PushZJBusinessBillForSettlement> finalList = list;
                transactionTemplate.execute(new TransactionCallback() {
                    @Override
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            VoucherBillDxZJ voucherBillDxZJ = voucherPushBillForPayApplicationForm(finalList, context);
                            voucherPushBillDxZJRepository.save(voucherBillDxZJ);
                            voucherBillDetailDxZJRepository.saveBatch(Global.mapperFacade.mapAsList(finalList, VoucherPushBillDetailDxZJ.class));
                            // 更新对应推凭状态和实签状态
                            List<Long> billIdList = finalList.stream().map(PushZJBusinessBillForSettlement::getBillId).collect(Collectors.toList());
                            updateBillRelatedStatus(billIdList,context.getCommunityIdList());
                            //发起审批流
                            String communityId = finalList.get(0).getCommunityId();
                            String communityName = finalList.get(0).getCommunityName();
                            pushBillZJDomainService.initiateApprove(voucherBillDxZJ, communityId, communityName, OperateTypeEnum.业务支付申请单);
                        } catch (Exception e) {
                            // 抛出异常，回滚事务
                            e.printStackTrace();
                            status.setRollbackOnly();
                            throw e;
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                log.error("支付申请单-报账单生成失败 异常：{}", e.getMessage());
            }
        }
        return null;
    }


    /**
     * 生成收入确认-实签-报账单
     **/
    public void doExecuteForPayIncomeConfirmXQ(VoucherBillGenerateForContractSettlementContext context){
        List<PushZJBusinessBillForSettlement> pushZJBusinessBills = businessZJBillsForSettlement(null, Lists.newArrayList(context.getCommunityIdList()), context.getBillIdList());
        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap = null;
        if (context.getEventType() == 4){
            stringListMap = this.groupPushBusinessBillForPayCostSQ(pushZJBusinessBills);
        } else {
            stringListMap = this.groupPushBusinessBillForIncomeSQ(pushZJBusinessBills, Boolean.FALSE);
        }
        List<String> communityIds = pushZJBusinessBills.stream()
                .map(PushZJBusinessBillForSettlement::getCommunityId)
                .filter(StringUtils::isNotBlank)
                .distinct().collect(Collectors.toList());
        log.info("stringListMap信息:" + JSON.toJSON(stringListMap));
        if (stringListMap != null) {
            try {
                Map<String, List<PushZJBusinessBillForSettlement>> finalStringListMap = stringListMap;
                transactionTemplate.execute(new TransactionCallback() {
                    @Override
                    public Object doInTransaction(TransactionStatus status) {
                        try {
                            for (String key : finalStringListMap.keySet()) {
                                List<PushZJBusinessBillForSettlement> list = finalStringListMap.get(key);
                                VoucherBillDxZJ dxZJ = voucherPushBillForIncomeSQ(list, context);
                                voucherPushBillDxZJRepository.save(dxZJ);
                                //对下结算实签，使用结算单审批流程ID
                                if((ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == context.getEventType() || ZJTriggerEventBillTypeEnum.收入确认实签.getCode() == context.getEventType())&& StringUtils.isNotEmpty(context.getProcessId())){
                                    FinanceProcessRecordZJ processRecord = new FinanceProcessRecordZJ();
                                    processRecord.setProcessId(context.getProcessId());
                                    processRecord.setMainDataId(String.valueOf(dxZJ.getId()));
                                    //流程记录表中增加审批状态字段
                                    processRecord.setReviewStatus(ApproveStatusEnum.待审批.getCode());
                                    processRecord.setIsJsdProcess(1);
                                    if(ZJTriggerEventBillTypeEnum.对下结算实签.getCode() == context.getEventType()){
                                        processRecord.setType(BusinessProcessType.DX_JS_FORM.getCode());
                                        log.info("对下结算实签，使用结算单审批流程ID:{}", JSONArray.toJSON(processRecord));
                                    }else{
                                        processRecord.setType(BusinessProcessType.SRQR_SQ_FORM.getCode());
                                        log.info("收入确认实签，使用确收单审批流程ID:{}", JSONArray.toJSON(processRecord));
                                    }
                                    processRecord.setDeleted(0);
                                    financeProcessRecordZJRepository.save(processRecord);
                                }
                                voucherBillDetailDxZJRepository.saveBatch(Global.mapperFacade.mapAsList(list, VoucherPushBillDetailDxZJ.class));
                                // 更新对应推凭状态和实签状态
                                List<Long> billIdList = list.stream().map(PushZJBusinessBillForSettlement::getBillId).collect(Collectors.toList());
                                updateBillRelatedStatus(billIdList,communityIds);
                            }
                        } catch (Exception e) {
                            log.error("确认收入-实签-报账单生成失败 异常：{}", e);
                            log.error("确认收入-实签-报账单生成失败 异常：{}", e.getMessage());
                            // 抛出异常，回滚事务
                            e.printStackTrace();
                            status.setRollbackOnly();
                            throw e;
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                log.error("确认收入-实签-报账单生成失败 异常：{}", e.getMessage());
            }
        }


        List<PushZJBusinessBillForSettlement> toReverseBills = businessZJBillsForSettlementForReverse(Lists.newArrayList(context.getCommunityIdList()), context.getBillIdList());
        if (CollectionUtils.isEmpty(toReverseBills)){
            return;
        }
        Map<String, List<PushZJBusinessBillForSettlement>> reverseMap = null;
        if (context.getEventType() == 4){
            reverseMap = this.groupPushBusinessBillForSettlement(toReverseBills, new BillRule());
            context.setEventType(7);
        } else {
            reverseMap = this.groupPushBusinessBillForIncomeSQ(toReverseBills, Boolean.TRUE);
            context.setEventType(8);
        }
        if (Objects.isNull(reverseMap) || reverseMap.size() == 0){
            return;
        }
        try {
            Map<String, List<PushZJBusinessBillForSettlement>> finalReverseMap = reverseMap;
            transactionTemplate.execute(new TransactionCallback() {
                @Override
                public Object doInTransaction(TransactionStatus status) {
                    try {
                        for (String key : finalReverseMap.keySet()) {
                            List<PushZJBusinessBillForSettlement> list = finalReverseMap.get(key);
                            voucherPushBillDxZJRepository.save(voucherPushBillForReverse(list, context));
                            voucherBillDetailDxZJRepository.saveBatch(Global.mapperFacade.mapAsList(list, VoucherPushBillDetailDxZJ.class));
                        }
                    } catch (Exception e) {
                        log.error("确认收入-实签-报账单生成失败 异常：{}", e);
                        log.error("确认收入-实签-报账单生成失败 异常：{}", e.getMessage());
                        // 抛出异常，回滚事务
                        status.setRollbackOnly();
                        throw e;
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("确认收入-实签-报账单生成失败 异常：{}", e.getMessage());
        }
    }


    private VoucherBillDxZJ voucherPushBillForReverse(List<PushZJBusinessBillForSettlement> details,
                                                       VoucherBillGenerateForContractSettlementContext context){
        VoucherBillDxZJ voucherBill = new VoucherBillDxZJ();
        // 这里使用这张表的，避免id重复，保证后续推送的口径唯一
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        voucherBill.setSettlementId(context.getSettlementId());
        // 中交推送方式只有手动推送
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(details.get(0).getCostCenterId());
        voucherBill.setCostCenterName(details.get(0).getCostCenterName());
        voucherBill.setBillEventType(context.getEventType());
        voucherBill.setBusinessType(details.get(0).getBusinessType());
        voucherBill.setBusinessTypeCode(details.get(0).getBusinessTypeId());
        voucherBill.setBusinessTypeName(details.get(0).getBusinessTypeName());
        voucherBill.setTaxType(details.get(0).transferFinancialTaxType());
        //项目id
        voucherBill.setCommunityId(details.get(0).getCommunityId());
        //项目名称
        voucherBill.setCommunityName(details.get(0).getCommunityName());
        Set<Long> set = new HashSet<>();
        for (PushZJBusinessBillForSettlement businessBill : details) {
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            businessBill.setTaxType(businessBill.transferFinancialTaxType());
            if (context.getEventType() != ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()){
                this.amountForSettlement(businessBill);
            }
            if (context.getEventType() == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()){
                if (StringUtils.equals(businessBill.getTaxRateStr(), "差额纳税")){
                    businessBill.setTaxRate(BigDecimal.ZERO);
                }
            }
            businessBill.setBillEventType(context.getEventType());
            set.add(businessBill.getBusinessUnitId());
        }
        if (StringUtils.isNotBlank(details.get(0).getBusinessType())) {
            String[] split = details.get(0).getBusinessType().substring(1, details.get(0).getBusinessType().length() - 1)
                    .split(",");
            String s = split[split.length - 1];
            voucherBill.setBusinessTypeCode(s);
            String[] splitid = details.get(0).getBusinessTypeId().substring(1, details.get(0).getBusinessTypeId().length() - 1)
                    .split(",");
            String id = splitid[splitid.length - 1];
            voucherBill.setBusinessType(id);
            String[] splitName = details.get(0).getBusinessTypeName().substring(1, details.get(0).getBusinessTypeName().length() - 1)
                    .split(",");
            String name = splitName[splitName.length - 1];
            voucherBill.setBusinessTypeName(name);
        }
        voucherBill.setTotalAmount(details.stream().map(PushZJBusinessBillForSettlement::getTaxIncludAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        return voucherBill;
    }

    protected abstract List<PushZJBusinessBillForSettlement> businessZJBillsForSettlementForReverse(List<String> newArrayList, List<String> billIdList);

    protected Map<String, List<PushZJBusinessBillForSettlement>> groupPushBusinessBillForPayCostSQ(List<PushZJBusinessBillForSettlement> businessBills){
        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap;
        if (CollectionUtils.isEmpty(businessBills)) {
            return null;
        }
        BillRule billRule = new BillRule();
        //业务类型-分组
        billRule.setSummaryRequirements("5");
        stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> searchKeyForSettlement(item, billRule)));
        return stringListMap;
    }

    /**
     * 通用收入确认单-业务单据-分组
     * 配置的汇总规则+费项的业务类型(隐含条件,恒定参与分组)
     **/
    private Map<String, List<PushZJBusinessBillForSettlement>> groupPushBusinessBillForIncomeSQ(List<PushZJBusinessBillForSettlement> businessBills, Boolean isJtCx){

        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap;
        if (CollectionUtils.isEmpty(businessBills)) {
            return null;
        }
        BillRule billRule = new BillRule();
        //业务类型-分组
        billRule.setSummaryRequirements("5");

        Boolean isIncomeSQ = Boolean.FALSE;

        if(isJtCx){
            //查询对应临时账单是否生成收入确认-计提
            List<Long> billIdList = businessBills.stream().map(PushZJBusinessBillForSettlement::getBillId).collect(Collectors.toList());
            List<VoucherPushBillDetailDxZJ> list = voucherBillDetailDxZJRepository.list(new LambdaQueryWrapper<VoucherPushBillDetailDxZJ>()
                    .in(VoucherPushBillDetailDxZJ::getBillId, billIdList)
                    .eq(VoucherPushBillDetailDxZJ::getDeleted, 0)
                    .eq(VoucherPushBillDetailDxZJ::getBillEventType, 5)
            );

            if(CollectionUtils.isNotEmpty(list)){
                VoucherBillDxZJ dxJZ = voucherPushBillDxZJRepository.queryByVoucherBillNo(list.get(0).getVoucherBillNo());
                if(Objects.nonNull(dxJZ) && Objects.nonNull(dxJZ.getRuleId())){
                    BillRule rule = billRuleRepository.getById(dxJZ.getRuleId());
                    if(Objects.nonNull(rule) && StringUtils.isNotEmpty(rule.getSummaryRequirements())){
                        List<String> ruleList = Arrays.asList(rule.getSummaryRequirements().split(","));
                        if(ruleList.contains("1")){
                            isIncomeSQ = Boolean.TRUE;
                        }
                    }
                }
            }
        }
        Boolean finalIsIncomeSQ = isIncomeSQ;
        stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> searchKeyForIncomeSQ(item, billRule, finalIsIncomeSQ)));
        return stringListMap;
    }

    private VoucherBillDxZJ voucherPushBillForPayApplicationForm(List<PushZJBusinessBillForSettlement> details,
                                                       VoucherBillGenerateForContractSettlementContext context){
        VoucherBillDxZJ voucherBill = new VoucherBillDxZJ();
        // 这里使用这张表的，避免id重复，保证后续推送的口径唯一
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        voucherBill.setSettlementId(JSON.toJSONString(context.getSettlementIdList()));
        // 中交推送方式只有手动推送
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(details.get(0).getCostCenterId());
        voucherBill.setCostCenterName(details.get(0).getCostCenterName());
        voucherBill.setBillEventType(context.getEventType());
        voucherBill.setBusinessType("E12C32FDD50B44BF96C4FC726E87FDFE");
        voucherBill.setBusinessTypeCode("002099");
        voucherBill.setBusinessTypeName("其他业务");
/*
        voucherBill.setBusinessType(details.get(0).getBusinessType());
        voucherBill.setBusinessTypeCode(details.get(0).getBusinessTypeId());
        voucherBill.setBusinessTypeName(details.get(0).getBusinessTypeName());
*/
        voucherBill.setTaxType(details.get(0).transferFinancialTaxType());
        //项目id
        voucherBill.setCommunityId(details.get(0).getCommunityId());
        //项目名称
        voucherBill.setCommunityName(details.get(0).getCommunityName());
        voucherBill.setPayAppId(context.getBizId());
        voucherBill.setExternalDepartmentCode(context.getExternalDepartmentCode());
        Set<Long> set = new HashSet<>();
        for (PushZJBusinessBillForSettlement businessBill : details) {
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            businessBill.setTaxType(businessBill.transferFinancialTaxType());
            if (StringUtils.isNotBlank(businessBill.getTaxRateStr()) &&
                    StringUtils.equals("差额纳税", businessBill.getTaxRateStr().trim())){
                businessBill.setTaxRate(BigDecimal.ZERO);
            }
            this.amountForSettlement(businessBill);
            businessBill.setBillEventType(context.getEventType());
            businessBill.setPayAppId(context.getBizId());
            set.add(businessBill.getBusinessUnitId());
        }
        if (StringUtils.isNotBlank(details.get(0).getBusinessType())) {
            String[] split = details.get(0).getBusinessType().substring(1, details.get(0).getBusinessType().length() - 1)
                    .split(",");
            String s = split[split.length - 1];
            voucherBill.setBusinessTypeCode(s);
            String[] splitid = details.get(0).getBusinessTypeId().substring(1, details.get(0).getBusinessTypeId().length() - 1)
                    .split(",");
            String id = splitid[splitid.length - 1];
            voucherBill.setBusinessType(id);
            String[] splitName = details.get(0).getBusinessTypeName().substring(1, details.get(0).getBusinessTypeName().length() - 1)
                    .split(",");
            String name = splitName[splitName.length - 1];
            voucherBill.setBusinessTypeName(name);
        }
        voucherBill.setTotalAmount(details.stream().map(PushZJBusinessBillForSettlement::getTaxIncludAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        return voucherBill;
    }


    private VoucherBillDxZJ voucherPushBillForIncomeSQ(List<PushZJBusinessBillForSettlement> details,
                                                       VoucherBillGenerateForContractSettlementContext context){
        VoucherBillDxZJ voucherBill = new VoucherBillDxZJ();
        // 这里使用这张表的，避免id重复，保证后续推送的口径唯一
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        voucherBill.setSettlementId(context.getSettlementId());
        // 中交推送方式只有手动推送
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(details.get(0).getCostCenterId());
        voucherBill.setCostCenterName(details.get(0).getCostCenterName());
        voucherBill.setBillEventType(context.getEventType());
        voucherBill.setBusinessType(details.get(0).getBusinessType());
        voucherBill.setBusinessTypeCode(details.get(0).getBusinessTypeId());
        voucherBill.setBusinessTypeName(details.get(0).getBusinessTypeName());
        voucherBill.setTaxType(details.get(0).transferFinancialTaxType());
        //默认审批状态
        voucherBill.setApproveState(PushBillApproveStateEnum.待发起.getCode());
        //项目id
        voucherBill.setCommunityId(details.get(0).getCommunityId());
        //项目名称
        voucherBill.setCommunityName(details.get(0).getCommunityName());
        Set<Long> set = new HashSet<>();
        for (PushZJBusinessBillForSettlement businessBill : details) {
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            businessBill.setTaxType(businessBill.transferFinancialTaxType());
            if (StringUtils.isNotBlank(businessBill.getTaxRateStr()) &&
                    StringUtils.equals("差额纳税", businessBill.getTaxRateStr().trim())){
                businessBill.setTaxRate(BigDecimal.ZERO);
            }
            this.amountForSettlement(businessBill);
            businessBill.setBillEventType(context.getEventType());
            set.add(businessBill.getBusinessUnitId());
        }
        if (StringUtils.isNotBlank(details.get(0).getBusinessType())) {
            String[] split = details.get(0).getBusinessType().substring(1, details.get(0).getBusinessType().length() - 1)
                    .split(",");
            String s = split[split.length - 1];
            voucherBill.setBusinessTypeCode(s);
            String[] splitid = details.get(0).getBusinessTypeId().substring(1, details.get(0).getBusinessTypeId().length() - 1)
                    .split(",");
            String id = splitid[splitid.length - 1];
            voucherBill.setBusinessType(id);
            String[] splitName = details.get(0).getBusinessTypeName().substring(1, details.get(0).getBusinessTypeName().length() - 1)
                    .split(",");
            String name = splitName[splitName.length - 1];
            voucherBill.setBusinessTypeName(name);
        }
        voucherBill.setTotalAmount(details.stream().map(PushZJBusinessBillForSettlement::getTaxIncludAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        autoReceiptRemark(context.getEventType(), details.get(0), voucherBill);
        return voucherBill;
    }

    /**
     * todo 业务是由
     **/
    private void autoReceiptRemark(Integer billEventType,
                                     PushZJBusinessBillForSettlement detail,
                                     VoucherBillDxZJ voucherBillDxZJ){
        // 自动生成业务是由
        String communityName = detail.getCommunityName();
        String contractName = detail.getContractName();
        StringBuilder builder = new StringBuilder();
        builder.append(communityName).append("-").append(contractName);
        builder.append("-").append(DateUtils.formatDate(new Date(), "yyyy年MM月")).append("-");
        builder.append("-");
        if (billEventType == ZJTriggerEventBillTypeEnum.对下结算实签.getCode()){
            builder.append("成本确认 ¥");
        } else if (billEventType == ZJTriggerEventBillTypeEnum.收入确认实签.getCode()){
            builder.append("收入确认 ¥");
        } else if (billEventType == ZJTriggerEventBillTypeEnum.收入确认计提冲销.getCode()){
            builder.append("收入计提冲销 ¥");
        } else if (billEventType == ZJTriggerEventBillTypeEnum.对下结算计提冲销.getCode()){
            builder.append("成本计提冲销 ¥");
        }
        builder.append(voucherBillDxZJ.getTotalAmount().divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP)).append("元");
        voucherBillDxZJ.setReceiptRemark(builder.toString());
    }


    /**
     * 对下结算单-业务单据-分组
     * 配置的汇总规则+费项的业务类型(隐含条件,恒定参与分组)
     **/
    private Map<String, List<PushZJBusinessBillForSettlement>> groupPushBusinessBillForSettlement(
            List<PushZJBusinessBillForSettlement> businessBills,
            BillRule rule){

        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(businessBills)) {
            return null;
        }
        BillRule billRule = Global.mapperFacade.map(rule, BillRule.class);
        // 强行添加 业务类型-分组
        if (StringUtils.isBlank(billRule.getSummaryRequirements())) {
            billRule.setSummaryRequirements("5");
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> searchKeyForSettlement(item, billRule)));
        } else {
            if (!billRule.getSummaryRequirements().contains("5")){
                billRule.setSummaryRequirements(billRule.getSummaryRequirements() +",5");
            }
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> searchKeyForSettlement(item, billRule)));
        }
        return stringListMap;
    }

    /**
     * 对下结算单-业务单据-分组
     * 配置的汇总规则+费项的业务类型(隐含条件,恒定参与分组)
     **/
    private Map<String, List<PushZJBusinessBillForSettlement>> groupPushBusinessBillForPayIncome(
            List<PushZJBusinessBillForSettlement> businessBills,
            BillRule rule){

        Map<String, List<PushZJBusinessBillForSettlement>> stringListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(businessBills)) {
            return null;
        }
        BillRule billRule = Global.mapperFacade.map(rule, BillRule.class);
        // 强行添加 业务类型-分组
        if (StringUtils.isBlank(billRule.getSummaryRequirements())) {
            billRule.setSummaryRequirements("5");
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> searchKeyForIncome(item, billRule)));
        } else {
            if (!billRule.getSummaryRequirements().contains("5")){
                billRule.setSummaryRequirements(billRule.getSummaryRequirements() +",5");
            }
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> searchKeyForIncome(item, billRule)));
        }
        return stringListMap;
    }


    private VoucherBillDxZJ voucherPushBillForSettlement(BillRule rule, List<PushZJBusinessBillForSettlement> details){
        VoucherBillDxZJ voucherBill = new VoucherBillDxZJ();
        // 这里使用这张表的，避免id重复，保证后续推送的口径唯一
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        voucherBill.setRuleId(rule.getId());
        voucherBill.setRuleName(rule.getRuleName());
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        // 中交推送方式只有手动推送
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(details.get(0).getCostCenterId());
        voucherBill.setCostCenterName(details.get(0).getCostCenterName());
        voucherBill.setBillEventType(rule.getEventType());
        voucherBill.setBusinessType(details.get(0).getBusinessType());
        voucherBill.setBusinessTypeCode(details.get(0).getBusinessTypeId());
        voucherBill.setBusinessTypeName(details.get(0).getBusinessTypeName());
        voucherBill.setTaxType(details.get(0).transferFinancialTaxType());
        //默认审批状态
        voucherBill.setApproveState(PushBillApproveStateEnum.待发起.getCode());
        //项目id
        voucherBill.setCommunityId(details.get(0).getCommunityId());
        //项目名称
        voucherBill.setCommunityName(details.get(0).getCommunityName());
        Set<Long> set = new HashSet<>();
        for (PushZJBusinessBillForSettlement businessBill : details) {
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            businessBill.setTaxType(businessBill.transferFinancialTaxType());
            if (rule.getEventType() != ZJTriggerEventBillTypeEnum.收入确认计提.getCode()){
                this.amountForSettlement(businessBill);
            }
            if (rule.getEventType() == ZJTriggerEventBillTypeEnum.收入确认计提.getCode()){
                if (StringUtils.equals(businessBill.getTaxRateStr(), "差额纳税")){
                    businessBill.setTaxRate(BigDecimal.ZERO);
                }
            }
            businessBill.setBillEventType(rule.getEventType());
            set.add(businessBill.getBusinessUnitId());
        }
        if (StringUtils.isNotBlank(details.get(0).getBusinessType())) {
            String[] split = details.get(0).getBusinessType().substring(1, details.get(0).getBusinessType().length() - 1)
                    .split(",");
            String s = split[split.length - 1];
            voucherBill.setBusinessTypeCode(s);
            String[] splitid = details.get(0).getBusinessTypeId().substring(1, details.get(0).getBusinessTypeId().length() - 1)
                    .split(",");
            String id = splitid[splitid.length - 1];
            voucherBill.setBusinessType(id);
            String[] splitName = details.get(0).getBusinessTypeName().substring(1, details.get(0).getBusinessTypeName().length() - 1)
                    .split(",");
            String name = splitName[splitName.length - 1];
            voucherBill.setBusinessTypeName(name);
        }
        voucherBill.setTotalAmount(details.stream().map(PushZJBusinessBillForSettlement::getTaxIncludAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        return voucherBill;
    }

    public VoucherBillZJ voucherPushBill(BillRule rule, List<PushZJBusinessBill> businessBills) {
        VoucherBillZJ voucherBill = new VoucherBillZJ();
        voucherBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
        voucherBill.setRuleId(rule.getId());
        voucherBill.setRuleName(rule.getRuleName());
        voucherBill.setVoucherBillNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hxbz", 20));
        voucherBill.setPushState(PushBillTypeEnum.待推送.getCode());
        voucherBill.setInferenceState(InferenceStateEnum.未推凭.getCode());
        // 中交推送方式只有手动推送
        voucherBill.setPushMethod(1);
        voucherBill.setCostCenterId(businessBills.get(0).getCostCenterId());
        voucherBill.setCostCenterName(businessBills.get(0).getCostCenterName());
        voucherBill.setBillEventType(rule.getEventType());
        Set<Long> set = new HashSet<>();
        for (PushZJBusinessBill businessBill : businessBills) {
            businessBill.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL_DETAIL));
            businessBill.setVoucherBillNo(voucherBill.getVoucherBillNo());
            businessBill.setVoucherBillDetailNo(IdentifierFactory.getInstance().serialNumber("pushbillZJ", "hzdj", 20));
            this.amount(businessBill);
            businessBill.setBillEventType(rule.getEventType());
            set.add(businessBill.getBusinessUnitId());
        }
        if (StringUtils.isNotBlank(businessBills.get(0).getBusinessType())) {
            String[] split = businessBills.get(0).getBusinessType().substring(1, businessBills.get(0).getBusinessType().length() - 1)
                    .split(",");
            String s = split[split.length - 1];
            voucherBill.setBusinessType(s);
            String[] splitid = businessBills.get(0).getBusinessTypeId().substring(1, businessBills.get(0).getBusinessTypeId().length() - 1)
                    .split(",");
            String id = splitid[splitid.length - 1];
            voucherBill.setBusinessTypeId(id);
            String[] splitName = businessBills.get(0).getBusinessTypeName().substring(1, businessBills.get(0).getBusinessTypeName().length() - 1)
                    .split(",");
            String name = splitName[splitName.length - 1];
            voucherBill.setBusinessTypeName(name);
        }
        voucherBill.setTotalAmount(businessBills.stream().collect(Collectors.summingLong(PushZJBusinessBill::getTaxIncludAmount)));
        return voucherBill;
    }

    /**
     * 金额换算
     *
     * @param businessBill
     */
    public abstract void amount(PushZJBusinessBill businessBill);

    /**
     * 金额换算-对下结算单
     **/
    public abstract void amountForSettlement(PushZJBusinessBillForSettlement businessBill);


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

    protected void countAmountForSettlement(PushZJBusinessBillForSettlement businessBill, Long settleAmount) {
        //(1+税率)
        if (Objects.isNull(settleAmount)) {
            settleAmount = 0L;
        }
        BigDecimal settlementAmountDecimal = new BigDecimal(settleAmount);
        BigDecimal taxAmount = Objects.isNull(businessBill.getTaxAmount()) ? BigDecimal.ZERO : businessBill.getTaxAmount();
        //不含税金额
        BigDecimal taxExcludAmount = settlementAmountDecimal.subtract(taxAmount);
        businessBill.setTaxIncludAmount(settlementAmountDecimal);
        businessBill.setTaxExcludAmount(taxExcludAmount);
        businessBill.setTaxAmount(taxAmount);
    }

    private String serchKey(PushZJBusinessBill pushBusinessBill, BillRule rule) {
        List<String> list = Arrays.asList(rule.getSummaryRequirements().split(","));
        List<String> groupSerch = Lists.newArrayList();
        for (String s : list) {
            if (Objects.equals(SummaryConditionsEnum.费项.getCode(), Integer.valueOf(s))) {
                groupSerch.add(pushBusinessBill.getChargeItemId().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.归属月.getCode(), Integer.valueOf(s))) {
                groupSerch.add(pushBusinessBill.getAccountDate().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.合同.getCode(), Integer.valueOf(s)) && StrUtil.isNotBlank(pushBusinessBill.getContractId())) {
                groupSerch.add(pushBusinessBill.getContractId().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.收费对象类型.getCode(), Integer.valueOf(s)) && Objects.nonNull(pushBusinessBill.getPayerType())) {
                groupSerch.add(pushBusinessBill.getPayerType().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.业务类型.getCode(), Integer.valueOf(s))) {
                if (null != pushBusinessBill.getBusinessType()) {
                    groupSerch.add(pushBusinessBill.getBusinessType().toString());
                } else {
                    groupSerch.add("".toString());
                }
            }
        }
        return String.join("_", groupSerch);
    }

    private String serchKey(PushZJBusinessBillForSettlement pushBusinessBill, BillRule rule) {
        List<String> list = Arrays.asList(rule.getSummaryRequirements().split(","));
        List<String> groupSerch = Lists.newArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        for (String s : list) {
            if (Objects.equals(SummaryConditionsEnum.费项.getCode(), Integer.valueOf(s))) {
                groupSerch.add(pushBusinessBill.getChargeItemId().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.归属月.getCode(), Integer.valueOf(s))) {
                String formattedDate = pushBusinessBill.getAccountDate().format(formatter);
                groupSerch.add(formattedDate);
            }
            if (Objects.equals(SummaryConditionsEnum.合同.getCode(), Integer.valueOf(s)) && StrUtil.isNotBlank(pushBusinessBill.getContractId())) {
                groupSerch.add(pushBusinessBill.getContractId().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.收费对象类型.getCode(), Integer.valueOf(s)) && Objects.nonNull(pushBusinessBill.getPayerType())) {
                groupSerch.add(pushBusinessBill.getPayerType().toString());
            }
            if (Objects.equals(SummaryConditionsEnum.业务类型.getCode(), Integer.valueOf(s))) {
                if (null != pushBusinessBill.getBusinessType()) {
                    groupSerch.add(pushBusinessBill.getBusinessType());
                } else {
                    groupSerch.add("");
                }
            }
        }
        return String.join("_", groupSerch);
    }

    private String searchKeyForSettlement(PushZJBusinessBillForSettlement pushBusinessBill, BillRule rule) {
        List<String> list = Lists.newArrayList();
        list.add(serchKey(pushBusinessBill, rule));
        list.add(pushBusinessBill.getContractId());
        list.add(pushBusinessBill.transferFinancialTaxType().toString());
        return String.join("_", list);
    }

    private String searchKeyForIncome(PushZJBusinessBillForSettlement pushBusinessBill, BillRule rule) {
        List<String> list = Lists.newArrayList();
        list.add(serchKey(pushBusinessBill, rule));
        list.add(pushBusinessBill.getCommunityId());
        return String.join("_", list);
    }

    private String searchKeyForIncomeSQ(PushZJBusinessBillForSettlement pushBusinessBill, BillRule rule,Boolean isIncomeSQ) {
        List<String> list = Lists.newArrayList();
        list.add(pushBusinessBill.getBusinessType());
        list.add(pushBusinessBill.getCommunityId());
        if(isIncomeSQ){
            list.add(pushBusinessBill.getChargeItemId().toString());
        }
        return String.join("_", list);
    }




    private Map<String, List<PushZJBusinessBill>> groupPushBusinessBill(List<PushZJBusinessBill> businessBills, BillRule rule) {
        Map<String, List<PushZJBusinessBill>> stringListMap = new HashMap<>();
        if (CollectionUtils.isEmpty(businessBills)) {
            return null;
        }
        BillRule billRule = Global.mapperFacade.map(rule, BillRule.class);
        if (StringUtils.isBlank(billRule.getSummaryRequirements())) {
            billRule.setSummaryRequirements("5");
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> serchKey(item, billRule)));
        } else {
            if (!billRule.getSummaryRequirements().contains("5")){
                billRule.setSummaryRequirements(billRule.getSummaryRequirements() +",5");
            }
            stringListMap = businessBills.stream().collect(Collectors.groupingBy(item -> serchKey(item, billRule)));
        }
        return stringListMap;
    }


    /**
     * 规则运行后置处理
     *
     * @param rule           凭证规则
     * @param billRuleRecord 推单规则运行记录
     */
    public void afterExecute(BillRule rule, BillRuleRecord billRuleRecord) {
        //更新运行记录
        billRuleRecordRepository.updateById(billRuleRecord);
        rule.setExecuteState(VoucherRuleExecuteStateEnum.空闲.getCode());
        //更新规则运行状态
        billRuleRepository.updateById(rule);
    }

    /**
     * 账单变为以推凭
     *
     * @param businessBills
     */
    protected void modifyInferenceStatus(List<PushZJBusinessBill> businessBills) {
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
        //更新规则为运行中状态
        billRuleRepository.updateExecuteStateById(rule.getId(), VoucherRuleExecuteStateEnum.运行中.getCode());
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
