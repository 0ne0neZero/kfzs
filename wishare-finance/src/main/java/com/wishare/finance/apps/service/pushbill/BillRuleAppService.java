package com.wishare.finance.apps.service.pushbill;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListF;
import com.wishare.finance.apps.model.signature.ExternalMaindataCalmappingListV;
import com.wishare.finance.apps.pushbill.fo.*;
import com.wishare.finance.apps.pushbill.vo.BillRuleV;
import com.wishare.finance.apps.pushbill.vo.RuleRemindConfigDetailV;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimRecordApproveStateEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.repository.FlowClaimRecordRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherApproveStateEnum;
import com.wishare.finance.domains.voucher.dto.ApproveResultDto;
import com.wishare.finance.domains.voucher.entity.PushBillConditionOptionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.support.fangyuan.entity.BillRule;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.FyVoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.repository.BillRuleRepository;
import com.wishare.finance.domains.voucher.support.fangyuan.service.BillRuleDomainService;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.BillRuleConditionZJTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.*;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.bpm.ProcessStartF;
import com.wishare.finance.infrastructure.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.finance.infrastructure.remote.vo.charge.ApproveFilter;
import com.wishare.finance.infrastructure.remote.vo.config.CfgExternalDataV;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.finance.infrastructure.utils.IdempotentUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.wishare.finance.infrastructure.conts.ErrorMessage.RUN_BUSINESS_RULE_REPEAT;
import static com.wishare.finance.infrastructure.utils.IdempotentUtil.IdempotentEnum.BUSINESS_BILL_RULE;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillRuleAppService {

    private final BillRuleDomainService billRuleDomainService;
    private final PushBillZJDomainService pushBillZJDomainService;
    private final FlowClaimRecordRepository flowClaimRecordRepository;
    private final PushBillZJAppService pushBillZJAppService;
    private final OrgClient orgClient;
    private final ConfigClient configClient;
    private final VoucherPushBillZJRepository voucherPushBillRepository;
    private final ExternalClient externalClient;
    private final ChargeClient chargeClient;
    private final BpmClient bpmClient;
    private final BillRuleRepository billRuleRepository;
    private final SpaceClient spaceClient;
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    public boolean executeBillRule(RunBillRuleF runBillRuleF) {
        String billRuleId = String.valueOf(runBillRuleF.getBillRuleId());
        IdempotentUtil.setIdempotent(billRuleId, BUSINESS_BILL_RULE, 2, RUN_BUSINESS_RULE_REPEAT);
        return billRuleDomainService.manualExecute(runBillRuleF.getBillRuleId());
    }

    /**
     * 新增推单规则
     *
     * @param addBillRuleF
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addRule(BillRuleF addBillRuleF) {
        return billRuleDomainService.addRule(addBillRuleF);
    }

    /**
     * 修改推单规则
     *
     * @param updateBillRuleF
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRule(BillRuleF updateBillRuleF) {
        return billRuleDomainService.updateRule(updateBillRuleF);
    }

    public boolean enableBillRule(EnableBillRuleF enableBillRuleF) {
        return billRuleDomainService.enable(enableBillRuleF.getBillRuleId(), enableBillRuleF.getDisabled());
    }

    @Transactional
    public boolean deleteBillRule(DeleteBillRuleF deleteBillRuleF) {
        return billRuleDomainService.delete(deleteBillRuleF.getVoucherRuleId());
    }

    public List<PushBillConditionOptionOBV> getConditionOptions(BillRuleConditionZJTypeEnum conditionType){
        return billRuleDomainService.getConditionOptions(conditionType);
    }

    public PageV<BillRuleV> selectBySearch(PageF<SearchF<?>> searchFPageF) {
        List<BillRule> returnRule = new ArrayList<>();
        List<BillRule> billRules = billRuleRepository.selectBySearch(searchFPageF);
        List<SpaceCommunityShortV> spaceCommunityShortVS = spaceClient.perCommunitys();
        List<String> communityIdList = spaceCommunityShortVS.stream().map(SpaceCommunityShortV::getId).collect(Collectors.toList());
        for (BillRule billRule : billRules) {
            List<ScopeF> scopeApplication = billRule.getScopeApplication();
            List<String> collect = scopeApplication.stream().map(ScopeF::getId).collect(Collectors.toList());
            if (collect.stream().allMatch(communityIdList::contains)){
                returnRule.add(billRule);
            }
        }
        int pageNum = (int) searchFPageF.getPageNum();
        int pageSize = (int) searchFPageF.getPageSize();
        List<BillRule> paginate = paginate(returnRule, pageNum, pageSize);
        PageV<BillRuleV> billRulePageV = new PageV<>();
        List<BillRuleV> billRuleVS = Global.mapperFacade.mapAsList(paginate, BillRuleV.class);
        billRulePageV.setRecords(billRuleVS);
        billRulePageV.setTotal(returnRule.size());
        billRulePageV.setPageNum(pageNum);
        billRulePageV.setPageSize(pageSize);
        billRulePageV.setLast(paginate.size() < pageSize);
        return billRulePageV;
    }
    public static <T> List<T> paginate(List<T> fullList, int pageNumber, int pageSize) {
        int totalSize = fullList.size();
        int fromIndex = (pageNumber - 1) * pageSize;
        if (fromIndex >= totalSize || fromIndex < 0) {
            return new ArrayList<>();
        }
        int toIndex = fromIndex + pageSize;
        if (toIndex > totalSize) {
            toIndex = totalSize;
        }
        return fullList.subList(fromIndex, toIndex);
    }

    public List<VoucherRuleConditionOptionOBV> getFYConditionOptions(FyVoucherRuleConditionTypeEnum conditionType){
        return billRuleDomainService.getFYConditionOptions(conditionType);
    }

    public boolean executeZjFundReceiptsBill(FundReceiptsBillZJF receiptsBillZJF) {
        log.info("根据流水认领记录生成资金收款单开始：{}", JSONArray.toJSON(receiptsBillZJF));
        // 判断是否需要审批 确认状态是审批中还是审批完成
        List<String> collect = receiptsBillZJF.getSupCpUnitIds().stream().distinct().collect(Collectors.toList());
        if (collect.size() > 1){
            throw new RuntimeException("生成资金收款单需要同一项目!");
        }
        Long voucherBillId = billRuleDomainService.executeZjFundReceiptsBill(receiptsBillZJF, collect);
        VoucherBillZJ voucherBillZJ = voucherPushBillRepository.getById(voucherBillId);
        List<FlowClaimRecordE> flowClaimRecordES = flowClaimRecordRepository.listByIds(receiptsBillZJF.getIds());
        for (FlowClaimRecordE flowClaimRecordE : flowClaimRecordES) {
            flowClaimRecordE.setVoucherBillId(voucherBillZJ.getId());
            flowClaimRecordE.setVoucherBillNo(voucherBillZJ.getVoucherBillNo());
        }
        flowClaimRecordRepository.updateBatchById(flowClaimRecordES);
        //202505变动，去除自动发起审批，改为手动审批
        /*if (Objects.nonNull(voucherBillId)) {
            try {
                log.info("资金收款单{}发起审批:", voucherBillId);
                ApproveResultDto approveResultDto = pushBillZJDomainService.initiateApprove(collect.get(0),
                        OperateTypeEnum.资金收款, voucherBillId, flowClaimRecordES.get(0));
//                ApproveResultDto approveResultDto = new ApproveResultDto();
//                approveResultDto.setApproveState(VoucherApproveStateEnum.无需审批.getCode());
                if (VoucherApproveStateEnum.无需审批.equalsByCode(approveResultDto.getApproveState())) {
                    log.info("无需审批");
                    pushBillZJAppService.approveAgree(voucherBillId, null);
                    return true;
                } else {
                    log.info("发起审批结束，进入审批流程");
                    //  更新流水状态
                    //  更新流水记录状态
                    return billRuleDomainService.updateFlow(receiptsBillZJF.getIds(),
                            FlowClaimRecordApproveStateEnum.审核中, FlowClaimStatusEnum.报账审核中, false);
                }
            } catch (Exception e) {
                log.error("报账审批错误:报账单id{}", voucherBillId, e);
                pushBillZJAppService.approveRefuse(voucherBillId, null);
                return false;
            }
        } else {
            return false;
        }*/
        return Boolean.TRUE;
    }

    public Boolean revenueApprove(RevenueApprove revenueApprove){

        List<VoucherBillZJ> voucherBillZJS = voucherPushBillRepository.listByIds(revenueApprove.getVoucherBillIds());
        for (VoucherBillZJ voucherBillZJ : voucherBillZJS) {
            LambdaQueryWrapper<VoucherBillFileZJ> wrapper = new LambdaQueryWrapper<VoucherBillFileZJ>()
                    .eq(VoucherBillFileZJ::getVoucherBillNo, voucherBillZJ.getVoucherBillNo())
                    .eq(VoucherBillFileZJ::getDeleted, 0);
            List<VoucherBillFileZJ> list = voucherBillFileZJRepository.list(wrapper);
            log.info("查询上传附近信息:" + JSON.toJSON(list));
            if (CollectionUtils.isEmpty(list)) {
                throw new BizException(400, "未上传附件信息不允许发起审批");
            }
        }
        List<Integer> eventTypeList = voucherBillZJS.stream().map(VoucherBillZJ::getBillEventType).distinct().collect(Collectors.toList());
        if (eventTypeList.size() >1 ) {
            throw new BizException(400, "只允许相同报账单类型推送");
        }
        OrgFinanceCostRv orgFinanceCostById = orgClient.getOrgFinanceCostById(revenueApprove.getCostCenterId());
        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(orgFinanceCostById.getCommunityId(),
                OperateTypeEnum.收入确认.getCode());
        // 校验是否维护 行政组织、行政部门
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", orgFinanceCostById.getCommunityId());
        // 调用外部数据映射接口
        // 调用根据行政组织获取核算组织接口
        String xzzz = null;
        String xzbm = null;
        List<String> collect = community.stream().map(CfgExternalDataV::getExternalDataType).collect(Collectors.toList());
        if (!collect.contains("department")){
            throw new BizException(400, "未维护行政部门");
        }
        if (!collect.contains("org")){
            throw new BizException(400, "未维护行政组织");
        }
        for (CfgExternalDataV cfgExternalDataV : community) {
            if("org".equals(cfgExternalDataV.getExternalDataType())){
                xzzz = cfgExternalDataV.getDataCode();
            }
            if ("department".equals(cfgExternalDataV.getExternalDataType())){
                xzbm = cfgExternalDataV.getDataCode();
            }

        }
        // 校验是否维护核算组织
        ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
        externalMaindataCalmappingListF.setZorgid(xzzz);
        ExternalMaindataCalmappingListV list1 = externalClient.list(externalMaindataCalmappingListF);
        if (CollectionUtils.isEmpty(list1.getInfoList())){
            throw new BizException(400, "未维护核算组织");
        }
        // 校验是否维护业务信息
        List<String> list = voucherBillZJS.stream().map(VoucherBillZJ::getBusinessTypeName).collect(Collectors.toList());
        if ( list == null || list.isEmpty() || list.contains("")){
            throw new BizException(400, "未维护业务类型");
        }
        List<String> listId = voucherBillZJS.stream().map(VoucherBillZJ::getBusinessTypeId).collect(Collectors.toList());
        for (String s : listId) {
            if (StringUtils.isBlank(s)){
                log.info("业务类型信息:" + s);
                throw new BizException(400, "未维护业务类型");
            }
        }
        Map<String, List<VoucherBillZJ>> groupByBusinessTypeId = voucherBillZJS.stream().collect(Collectors.groupingBy(VoucherBillZJ::getBusinessTypeId));
        for (String s : groupByBusinessTypeId.keySet()) {
            if (StringUtils.isBlank(s)){
                throw new BizException(400, "未维护业务类型");
            }
        }
        if (approveFilter.getApproveWay() == 2) {
            // 无需审批 直接调用推送按钮
            for (String s : groupByBusinessTypeId.keySet()) {
                SyncBatchPushZJBillF syncBatchPushZJBillF = new SyncBatchPushZJBillF();
                syncBatchPushZJBillF.setXZZZ(xzzz);
                syncBatchPushZJBillF.setXZBM(xzbm);
                syncBatchPushZJBillF.setVoucherSystem(2);
                syncBatchPushZJBillF.setYWLX(s.trim());
                List<VoucherBillZJ> voucherBillZJS1 = groupByBusinessTypeId.get(s);
                List<Long> collect1 = voucherBillZJS1.stream().map(VoucherBillZJ::getId).distinct().collect(Collectors.toList());
                syncBatchPushZJBillF.setVoucherIds(collect1);
                pushBillZJDomainService.syncBatchPushBill(syncBatchPushZJBillF);
            }
        } else {
            for (Long voucherBillId : revenueApprove.getVoucherBillIds()) {
                WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
                log.info("ChargeApproveAppService.batchBpmAdjust获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
                if (ObjectUtil.isNull(wflowModelHistorysV)) {
                    throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
                }
                ProcessStartF processStartF = new ProcessStartF();
                Map<String, Object> formData = new HashMap<>();
                formData.put("flowType",OperateTypeEnum.收入确认.getDes());
                formData.put("flowId", voucherBillId);
                processStartF.setFormData(formData);
                processStartF.setBusinessKey(String.valueOf(voucherBillId));
                processStartF.setBusinessType(OperateTypeEnum.收入确认.getDes());
                processStartF.setSuitableTargetType("PROJECT");
                processStartF.setSuitableTargetId(orgFinanceCostById.getCommunityId());

                CommunityShortRV communityInfo = spaceClient.getCommunityInfo(orgFinanceCostById.getCommunityId());
                processStartF.setSuitableTargetId(orgFinanceCostById.getCommunityId());
                processStartF.setSuitableTargetName(communityInfo.getName());
                String s = null;
                try {
                    s = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
                } catch (Exception e) {
                    log.info("流程发起异常：{}",e);
                    log.error("流程发起异常：{}",e);
                    throw new OwlBizException("流程发起超时，请稍后重试！");
                }
                VoucherBillZJ byId = voucherPushBillRepository.getById(voucherBillId);
                byId.setProcInstId(s);
                byId.setApproveState(1);
                byId.setPushState(4);
                voucherPushBillRepository.updateById(byId);
            }
        }

        return true;
    }



    public Boolean revenueApproveNew(RevenueApprove revenueApprove,OperateTypeEnum operateTypeEnum){

        List<VoucherBillDxZJ> voucherBillZJS = voucherPushBillDxZJRepository.listByIds(revenueApprove.getVoucherBillIds());
        for (VoucherBillDxZJ voucherBillZJ : voucherBillZJS) {
            LambdaQueryWrapper<VoucherBillFileZJ> wrapper = new LambdaQueryWrapper<VoucherBillFileZJ>()
                    .eq(VoucherBillFileZJ::getVoucherBillNo, voucherBillZJ.getVoucherBillNo())
                    .eq(VoucherBillFileZJ::getDeleted, 0);
            List<VoucherBillFileZJ> list = voucherBillFileZJRepository.list(wrapper);
            log.info("查询上传附近信息:" + JSON.toJSON(list));
            if (CollectionUtils.isEmpty(list)) {
                throw new BizException(400, "未上传附件信息不允许发起审批new");
            }
        }
        List<Integer> eventTypeList = voucherBillZJS.stream().map(VoucherBillDxZJ::getBillEventType).distinct().collect(Collectors.toList());
        if (eventTypeList.size() >1 ) {
            throw new BizException(400, "只允许相同报账单类型推送");
        }
        OrgFinanceCostRv orgFinanceCostById = orgClient.getOrgFinanceCostById(revenueApprove.getCostCenterId());
        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(orgFinanceCostById.getCommunityId(),
                operateTypeEnum.getCode());
        // 校验是否维护 行政组织、行政部门
        List<CfgExternalDataV> community = configClient.getExternalMapByCode("community", orgFinanceCostById.getCommunityId());
        // 调用外部数据映射接口
        // 调用根据行政组织获取核算组织接口
        String xzzz = null;
        String xzbm = null;
        List<String> collect = community.stream().map(CfgExternalDataV::getExternalDataType).collect(Collectors.toList());
        if (!collect.contains("department")){
            throw new BizException(400, "未维护行政部门");
        }
        if (!collect.contains("org")){
            throw new BizException(400, "未维护行政组织");
        }
        for (CfgExternalDataV cfgExternalDataV : community) {
            if("org".equals(cfgExternalDataV.getExternalDataType())){
                xzzz = cfgExternalDataV.getDataCode();
            }
            if ("department".equals(cfgExternalDataV.getExternalDataType())){
                xzbm = cfgExternalDataV.getDataCode();
            }

        }
        // 校验是否维护核算组织
        ExternalMaindataCalmappingListF externalMaindataCalmappingListF = new ExternalMaindataCalmappingListF();
        externalMaindataCalmappingListF.setZorgid(xzzz);
        ExternalMaindataCalmappingListV list1 = externalClient.list(externalMaindataCalmappingListF);
        if (CollectionUtils.isEmpty(list1.getInfoList())){
            throw new BizException(400, "未维护核算组织");
        }
        // 校验是否维护业务信息
        List<String> list = voucherBillZJS.stream().map(VoucherBillDxZJ::getBusinessTypeName).collect(Collectors.toList());
        if ( list == null || list.isEmpty() || list.contains("")){
            throw new BizException(400, "未维护业务类型");
        }
        List<String> listId = voucherBillZJS.stream().map(VoucherBillDxZJ::getBusinessType).collect(Collectors.toList());
        for (String s : listId) {
            if (StringUtils.isBlank(s)){
                log.info("业务类型信息:" + s);
                throw new BizException(400, "未维护业务类型");
            }
        }
        Map<String, List<VoucherBillDxZJ>> groupByBusinessTypeId = voucherBillZJS.stream().collect(Collectors.groupingBy(VoucherBillDxZJ::getBusinessType));
        for (String s : groupByBusinessTypeId.keySet()) {
            if (StringUtils.isBlank(s)){
                throw new BizException(400, "未维护业务类型");
            }
        }
        if (approveFilter.getApproveWay() == 2) {
            // 无需审批 直接调用推送按钮
            for (String s : groupByBusinessTypeId.keySet()) {
                SyncBatchPushZJBillF syncBatchPushZJBillF = new SyncBatchPushZJBillF();
                syncBatchPushZJBillF.setXZZZ(xzzz);
                syncBatchPushZJBillF.setXZBM(xzbm);
                syncBatchPushZJBillF.setVoucherSystem(2);
                syncBatchPushZJBillF.setYWLX(s.trim());
                List<VoucherBillDxZJ> voucherBillZJS1 = groupByBusinessTypeId.get(s);
                List<Long> collect1 = voucherBillZJS1.stream().map(VoucherBillDxZJ::getId).distinct().collect(Collectors.toList());
                syncBatchPushZJBillF.setVoucherIds(collect1);
                pushBillZJDomainService.syncBatchPushBill(syncBatchPushZJBillF);
            }
        } else {
            for (Long voucherBillId : revenueApprove.getVoucherBillIds()) {
                WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
                log.info("ChargeApproveAppService.batchBpmAdjust获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));
                if (ObjectUtil.isNull(wflowModelHistorysV)) {
                    throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
                }
                ProcessStartF processStartF = new ProcessStartF();
                Map<String, Object> formData = new HashMap<>();
                formData.put("flowType",operateTypeEnum.getDes());
                formData.put("flowId", voucherBillId);
                processStartF.setFormData(formData);
                processStartF.setBusinessKey(String.valueOf(voucherBillId));
                processStartF.setBusinessType(operateTypeEnum.getDes());
                processStartF.setSuitableTargetType("PROJECT");
                processStartF.setSuitableTargetId(orgFinanceCostById.getCommunityId());
                String s = null;
                try {
                    s = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
                } catch (Exception e) {
                    log.info("流程发起异常：{}",e);
                    log.error("流程发起异常：{}",e);
                    throw new OwlBizException("流程发起超时，请稍后重试！");
                }
                VoucherBillZJ byId = voucherPushBillRepository.getById(voucherBillId);
                byId.setProcInstId(s);
                byId.setApproveState(1);
                byId.setPushState(4);
                voucherPushBillRepository.updateById(byId);
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
        return billRuleDomainService.remindRuleDetail(id);
    }

    /**
     * 测试消息发送
     *
     * @return
     */
    public Boolean testSend() {
        return billRuleDomainService.testSend();
    }

    public void send(){
        billRuleDomainService.send();
    }
}
