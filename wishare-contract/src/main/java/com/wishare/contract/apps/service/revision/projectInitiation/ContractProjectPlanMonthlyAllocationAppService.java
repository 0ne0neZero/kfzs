package com.wishare.contract.apps.service.revision.projectInitiation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationF;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectPlanMonthlyAllocationE;
import com.wishare.contract.domains.enums.ContractErrMsgEnum;
import com.wishare.contract.domains.enums.CostControlTypeEnum;
import com.wishare.contract.domains.mapper.revision.projectInitiation.ContractProjectPlanMonthlyAllocationMapper;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanMonthlyAllocationV;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectPlanV;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.CostBaseResponse;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.DynamicCostSurplusInfo;
import com.wishare.contract.domains.vo.revision.projectInitiation.cost.DynamicCostSurplusPropReqF;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.utils.ErrorAssertUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * 合约规划月度分摊Service实现类
 */
@Slf4j
@Service
@AllArgsConstructor
public class ContractProjectPlanMonthlyAllocationAppService extends ServiceImpl<ContractProjectPlanMonthlyAllocationMapper, ContractProjectPlanMonthlyAllocationE> implements IOwlApiBase {

    private final ExternalFeignClient externalFeignClient;

    private final ContractProjectPlanMonthlyAllocationMapper contractProjectPlanMonthlyAllocationMapper;

    // 精度
    private static final int SCALE_INTERMEDIATE = 6;
    // 小数位数
    private static final int SCALE_FINAL = 2;

