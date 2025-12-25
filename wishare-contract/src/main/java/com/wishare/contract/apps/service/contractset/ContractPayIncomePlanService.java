package com.wishare.contract.apps.service.contractset;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.revision.income.ContractSrfxxF;
import com.wishare.contract.apps.fo.revision.income.PayIncomeListQuery;
import com.wishare.contract.domains.bo.CommonRangeAmountBO;
import com.wishare.contract.domains.consts.SettlementStatusEnum;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.income.fund.ContractIncomeFundE;
import com.wishare.contract.domains.enums.IriStatusEnum;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.domains.mapper.contractset.ContractPayIncomePlanMapper;
import com.wishare.contract.domains.service.revision.common.CommonRangeAmountService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeExpandService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.income.fund.ContractIncomeFundService;
import com.wishare.contract.domains.vo.revision.income.PayIncomeListV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ContractPayIncomePlanService  extends ServiceImpl<ContractPayIncomePlanMapper, PayIncomePlanE> implements IOwlApiBase {

    @Resource
    private ContractIncomeConcludeExpandService contractIncomeConcludeExpandService;
    @Resource
    private ContractPayIncomePlanMapper payIncomePlanMapper;

    @Resource
    @Lazy
    private ContractIncomeFundService contractIncomeFundService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static String SRJH_PLAN_CODE_KEY = "PLAN:SRJH:CODE";
    @Resource
    @Lazy
    private ContractIncomeConcludeService contractIncomeConcludeService;
    @Autowired
    private CommonRangeAmountService commonRangeAmountService;

    public List<PayIncomePlanE> generateIncomePlans(ContractIncomePlanConcludeE contractIncomePlanConclude, ContractIncomeConcludeE incomeConcludeE, Boolean isSave) {
        List<PayIncomePlanE> incomePlans = new ArrayList<>();

        LocalDate startDate = contractIncomePlanConclude.getCostStartTime();
        LocalDate endDate = contractIncomePlanConclude.getCostEndTime();
        BigDecimal totalAmount = contractIncomePlanConclude.getPlannedCollectionAmount();
        //BigDecimal remainingAmount = totalAmount;

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        LocalDate currentStartDate = startDate;
        LocalDate currentEndDate;
        CommonRangeAmountBO rangrAmount = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),startDate,endDate,totalAmount);
        currentEndDate = currentStartDate.withDayOfMonth(currentStartDate.lengthOfMonth());
        if (currentEndDate.isAfter(endDate)) {
            currentEndDate = endDate;
        }
        //BigDecimal firstPeriodAmount = calculateAmount(contractIncomePlanConclude, currentStartDate, currentEndDate, totalDays, remainingAmount);
        incomePlans.add(createIncomePlan(contractIncomePlanConclude, currentStartDate, currentEndDate, rangrAmount.getStartMonthAmount()));
        //remainingAmount = remainingAmount.subtract(firstPeriodAmount);

        currentStartDate = currentEndDate.plusDays(1);
        while (!currentStartDate.isAfter(endDate)) {
            currentEndDate = currentStartDate.withDayOfMonth(currentStartDate.lengthOfMonth());
            if (currentEndDate.isAfter(endDate)) {
                currentEndDate = endDate;
            }
            BigDecimal monthlyAmount = BigDecimal.ZERO;
            // 如果是最后一条（下一次循环会退出）
            if (currentEndDate.equals(endDate)) {
                monthlyAmount = rangrAmount.getEndMonthAmount();
            }else{
                monthlyAmount = rangrAmount.getAvgMonthAmount();
            }

            //BigDecimal monthlyAmount = calculateAmount(contractIncomePlanConclude, currentStartDate, currentEndDate, totalDays, remainingAmount);
            incomePlans.add(createIncomePlan(contractIncomePlanConclude, currentStartDate, currentEndDate, monthlyAmount));
            //remainingAmount = remainingAmount.subtract(monthlyAmount);

            currentStartDate = currentEndDate.plusDays(1);
        }

        /*if (remainingAmount.compareTo(BigDecimal.ZERO) > 0 && !incomePlans.isEmpty()) {
            PayIncomePlanE lastPlan = incomePlans.get(incomePlans.size() - 1);
            lastPlan.setPlannedCollectionAmount(lastPlan.getPlannedCollectionAmount().add(remainingAmount).setScale(2,BigDecimal.ROUND_HALF_UP));
        }*/

        if (CollectionUtils.isNotEmpty(incomePlans)) {
/*            ContractZffxxF contractZffxxF = new ContractZffxxF();
            List<ContractIncomeConcludeExpandE> contractIncomeConcludeExpandEList = contractIncomeConcludeExpandService.lambdaQuery().eq(ContractIncomeConcludeExpandE::getContractId,contractIncomePlanConclude.getContractId()).list();
            if (CollectionUtils.isNotEmpty(contractIncomeConcludeExpandEList)) {
                String fkdwxxStr = contractIncomeConcludeExpandEList.get(0).getFkdwxx();
                if (StringUtils.isNotBlank(fkdwxxStr)) {
                    List<ContractZffxxF> fkdwxx = JSONObject.parseArray(fkdwxxStr,ContractZffxxF.class);
                    contractZffxxF = fkdwxx.get(0);
                }
                //
            }*/
            List<ContractIncomeFundE> contractIncomeFundEList = contractIncomeFundService.lambdaQuery().eq(ContractIncomeFundE::getId,contractIncomePlanConclude.getContractPayFundId()).list();
            IdentityInfo identityInfo = curIdentityInfo();
            //ContractZffxxF finalContractZffxxF = contractZffxxF;

            String draweeId = "";
            String draweeName = "";
            List<ContractIncomeConcludeExpandE> contractIncomeConcludeExpandES = contractIncomeConcludeExpandService.lambdaQuery().eq(ContractIncomeConcludeExpandE::getContractId, incomeConcludeE.getId()).list();

            if (CollectionUtils.isNotEmpty(contractIncomeConcludeExpandES)) {
                ContractIncomeConcludeExpandE contractIncomeConcludeExpandE = contractIncomeConcludeExpandES.get(0);
                String fkdwxx = contractIncomeConcludeExpandE.getFkdwxx();
                if (StringUtils.isNotBlank(fkdwxx)) {
                    List<ContractSrfxxF> fkdxxOrsrfxx = JSONObject.parseArray(fkdwxx, ContractSrfxxF.class);
                    if(CollectionUtils.isNotEmpty(fkdxxOrsrfxx)) {
                        ContractSrfxxF srf = fkdxxOrsrfxx.get(0);
                        draweeId = StringUtils.isNotBlank(srf.getTruedraweeid()) ? srf.getTruedraweeid() : srf.getDraweeid();
                        draweeName = StringUtils.isNotBlank(srf.getTruedrawee()) ? srf.getTruedrawee() : srf.getDrawee();
                    }
                }
            }

            BigDecimal tempNoTaxAmount = BigDecimal.ZERO;
            for (int i = 0; i < incomePlans.size(); i++) {
                PayIncomePlanE income = incomePlans.get(i);
                income.setTenantId(identityInfo.getTenantId());
                income.setCreator(identityInfo.getUserId());
                income.setCreatorName(identityInfo.getUserName());
                income.setGmtCreate(LocalDateTime.now());
                income.setBillCreationTime(LocalDateTime.now());
                income.setOperator(identityInfo.getUserName());
                income.setOurParty(incomeConcludeE.getOurParty());
                income.setOurPartyId(incomeConcludeE.getOurPartyId());
                income.setBillSource("收入合同");
                income.setPushStatus(0);
                income.setTaxRate(contractIncomePlanConclude.getTaxRate());
                income.setTaxRateId(String.valueOf(contractIncomePlanConclude.getTaxRateId()));
                income.setDrawee("往来单位");
                income.setDraweeId(draweeId);
                income.setDraweeName(draweeName);
                income.setPlanId(contractIncomePlanConclude.getId());
                income.setPayFundId(contractIncomePlanConclude.getContractPayFundId());
                income.setIriStatus(IriStatusEnum.no.getCode());
                income.setTermDate(contractIncomePlanConclude.getTermDate());
                income.setSettlementStatus(SettlementStatusEnum.未确收.getCode());
                income.setPaymentAmount(BigDecimal.ZERO);
                if (CollectionUtils.isNotEmpty(contractIncomeFundEList)) {
                    income.setServiceType(contractIncomeFundEList.get(0).getType());
                    income.setPayType(contractIncomeFundEList.get(0).getPayType());
                    income.setPayTypeId(contractIncomeFundEList.get(0).getPayTypeId());
                    income.setChargeItem(contractIncomeFundEList.get(0).getChargeItem());
                    income.setAmountNum(contractIncomeFundEList.get(0).getAmountNum());
                }

                if (!"差额纳税".equals(contractIncomePlanConclude.getTaxRate())) {
                    BigDecimal taxRateDecimal = parseTaxRate(contractIncomePlanConclude.getTaxRate());
                    BigDecimal noTaxAmount;
                    if (i != (incomePlans.size()-1)) {
//                        taxAmount = income.getPlannedCollectionAmount().multiply(taxRateDecimal).setScale(2, BigDecimal.ROUND_HALF_UP);
                        noTaxAmount = income.getPlannedCollectionAmount().divide(BigDecimal.ONE.add(taxRateDecimal),2, RoundingMode.HALF_UP);
                        tempNoTaxAmount = tempNoTaxAmount.add(noTaxAmount);
                    }else{
                        noTaxAmount = contractIncomePlanConclude.getNoTaxAmount().subtract(tempNoTaxAmount);
                    }
                    income.setNoTaxAmount(noTaxAmount);
                    income.setOriginNoTaxAmount(noTaxAmount);
                    BigDecimal taxAmount = income.getPlannedCollectionAmount().subtract(noTaxAmount);
                    income.setTaxAmount(taxAmount);
                    income.setOriginTaxAmount(taxAmount);
                }
                income.setConfirmedAmountReceivedTemp(income.getPlannedCollectionAmount());
                //收入计划规则：SRJH-应结算日期-CT码-两位自增数【CBYG-20241131-CT码-01】
                income.setIncomePlanCode(generatePlanCode(SRJH_PLAN_CODE_KEY,"" + income.getPlannedCollectionTime().getYear() + income.getPlannedCollectionTime().getMonthValue() + income.getPlannedCollectionTime().getDayOfMonth(),incomeConcludeE.getConmaincode()));
            }
            if ("差额纳税".equals(contractIncomePlanConclude.getTaxRate())) {
                BigDecimal totalTaxRateAmount = contractIncomePlanConclude.getTaxAmount();
                int periodCount = incomePlans.size();
                if(Objects.isNull(totalTaxRateAmount)){
                    totalTaxRateAmount = BigDecimal.ZERO;
                }
                BigDecimal averageAmount = totalTaxRateAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                BigDecimal sumAllocated = BigDecimal.ZERO;
                for (int i = 0; i < periodCount; i++) {
                    BigDecimal taxAmount;
                    if (i < periodCount - 1) {
                        taxAmount = averageAmount;
                        sumAllocated = sumAllocated.add(averageAmount);
                    } else {
                        taxAmount = totalTaxRateAmount.subtract(sumAllocated);
                    }
                    incomePlans.get(i).setTaxAmount(taxAmount);
                    incomePlans.get(i).setOriginTaxAmount(taxAmount);
                    incomePlans.get(i).setNoTaxAmount(incomePlans.get(i).getPlannedCollectionAmount().subtract(taxAmount));
                    incomePlans.get(i).setOriginNoTaxAmount(incomePlans.get(i).getPlannedCollectionAmount().subtract(taxAmount));
                }
            }
            incomePlans.sort(Comparator.comparing(PayIncomePlanE::getIncomePlanCode));
            if(isSave){
                this.saveOrUpdateBatch(incomePlans);
            }
        }
        return incomePlans;
    }

    private  BigDecimal calculateAmount(ContractIncomePlanConcludeE contractIncomePlanConclude, LocalDate startDate, LocalDate endDate, long totalDays, BigDecimal remainingAmount) {
        long periodDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal proportion = BigDecimal.valueOf(periodDays).divide(BigDecimal.valueOf(totalDays), 20, BigDecimal.ROUND_HALF_UP);
        return contractIncomePlanConclude.getPlannedCollectionAmount().multiply(proportion).setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    private  PayIncomePlanE createIncomePlan(ContractIncomePlanConcludeE contractIncomePlanConclude, LocalDate startDate, LocalDate endDate, BigDecimal amount) {
        PayIncomePlanE incomePlan = new PayIncomePlanE();
        incomePlan.setContractId(contractIncomePlanConclude.getContractId());
        incomePlan.setPlannedCollectionAmount(amount);
        incomePlan.setPlannedCollectionTime(contractIncomePlanConclude.getPlannedCollectionTime());
        incomePlan.setBelongingMonth(formatDateToMonth(startDate));
        incomePlan.setCostStartTime(startDate);
        incomePlan.setCostEndTime(endDate);
        incomePlan.setContractNo(contractIncomePlanConclude.getContractNo());
        incomePlan.setCostEstimationCode(contractIncomePlanConclude.getCostEstimationCode());

        return incomePlan;
    }

    private  String formatDateToMonth(LocalDate date) {
        return date.getYear() + "-" + String.format("%02d", date.getMonthValue());
    }



    public List<String> getIncomePlanByConcludePlanId(String concludePlanId) {
        QueryWrapper<PayIncomePlanE> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PayIncomePlanE::getDeleted, 0)
                .eq(PayIncomePlanE::getDeleted, concludePlanId);
        List<String> result = this.list(wrapper).stream().map(plan -> plan.getId()).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(result) ? result : Collections.emptyList();
    }



    private static BigDecimal parseTaxRate(String taxRateStr) {
        if (taxRateStr == null || taxRateStr.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            String rate = taxRateStr.replace("%", "");
            BigDecimal rateDecimal = new BigDecimal(rate).divide(BigDecimal.valueOf(100));
            return rateDecimal;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无效的税率格式: " + taxRateStr);
        }
    }

    public PageV<PayIncomeListV> listPlan(PageF<SearchF<PayIncomeListQuery>> form) {
        Page<PayIncomeListQuery> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<PayIncomeListQuery> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        IPage<PayIncomeListV> pageList = payIncomePlanMapper.listPlan(pageF, planConditionPage(queryModel, getIdentityInfo().get().getTenantId()));
        if (null != pageList && CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<ContractIncomeConcludeE> concludeEList = this.contractIncomeConcludeService.lambdaQuery().in(ContractIncomeConcludeE::getId,pageList.getRecords().stream().map(PayIncomeListV::getContractId).collect(Collectors.toList())).list();
            Map<String,ContractIncomeConcludeE> concludeEMap =  concludeEList.stream().collect(Collectors.toMap(ContractIncomeConcludeE::getId, v->v,(v1, v2)->v1));
            pageList.getRecords().forEach(record-> {
                ContractIncomeConcludeE contractIncomeConcludeE = concludeEMap.get(record.getContractId());
                record.setIriStatusName(IriStatusEnum.parseName(record.getIriStatus()));
                record.setCostCenterName(contractIncomeConcludeE.getCostCenterName());
                record.setName(contractIncomeConcludeE.getName());
                record.setRegion(contractIncomeConcludeE.getRegion());
                record.setCommunityName(contractIncomeConcludeE.getCommunityName());
                record.setConmaincode(contractIncomeConcludeE.getConmaincode());
                record.setOppositeOne(contractIncomeConcludeE.getOppositeOne());
                record.setDepartName(contractIncomeConcludeE.getDepartName());
                record.setSettlementStatusName(SettlementStatusEnum.getEnum(record.getSettlementStatus()).name());
                record.setNoReceiptAmount(record.getPlannedCollectionAmount());
                if (null != record.getPaymentAmount()) {
                    record.setNoReceiptAmount(record.getPlannedCollectionAmount().subtract(record.getPaymentAmount()));
                }
            });
        }
        return PageV.of(form, pageList.getTotal(), Global.mapperFacade.mapAsList(pageList.getRecords(), PayIncomeListV.class));

    }

    private QueryWrapper<PayIncomeListQuery> planConditionPage(QueryWrapper<PayIncomeListQuery> queryModel, String tenantId) {
        queryModel.eq("cc.deleted", 0);
        queryModel.eq("cc.tenantId", tenantId);
        return queryModel;
    }

    //收入计划规则：SRJH-应结算日期-CT码-两位自增数【CBYG-20241131-CT码-01】
    public String generatePlanCode(String redisKey, String plannedCollectionDate, String ctCode) {
        // 调用自增方法获取序列号
        String serialNumber = calculateCostEstimationCode(redisKey);
        // 生成成本预估编码
        return String.format("SRJH-%s-%s-%s", plannedCollectionDate, ctCode, serialNumber);
    }


    private String calculateCostEstimationCode(String redisKey){
        Long increment = redisTemplate.opsForValue().increment(redisKey, 1L);
        if (increment > 99L){
            // 当序列号超过最大值时，重置为0
            redisTemplate.opsForValue().set(redisKey, "0");
            increment = redisTemplate.opsForValue().increment(redisKey, 1L);
        }
        // 格式化序列号，前面补零
        String serialStr = String.format("%0"+2+"d", increment);
        return serialStr;
    }
}
