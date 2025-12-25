package com.wishare.contract.apps.service.revision.projectInitiation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.contract.apps.fo.revision.income.ContractFjxxF;
import com.wishare.contract.apps.fo.revision.projectInitiation.*;
import com.wishare.contract.apps.remote.clients.BpmClient;
import com.wishare.contract.apps.remote.clients.ChargeClient;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.component.OrgEnhanceComponent;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalDataF;
import com.wishare.contract.apps.remote.fo.opapprove.OpinionApprovalF;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.UserStateRV;
import com.wishare.contract.apps.remote.vo.bpm.ProcessStartF;
import com.wishare.contract.apps.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.contract.apps.remote.vo.charge.ApproveFilter;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectInitiationE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectOrderE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectPlanE;
import com.wishare.contract.domains.enums.BillTypeEnum;
import com.wishare.contract.domains.enums.OperationTypeEnum;
import com.wishare.contract.domains.enums.revision.BPMStatusEnum;
import com.wishare.contract.domains.enums.revision.ContractConcludeEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.enums.revision.log.LogActionTypeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.projectInitiation.ContractProjectInitiationMapper;
import com.wishare.contract.domains.service.revision.log.RevisionLogService;
import com.wishare.contract.domains.vo.revision.fwsso.FwSSoBaseInfoF;
import com.wishare.contract.domains.vo.revision.opapprove.OpinionApprovalV;
import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateV;
import com.wishare.contract.domains.vo.revision.projectInitiation.*;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.*;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.contract.infrastructure.utils.query.LambdaQueryWrapperX;
import com.wishare.contract.infrastructure.utils.query.WrapperX;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.consts.Const;
import com.wishare.starter.enums.GatewayTagEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class ContractProjectInitiationAppService extends ServiceImpl<ContractProjectInitiationMapper, ContractProjectInitiationE> implements IOwlApiBase {

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ContractProjectInitiationMapper contractProjectInitiationMapper;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ContractProjectPlanAppService contractProjectPlanAppService;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ContractProjectPlanMonthlyAllocationAppService contractProjectPlanMonthlyAllocationAppService;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ContractProjectPlanCostConfirmAppService contractProjectPlanCostConfirmAppService;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ContractProjectOrderAppService contractProjectOrderAppService;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ContractProcessRecordMapper contractProcessRecordMapper;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private RevisionLogService logService;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private OrgEnhanceComponent orgEnhanceComponent;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private ChargeClient chargeClient;

    @Getter
    @Setter(onMethod_ = {@Autowired})
    private BpmClient bpmClient;

    @Value("${process.create.bizCode:}")
    private String bizCode;

    @Value("${process.create.flag:0}")
    private Integer createProcessFlag;

    // 虚拟项目类型id
    private static final String VIRTUAL_PROJECT_TYPE_ID = "13590120111815";


    public PageV<ContractProjectInitiationV> frontPage(PageF<SearchF<ContractProjectInitiationPageF>> request) {
        Page<ContractProjectInitiationE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

        QueryWrapper<ContractProjectInitiationPageF> queryWrapper = request.getConditions().getQueryModel();

        UserStateRV userStateRV = userFeignClient.getStateByUserId(userId());
        boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
        UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId());
        if (userInfoRv.getOrgIds().contains(13554968509111L)) {
            superAccount = true;
        }
        if (!superAccount) {
            Set<String> orgListByOrgId = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            queryWrapper.and(i -> i.in(ContractProjectInitiationPageF.DEPART_ID, orgListByOrgId)
                    .or(m -> m.isNull(ContractProjectInitiationPageF.DEPART_ID)));
        }
        IPage<ContractProjectInitiationE> page = contractProjectInitiationMapper.selectFrontPage(pageF, queryWrapper);

        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractProjectInitiationV.class));
    }

    public PageV<ContractProjectInitiationV> frontPageForExport(PageF<SearchF<ContractProjectInitiationPageF>> request) {
        Page<ContractProjectInitiationE> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

        QueryWrapper<ContractProjectInitiationPageF> queryWrapper = request.getConditions().getQueryModel();

        UserStateRV userStateRV = userFeignClient.getStateByUserId(userId());
        boolean superAccount = Objects.nonNull(userStateRV) && userStateRV.isSuperAccount();//-- 为 TRUE 时当前账号为超级管理员
        UserInfoRv userInfoRv = userFeignClient.getUsreInfoByUserId(userId());
        if (userInfoRv.getOrgIds().contains(13554968509111L)) {
            superAccount = true;
        }
        if (!superAccount) {
            Set<String> orgListByOrgId = orgEnhanceComponent.getChildrenOrgListByOrgId(Global.mapperFacade.mapAsList(orgIds(), String.class));
            queryWrapper.and(i -> i.in(ContractProjectInitiationPageF.DEPART_ID, orgListByOrgId)
                    .or(m -> m.isNull(ContractProjectInitiationPageF.DEPART_ID)));
        }
        IPage<ContractProjectInitiationE> page = contractProjectInitiationMapper.selectFrontPage(pageF, queryWrapper);

        Map<String, ContractProjectInitiationE> projectInitiationVMap = page.getRecords().stream().collect(Collectors.toMap(ContractProjectInitiationE::getId, v -> v));

        Map<String, List<ContractProjectPlanE>> projectPlanEMap = contractProjectPlanAppService.list(projectInitiationVMap.keySet()).stream().collect(Collectors.groupingBy(ContractProjectPlanE::getProjectInitiationId));

        List<ContractProjectInitiationV> projectInitiationVList = new ArrayList<>();

        projectInitiationVMap.entrySet().stream().forEach(entry -> {
            List<ContractProjectPlanE> projectPlanES = projectPlanEMap.get(entry.getKey());
            if (CollectionUtils.isNotEmpty(projectPlanES)) {
                List<ContractProjectInitiationV> contractProjectInitiationVS = projectPlanES.stream().map(e -> {
                    ContractProjectInitiationV map = Global.mapperFacade.map(entry.getValue(), ContractProjectInitiationV.class);
                    map.setContractPlanName(e.getContractPlanName())
                            .setAllocationAmount(e.getAllocationAmount())
                            .setContractMonthlyOccurredAmount(e.getContractMonthlyOccurredAmount())
                            .setContractMonthlyOccurredPercentage(e.getContractMonthlyOccurredPercentage())
                            .setContractYearlyOccurredAmount(e.getContractYearlyOccurredAmount())
                            .setContractYearlyOccurredPercentage(e.getContractYearlyOccurredPercentage())
                            .setConfirmAmount(e.getConfirmAmount());

                    return map;
                }).collect(Collectors.toList());
                projectInitiationVList.addAll(contractProjectInitiationVS);

            } else {
                ContractProjectInitiationV map = Global.mapperFacade.map(entry.getValue(), ContractProjectInitiationV.class);
                projectInitiationVList.add(map);
            }
        });

        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(projectInitiationVList, ContractProjectInitiationV.class));
    }

    /**
     * 查询立项详情
     *
     * @param id
     * @return
     */
    public ContractProjectInitiationV getDetail(String id) {
        return this.getDetail(id, false);
    }

    /**
     * 查询立项详情
     *
     * @param id
     * @param queryMonthlyFlag 是否查询月度分摊明细, 1 发起立项不查询,校验再逻辑 2 审批中和审批通过不查询,用历史数据
     * @return
     */
    public ContractProjectInitiationV getDetail(String id, Boolean queryMonthlyFlag) {
        ContractProjectInitiationE result = this.getById(id);
        // 获取关联的合约规划列表
        if (result != null) {
            Integer reviewStatus = result.getReviewStatus();
            queryMonthlyFlag = queryMonthlyFlag
                    && Objects.equals(result.getIsContractSigned(), 0)
                    && !(ReviewStatusEnum.审批中.getCode().equals(reviewStatus) || ReviewStatusEnum.已通过.getCode().equals(reviewStatus));
            ContractProjectInitiationV map = Global.mapperFacade.map(result, ContractProjectInitiationV.class);
            contractProjectPlanAppService.getByProjectInitiationId(map, queryMonthlyFlag);

            List<ContractProjectOrderV> orderList = contractProjectOrderAppService.getByProjectInitiationId(id);
            map.setContractProjectOrderList(orderList);

            // 附件信息
            map.setPricingBasisAttachmentList(
                    StrUtil.isNotBlank(map.getPricingBasisAttachment())
                            ? JSONObject.parseArray(map.getPricingBasisAttachment(), ContractFjxxF.class)
                            : Collections.emptyList()
            );

            map.setImplementationPlanAttachmentList(
                    StrUtil.isNotBlank(map.getImplementationPlanAttachment())
                            ? JSONObject.parseArray(map.getImplementationPlanAttachment(), ContractFjxxF.class)
                            : Collections.emptyList()
            );
            return map;
        }

        return null;
    }

    /**
     * 计算 “该分类月度含税金额(含本次金额)”
     * 根据本次立项选定项目、具体类型、立项发起时间，汇总该项目、选定的具体类型在本次立项创建时间所在月内已经审批通过的立项含税金额、累加本次立项含税金额;
     *
     * @param projectInitiationV
     * @return
     */
    public BigDecimal calcMonthlyWithTaxAmount(ContractProjectInitiationV projectInitiationV) {
        LocalDateTime gmtCreate = projectInitiationV.getGmtCreate();
        YearMonth ym = YearMonth.from(gmtCreate);

        // 月第一天 00:00:00
        LocalDateTime startDate = ym.atDay(1).atStartOfDay();

        // 月最后一天 23:59:59.999999999
        LocalDateTime endDate = ym.atEndOfMonth().atTime(LocalTime.MAX);

        LambdaQueryWrapper queryWrappers = new LambdaQueryWrapper<ContractProjectInitiationE>()
                .select(ContractProjectInitiationE::getAmountWithTax)
                .eq(ContractProjectInitiationE::getCommunityId, projectInitiationV.getCommunityId())
                .eq(ContractProjectInitiationE::getInitiationTypeCode, projectInitiationV.getInitiationTypeCode())
                .eq(null != projectInitiationV, ContractProjectInitiationE::getSpecificCategoryCode, projectInitiationV.getSpecificCategoryCode())
                .eq(ContractProjectInitiationE::getReviewStatus, ReviewStatusEnum.已通过.getCode())
                .ge(ContractProjectInitiationE::getGmtCreate, startDate)
                .le(ContractProjectInitiationE::getGmtCreate, endDate)
                .eq(ContractProjectInitiationE::getDeleted, 0);

        List<ContractProjectInitiationE> amountWithTaxList = this.list(queryWrappers);
        return NumberUtil.add(
                amountWithTaxList.stream()
                        .map(ContractProjectInitiationE::getAmountWithTax)
                        .reduce(BigDecimal::add).orElse(BigDecimal.ZERO),
                projectInitiationV.getAmountWithTax()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public String save(ContractProjectInitiationSaveF saveF) {
        saveF.setTenantId(tenantId());
        ContractProjectInitiationE entity = Global.mapperFacade.map(saveF, ContractProjectInitiationE.class);
        // 附件信息
        entity.setPricingBasisAttachment(
                CollectionUtils.isNotEmpty(saveF.getPricingBasisAttachmentList())
                        ? JSONObject.toJSONString(saveF.getPricingBasisAttachmentList())
                        : ""
        );

        entity.setImplementationPlanAttachment(
                CollectionUtils.isNotEmpty(saveF.getImplementationPlanAttachmentList())
                        ? JSONObject.toJSONString(saveF.getImplementationPlanAttachmentList())
                        : ""
        );
        String contractProjectInitiation = UidHelper.nextIdPrefixKeyByYMDStr(tenantId(), "LXD");
        entity.setProjectCode(contractProjectInitiation);
        List<ContractProjectPlanF> contractPlanList = saveF.getContractPlanList();
        if (CollUtil.isNotEmpty(contractPlanList)) {
            List<String> contractPlanNameList = new ArrayList<>();
            contractPlanList.stream().forEach(plan -> {
                contractPlanNameList.add(plan.getContractPlanName());
                entity.setMonthlyUsedAmount(plan.getCostItemMonthlyAmount());
                entity.setMonthlyUsedPercentage(plan.getCostItemMonthlyPercentage());
                entity.setYearlyUsedAmount(plan.getCostItemYearlyAmount());
                entity.setYearlyUsedPercentage(plan.getCostItemYearlyPercentage());
            });
            String contractPlanNames = contractPlanNameList.stream().collect(Collectors.joining("、"));
            entity.setContractPlanName(contractPlanNames);
        }

        boolean result = this.save(entity);

        // 保存合约规划信息
        String projectInitiationId = entity.getId();
        if (result && CollUtil.isNotEmpty(contractPlanList)) {
            contractProjectPlanAppService.saveBatch(entity, contractPlanList);
        }

        if (saveF.getStartApprovalFlag()) {
            return this.startApproval(projectInitiationId);
        }
        return projectInitiationId;
    }

    @Transactional(rollbackFor = Exception.class)
    public String update(ContractProjectInitiationUpdateF updateF) {
        ContractProjectInitiationE entity = Global.mapperFacade.map(updateF, ContractProjectInitiationE.class);
        // 附件信息
        entity.setPricingBasisAttachment(
                CollectionUtils.isNotEmpty(updateF.getPricingBasisAttachmentList())
                        ? JSONObject.toJSONString(updateF.getPricingBasisAttachmentList())
                        : ""
        );

        entity.setImplementationPlanAttachment(
                CollectionUtils.isNotEmpty(updateF.getImplementationPlanAttachmentList())
                        ? JSONObject.toJSONString(updateF.getImplementationPlanAttachmentList())
                        : ""
        );

        List<ContractProjectPlanF> contractPlanList = updateF.getContractPlanList();
        if (CollUtil.isNotEmpty(contractPlanList)) {
            List<String> contractPlanNameList = new ArrayList<>();
            contractPlanList.stream().forEach(plan -> {
                contractPlanNameList.add(plan.getContractPlanName());
                entity.setMonthlyUsedAmount(plan.getCostItemMonthlyAmount());
                entity.setMonthlyUsedPercentage(plan.getCostItemMonthlyPercentage());
                entity.setYearlyUsedAmount(plan.getCostItemYearlyAmount());
                entity.setYearlyUsedPercentage(plan.getCostItemYearlyPercentage());
            });
            String contractPlanNames = contractPlanNameList.stream().collect(Collectors.joining("、"));
            entity.setContractPlanName(contractPlanNames);
        }
        boolean result = this.updateById(entity);

        String projectInitiationId = updateF.getId();
        // 更新合约规划信息
        if (result) {
            if (CollUtil.isNotEmpty(contractPlanList)) {
                contractProjectPlanAppService.saveBatch(entity, contractPlanList);
            } else {
                // 如果更新时合约规划为空,说明立项修改为合同签订,则清空历史合约规划相关信息
                contractProjectPlanAppService.deleteAllByProjectInitiationId(projectInitiationId);
            }
        }

        if (updateF.getStartApprovalFlag()) {
            return this.startApproval(projectInitiationId);
        }

        return projectInitiationId;
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateForCostConfirm(ContractProjectInitiationUpdateF updateF) {
        ContractProjectInitiationE entity = Global.mapperFacade.map(updateF, ContractProjectInitiationE.class);

        boolean result = this.updateById(entity);

        String projectInitiationId = updateF.getId();
        // 更新合约规划信息
        if (result && CollUtil.isNotEmpty(updateF.getContractPlanList())) {
            contractProjectPlanAppService.updateForCostConfirm(projectInitiationId, updateF.getContractPlanList());
        }
        if (result && CollUtil.isNotEmpty(updateF.getContractList())) {
            contractProjectPlanAppService.saveOrUpdateBatchForCostConfirm(updateF.getContractList());
        }

        return projectInitiationId;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String id) {
        // 删除立项
        ContractProjectInitiationE entity = this.getBaseMapper().selectById(id);
        if (entity == null) {
            return false;
        }
        // 删除关联的合约规划
        contractProjectPlanAppService.deleteAllByProjectInitiationId(id);
        // 删除关联订单
        contractProjectOrderAppService.deleteByProjectInitiationId(id);
        return this.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrderById(String id) {
        ContractProjectOrderE entity = contractProjectOrderAppService.getById(id);
        if (entity == null) {
            return false;
        }
        // 删除订单
        boolean b = contractProjectOrderAppService.deleteById(id);

        // 恢复立项可用金额
        ContractProjectInitiationE initiationE = this.getById(entity.getProjectInitiationId());
        initiationE.setOrderTotalAmount(
                NumberUtil.sub(initiationE.getOrderTotalAmount(), entity.getOrderAmountWithoutTax())
        ).setRemainingAmountWithoutTax(
                NumberUtil.add(initiationE.getRemainingAmountWithoutTax(), entity.getOrderAmountWithoutTax())
        );
        this.updateById(initiationE);

        return b;
    }

    /**
     * 发起审批
     *
     * @param id 立项ID
     * @return 审批结果
     */
    @Transactional(rollbackFor = Exception.class)
    public String startApproval(String id) {
        //-- α.校验ID正确性
        ContractProjectInitiationV contractProjectInitiation = this.getDetail(id);
        if (Objects.isNull(contractProjectInitiation)) {
            throw new OwlBizException("根据立项ID检索数据失败");
        }

        // 调用成本系统接口判断合约规划管控方式及可用余额是否满足当前立项金额，若满足则返回发起OA审批，否则给出提示“当前立项金额超出成本系统合约规划管控金额”
        /**
         * - 项目为虚拟项目则不控 直接通过
         * - 当前费项对应立项年份没有合约规划 直接通过
         * - 当前费项对应立项年份的控制逻辑为否 直接通过
         *
         * - 如果为真实项目判断合约规划费项金额释放满足
         * -- 年控,则当年金额不超过即可
         * -- 月控,则每个月的金额都不能超过合约规划金额,
         */

        String communityTypeId = contractProjectInitiation.getCommunityTypeId();
        if (!VIRTUAL_PROJECT_TYPE_ID.equals(communityTypeId)) {
            String communityId = contractProjectInitiation.getCommunityId();
            List<String> yearList = this.getYearList(contractProjectInitiation);
            int monthValue = contractProjectInitiation.getPlanStartTime().getMonthValue();
            contractProjectInitiation.getContractPlanList().stream().forEach(contractPlan -> {
                List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationDetails = contractProjectPlanMonthlyAllocationAppService.getMonthlyAllocationDetail(contractPlan, communityId, yearList, monthValue, true);
                // 更新最新的月度分摊
                contractProjectPlanMonthlyAllocationAppService.saveBatch(
                        contractPlan.getProjectInitiationId(), contractPlan.getId(), monthlyAllocationDetails
                );
                contractPlan.setMonthlyAllocationList(monthlyAllocationDetails);
            });
        }

        //-- β.记录日志
        logService.insertOneLog(id, contractProjectInitiation.getProjectName(), LogActionTypeEnum.提交.getCode());
        ContractProjectInitiationE contractProjectInitiationE = new ContractProjectInitiationE();
        contractProjectInitiationE.setId(contractProjectInitiation.getId());
        contractProjectInitiationE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
        this.updateById(contractProjectInitiationE);

        try {
            String process = createProcessFlag.equals(0) ? "流程开关关闭" : this.createProcess(contractProjectInitiation);
            if (StringUtils.isNotBlank(process)) {
                // 成本系统合约规划占用
                this.syncDynamicCostIncurred(contractProjectInitiation, BillTypeEnum.PROJECT_INIT, OperationTypeEnum.OCCUPY);
            }
            return process;
        } catch (Exception e) {
            // 回退立项编号的序号
            log.info("立项管理流程发起异常：{}", e.getMessage());
            throw new OwlBizException("OA流程发起超时，请稍后重试！");
        }
    }

    /**
     * 成本系统合约规划金额同步
     *
     * @param contractProjectInitiation
     * @param billTypeEnum
     * @param operationTypeEnum
     */
    public void syncDynamicCostIncurred(ContractProjectInitiationV contractProjectInitiation, BillTypeEnum billTypeEnum, OperationTypeEnum operationTypeEnum) {
        if (CollUtil.isEmpty(contractProjectInitiation.getContractPlanList())) {
            return;
        }
        log.info("ContractProjectInitiationAppService.syncDynamicCostIncurred() called with parameters => 【contractProjectInitiation = {}】,【billTypeEnum = {}】,【operationTypeEnum = {}】", JSON.toJSONString(contractProjectInitiation), JSON.toJSONString(billTypeEnum), JSON.toJSONString(operationTypeEnum));
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();

        // 成本系统合约规划占用
        DynamicCostIncurredReqF dynamicCostIncurredReqF = new DynamicCostIncurredReqF()
                .setBillGuid(contractProjectInitiation.getId())
                .setBillCode(contractProjectInitiation.getProjectCode())
                .setBillName(contractProjectInitiation.getProjectName())
                .setContractId(contractProjectInitiation.getId())
                .setContractNo(contractProjectInitiation.getProjectCode())
                .setContractName(contractProjectInitiation.getProjectName())
                .setBillTypeName(billTypeEnum.getName())
                .setBillTypeEnum(billTypeEnum.getCode())
                .setBuGuid(contractProjectInitiation.getBuGuid())
                .setProjectGuid(contractProjectInitiation.getProjectGuid())
                .setBusinessGuid(contractProjectInitiation.getBusinessGuid())
                .setBusinessUnitCode(contractProjectInitiation.getBusinessUnitCode())
                .setOperationType(operationTypeEnum.getCode())
                .setContactor(operationTypeEnum.getName())
                .setIncurredAmount(
                        OperationTypeEnum.RELEASE.equals(operationTypeEnum)
                                ? contractProjectInitiation.getAmountWithoutTax().negate()
                                : contractProjectInitiation.getAmountWithoutTax()
                );

        contractProjectInitiation.getContractPlanList().stream().forEach(contractPlan -> {

            List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataListDTOS = contractPlan.getMonthlyAllocationList().stream()
                    .filter(e -> e.getType().equals(1))
                    .map(monthlyAllocation -> {
                        DynamicCostIncurredReqF.IncurredDataListDTO incurredDataDTO = new DynamicCostIncurredReqF.IncurredDataListDTO()
                                .setAccountItemFullCode(contractPlan.getCostChargeItemCode());

                        switch (operationTypeEnum) {
                            case OCCUPY:
                                incurredDataDTO.occupiedDynamicCostIncurred(incurredDataDTO, monthlyAllocation);
                                break;
                            case RELEASE:
                                incurredDataDTO.freeDynamicCostIncurred(incurredDataDTO, monthlyAllocation);
                                break;
                        }
                        return incurredDataDTO;

                    }).collect(Collectors.toList());

            incurredDataList.addAll(incurredDataListDTOS);

        });
        dynamicCostIncurredReqF.setIncurredDataList(incurredDataList);

        log.info("ContractProjectInitiationAppService.syncDynamicCostIncurred.getDynamicCostIncurred() called with parameters => 【dynamicCostIncurredReqF = {}】", JSON.toJSONString(dynamicCostIncurredReqF));
        CostBaseResponse<List<DynamicCostIncurredRespF>> dynamicCostIncurred = externalFeignClient.getDynamicCostIncurred(dynamicCostIncurredReqF);
        log.info("ContractProjectInitiationAppService.syncDynamicCostIncurred.getDynamicCostIncurred contractProjectInitiationId:{}, dynamicCostIncurred:{}", contractProjectInitiation.getId(), JSON.toJSONString(dynamicCostIncurred));
    }

    /**
     * 收入确认成本系统合约规划金额同步
     *
     * @param contractProjectInitiation
     * @param billTypeEnum
     */
    public void syncCostConfirmDynamicCostIncurred(ContractProjectInitiationV contractProjectInitiation, BillTypeEnum billTypeEnum) {
        log.info("ContractProjectInitiationAppService.syncCostConfirmDynamicCostIncurred() called with parameters => 【contractProjectInitiation = {}】,【billTypeEnum = {}】,【operationTypeEnum = {}】", JSON.toJSONString(contractProjectInitiation), JSON.toJSONString(billTypeEnum));
        List<String> yearList = this.getYearList(contractProjectInitiation);

        BigDecimal incurredAmountRelease = BigDecimal.ZERO;
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataReleaseList = new ArrayList<>();

        BigDecimal incurredAmount = BigDecimal.ZERO;
        List<DynamicCostIncurredReqF.IncurredDataListDTO> incurredDataList = new ArrayList<>();

        for (ContractProjectPlanV contractPlan : contractProjectInitiation.getContractPlanList()) {
            Map<String, Map<Integer, ContractProjectPlanMonthlyAllocationV>> monthlyAllocationVMap = contractPlan.getCostConfirmMonthlyAllocationList()
                    .stream()
                    .filter(e -> e.getType().equals(1) || e.getType().equals(3))
                    .collect(Collectors.groupingBy(
                            ContractProjectPlanMonthlyAllocationV::getYear,
                            Collectors.toMap(ContractProjectPlanMonthlyAllocationV::getType, e -> e)
                    ));

            for (String year : yearList) {
                Map<Integer, ContractProjectPlanMonthlyAllocationV> monthlyAllocationVYearMap = monthlyAllocationVMap.getOrDefault(year, new HashMap<>());

                // 释放
                ContractProjectPlanMonthlyAllocationV monthlyAllocationType3 = monthlyAllocationVYearMap.get(3);
                if (monthlyAllocationType3 != null) {
                    DynamicCostIncurredReqF.IncurredDataListDTO incurredDataReleaseDTO = new DynamicCostIncurredReqF.IncurredDataListDTO()
                            .setAccountItemFullCode(contractPlan.getCostChargeItemCode());
                    incurredDataReleaseDTO.freeDynamicCostIncurred(incurredDataReleaseDTO, monthlyAllocationType3);
                    incurredAmountRelease = NumberUtil.add(incurredAmountRelease, incurredDataReleaseDTO.getYearShareAmount());
                    incurredDataReleaseList.add(incurredDataReleaseDTO);
                }

                // 占用
                ContractProjectPlanMonthlyAllocationV monthlyAllocationType1 = monthlyAllocationVYearMap.get(1);
                if (monthlyAllocationType1 != null) {
                    DynamicCostIncurredReqF.IncurredDataListDTO incurredDataDTO = new DynamicCostIncurredReqF.IncurredDataListDTO()
                            .setAccountItemFullCode(contractPlan.getCostChargeItemCode());
                    incurredDataDTO.occupiedDynamicCostIncurred(incurredDataDTO, monthlyAllocationType1);
                    incurredAmount = NumberUtil.add(incurredAmount, incurredDataDTO.getYearShareAmount());
                    incurredDataList.add(incurredDataDTO);
                }
            }
        }

        DynamicCostIncurredReqF dynamicCostIncurredReqF = new DynamicCostIncurredReqF()
                .setBillGuid(contractProjectInitiation.getId())
                .setBillCode(contractProjectInitiation.getProjectCode())
                .setBillName(contractProjectInitiation.getProjectName())
                .setContractId(contractProjectInitiation.getId())
                .setContractNo(contractProjectInitiation.getProjectCode())
                .setContractName(contractProjectInitiation.getProjectName())
                .setBillTypeName(billTypeEnum.getName())
                .setBillTypeEnum(billTypeEnum.getCode())
                .setBuGuid(contractProjectInitiation.getBuGuid())
                .setProjectGuid(contractProjectInitiation.getProjectGuid())
                .setBusinessGuid(contractProjectInitiation.getBusinessGuid())
                .setBusinessUnitCode(contractProjectInitiation.getBusinessUnitCode());

        if (CollUtil.isNotEmpty(incurredDataReleaseList)) {
            // 成本系统合约规划释放
            dynamicCostIncurredReqF.setOperationType(OperationTypeEnum.RELEASE.getCode())
                    .setContactor(OperationTypeEnum.RELEASE.getName())
                    .setIncurredDataList(incurredDataReleaseList)
                    .setIncurredAmount(incurredAmountRelease);
            log.info("ContractProjectInitiationAppService.syncCostConfirmDynamicCostIncurred.getDynamicCostIncurred() called with parameters => 【dynamicCostIncurredReleaseReqF = {}】", JSON.toJSONString(dynamicCostIncurredReqF));
            CostBaseResponse<List<DynamicCostIncurredRespF>> dynamicCostIncurredRelease = externalFeignClient.getDynamicCostIncurred(dynamicCostIncurredReqF);
            log.info("ContractProjectInitiationAppService.syncCostConfirmDynamicCostIncurred.getDynamicCostIncurred() contractProjectInitiationId:{}, dynamicCostIncurredRelease:{}", contractProjectInitiation.getId(), JSON.toJSONString(dynamicCostIncurredRelease));
        }

        // 成本系统合约规划占用
        dynamicCostIncurredReqF
                .setOperationType(OperationTypeEnum.OCCUPY.getCode())
                .setContactor(OperationTypeEnum.OCCUPY.getName())
                .setIncurredDataList(incurredDataList)
                .setIncurredAmount(incurredAmount);

        log.info("ContractProjectInitiationAppService.syncCostConfirmDynamicCostIncurred.getDynamicCostIncurred() called with parameters => 【dynamicCostIncurredReqF = {}】", JSON.toJSONString(dynamicCostIncurredReqF));
        CostBaseResponse<List<DynamicCostIncurredRespF>> dynamicCostIncurred = externalFeignClient.getDynamicCostIncurred(dynamicCostIncurredReqF);
        log.info("ContractProjectInitiationAppService.syncCostConfirmDynamicCostIncurred.getDynamicCostIncurred() contractProjectInitiationId:{}, dynamicCostIncurred:{}", contractProjectInitiation.getId(), JSON.toJSONString(dynamicCostIncurred));
    }

    /**
     * 立项管理创建流程
     *
     * @param contractProjectInitiation
     * @return
     */
    public String createProcess(ContractProjectInitiationV contractProjectInitiation) {
        LambdaQueryWrapper queryWrappers = new LambdaQueryWrapper<ContractProcessRecordE>()
                .eq(ContractProcessRecordE::getContractId, contractProjectInitiation.getId())
                .eq(ContractProcessRecordE::getType, ContractConcludeEnum.PROJECT_INITIATION_COST_CONFIRM.getCode())
                .eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE sk = contractProcessRecordMapper.selectOne(queryWrappers);
        if (ObjectUtils.isNotEmpty(sk) && StringUtils.isNotBlank(sk.getProcessId())) {
            BusinessInfoF businessInfoF = this.buildBusinessInfoF(contractProjectInitiation);
            businessInfoF.setProcessId(sk.getProcessId());
            log.info("立项管理更新审批表单数据:{}", JSON.toJSONString(businessInfoF));
            //响应结构保持不变
            ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
            if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                log.info("立项管理流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                sk.setReviewStatus(ReviewStatusEnum.已拒绝.getCode());
                contractProcessRecordMapper.updateById(sk);
                throw new OwlBizException("流程更新失败");
            }
            //流程正常发起了，将流程记录表的审批状态修改为"审批中"
            sk.setReviewStatus(ReviewStatusEnum.审批中.getCode());
            contractProcessRecordMapper.updateById(sk);
            //流程名更新成功后再继续原逻辑
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if (ObjectUtils.isNotEmpty(s)) {
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(sk.getProcessId());
            return externalFeignClient.validateFw(f);
        }
        BusinessInfoF businessInfoF = this.buildBusinessInfoF(contractProjectInitiation);
        log.info("立项管理新增审批表单数据:{}", JSON.toJSONString(businessInfoF));
        //发起流程
        ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
        Integer reviewStatusCode = ReviewStatusEnum.审批中.getCode();
        // 若创建失败数据不入库
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("立项合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            reviewStatusCode = ReviewStatusEnum.已拒绝.getCode();
        }

        // ζ.构造入库数据,能存的都存下来
        String requestid = processCreateV.getET_RESULT().getRequestid();
        ContractProcessRecordE contractProcessRecordE = Builder.of(ContractProcessRecordE::new)
                .with(ContractProcessRecordE::setProcessId, requestid) // 流程请求id
                .with(ContractProcessRecordE::setContractId, contractProjectInitiation.getId()) // 立项ID
                .with(ContractProcessRecordE::setReviewStatus, reviewStatusCode) // 审核状态
                .with(ContractProcessRecordE::setTenantId, contractProjectInitiation.getTenantId()) // 租户ID
                .with(ContractProcessRecordE::setCreator, contractProjectInitiation.getCreator()) // 创建人
                .with(ContractProcessRecordE::setCreatorName, contractProjectInitiation.getCreatorName()) // 创建人名称
                .with(ContractProcessRecordE::setType, ContractConcludeEnum.PROJECT_INITIATION_COST_CONFIRM.getCode())
                .build();
        // η.非并发接口,为保证幂等性,无对应记录再插入数据 CHECK 代码逻辑不适用并发环境;暂不做redis缓存
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eqIfPresent(ContractProcessRecordE::getProcessId, requestid)
                .eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(recordE)) {
            contractProcessRecordMapper.updateById(contractProcessRecordE);
            log.info("返回的立项流程已存在,已更新数据库记录");
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if (ObjectUtils.isNotEmpty(s)) {
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(requestid);
            return externalFeignClient.validateFw(f);
        }

        // θ.数据入 [contract_process_record] 库
        contractProcessRecordMapper.insert(contractProcessRecordE);
        log.info("返回的立项流程不存在,已插入数据库记录");

        if (StringUtils.isNotBlank(requestid)) {
            FwSSoBaseInfoF f = new FwSSoBaseInfoF();
            UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
            if (ObjectUtils.isNotEmpty(s)) {
                f.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
                f.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
            }
            f.setRequestId(requestid);
            return externalFeignClient.validateFw(f);
        }

        return "";
    }

    private BusinessInfoF buildBusinessInfoF(ContractProjectInitiationV contractProjectInitiation) {
        BusinessInfoF businessInfoF = new BusinessInfoF();
        businessInfoF.setFormDataId(contractProjectInitiation.getId());
        businessInfoF.setEditFlag(0);
        businessInfoF.setFormType(ContractConcludeEnum.PROJECT_INITIATION_COST_CONFIRM.getCode());
        businessInfoF.setFlowType(bizCode);
        businessInfoF.setContractName(contractProjectInitiation.getProjectName());
        //下面是表单参数
        // 集团总部 0 物管事业部 1
        businessInfoF.setGlzz(
                contractProjectInitiation.getRegionCode().equals(0) ? 0 : 1
        );
        /**
         * 华北区域	0
         * 华东区域	1
         * 华南区域	2
         * 西部区域	3
         */
        businessInfoF.setSsqy(contractProjectInitiation.getRegionCode() - 1);
        businessInfoF.setXmmc(contractProjectInitiation.getCommunityName());
        businessInfoF.setXmlx(contractProjectInitiation.getCommunityTypeCode());
        // businessInfoF.setContractName(contractProjectInitiation.getProjectName());
        businessInfoF.setLxlx(contractProjectInitiation.getInitiationTypeCode());
        businessInfoF.setJtfl(contractProjectInitiation.getSpecificCategoryCode());
        businessInfoF.setLxsy(contractProjectInitiation.getInitiationReason());
        businessInfoF.setHsje(contractProjectInitiation.getAmountWithTax());

        businessInfoF.setByljsyje(
                // 根据本次立项选定项目、具体类型、立项发起时间，汇总该项目、选定的具体类型在本次立项创建时间所在月内已经审批通过的立项含税金额、累加本次立项含税金额;
                this.calcMonthlyWithTaxAmount(contractProjectInitiation).toString()
        );

        return businessInfoF;
    }

    public OpinionApprovalV opinionApproval(String id) {
        OpinionApprovalF opinionApprovalF = new OpinionApprovalF();
        OpinionApprovalDataF opinionApprovalDataF = new OpinionApprovalDataF();
        LambdaQueryWrapperX<ContractProcessRecordE> queryWrapper = WrapperX.lambdaQueryX();
        queryWrapper.eq(ContractProcessRecordE::getContractId, id)
                .eq(ContractProcessRecordE::getDeleted, 0);
        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(recordE)) {
            return new OpinionApprovalV();
        }
        opinionApprovalDataF.setFormdataid(id);
        opinionApprovalDataF.setRequestId(recordE.getProcessId());
        opinionApprovalF.setIT_DATA(opinionApprovalDataF);
        return externalFeignClient.opinionApproval(opinionApprovalF);
    }

    /**
     * 下单-京东慧采
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public String startOrderForJD(StartOrderForJDReqF req) {
        //-- α.校验ID正确性
        ContractProjectInitiationE contractProjectInitiationE = this.getById(req.getId());
        if (Objects.isNull(contractProjectInitiationE)) {
            throw new OwlBizException("根据立项ID检索数据失败");
        }

        // 保存订单表信息
        String contractProjectOrderId = contractProjectOrderAppService.saveForJDHuiCai(req);
        req.setOrderId(contractProjectOrderId);
        log.info("ContractProjectInitiationAppService.startOrderForJD:{}", JSON.toJSONString(req));
        String trustLoginUrl = externalFeignClient.trustLogin(req);
        log.info("ContractProjectInitiationAppService.startOrderForJD returned:{}", trustLoginUrl);
        return trustLoginUrl;
    }

    /**
     * 订单校验
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean projectInitiationOrderVerification(ReceiveOrderResp req) {
        log.info("ContractProjectInitiationAppService.projectInitiationOrderVerification() called with parameters => 【req = {}】", JSON.toJSONString(req));
        //-- α.校验ID正确性
        String contractProjectOrderId = req.getPr();
        ContractProjectOrderE contractProjectOrderE = contractProjectOrderAppService.getById(contractProjectOrderId);
        if (Objects.isNull(contractProjectOrderE)) {
            throw new OwlBizException("根据订单ID检索数据失败");
        }
        Boolean result;


        ContractProjectInitiationE initiationE = this.getById(contractProjectOrderE.getProjectInitiationId());
        // 校验立项金额是否满足刻度
        if (NumberUtil.sub(initiationE.getAmountWithoutTax(), initiationE.getOrderTotalAmount(), req.getOrderAmount()).compareTo(BigDecimal.ZERO) >= 0) {
            String communityId = initiationE.getCommunityId();

            this.setIdentityInfo(contractProjectOrderE.getCreator(), contractProjectOrderE.getCreatorName(), contractProjectOrderE.getTenantId());

            ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(communityId, ContractConcludeEnum.PROJECT_INITIATION_ORDER_FOR_JD.getCode());
            log.info("立项管理慧采下单：获取审批规则,结果:{}", JSON.toJSONString(approveFilter));

            WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
            log.info("立项管理慧采下单：获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));

            if (ObjectUtil.isNull(wflowModelHistorysV)) {
                throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
            }

            ProcessStartF processStartF = new ProcessStartF();
            Map<String, Object> formData = new HashMap<>();
            formData.put("flowType", "立项管理慧采下单");
            formData.put("flowId", contractProjectOrderId);
            processStartF.setFormData(formData);
            processStartF.setBusinessKey(contractProjectOrderId);
            processStartF.setBusinessType("立项管理慧采下单");
            processStartF.setSuitableTargetType("PROJECT");
            processStartF.setSuitableTargetId(communityId);
            processStartF.setSuitableTargetName(initiationE.getCommunityName());
            log.info("立项管理慧采下单：发起审批流程入参,processStartF:{}", JSON.toJSONString(processStartF));
            try {
                String bpmProcInstId = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
                contractProjectOrderE.setBpmProcInstId(bpmProcInstId);
            } catch (Exception e) {
                log.info("流程发起异常：{}", e);
                log.error("流程发起异常：{}", e);
                throw new OwlBizException("流程发起超时，请稍后重试！");
            }

            int goodsCount = 0;
            BigDecimal orderAmountWithoutTax = BigDecimal.ZERO;
            List<ContractProjectOrderInfoV> contractProjectOrderInfoVS = new ArrayList<>();
            for (ReceiveOrderResp.SkuDTO item : req.getSku()) {
                goodsCount += item.getQuantity();

                BigDecimal totalAmount = NumberUtil.mul(item.getQuantity(), item.getPrice());
                BigDecimal totalAmountWithoutTax = NumberUtil.mul(item.getQuantity(), NumberUtil.sub(item.getPrice(), item.getPriceUnit()));
                orderAmountWithoutTax = NumberUtil.add(orderAmountWithoutTax, totalAmountWithoutTax);

                ContractProjectOrderInfoV contractProjectOrderInfoV = new ContractProjectOrderInfoV();
                contractProjectOrderInfoV
                        .setOrderNumber(req.getOrderNumber())
                        .setName(item.getDescription())
                        .setQuantity(item.getQuantity())
                        .setProductModel(item.getProductModel())
                        .setTaxRate(item.getTaxRate())
                        .setAmount(totalAmount)
                        .setTaxAmount(item.getPriceUnit())
                        .setAmountWithoutTax(totalAmountWithoutTax)
                        .setUnitPrice(item.getPrice())
                        .setUnit(item.getUnit())
                        .setOrderCreateTime(req.getOrderCreateTime())
                        .setOrderAccount(req.getOrderAccount());
                contractProjectOrderInfoVS.add(contractProjectOrderInfoV);
            }
            // 更新订单信息
            contractProjectOrderE
                    .setOrderNumber(req.getOrderNumber())
                    .setOrderAmount(req.getOrderAmount())
                    .setOrderAmountWithoutTax(orderAmountWithoutTax)
                    .setGoodsCount(goodsCount)
                    .setOrderCreateTime(req.getOrderCreateTime())
                    .setOrderAccount(req.getOrderAccount())
                    .setOrderStatus(0)
                    .setBpmReviewStatus(0)
                    .setGoodsInfo(JSON.toJSONString(contractProjectOrderInfoVS));

            contractProjectOrderAppService.updateById(contractProjectOrderE);

            // 更新立项的订单总金额
            BigDecimal newOrderTotalAmount = NumberUtil.add(initiationE.getOrderTotalAmount(), orderAmountWithoutTax);
            initiationE.setOrderTotalAmount(newOrderTotalAmount)
                    .setRemainingAmountWithoutTax(NumberUtil.sub(initiationE.getAmountWithoutTax(), newOrderTotalAmount));
            result = this.updateById(initiationE);
        } else {
            // 取消订单
            StartOrderForJDReqF startOrderForJDReqF = new StartOrderForJDReqF()
                    .setJdOrderId(req.getOrderNumber())
                    .setUserName(contractProjectOrderE.getJdHuiCaiUserName())
                    .setPwdMd5(contractProjectOrderE.getJdHuiCaiPwdMd5());
            log.info("ContractProjectInitiationAppService.projectInitiationOrderVerification startOrderForJDReqF:{}", JSON.toJSONString(startOrderForJDReqF));
            result = externalFeignClient.cancelOrder(startOrderForJDReqF);
            log.info("ContractProjectInitiationAppService.projectInitiationOrderVerification result:{}", JSON.toJSONString(result));
        }

        return result;
    }

    /**
     * 设置网关
     * @param userId
     * @param userName
     * @param tenantId
     */
    private static void setIdentityInfo(String userId, String userName, String tenantId) {
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setUserId(userId);
        identityInfo.setUserName(userName);
        identityInfo.setTenantId(tenantId);
        identityInfo.setGateway(GatewayTagEnum.社区运营平台网关.getTag());
        ThreadLocalUtil.set(Const.IDENTITY_INFO, identityInfo);
    }

    /**
     * 订单校验
     *
     * @param req
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean syncOrderStatus(DeliverReq req) {
        // //-- α.校验ID正确性
        // // String contractProjectOrderId = req.getPrCode();
        // ContractProjectOrderE contractProjectOrderE = contractProjectOrderAppService.getById(contractProjectOrderId);
        // if (Objects.isNull(contractProjectOrderE)) {
        //     throw new OwlBizException("根据订单ID检索数据失败");
        // }
        // Boolean result;
        return false;
    }

    /**
     * 获取费项映射关系树
     *
     * @return
     */
    public CostBaseResponse<List<CostItemNode>> getCodeMappingTree(String zjCode) {
        return externalFeignClient.getCodeMappingTree(zjCode);
    }

    /**
     * 查询月度分摊明细
     *
     * @return
     */
    public List<ContractProjectPlanMonthlyAllocationV> getMonthlyAllocationDetail(DynamicCostSurplusPropReqF req) {
        return contractProjectPlanMonthlyAllocationAppService.getMonthlyAllocationDetail(req);

    }

    /**
     * 查询项目下费项可用金额明细
     *
     * @return
     */
    public CostBaseResponse<DynamicCostSurplusInfo> getDynamicCostSurplusProp(DynamicCostSurplusPropReqF req) {
        LocalDate planStartTime = req.getPlanStartTime();
        LocalDate planEndTime = req.getPlanEndTime();
        List<String> yearList = IntStream.rangeClosed(
                        planStartTime.getYear(),
                        planEndTime.getYear()
                )
                .mapToObj(String::valueOf).collect(Collectors.toList());
        req.setYears(yearList.stream().collect(Collectors.joining(",")))
                .setMonth(planStartTime.getMonthValue());
        return externalFeignClient.getDynamicCostSurplusProp(req);
    }

    /**
     * 合约规划成本确认
     */
    public Boolean contractPlanConfirm(ContractProjectInitiationUpdateF req) {
        // 发起审批
        String projectInitiationId = req.getId();
        ContractProjectInitiationV contractProjectInitiation = this.getDetail(projectInitiationId);
        String communityId = contractProjectInitiation.getCommunityId();
        String tenantId = contractProjectInitiation.getTenantId();
        String revisedContentForBpm = null;
        String originalContentForBpm = null;

        Map<String, ContractProjectPlanCostConfirmV> planCostConfirmVMap =
                Optional.ofNullable(contractProjectInitiation.getCostConfirmList())
                        .orElse(Collections.emptyList())
                        .stream()
                        .collect(Collectors.groupingBy(
                                ContractProjectPlanCostConfirmV::getProjectInitiationId,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(Comparator.comparing(
                                                ContractProjectPlanCostConfirmV::getGmtCreate,
                                                Comparator.nullsFirst(Comparator.naturalOrder())
                                        )),
                                        optional -> optional.orElse(new ContractProjectPlanCostConfirmV())
                                )
                        ));

        Map<String, ContractProjectPlanV> contractProjectPlanVMap = contractProjectInitiation.getContractPlanList().stream()
                .collect(Collectors.toMap(ContractProjectPlanV::getId, e -> e));

        List<String> yearList = this.getYearList(contractProjectInitiation);

        if (CollectionUtils.isNotEmpty(req.getContractPlanList())) {
            // 保存成本确认明细
            StringJoiner revisedContent = new StringJoiner(";");
            req.getContractPlanList().stream().forEach(contractPlan -> {
                String contractPlanId = contractPlan.getId();
                ContractProjectPlanV originalContractProjectPlanV = contractProjectPlanVMap.get(contractPlanId);

                List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS =
                        contractProjectPlanMonthlyAllocationAppService.distributeCost(contractPlan.getConfirmAmount(), contractProjectInitiation.getPlanStartTime(), contractProjectInitiation.getPlanEndTime());

                // 保存成本确认月度分摊明细
                List<ContractProjectPlanMonthlyAllocationV> costConfirmMonthlyAllocationDetail = contractProjectPlanMonthlyAllocationAppService.getMonthlyAllocationDetail(
                        originalContractProjectPlanV.getMonthlyAllocationList(),
                        monthlyAllocationVS,
                        yearList);
                contractPlan.setCostConfirmMonthlyAllocationList(costConfirmMonthlyAllocationDetail);

                revisedContent.add(contractPlan.getContractPlanName() + ":" + NumberUtil.decimalFormat("#,##0.00", contractPlan.getConfirmAmount()) + "元");

            });

            ContractProjectPlanF contractProjectPlanF = req.getContractPlanList().get(0);

            ContractProjectPlanCostConfirmV originalCostConfirmV = planCostConfirmVMap.get(req.getId());

            ContractProjectPlanCostConfirmF contractProjectPlanCostConfirmInfoF = new ContractProjectPlanCostConfirmF()
                    .setProjectInitiationId(projectInitiationId)
                    .setContractProjectPlanId(contractProjectPlanF.getId())
                    .setOriginalContent(null == originalCostConfirmV ? "" : originalCostConfirmV.getRevisedContent())
                    .setBpmReviewStatus(BPMStatusEnum.已发起.getCode())
                    .setTenantId(tenantId)
                    .setType(0)
                    .setRevisedContent(revisedContent.toString());

            contractProjectPlanF.setCostConfirmList(Lists.newArrayList(contractProjectPlanCostConfirmInfoF));
            revisedContentForBpm = "确认金额：" + contractProjectPlanCostConfirmInfoF.getRevisedContent();
            originalContentForBpm = "确认金额：" + contractProjectPlanCostConfirmInfoF.getOriginalContent();
        }

        if (CollUtil.isNotEmpty(req.getContractList())) {
            // 保存合同关联明细
            for (ContractProjectPlanF contractPlan : req.getContractList()) {
                String contractPlanId = contractPlan.getId();
                ContractProjectPlanCostConfirmV originalCostConfirmV = planCostConfirmVMap.get(contractPlanId);

                ContractProjectPlanCostConfirmF contractProjectPlanCostConfirmInfoF = new ContractProjectPlanCostConfirmF()
                        .setProjectInitiationId(projectInitiationId)
                        .setOriginalContent(null == originalCostConfirmV ? "" : originalCostConfirmV.getRevisedContent())
                        .setBpmReviewStatus(BPMStatusEnum.已发起.getCode())
                        .setTenantId(tenantId)
                        .setType(1)
                        .setRevisedContent(contractPlan.getContractPlanName());

                contractPlan.setType(1).setTenantId(tenantId)
                        .setCostConfirmList(Lists.newArrayList(contractProjectPlanCostConfirmInfoF));

                revisedContentForBpm = "关联合同：" + contractProjectPlanCostConfirmInfoF.getRevisedContent();
                if (null != contractPlan.getAllocationAmount()) {
                    revisedContentForBpm += "，释放成本" + NumberUtil.decimalFormat("#,##0.00", contractPlan.getAllocationAmount()) + "元";
                }
                originalContentForBpm = "关联合同：" + contractProjectPlanCostConfirmInfoF.getOriginalContent();
            }
        }

        ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(communityId, ContractConcludeEnum.PROJECT_INITIATION_COST_CONFIRM.getCode());
        log.info("立项管理成本确认：获取审批规则,结果:{}", JSON.toJSONString(approveFilter));

        WflowModelHistorysV wflowModelHistorysV = bpmClient.getProcessModelByFormId(approveFilter.getApproveRule());
        log.info("立项管理成本确认：获取审批流程入参,wflowModelHistorysV:{},结果:{}", approveFilter.getApproveRule(), JSON.toJSONString(wflowModelHistorysV));

        if (ObjectUtil.isNull(wflowModelHistorysV)) {
            throw BizException.throw301("获取审批流程为空,请确认流程是否存在");
        }

        Map<String, Object> formData = this.formData(wflowModelHistorysV.getFormItems(), contractProjectInitiation, originalContentForBpm, revisedContentForBpm);
        formData.put("flowType", "立项管理成本确认");
        formData.put("flowId", projectInitiationId);

        ProcessStartF processStartF = new ProcessStartF();
        processStartF.setFormData(formData);
        processStartF.setBusinessKey(projectInitiationId);
        processStartF.setBusinessType("立项管理成本确认");
        processStartF.setSuitableTargetType("PROJECT");
        processStartF.setSuitableTargetId(communityId);
        processStartF.setSuitableTargetName(contractProjectInitiation.getCommunityName());
        log.info("立项管理成本确认：发起审批流程入参,processStartF:{}", JSON.toJSONString(processStartF));
        try {
            String bpmProcInstId = bpmClient.processStart(wflowModelHistorysV.getProcessDefId(), processStartF);
            req.setBpmProcInstId(bpmProcInstId)
                    .setBpmReviewStatus(BPMStatusEnum.已发起.getCode());

        } catch (Exception e) {
            log.info("流程发起异常：{}", e);
            log.error("流程发起异常：{}", e);
            throw new OwlBizException("流程发起超时，请稍后重试！");
        }

        this.updateForCostConfirm(req);

        return true;
    }

    /**
     * 封装审批流参数
     *
     * @return
     */
    private Map<String, Object> formData(String formItem, ContractProjectInitiationV initiation, String originalContent, String revisedContent) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        JSONArray formItems = JSONArray.parseArray(formItem);

        formItems.forEach(i -> {
            FormItem item = JSONObject.parseObject(i.toString(), FormItem.class);
            switch (item.getTitle()) {
                case "项目名称":
                    objectObjectHashMap.put(item.getId(), initiation.getCommunityName());
                    break;
                case "所属区域":
                    objectObjectHashMap.put(item.getId(), initiation.getRegion());
                    break;
                case "立项单号":
                    objectObjectHashMap.put(item.getId(), initiation.getProjectCode());
                    break;
                case "立项标题":
                    objectObjectHashMap.put(item.getId(), initiation.getProjectName());
                    break;
                case "立项类型\\分类":
                    if (StrUtil.isNotBlank(initiation.getSpecificCategory())) {
                        objectObjectHashMap.put(item.getId(), initiation.getInitiationType() + "\\" + initiation.getSpecificCategory());
                    } else {
                        objectObjectHashMap.put(item.getId(), initiation.getInitiationType());
                    }
                    break;
                case "立项不含税金额":
                    objectObjectHashMap.put(item.getId(), NumberUtil.decimalFormat("#,##0.00", initiation.getAmountWithoutTax()) + "元");
                    break;
                case "立项含税金额":
                    objectObjectHashMap.put(item.getId(), NumberUtil.decimalFormat("#,##0.00", initiation.getAmountWithTax()) + "元");
                    break;
                case "本次变更内容":
                    objectObjectHashMap.put(item.getId(), revisedContent);
                    break;
                case "上次变更内容":
                    objectObjectHashMap.put(item.getId(), originalContent);
                    break;
                case "立项事由":
                    objectObjectHashMap.put(item.getId(), initiation.getInitiationReason());
                    break;
                case "经办人":
                    objectObjectHashMap.put(item.getId(), initiation.getCreatorName());
                    break;
            }
        });
        return objectObjectHashMap;

    }

    private static @NotNull List<String> getYearList(ContractProjectInitiationV contractProjectInitiation) {
        List<String> yearList = IntStream.rangeClosed(
                        contractProjectInitiation.getPlanStartTime().getYear(),
                        contractProjectInitiation.getPlanEndTime().getYear()
                )
                .mapToObj(String::valueOf).collect(Collectors.toList());
        return yearList;
    }

}