    public List<ContractProjectPlanMonthlyAllocationV> getByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectPlanMonthlyAllocationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getDeleted, 0);
        List<ContractProjectPlanMonthlyAllocationE> monthlyAllocationES = this.list(queryWrapper);
        return Global.mapperFacade.mapAsList(monthlyAllocationES, ContractProjectPlanMonthlyAllocationV.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(String projectInitiationId, String contractProjectPlanId, List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationList) {
        // 先删除原有的合约规划月度分摊
        this.deleteByContractProjectPlanId(contractProjectPlanId, 0);

        List<ContractProjectPlanMonthlyAllocationE> contractProjectPlanES = monthlyAllocationList.stream().map(e -> {
            ContractProjectPlanMonthlyAllocationE monthlyAllocationE = Global.mapperFacade.map(e, ContractProjectPlanMonthlyAllocationE.class);
            monthlyAllocationE
                    .setId(null)
                    .setProjectInitiationId(projectInitiationId)
                    .setContractProjectPlanId(contractProjectPlanId)
                    .setTenantId(tenantId());
            return monthlyAllocationE;
        }).collect(Collectors.toList());

        // 批量保存新的合约规划月度分摊
        return this.saveBatch(contractProjectPlanES);
    }

    /**
     * 保存成本确认月度分摊明细
     *
     * @param projectInitiationId
     * @param contractProjectPlanId
     * @param monthlyAllocationList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchForCostConfirm(String projectInitiationId, String contractProjectPlanId, List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationList) {
        // 物理删除上一次逻辑删除的合约规划月度分摊
        this.physicalDeletionByContractProjectPlanId(contractProjectPlanId, 1, 1);
        // 逻辑删除合约规划月度分摊, 为确认审批驳回做准备
        this.deleteByContractProjectPlanId(contractProjectPlanId, 1);

        List<ContractProjectPlanMonthlyAllocationE> contractProjectPlanES = monthlyAllocationList.stream().map(e -> {
            ContractProjectPlanMonthlyAllocationE monthlyAllocationE = Global.mapperFacade.map(e, ContractProjectPlanMonthlyAllocationE.class);
            monthlyAllocationE.setProjectInitiationId(projectInitiationId)
                    .setId(null)
                    .setContractProjectPlanId(contractProjectPlanId)
                    .setTenantId(tenantId());
            return monthlyAllocationE;
        }).collect(Collectors.toList());

        // 批量保存新的合约规划月度分摊
        return this.saveBatch(contractProjectPlanES);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectInitiationId(String projectInitiationId, Integer monthlyAllocationType) {
        LambdaQueryWrapper<ContractProjectPlanMonthlyAllocationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getMonthlyAllocationType, monthlyAllocationType);
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByContractProjectPlanId(String contractProjectPlanId, Integer monthlyAllocationType) {
        LambdaQueryWrapper<ContractProjectPlanMonthlyAllocationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getContractProjectPlanId, contractProjectPlanId);
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getMonthlyAllocationType, monthlyAllocationType);
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

    /**
     * 物理删除月度分摊, 成本确认审批驳回处理
     *
     * @param projectInitiationId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int physicalDeletionByProjectInitiationId(String projectInitiationId, Integer monthlyAllocationType, Integer deleted) {
        return contractProjectPlanMonthlyAllocationMapper.physicalDelete(projectInitiationId, monthlyAllocationType, deleted);
    }

    /**
     * 物理删除月度分摊, 成本确认审批驳回处理
     *
     * @param contractProjectPlanId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int physicalDeletionByContractProjectPlanId(String contractProjectPlanId, Integer monthlyAllocationType, Integer deleted) {
        return contractProjectPlanMonthlyAllocationMapper.physicalDeleteByContractProjectPlanId(contractProjectPlanId, monthlyAllocationType, deleted);
    }

    /**
     * 审批驳回, 删除最新的月度分摊, 恢复上一版逻辑删除的月度分摊
     *
     * @param projectInitiationId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean recovery(String projectInitiationId) {
        // 删除最新的月度分摊
        this.physicalDeletionByProjectInitiationId(projectInitiationId, 1, 0);

        // 恢复上一版逻辑删除的月度分摊
        return contractProjectPlanMonthlyAllocationMapper.update(projectInitiationId, 1, 1);

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectInitiationId(String projectInitiationId) {
        LambdaQueryWrapper<ContractProjectPlanMonthlyAllocationE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getProjectInitiationId, projectInitiationId);
        queryWrapper.eq(ContractProjectPlanMonthlyAllocationE::getDeleted, 0);

        return this.remove(queryWrapper);
    }

    /**
     * 查询项目下费项可用金额明细
     *
     * @return
     */
    public List<ContractProjectPlanMonthlyAllocationV> getMonthlyAllocationDetail(DynamicCostSurplusPropReqF req) {
        LocalDate planStartTime = req.getPlanStartTime();
        LocalDate planEndTime = req.getPlanEndTime();
        List<String> yearList = IntStream.rangeClosed(
                        planStartTime.getYear(),
                        planEndTime.getYear()
                )
                .mapToObj(String::valueOf).collect(Collectors.toList());


        long monthsBetween = ChronoUnit.MONTHS.between(
                YearMonth.from(planStartTime),
                YearMonth.from(planEndTime)
        );

        LongStream.rangeClosed(0, monthsBetween)
                .mapToObj(month -> YearMonth.from(planStartTime).plusMonths(month))
                .collect(Collectors.toList());

        List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = this.distributeCost(req.getAllocationAmount(), planStartTime, planEndTime);

        ContractProjectPlanV contractProjectPlanV = new ContractProjectPlanV()
                .setAllocationAmount(req.getAllocationAmount())
                .setCostChargeItemCode(req.getCbCode())
                .setMonthlyAllocationList(monthlyAllocationVS);

        return this.getMonthlyAllocationDetail(contractProjectPlanV, req.getMdmId(), yearList, planEndTime.getMonthValue(), req.getVerificationFlag());

    }

    /**
     * 校验合约规划可用余额是否满足当前立项金额
     * -- 年控,则当年金额不超过即可
     * -- 月控,则每个月的金额都不能超过合约规划金额,
     *
     * @param contractPlan
     * @param communityId
     * @param yearList
     * @param verificationFlag 是否校验
     */
    public List<ContractProjectPlanMonthlyAllocationV> getMonthlyAllocationDetail(ContractProjectPlanV contractPlan, String communityId, List<String> yearList, Integer monthValue, Boolean verificationFlag) {
        List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = new ArrayList<>();

        Map<String, DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO> dynamicCostSurplusDetailMap = this.getDynamicCostSurplusPropMap(yearList, monthValue, communityId, contractPlan);

        Map<String, ContractProjectPlanMonthlyAllocationV> monthlyAllocationVMap = contractPlan.getMonthlyAllocationList().stream()
                .filter(e -> e.getType().equals(1))
                .collect(Collectors.toMap(ContractProjectPlanMonthlyAllocationV::getYear, e -> e));

        yearList.stream().forEach(year -> {
            ContractProjectPlanMonthlyAllocationV monthlyAllocationType0 = new ContractProjectPlanMonthlyAllocationV();
            String dynamicCostGuid = null;
            String costControlTypeName = null;
            Integer costControlTypeEnum = null;

            ContractProjectPlanMonthlyAllocationV monthlyAllocationType1 = monthlyAllocationVMap.getOrDefault(year, new ContractProjectPlanMonthlyAllocationV());

            // 如果当年合约规划不为空
            DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO dynamicCostSurplusDetailDTO = dynamicCostSurplusDetailMap.get(year);
            if (Objects.nonNull(dynamicCostSurplusDetailDTO)) {

                DynamicCostSurplusInfo.DynamicCostSurplusDTO dynamicCostSurplus = dynamicCostSurplusDetailDTO.getDynamicCostSurplus();

                if (verificationFlag) {
                    if (CostControlTypeEnum.YEAR.getCode().equals(dynamicCostSurplusDetailDTO.getCostControlTypeEnum())) {
                        // 年控,则当年金额不超过即可
                        this.amountCheck(monthlyAllocationType1.getYearSurplus(), dynamicCostSurplus.getYearSurplus());

                    } else if (CostControlTypeEnum.MONTH.getCode().equals(dynamicCostSurplusDetailDTO.getCostControlTypeEnum())) {
                        // 月控,则每个月的金额都不能超过合约规划金额
                        this.amountCheck(monthlyAllocationType1.getJanSurplus(), dynamicCostSurplus.getJanSurplus());
                        this.amountCheck(monthlyAllocationType1.getFebSurplus(), dynamicCostSurplus.getFebSurplus());
                        this.amountCheck(monthlyAllocationType1.getMarSurplus(), dynamicCostSurplus.getMarSurplus());
                        this.amountCheck(monthlyAllocationType1.getAprSurplus(), dynamicCostSurplus.getAprSurplus());
                        this.amountCheck(monthlyAllocationType1.getMaySurplus(), dynamicCostSurplus.getMaySurplus());
                        this.amountCheck(monthlyAllocationType1.getJunSurplus(), dynamicCostSurplus.getJunSurplus());
                        this.amountCheck(monthlyAllocationType1.getJulSurplus(), dynamicCostSurplus.getJulSurplus());
                        this.amountCheck(monthlyAllocationType1.getAugSurplus(), dynamicCostSurplus.getAugSurplus());
                        this.amountCheck(monthlyAllocationType1.getSepSurplus(), dynamicCostSurplus.getSepSurplus());
                        this.amountCheck(monthlyAllocationType1.getOctSurplus(), dynamicCostSurplus.getOctSurplus());
                        this.amountCheck(monthlyAllocationType1.getNovSurplus(), dynamicCostSurplus.getNovSurplus());
                        this.amountCheck(monthlyAllocationType1.getDecSurplus(), dynamicCostSurplus.getDecSurplus());
                    }
                }

                monthlyAllocationType0 = Global.mapperFacade.map(dynamicCostSurplus, ContractProjectPlanMonthlyAllocationV.class);
                dynamicCostGuid = dynamicCostSurplusDetailDTO.getDynamicCostGuid();
                costControlTypeName =  dynamicCostSurplusDetailDTO.getCostControlTypeName();
                costControlTypeEnum = dynamicCostSurplusDetailDTO.getCostControlTypeEnum();
            }
            // 补充 可用金额 和 分摊后剩余金额
            monthlyAllocationType0
                    .setYear(year).setType(0)
                    .setMonthlyAllocationType(0)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum);

            monthlyAllocationType1
                    .setYear(year).setType(1)
                    .setMonthlyAllocationType(0)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum);;

            ContractProjectPlanMonthlyAllocationV monthlyAllocationType2 = new ContractProjectPlanMonthlyAllocationV()
                    .setYear(year).setType(2)
                    .setMonthlyAllocationType(0)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum)
                    .calcMonthlyAllocationCalculation(monthlyAllocationType0, monthlyAllocationType1);

            monthlyAllocationVS.add(monthlyAllocationType0);
            monthlyAllocationVS.add(monthlyAllocationType1);
            monthlyAllocationVS.add(monthlyAllocationType2);
        });
        return monthlyAllocationVS;
    }

    /**
     * 组装成本确认的月度分摊明细
     *
     * @param originalMonthlyAllocationList 合约规划月度分摊
     * @param newMonthlyAllocationVS 成本确认 分摊金额 月度分摊
     * @param yearList
     * @return
     */
    public List<ContractProjectPlanMonthlyAllocationV> getMonthlyAllocationDetail(List<ContractProjectPlanMonthlyAllocationV> originalMonthlyAllocationList, List<ContractProjectPlanMonthlyAllocationV> newMonthlyAllocationVS, List<String> yearList) {
        List<ContractProjectPlanMonthlyAllocationV> monthlyAllocationVS = new ArrayList<>();

        Map<String, Map<Integer, ContractProjectPlanMonthlyAllocationV>> monthlyAllocationVMap = originalMonthlyAllocationList.stream()
                .collect(Collectors.groupingBy(
                        ContractProjectPlanMonthlyAllocationV::getYear,
                        Collectors.toMap(ContractProjectPlanMonthlyAllocationV::getType, e -> e)
                ));

        Map<String, ContractProjectPlanMonthlyAllocationV> newMonthlyAllocationVMap = newMonthlyAllocationVS.stream()
                .collect(Collectors.toMap(ContractProjectPlanMonthlyAllocationV::getYear, e -> e));

        yearList.stream().forEach(year -> {
            String dynamicCostGuid = null;
            String costControlTypeName = null;
            Integer costControlTypeEnum = null;

            Map<Integer, ContractProjectPlanMonthlyAllocationV> monthlyAllocationVYearMap = monthlyAllocationVMap.getOrDefault(year, new HashMap<>());
            ContractProjectPlanMonthlyAllocationV monthlyAllocationType1 = newMonthlyAllocationVMap.getOrDefault(year, new ContractProjectPlanMonthlyAllocationV());

            ContractProjectPlanMonthlyAllocationV monthlyAllocationType0 = monthlyAllocationVYearMap.getOrDefault(0, new ContractProjectPlanMonthlyAllocationV());
            ContractProjectPlanMonthlyAllocationV originalMonthlyAllocationType1 = monthlyAllocationVYearMap.getOrDefault(1, new ContractProjectPlanMonthlyAllocationV());
            ContractProjectPlanMonthlyAllocationV monthlyAllocationType2 = monthlyAllocationVYearMap.getOrDefault(2, new ContractProjectPlanMonthlyAllocationV());

            if (Objects.nonNull(monthlyAllocationType2)) {
                dynamicCostGuid = monthlyAllocationType2.getDynamicCostGuid();
                costControlTypeName =  monthlyAllocationType2.getCostControlTypeName();
                costControlTypeEnum = monthlyAllocationType2.getCostControlTypeEnum();
            }
            // 补充 可用金额 和 分摊后剩余金额
            monthlyAllocationType0
                    .setYear(year).setType(0)
                    .setMonthlyAllocationType(1)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum);

            monthlyAllocationType1
                    .setYear(year).setType(1)
                    .setMonthlyAllocationType(1)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum);

            ContractProjectPlanMonthlyAllocationV newMonthlyAllocationType2 = new ContractProjectPlanMonthlyAllocationV()
                    .setYear(year).setType(2)
                    .setMonthlyAllocationType(1)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum)
                    .calcMonthlyAllocationCalculation(monthlyAllocationType0, monthlyAllocationType1);

            originalMonthlyAllocationType1
                    .setYear(year).setType(3)
                    .setMonthlyAllocationType(1)
                    .setDynamicCostGuid(dynamicCostGuid)
                    .setCostControlTypeName(costControlTypeName)
                    .setCostControlTypeEnum(costControlTypeEnum);

            monthlyAllocationVS.add(monthlyAllocationType0);
            monthlyAllocationVS.add(monthlyAllocationType1);
            monthlyAllocationVS.add(newMonthlyAllocationType2);
            monthlyAllocationVS.add(originalMonthlyAllocationType1);
        });
        return monthlyAllocationVS;
    }

    /**
     * 立项金额和成本系统合约规划管控可用金额校验
     *
     * @param contractMonthlyAmount 立项月份金额
     * @param costMonthlyAmount     成本月份金额
     */
    private void amountCheck(BigDecimal contractMonthlyAmount, BigDecimal costMonthlyAmount) {
        if (Objects.isNull(contractMonthlyAmount) || Objects.isNull(costMonthlyAmount)) {
            return;
        }
        ErrorAssertUtil.isFalseThrow402(NumberUtil.isGreater(contractMonthlyAmount, costMonthlyAmount), ContractErrMsgEnum.CONTRACT_PROJECT_INITIATION_AMOUNT_EXCEEDS);
    }

    /**
     * 查询项目下费项可用金额明细
     *
     * @return
     */
    public Map<String, DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO> getDynamicCostSurplusPropMap(List<String> yearList, Integer monthValue, String communityId, ContractProjectPlanV contractProjectPlanV) {
        DynamicCostSurplusPropReqF dynamicCostSurplusPropReqF = new DynamicCostSurplusPropReqF()
                .setMdmId(communityId)
                .setCbCode(contractProjectPlanV.getCostChargeItemCode())
                .setYears(yearList.stream().collect(Collectors.joining(",")))
                .setSourceType(1)
                .setMonth(monthValue);

        log.info("ContractProjectPlanMonthlyAllocationAppService.getDynamicCostSurplusPropMap:{}", JSON.toJSONString(dynamicCostSurplusPropReqF));
        CostBaseResponse<DynamicCostSurplusInfo> dynamicCostSurplusProp = externalFeignClient.getDynamicCostSurplusProp(dynamicCostSurplusPropReqF);
        log.info("ContractProjectPlanMonthlyAllocationAppService.getDynamicCostSurplusPropMap returned dynamicCostSurplusProp:{}", JSON.toJSONString(dynamicCostSurplusProp));

        Map<String, DynamicCostSurplusInfo.DynamicCostSurplusDetailDTO> dynamicCostSurplusDetailMap =
                Optional.ofNullable(dynamicCostSurplusProp)
                        .map(CostBaseResponse::getData)
                        .map(DynamicCostSurplusInfo::getList)
                        .map(list -> list.stream()
                                .filter(ObjectUtils::isNotEmpty) // 过滤空对象
                                .collect(Collectors.toMap(
                                        item -> ObjectUtils.defaultIfNull(item.getBudgetYear(), "未知年份"),
                                        Function.identity(),
                                        (existing, replacement) -> existing
                                )))
                        .orElse(Collections.emptyMap());

        return dynamicCostSurplusDetailMap;
    }

    /**
     * 根据提供的规则计算月度成本分摊。
     * 月度成本自动分摊规则：合同清单项不含税金额，自动分摊到合同周期内每个月的规则
     * 1、月金额 = 合同金额 / （完整月数 + 不完整月履约天数 / 当月天数）、向下取整；
     * 2、如果不完整月在前且结束时间为月底最后一天或不完整月在后且开始时间为月初第一天，【倒减法】此月金额=合同金额-所有完整月数合计；
     * 3、如果不完整月前后都存在，月前的补完整金额按天计算金额(即履约金额=月金额*(履约天数/当月天数))，【到减法】月后的不完整月使用到减法计算金额
     *
     * @param allocationAmount 。
     * @param planStartTime    合同开始日期。
     * @param planEndTime      合同结束日期。
     * @return 合同期内每年的 ContractProjectPlanMonthlyAllocationV 列表。
     */
    public List<ContractProjectPlanMonthlyAllocationV> distributeCost(BigDecimal allocationAmount, LocalDate planStartTime, LocalDate planEndTime) {

        // --- 1. 计算合同持续时间指标 (分母) ---

        // 计算合同总月数 (从起始月1号到结束月1号，然后 +1)
        long totalMonthsCount = ChronoUnit.MONTHS.between(planStartTime.withDayOfMonth(1), planEndTime.withDayOfMonth(1)) + 1;

        // 判断起始月和结束月是否为完整月
        boolean firstMonthIsComplete = (planStartTime.getDayOfMonth() == 1);
        LocalDate lastDayOfEndMonth = planEndTime.with(TemporalAdjusters.lastDayOfMonth());
        boolean lastMonthIsComplete = (planEndTime.getDayOfMonth() == lastDayOfEndMonth.getDayOfMonth());

        // 计算完整月数量
        long completeContractMonthsCount = totalMonthsCount;

        if (!firstMonthIsComplete) {
            completeContractMonthsCount--;
        }
        if (totalMonthsCount > 1 && !lastMonthIsComplete) {
            completeContractMonthsCount--;
        }
        // 单月合同的特殊处理：如果只有一个月且不完整，则完整月数为0
        if (totalMonthsCount == 1 && (!firstMonthIsComplete || !lastMonthIsComplete)) {
            completeContractMonthsCount = 0;
        }

        BigDecimal completeMonths = new BigDecimal(completeContractMonthsCount);

        // --- 计算不完整月份的履约天数比例之和 ---
        BigDecimal incompleteMonthRatioSum = BigDecimal.ZERO;
        // 存储不完整月的比例，供按天计算使用
        Map<LocalDate, BigDecimal> incompleteMonthRatios = new HashMap<>();

        // 1. 起始不完整月 (如果存在)
        if (!firstMonthIsComplete) {
            LocalDate monthStart = planStartTime.withDayOfMonth(1);
            long contractDays = planStartTime.lengthOfMonth() - planStartTime.getDayOfMonth() + 1;
            BigDecimal totalDaysInMonth = new BigDecimal(planStartTime.lengthOfMonth());
            BigDecimal ratio = new BigDecimal(contractDays).divide(totalDaysInMonth, SCALE_INTERMEDIATE, RoundingMode.HALF_UP);
            incompleteMonthRatioSum = incompleteMonthRatioSum.add(ratio);
            incompleteMonthRatios.put(monthStart, ratio);
        }

        // 2. 结束不完整月 (如果存在，且不是起始月本身)
        if (totalMonthsCount > 1 && !lastMonthIsComplete) {
            LocalDate monthStart = planEndTime.withDayOfMonth(1);
            long contractDays = planEndTime.getDayOfMonth();
            BigDecimal totalDaysInMonth = new BigDecimal(planEndTime.lengthOfMonth());
            BigDecimal ratio = new BigDecimal(contractDays).divide(totalDaysInMonth, SCALE_INTERMEDIATE, RoundingMode.HALF_UP);
            incompleteMonthRatioSum = incompleteMonthRatioSum.add(ratio);
            incompleteMonthRatios.put(monthStart, ratio);
        } else if (totalMonthsCount == 1 && !firstMonthIsComplete) {
            // 3. 单个月份合同且不完整
            LocalDate monthStart = planStartTime.withDayOfMonth(1);
            long contractDays = ChronoUnit.DAYS.between(planStartTime, planEndTime) + 1;
            BigDecimal totalDaysInMonth = new BigDecimal(planStartTime.lengthOfMonth());
            BigDecimal ratio = new BigDecimal(contractDays).divide(totalDaysInMonth, SCALE_INTERMEDIATE, RoundingMode.HALF_UP);
            if (!incompleteMonthRatios.containsKey(monthStart)) {
                incompleteMonthRatioSum = incompleteMonthRatioSum.add(ratio);
                incompleteMonthRatios.put(monthStart, ratio);
            }
        }


        // 分母: (完整月数 + 不完整月履约天数 / 当月天数)
        BigDecimal denominator = completeMonths.add(incompleteMonthRatioSum);
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return new ArrayList<>();
        }

        // --- 2. 计算基础月金额 (规则 1) ---
        // 月金额 = 合同金额 / Denominator, 向下取整 (Truncate) 到 SCALE_FINAL (2)
        BigDecimal baseAmountHighPrecision = allocationAmount.divide(denominator, SCALE_INTERMEDIATE, RoundingMode.HALF_UP);
        // 规则 1 要求向下取整
        BigDecimal baseMonthlyAmount = baseAmountHighPrecision.setScale(SCALE_FINAL, RoundingMode.DOWN);


        // --- 3. 逐月分配金额 ---
        Map<LocalDate, BigDecimal> monthlyAmounts = new HashMap<>();
        LocalDate contractEndMonth = planEndTime.withDayOfMonth(1);

        // 标记哪个月份将使用【倒减法】
        LocalDate reverseSubtractionMonth = null;

        // --- 确定【倒减法】月份 (规则 2 & 3 及精度修正) ---

        // 1. **全完整月合同**：将最后一个月设为【倒减法】月，以吸收所有向下取整造成的精度损失。
        if (firstMonthIsComplete && lastMonthIsComplete) {
            reverseSubtractionMonth = contractEndMonth;
        }
        // 2. **边界不完整月合同**
        // A. 如果前后都不完整 (Rule 3)，【倒减法】月为月末不完整月
        else if (!firstMonthIsComplete && !lastMonthIsComplete) {
            reverseSubtractionMonth = contractEndMonth;
        }
        // B. 如果只有前不完整，且结束时间是月底 (Rule 2)，【倒减法】月为起始月
        else if (!firstMonthIsComplete && lastMonthIsComplete) {
            reverseSubtractionMonth = planStartTime.withDayOfMonth(1);
        }
        // C. 如果只有后不完整，且开始时间是月初 (Rule 2)，【倒减法】月为结束月
        else if (firstMonthIsComplete && !lastMonthIsComplete) {
            reverseSubtractionMonth = contractEndMonth;
        }
        // D. 单月合同且不完整 
        else if (totalMonthsCount == 1 && (!firstMonthIsComplete || !lastMonthIsComplete)) {
            reverseSubtractionMonth = contractEndMonth;
        }


        LocalDate currentMonth = planStartTime.withDayOfMonth(1);

        // 循环所有合同覆盖的月份
        while (!currentMonth.isAfter(contractEndMonth)) {

            BigDecimal amountForMonth = BigDecimal.ZERO;

            // 确定当前月是否是边界不完整月
            boolean isIncompleteStart = currentMonth.isEqual(planStartTime.withDayOfMonth(1)) && !firstMonthIsComplete;
            boolean isIncompleteEnd = currentMonth.isEqual(contractEndMonth) && !lastMonthIsComplete;

            // 当前月是否为完整月
            boolean isCompleteMonth = !isIncompleteStart && !isIncompleteEnd;
            if (totalMonthsCount == 1 && (isIncompleteStart || isIncompleteEnd)) {
                isCompleteMonth = false;
            }

            // 确定当前月是否为【倒减法】月
            boolean isReverseMonth = currentMonth.isEqual(reverseSubtractionMonth);

            if (isCompleteMonth) {
                // 完整月
                if (isReverseMonth) {
                    amountForMonth = BigDecimal.ZERO.setScale(SCALE_FINAL); // 设为 0，等待倒减法计算
                } else {
                    amountForMonth = baseMonthlyAmount;
                }

            } else if (isIncompleteStart) {
                // 不完整起始月
                if (isReverseMonth) {
                    amountForMonth = BigDecimal.ZERO.setScale(SCALE_FINAL); // 等待倒减法
                } else {
                    // 按天计算 (Rule 3 前部分)
                    BigDecimal ratio = incompleteMonthRatios.getOrDefault(currentMonth, BigDecimal.ZERO);
                    amountForMonth = baseMonthlyAmount.multiply(ratio).setScale(SCALE_FINAL, RoundingMode.HALF_UP);
                }
            } else if (isIncompleteEnd) {
                // 不完整结束月 (必然是【倒减法】月)
                amountForMonth = BigDecimal.ZERO.setScale(SCALE_FINAL); // 占位，等待计算
            }

            // 仅对非零金额或需要倒减法的月份进行记录
            if (amountForMonth.compareTo(BigDecimal.ZERO) > 0 || isReverseMonth) {
                monthlyAmounts.put(currentMonth, amountForMonth);
            }

            currentMonth = currentMonth.plusMonths(1);
        }


        // --- 4. 执行【倒减法】(规则 2 & 3) ---
        if (reverseSubtractionMonth != null) {
            // 计算截止到目前已分配的总金额 (排除倒减法月，因为其金额可能是 0)
            LocalDate finalReverseSubtractionMonth = reverseSubtractionMonth;
            BigDecimal sumBeforeReverse = monthlyAmounts.entrySet().stream()
                    .filter(e -> !e.getKey().equals(finalReverseSubtractionMonth))
                    .map(Map.Entry::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(SCALE_FINAL, RoundingMode.HALF_UP);

            // 剩余金额 (应分配给【倒减法】月)
            BigDecimal remainingAmount = allocationAmount.subtract(sumBeforeReverse);

            // 将剩余金额分配给【倒减法】月
            monthlyAmounts.put(reverseSubtractionMonth, remainingAmount.setScale(SCALE_FINAL, RoundingMode.HALF_UP));
        }

        // --- 5. 最终分组和映射到输出结构 ---
        Map<String, List<Map.Entry<LocalDate, BigDecimal>>> amountsByYear = monthlyAmounts.entrySet().stream()
                .collect(Collectors.groupingBy(e -> String.valueOf(e.getKey().getYear())));

        List<ContractProjectPlanMonthlyAllocationV> result = new ArrayList<>();

        for (Map.Entry<String, List<Map.Entry<LocalDate, BigDecimal>>> entry : amountsByYear.entrySet()) {
            ContractProjectPlanMonthlyAllocationV distribution = new ContractProjectPlanMonthlyAllocationV();
            distribution.setYear(entry.getKey())
                    .setType(1);
            BigDecimal yearTotal = BigDecimal.ZERO;

            for (Map.Entry<LocalDate, BigDecimal> monthEntry : entry.getValue()) {
                int month = monthEntry.getKey().getMonthValue();
                BigDecimal amount = monthEntry.getValue();
                distribution.setMonthSurplus(month, amount); // setMonthAmount 中包含setScale(2)
                yearTotal = yearTotal.add(amount);
            }
            // 确保年度合计也保留两位小数
            distribution.setYearSurplus(yearTotal.setScale(SCALE_FINAL, RoundingMode.HALF_UP));
            result.add(distribution);
        }

        // 确保年份按时间顺序排列
        result.sort((a, b) -> a.getYear().compareTo(b.getYear()));

        return result;
    }

}