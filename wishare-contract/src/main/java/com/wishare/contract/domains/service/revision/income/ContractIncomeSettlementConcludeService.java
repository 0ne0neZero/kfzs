package com.wishare.contract.domains.service.revision.income;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementAddF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludePageF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludeUpdateF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionUpdateF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsUpdateF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomePlanListF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomeSettlementConcludeListF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomeSettlementPeriodF;
import com.wishare.contract.apps.fo.revision.income.settlement.IncomePlanPeriodF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementPeriodF;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.vo.UserInfoRv;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.apps.service.contractset.ContractPayIncomePlanService;
import com.wishare.contract.apps.service.revision.common.ContractInfoToFxmCommonService;
import com.wishare.contract.domains.bo.CommonRangeAmountBO;
import com.wishare.contract.domains.bo.CommonRangeDateBO;
import com.wishare.contract.domains.bo.CommonRangeDayAmountBO;
import com.wishare.contract.domains.bo.SettlementPlanBO;
import com.wishare.contract.domains.consts.SettlementStatusEnum;
import com.wishare.contract.domains.entity.contractset.ContractProcessRecordE;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.income.*;
import com.wishare.contract.domains.entity.revision.income.settdetails.ContractIncomeConcludeSettdeductionE;
import com.wishare.contract.domains.entity.revision.income.settdetails.ContractIncomeSettDetailsE;
import com.wishare.contract.domains.entity.revision.invoice.ContractSettlementsBillDetailsE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsFundE;
import com.wishare.contract.domains.entity.revision.pay.settdetails.ContractPaySettDetailsE;
import com.wishare.contract.domains.enums.SplitModeEnum;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.mapper.contractset.ContractPayIncomePlanMapper;
import com.wishare.contract.domains.mapper.contractset.ContractProcessRecordMapper;
import com.wishare.contract.domains.mapper.revision.income.*;
import com.wishare.contract.domains.mapper.revision.income.settdetails.ContractIncomeConcludeSettdeductionMapper;
import com.wishare.contract.domains.mapper.revision.income.settdetails.ContractIncomeSettDetailsMapper;
import com.wishare.contract.domains.mapper.revision.invoice.ContractSettlementsBillDetailsMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsBillMapper;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsFundMapper;
import com.wishare.contract.domains.service.contractset.ContractOrgCommonService;
import com.wishare.contract.domains.service.revision.common.CommonRangeAmountService;
import com.wishare.contract.domains.service.revision.income.settdetails.ContractIncomeConcludeSettdeductionService;
import com.wishare.contract.domains.service.revision.income.settdetails.ContractIncomeSettDetailsService;
import com.wishare.contract.domains.service.revision.pay.bo.PlanWriteOffBo;
import com.wishare.contract.domains.vo.contractset.ContractOrgPermissionV;
import com.wishare.contract.domains.vo.revision.fwsso.FwSSoBaseInfoF;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeSumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementDetailsV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeConcludeSettdeductionV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettDetailsV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettdeductionDetailV;
import com.wishare.contract.domains.vo.revision.income.settlement.*;
import com.wishare.contract.domains.vo.revision.invoice.ContractSettlementsBillDetailsV;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementConcludeInfoV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractIncomeBillV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractSettFundV;
import com.wishare.contract.domains.vo.revision.pay.settlement.SettlementSimpleStr;
import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateResultV;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateReturnV;
import com.wishare.contract.domains.vo.revision.procreate.ProcessCreateV;
import com.wishare.contract.infrastructure.utils.BigDecimalUtils;
import com.wishare.contract.infrastructure.utils.build.Builder;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.enums.WithinDateTimeEnum;
import com.wishare.tools.starter.fo.filestorage.FormalF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.service.FileStorageService;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/13:57
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class ContractIncomeSettlementConcludeService extends ServiceImpl<ContractIncomeSettlementConcludeMapper, ContractIncomeSettlementConcludeE> implements IOwlApiBase {
    private static final String CONTRACT_INCOME_CONCLUDE_SETTLEMENT = "contract_income_conclude_settlement";

    /**
     * 逗号,用于分隔图片
     */
    public static final String COMMA = ",";
    private static final String DIFF_TAX_TYPE_NAME = "差额纳税";


    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeSettlementConcludeMapper contractIncomeSettlementConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeSettDetailsMapper contractIncomeSettDetailsMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeSettdeductionMapper contractIncomeConcludeSettdeductionMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomePlanConcludeMapper contractIncomePlanConcludeMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillMapper contractPayBillMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsFundMapper contractSettlementsFundMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillDetailsMapper contractSettlementsBillDetailsMapper;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private FileStorageService fileStorageService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeSettDetailsService contractIncomeSettDetailsService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeSettdeductionService contractIncomeConcludeSettdeductionService;
    @Setter(onMethod_ = {@Autowired})
    @Getter
    private FileStorage fileStorage;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomePlanConcludeService contractIncomePlanConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeSettlementPeriodMapper settlementPeriodMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeSettlementPlanRelationMapper settlementPlanRelationMapper;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludeSettlementContractSnapshotMapper settlementContractSnapshotMapper;
    @Setter(onMethod_ = {@Autowired})
    private ContractOrgCommonService contractOrgCommonService;

    @Setter(onMethod_ = {@Autowired})
    private ContractProcessRecordMapper contractProcessRecordMapper;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ExternalFeignClient externalFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private ContractPayIncomePlanService contractPayIncomePlanService;

    @Setter(onMethod_ = {@Autowired})
    private ContractInfoToFxmCommonService contractInfoToFxmCommonService;

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeConcludePlanFxmReceiptRecordService fxmReceiptRecordService;

    @Value("${process.create.bizCode:}")
    private String bizCode;

    @Value("${income.sett.isDev:true}")
    private Boolean isDev ;
    @Autowired
    private CommonRangeAmountService commonRangeAmountService;
    @Autowired
    private ContractPayIncomePlanMapper contractPayIncomePlanMapper;
    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    public ContractIncomeSettlementDetailsV getDetailsById(String id) {
        ContractIncomeSettlementConcludeE map = contractIncomeSettlementConcludeMapper.selectById(id);
        if (Objects.isNull(map)){
            throw new OwlBizException("确收单不存在");
        }
        ContractIncomeSettlementDetailsV contractPayPlanDetailsV = Global.mapperFacade.map(map, ContractIncomeSettlementDetailsV.class);
        //查询对应的合同，编号，客户名称
        ContractIncomeConcludeE contractPayConcludeE = contractIncomeConcludeMapper.selectById(map.getContractId());
        contractPayPlanDetailsV.setContractName(contractPayConcludeE.getName());
        contractPayPlanDetailsV.setContractNo(contractPayConcludeE.getContractNo());
        contractPayPlanDetailsV.setCustomerName(contractPayConcludeE.getPartyBName());
        //查询对应的结算单明细信息
        LambdaQueryWrapper<ContractIncomeSettDetailsE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractIncomeSettDetailsE::getSettlementId, id);
        List<ContractIncomeSettDetailsE> contractIncomeSettlementConcludeEList = contractIncomeSettDetailsMapper.selectList(queryWrapper);
        List<ContractIncomeSettDetailsV> contractPaySettDetailsVList = Global.mapperFacade.mapAsList(contractIncomeSettlementConcludeEList, ContractIncomeSettDetailsV.class);
        //扣款明细详情
        LambdaQueryWrapper<ContractIncomeConcludeSettdeductionE> queryWrapper10 = new LambdaQueryWrapper<>();
        queryWrapper10.eq(ContractIncomeConcludeSettdeductionE::getSettlementId, id)
                .eq(ContractIncomeConcludeSettdeductionE :: getDeleted, 0);
        List<ContractIncomeConcludeSettdeductionE> contractIncomeConcludeSettdeductionEList = contractIncomeConcludeSettdeductionMapper.selectList(queryWrapper10);
        List<ContractIncomeConcludeSettdeductionV> contractIncomeConcludeSettdeductionVList = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(contractIncomeConcludeSettdeductionEList)){
            contractIncomeConcludeSettdeductionVList = Global.mapperFacade.mapAsList(contractIncomeConcludeSettdeductionEList, ContractIncomeConcludeSettdeductionV.class);
            contractPayPlanDetailsV.setContractIncomeConcludeSettdeductionVList(contractIncomeConcludeSettdeductionVList);
        }

        //查询对应的收票信息
        LambdaQueryWrapper<ContractSettlementsBillE> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ContractSettlementsBillE::getSettlementId, id);
        List<ContractSettlementsBillE> contractPayBillES = contractPayBillMapper.selectList(queryWrapper1);
        List<ContractIncomeBillV> contractIncomeBillVList = Global.mapperFacade.mapAsList(contractPayBillES, ContractIncomeBillV.class);
        for (ContractIncomeBillV s : contractIncomeBillVList) {
            s.setBillTypeName(BillTypeEnum.parseName(Integer.parseInt(s.getBillType())));
        }
        if(ObjectUtils.isNotEmpty(contractIncomeBillVList)){
            List<String> billId = contractIncomeBillVList.stream().map( s -> s.getId()).collect(Collectors.toList());
            List<Integer> reviewStatus = new ArrayList<>();
            reviewStatus.add(1);
            reviewStatus.add(2);
            LambdaQueryWrapper<ContractSettlementsBillDetailsE> queryWrapper99 = new LambdaQueryWrapper<>();
            queryWrapper99.in(ContractSettlementsBillDetailsE::getBillId, billId)
                    .in(ContractSettlementsBillDetailsE::getReviewStatus, reviewStatus);
            List<ContractSettlementsBillDetailsE> contractSettlementsBillDetailsEList = contractSettlementsBillDetailsMapper.selectList(queryWrapper99);
            if(ObjectUtils.isNotEmpty(contractSettlementsBillDetailsEList)){
                List<ContractSettlementsBillDetailsV>  contractSettlementsBillDetailsVList = Global.mapperFacade.mapAsList(contractSettlementsBillDetailsEList, ContractSettlementsBillDetailsV.class);
                for(ContractSettlementsBillDetailsV s : contractSettlementsBillDetailsVList){
                    s.setReviewStatusName(ReviewStatusEnum.parseName(s.getReviewStatus()));
                    List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.发票类型.getCode(), s.getBillType().toString());
                    if (CollectionUtils.isNotEmpty(value)) {
                        s.setBillTypeName(value.get(0).getName());
                    }
                }
                contractPayPlanDetailsV.setContractSettlementsBillDetailsVList(contractSettlementsBillDetailsVList);
            }
        }
        //查询对应的付款信息
        LambdaQueryWrapper<ContractSettlementsFundE> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(ContractSettlementsFundE::getSettlementId, id);
        List<ContractSettlementsFundE> contractSettlementsFundES = contractSettlementsFundMapper.selectList(queryWrapper2);
        List<ContractSettFundV> contractSettFundVList = Global.mapperFacade.mapAsList(contractSettlementsFundES, ContractSettFundV.class);

        contractPayPlanDetailsV.setContractSettFundVList(contractSettFundVList);
        contractPayPlanDetailsV.setContractIncomeBillVList(contractIncomeBillVList);
        if(CollectionUtils.isNotEmpty(contractPaySettDetailsVList)){
            contractPaySettDetailsVList.forEach(x->x.setChargeItemAllPath(financeFeignClient.chargeName(x.getChargeItemId())));
        }
        contractPayPlanDetailsV.setContractIncomeSettDetailsSaveFList(contractPaySettDetailsVList);


        List<ContractIncomePlanForSettlementV> contractPayPlanForSettlementVList = settlementPlanRelationMapper.getPlanList(id);
        contractPayPlanDetailsV.setContractIncomePlanForSettlementVList(contractPayPlanForSettlementVList);

        List<IncomePlanPeriodV> contractIncomeSettlementPeriodVList = settlementPeriodMapper.getPeriodList(id);
        contractPayPlanDetailsV.setContractIncomeSettlementPeriodVList(contractIncomeSettlementPeriodVList);
        if (CollectionUtils.isNotEmpty(contractPayPlanForSettlementVList)){
            // start~end 逗号分隔
            String periodStr = contractIncomeSettlementPeriodVList.stream().map(e ->
                    DateUtil.format(e.getStartDate(), "yyyy-MM-dd") + "~" + DateUtil.format(e.getEndDate(), "yyyy-MM-dd"))
                    .collect(Collectors.joining(","));
            contractPayPlanDetailsV.setPeriodStr(periodStr);
            /*if(CollectionUtils.isNotEmpty(contractPaySettlementConcludeEList)){
                List<String> funIdList = contractPaySettlementConcludeEList.stream()
                        .map(ContractIncomeSettDetailsE::getPayFundId)
                        .distinct()
                        .collect(Collectors.toList());
                List<PayIncomePlanE> payIncomePlanEList = contractPayIncomePlanMapper.getCostListByPlanAndCostTime(funIdList, contractIncomeSettlementPeriodVList);
                payIncomePlanEList = payIncomePlanEList.stream()
                        .sorted(Comparator.comparing(PayIncomePlanE::getChargeItem)
                                .thenComparing(PayIncomePlanE::getTermDate)
                                .thenComparing(PayIncomePlanE::getCostStartTime))
                        .collect(Collectors.toList());
                contractPayPlanDetailsV.setPayIncomePlanEList(payIncomePlanEList);
            }*/

        }
        List<PayIncomePlanE> payIncomePlanEList = new ArrayList<>();
        if(Objects.nonNull(contractIncomeSettlementConcludeEList.get(0).getStartDate()) && Objects.nonNull(contractIncomeSettlementConcludeEList.get(0).getEndDate())){
            for(ContractIncomeSettDetailsE settD : contractIncomeSettlementConcludeEList){
                payIncomePlanEList.addAll(contractPayIncomePlanMapper.getCostListByPlanAndCostTime(
                        map.getContractId(),
                        settD.getPayFundId(),
                        Date.from(settD.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }else{
            if (CollectionUtils.isNotEmpty(contractIncomeSettlementPeriodVList)){
                List<ContractIncomePlanConcludeE> planFunList = contractIncomePlanConcludeMapper.getFunDateList(map.getContractId(),contractIncomeSettlementConcludeEList.stream().map(ContractIncomeSettDetailsE :: getPayFundId).collect(Collectors.toList()),
                        contractIncomeSettlementPeriodVList);
                for(ContractIncomePlanConcludeE settD : planFunList){
                    payIncomePlanEList.addAll(contractPayIncomePlanMapper.getCostListByPlanAndCostTime(
                            map.getContractId(),
                            settD.getContractPayFundId(),
                            Date.from(settD.getCostStartTime().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(settD.getCostEndTime().atStartOfDay(ZoneId.systemDefault()).toInstant())));
                }
            }
        }
        contractPayPlanDetailsV.setPayIncomePlanEList(payIncomePlanEList);


        // 合同相关信息
        ContractIncomeConcludeSettlementContractSnapshotE snapshot = settlementContractSnapshotMapper.querySnapshotBySettlementId(id);
        if (Objects.isNull(snapshot)){
            snapshot = settlementContractSnapshotMapper.querySnapshotByContractId(map.getContractId());
        }



        /*if (CollectionUtils.isNotEmpty(contractPayPlanForSettlementVList)){
            // 获取planId集合
            List<String> planIdList = contractPayPlanForSettlementVList.stream()
                    .map(ContractIncomePlanForSettlementV::getPlanId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(planIdList)){
                List<PayIncomePlanE> payIncomePlanEList = contractPayIncomePlanService.lambdaQuery().in(PayIncomePlanE::getPlanId,planIdList).list();
                payIncomePlanEList = payIncomePlanEList.stream()
                        .sorted(Comparator.comparing(PayIncomePlanE::getChargeItem)
                        .thenComparing(PayIncomePlanE::getTermDate)
                        .thenComparing(PayIncomePlanE::getCostStartTime))
                        .collect(Collectors.toList());
                contractPayPlanDetailsV.setPayIncomePlanEList(payIncomePlanEList);
            }
        }*/

        ContractIncomeSettlementContractInfoV contractInfoV = Global.mapperFacade.map(snapshot, ContractIncomeSettlementContractInfoV.class);
        contractPayPlanDetailsV.setContractIncomeSettlementContractInfoV(contractInfoV);

        return contractPayPlanDetailsV;
    }


    @Transactional(rollbackFor = Exception.class)
    public String save(ContractIncomeSettlementAddF addF) {
        checkBeforeSaveSettlement(addF);

        ContractIncomeSettlementConcludeE map = Global.mapperFacade.map(addF, ContractIncomeSettlementConcludeE.class);
        BigDecimal plannedCollectionAmountSum = BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;
        BigDecimal sumChooseAmount = BigDecimal.ZERO;
        BigDecimal sumSettleAmount = BigDecimal.ZERO;
        for(int i = 0; i < addF.getContractIncomeSettDetailsSaveFList().size(); i ++){
            plannedCollectionAmountSum = plannedCollectionAmountSum.add(addF.getContractIncomeSettDetailsSaveFList().get(i).getAmount());
        }
        for(int i = 0; i < addF.getContractIncomeConcludeSettdeductionSaveFList().size(); i ++){
            deductionAmount = deductionAmount.add(addF.getContractIncomeConcludeSettdeductionSaveFList().get(i).getAmount());
        }

        //父结算单
        ContractIncomeSettlementConcludeV sb = contractIncomeSettlementConcludeMapper.getContractIncomeSettlementInfo(addF.getContractId());

        if(ObjectUtils.isNotEmpty(sb)){
            map.setPid(sb.getId());
        }else{
            ContractIncomeSettlementConcludeE pidMap = new ContractIncomeSettlementConcludeE();
            pidMap.setContractId(map.getContractId());
            pidMap.setContractName(map.getContractName());
            pidMap.setCustomer(map.getCustomer());
            pidMap.setCustomerName(map.getCustomer());
            pidMap.setPlannedCollectionAmount(plannedCollectionAmountSum);
            pidMap.setDeductionAmount(deductionAmount);
            pidMap.setTenantId(tenantId());
            contractIncomeSettlementConcludeMapper.insert(pidMap);
            map.setPid(pidMap.getId());
        }
        map.setPayFundNumber(genFundSettleNode(addF.getContractId()));
        map.setPlannedCollectionAmount(plannedCollectionAmountSum);
        map.setDeductionAmount(deductionAmount);
        map.setTenantId(tenantId());
        map.setReviewStatus(ReviewStatusEnum.DRAFT.getCode());
        List<String> planId = addF.getPlanIdList();
        List<ContractIncomePlanConcludeE> contractIncomePlanConcludeVList = contractIncomePlanConcludeService.getByIdList(planId);
        for(ContractIncomePlanConcludeE s : contractIncomePlanConcludeVList){
            sumChooseAmount = sumChooseAmount.add(s.getPlannedCollectionAmount());
            sumSettleAmount = sumSettleAmount.add(s.getSettlementAmount());
        }
        if(plannedCollectionAmountSum.add(sumSettleAmount).compareTo(sumChooseAmount) > 0){
            throw new OwlBizException("结算总金额不能超过付款计划总金额!");
        }
        map.setTermDate(addF.getTermDate());
        contractIncomeSettlementConcludeMapper.insert(map);
        List<ContractIncomePlanConcludeE> planFunList = contractIncomePlanConcludeMapper.getFunDateList(map.getContractId(),addF.getContractIncomeSettDetailsSaveFList().stream().map(ContractIncomeSettDetailsSaveF :: getPayFundId).collect(Collectors.toList()),
                Global.mapperFacade.mapAsList(addF.getContractIncomeSettlementPeriodSaveFList(), IncomePlanPeriodV.class));
        for(int i = 0; i < addF.getContractIncomeSettDetailsSaveFList().size(); i ++) {
            ContractIncomeSettDetailsE contractIncomeSettDetailsE = Global.mapperFacade.map(addF.getContractIncomeSettDetailsSaveFList().get(i), ContractIncomeSettDetailsE.class);
            contractIncomeSettDetailsE.setSettlementId(map.getId());
            contractIncomeSettDetailsE.setTenantId(tenantId());
            if (StringUtils.isNotBlank(addF.getContractIncomeSettDetailsSaveFList().get(i).getTaxRateId())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), addF.getContractIncomeSettDetailsSaveFList().get(i).getTaxRateId());
                if (CollectionUtils.isNotEmpty(value)) {
                    contractIncomeSettDetailsE.setTaxRate(value.get(0).getName());
                }
            }
            ContractIncomePlanConcludeE planFun = planFunList.stream().filter(s -> contractIncomeSettDetailsE.getPayFundId().equals(s.getContractPayFundId())).findFirst().orElse(new ContractIncomePlanConcludeE());
            contractIncomeSettDetailsE.setStartDate(planFun.getCostStartTime());
            contractIncomeSettDetailsE.setEndDate(planFun.getCostEndTime());
            contractIncomeSettDetailsMapper.insert(contractIncomeSettDetailsE);
        }
        for(int i = 0; i < addF.getContractIncomeConcludeSettdeductionSaveFList().size(); i ++) {
            ContractIncomeConcludeSettdeductionE contractPayConcludeSettdeductionE = Global.mapperFacade.map(addF.getContractIncomeConcludeSettdeductionSaveFList().get(i), ContractIncomeConcludeSettdeductionE.class);
            contractPayConcludeSettdeductionE.setSettlementId(map.getId());
            contractPayConcludeSettdeductionE.setTenantId(tenantId());
            contractIncomeConcludeSettdeductionMapper.insert(contractPayConcludeSettdeductionE);
        }
        //计算金额减免逻辑
        extractedReductionAmount(addF.getContractIncomeConcludeSettdeductionSaveFList(), contractIncomePlanConcludeVList);
        if (CollectionUtils.isNotEmpty(addF.getContractIncomeSettlementPeriodSaveFList())){
            List<ContractIncomeConcludeSettlementPeriodE> periodList = Lists.newArrayList();
            for (ContractIncomeSettlementPeriodF periodF : addF.getContractIncomeSettlementPeriodSaveFList()) {
                ContractIncomeConcludeSettlementPeriodE periodE = new ContractIncomeConcludeSettlementPeriodE();
                periodE.setSettlementId(map.getId());
                periodE.setStartDate(periodF.getStartDate());
                periodE.setEndDate(periodF.getEndDate());
                periodList.add(periodE);
            }
            settlementPeriodMapper.insertBatch(periodList);

        }
        if (CollectionUtils.isNotEmpty(addF.getPlanIdList())){
            List<ContractIncomeConcludeSettlementPlanRelationE> relationList = Lists.newArrayList();
            for (String curPlanId : addF.getPlanIdList()) {
                ContractIncomeConcludeSettlementPlanRelationE relationE = new ContractIncomeConcludeSettlementPlanRelationE();
                relationE.setPlanId(curPlanId);
                relationE.setSettlementId(map.getId());
                relationList.add(relationE);
            }
            settlementPlanRelationMapper.insertBatch(relationList);
        }
        handlePidPaySettlementInfo(map.getPid());
        dealSettlementContractSnapshot(map.getId(), addF.getContractId(), false);
        updateSettlementStep(map.getId(), 1);
        return map.getId();
    }

    private void extractedReductionAmount(List<ContractIncomeConcludeSettdeductionSaveF> contractIncomeConcludeSettdeductionSaveFList, List<ContractIncomePlanConcludeE> contractIncomePlanConcludeVList) {
        if (CollectionUtils.isNotEmpty(contractIncomeConcludeSettdeductionSaveFList)){
            // 按减免清单中费项分组，并汇总其减免金额
            Map<Long, BigDecimal> groupedByChargeItemId = contractIncomeConcludeSettdeductionSaveFList.stream()
                    .collect(Collectors.groupingBy(
                            ContractIncomeConcludeSettdeductionSaveF::getChargeItemId,
                            Collectors.reducing(BigDecimal.ZERO, ContractIncomeConcludeSettdeductionSaveF::getAmount,BigDecimal::add)
                    ));
            List<Long> chargeItemIdList = groupedByChargeItemId.keySet().stream().collect(Collectors.toList());

            //筛选减免清单中费项相同的计划数据
            List<ContractIncomePlanConcludeE> chargePlanList = contractIncomePlanConcludeVList.stream()
                    .filter(plan -> chargeItemIdList.contains(plan.getChargeItemId()))
                    .collect(Collectors.toList());
            //获取所有符合计划的未结算总额
            BigDecimal planTotalAmount = chargePlanList.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            //处理减免金额逻辑
            for (Map.Entry<Long, BigDecimal> entry : groupedByChargeItemId.entrySet()) {
                Long chargeItemId = entry.getKey();
                BigDecimal reductionTotalAmount = entry.getValue();

                //获取减免费项对应计划数据
                List<ContractIncomePlanConcludeE> funPlanList = chargePlanList.stream()
                        .filter(plan -> chargeItemId.equals(plan.getChargeItemId()))
                        .collect(Collectors.toList());

                // 按清单分组计划并累计其计划未结算金额
                Map<String, BigDecimal> funTotalAmount = funPlanList.stream()
                        .collect(Collectors.groupingBy(
                                ContractIncomePlanConcludeE::getContractPayFundId,
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        plan -> plan.getPlannedCollectionAmount().subtract(plan.getSettlementAmount()),
                                        BigDecimal::add
                                )
                        ));

                // 计算各清单占用比例,且计算对应减免金额
                Map<String, BigDecimal> funReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funTotalAmount, planTotalAmount,reductionTotalAmount);
                //获取清单下计划数，并按期数排序
                Map<String, List<ContractIncomePlanConcludeE>> groupedAndSorted = funPlanList.stream()
                        .collect(Collectors.groupingBy(
                                ContractIncomePlanConcludeE::getContractPayFundId,
                                Collectors.collectingAndThen(
                                        Collectors.toList(),
                                        list -> list.stream()
                                                .sorted(Comparator.comparing(ContractIncomePlanConcludeE::getTermDate))
                                                .collect(Collectors.toList())
                                )
                        ));
                for (Map.Entry<String, List<ContractIncomePlanConcludeE>> plan : groupedAndSorted.entrySet()) {
                    String key = plan.getKey();
                    //该清单减免金额
                    BigDecimal planReductionAmount = funReductionAmountMap.get(key);
                    List<ContractIncomePlanConcludeE> planList = plan.getValue();
                    //获取该清单计划未结算金额
                    BigDecimal planAmount = planList.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Map<String, BigDecimal> planDeAmount = planList.stream()
                            .collect(Collectors.groupingBy(
                                    ContractIncomePlanConcludeE::getId,
                                    Collectors.reducing(
                                            BigDecimal.ZERO,
                                            p -> p.getPlannedCollectionAmount().subtract(p.getSettlementAmount()),
                                            BigDecimal::add
                                    )
                            ));
                    Map<String, BigDecimal> planReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(planDeAmount, planAmount,planReductionAmount);
                    List<CommonRangeDayAmountBO> dayAmountList = new ArrayList<>();
                    for (Map.Entry<String, BigDecimal> pAmout : planReductionAmountMap.entrySet()) {
                        CommonRangeDayAmountBO bo = new CommonRangeDayAmountBO();
                        bo.setId(pAmout.getKey());
                        bo.setReductionAmount(pAmout.getValue());
                        dayAmountList.add(bo);
                    }
                    contractIncomePlanConcludeMapper.updateIncomePlan(dayAmountList);
                }
            }

        }
    }

    private void checkBeforeSaveSettlement(ContractIncomeSettlementAddF addF) {
        List<String> planIdList = addF.getPlanIdList();
        Map<String, List<ContractIncomePlanConcludeE>> groupMap = checkPlanList(planIdList);
        List<String> existedSettlementIds = settlementPlanRelationMapper.selectExistRuningSettlement(planIdList);
        if (CollectionUtils.isNotEmpty(existedSettlementIds)){
            throw new OwlBizException("所选结算计划已被其他结算单选择，请重新选择");
        }
        // 所有金额必须大于等于0
        if (Objects.nonNull(addF.getActualSettlementAmount()) && addF.getActualSettlementAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("实际结算金额不能为空或小于0");
        }
        if (Objects.nonNull(addF.getTotalSettledAmount()) && addF.getTotalSettledAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("累计结算金额不能为空或小于0");
        }
        if (CollectionUtils.isNotEmpty(addF.getContractIncomeSettDetailsSaveFList())) {
            for (ContractIncomeSettDetailsSaveF detailsSaveF : addF.getContractIncomeSettDetailsSaveFList()) {
                if (detailsSaveF.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("结算明细-金额不能小于0");
                }
            }
        } else {
            throw new OwlBizException("结算明细不能为空");
        }
        if (CollectionUtils.isNotEmpty(addF.getContractIncomeConcludeSettdeductionSaveFList())) {
            for (ContractIncomeConcludeSettdeductionSaveF deductionSaveF : addF.getContractIncomeConcludeSettdeductionSaveFList()) {
                if (deductionSaveF.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("扣款明细汇总金额不能小于0");
                }
            }
        }

        //确收明细中，税率为差额纳税的明细，税额+不含税金额 必须等于 明细总额
        List<ContractIncomeSettDetailsSaveF> targetDetails = addF.getContractIncomeSettDetailsSaveFList().stream()
                .filter(detail -> StringUtils.equals("差额纳税", detail.getTaxRate())
                        && ObjectUtils.isNotEmpty(detail.getTaxRateAmount())
                        && ObjectUtils.isNotEmpty(detail.getAmountWithOutRate())
                        && ObjectUtils.isNotEmpty(detail.getAmount()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(targetDetails)) {
            for (ContractIncomeSettDetailsSaveF targetDetail : targetDetails) {
                BigDecimal addAmount = targetDetail.getTaxRateAmount().add(targetDetail.getAmountWithOutRate());
                if (targetDetail.getAmount().compareTo(addAmount) != 0) {
                    throw new OwlBizException("差额纳税的税额加不含税金额必须等于明细总额");
                }
            }
        }

        // 扣款总金额不能大于结算明细的汇总金额
        BigDecimal settlementAmount = addF.getContractIncomeSettDetailsSaveFList().stream()
                .map(ContractIncomeSettDetailsSaveF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deductionAmount = addF.getContractIncomeConcludeSettdeductionSaveFList().stream()
                .map(ContractIncomeConcludeSettdeductionSaveF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (deductionAmount.compareTo(settlementAmount) > 0) {
            throw new OwlBizException("扣款总金额不能大于收款明细的汇总金额");
        }

        // 结算明细分组金额不能大于对应分组成本预估金额的汇总
        Map<String, List<ContractIncomeSettDetailsSaveF>> groupSettleMap = addF.getContractIncomeSettDetailsSaveFList().stream()
                .collect(Collectors.groupingBy(ContractIncomeSettDetailsSaveF::getPayFundId));
        for (Map.Entry<String, List<ContractIncomeSettDetailsSaveF>> entry : groupSettleMap.entrySet()) {
            String key = entry.getKey();
            List<ContractIncomePlanConcludeE> curPlans = groupMap.get(key);
            if (CollectionUtils.isEmpty(curPlans)){
                throw new OwlBizException("确收明细的清单项、费项、税率 要和所选收款计划保持一致");
            }
            List<ContractIncomeSettDetailsSaveF> value = entry.getValue();
            BigDecimal settleAmount = value.stream()
                    .map(ContractIncomeSettDetailsSaveF::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal unSettleAmount = curPlans.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleAmount.compareTo(unSettleAmount) > 0) {
                throw new OwlBizException("确收明细分组金额不能大于对应分组收款计划未结算金额");
            }
        }

        String contractId = addF.getContractId();
        ContractIncomeSettlementConcludeV exist = contractIncomeSettlementConcludeMapper.getApprovingContractPaySettlementInfo(contractId);
        if (Objects.nonNull(exist)){
            throw new OwlBizException("系统内有未完成的确收审批单，请审核通过后再创建确收审批单");
        }
    }

    private Map<String, List<ContractIncomePlanConcludeE>> checkPlanList(List<String> planIdList) {
        if (CollectionUtils.isEmpty(planIdList)) {
            throw new OwlBizException("请选择收款计划");
        }
        LambdaQueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomePlanConcludeE::getId, planIdList)
                .eq(ContractIncomePlanConcludeE::getDeleted,0)
                .ne(ContractIncomePlanConcludeE::getPaymentStatus, 2);
        List<ContractIncomePlanConcludeE> payPlanList = contractIncomePlanConcludeMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(payPlanList)){
            throw new OwlBizException("所选收款计划不存在或已结算");
        }
        // 按照 chargeItemId typeId taxRateId 分组,
        Map<String, List<ContractIncomePlanConcludeE>> groupMap = payPlanList.stream()
                .collect(Collectors.groupingBy(ContractIncomePlanConcludeE::getContractPayFundId));
        for (List<ContractIncomePlanConcludeE> list : groupMap.values()) {
            //组内按照期数升序排序
            list.sort(Comparator.comparing(ContractIncomePlanConcludeE::getTermDate));
            Integer curTermDate = null;
            LocalDate currentDate = null;
            for (ContractIncomePlanConcludeE e : list) {
                if (!Objects.isNull(curTermDate)) {
                    //  组内应该允许相邻期数相等
                    if (e.getTermDate() != curTermDate + 1) {
                        throw new OwlBizException("所选收款计划期数必须严格连续");
                    }
                }

                if (!Objects.isNull(currentDate)) {
                    // 组内应该允许相邻计划的时间不连续[期数相等不校验]
                    if (e.getTermDate() == curTermDate+1 &&
                            !currentDate.plusDays(1).isEqual(e.getCostStartTime())) {
                        throw new OwlBizException("所选收款计划时间必须严格连续");
                    }
                }
                curTermDate = e.getTermDate();
                currentDate = e.getCostEndTime();
            }
        }
        return groupMap;
    }

    private void dealSettlementContractSnapshot(String settlementId, String contractId,boolean needDel) {
        if (needDel){
            settlementContractSnapshotMapper.deleteBySettlementId(settlementId);
        }
        ContractIncomeConcludeSettlementContractSnapshotE snapshotE = settlementContractSnapshotMapper.querySnapshotByContractId(contractId);
        if (Objects.isNull(snapshotE)){
            throw new OwlBizException("合同不存在或数据异常，请检查!");
        }
        snapshotE.setSettlementId(settlementId);
        settlementContractSnapshotMapper.insert(snapshotE);
    }

    public void handlePidPaySettlementInfo(String pid) {
        ContractIncomeSettlementConcludeE setPid = contractIncomeSettlementConcludeMapper.selectById(pid);
        LambdaQueryWrapper<ContractIncomeSettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomeSettlementConcludeE::getPid, pid)
                .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
        List<ContractIncomeSettlementConcludeE> contractPaySettlementConcludeEList = contractIncomeSettlementConcludeMapper.selectList(queryWrapper);
        BigDecimal planAmount = BigDecimal.ZERO;
        BigDecimal invoiceAmount = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        BigDecimal dueAmount = BigDecimal.ZERO;
        for(ContractIncomeSettlementConcludeE s : contractPaySettlementConcludeEList){
            planAmount = planAmount.add(s.getPlannedCollectionAmount());
            invoiceAmount = invoiceAmount.add(s.getInvoiceApplyAmount());
            payAmount = payAmount.add(s.getPaymentAmount());
            dueAmount = dueAmount.add(s.getDeductionAmount());
        }
        setPid.setDeductionAmount(dueAmount);
        setPid.setInvoiceApplyAmount(invoiceAmount);
        setPid.setPlannedCollectionAmount(planAmount);
        setPid.setPaymentAmount(payAmount);
        contractIncomeSettlementConcludeMapper.updateById(setPid);
    }

    private String genFundSettleNode(String contractId){
        String date = new SimpleDateFormat("yyyyMM", Locale.CHINESE).format(new Date());
        ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.selectById(contractId);
        LambdaQueryWrapper<ContractIncomeSettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomeSettlementConcludeE::getContractId, contractId)
                .notIn(ContractIncomeSettlementConcludeE::getPid,'0')
                .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
        List<ContractIncomeSettlementConcludeE> contractPayPlanConcludeEList = contractIncomeSettlementConcludeMapper.selectList(queryWrapper);
        int amount = contractPayPlanConcludeEList.size() + 1;
        return contractIncomeConcludeE.getContractNo() + "-" + date.substring(2,6) + "-0" + amount;
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public IPage<ContractIncomeSettlementConcludeV> page(PageF<SearchF<ContractIncomeSettlementConcludePageF>> form) {
        Page<ContractIncomeSettlementConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractIncomeSettlementConcludePageF> queryModel = form.getConditions().getQueryModel();
        return contractIncomeSettlementConcludeMapper.collectionPaySettlementPage(pageF, conditionPage(queryModel, curIdentityInfo().getTenantId()));
    }

    /**
     * 该接口供给后端
     *
     * @param form 请求分页的参数
     * @return 查询出的分页列表
     */
    public ContractIncomeSettlementConcludeSumV accountAmountSum(PageF<SearchF<ContractIncomeSettlementConcludePageF>> form) {
        Page<ContractIncomeSettlementConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        QueryWrapper<ContractIncomeSettlementConcludePageF> queryModel = form.getConditions().getQueryModel();
        return contractIncomeSettlementConcludeMapper.accountAmountSum(conditionPage(queryModel, curIdentityInfo().getTenantId()));
    }

    /**
     * 根据Id更新
     *
     * @param contractIncomeConcludeF 根据Id更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ContractIncomeSettlementConcludeUpdateF contractIncomeConcludeF) {
        if (contractIncomeConcludeF.getId() == null) {
            throw new IllegalArgumentException();
        }
        // 前置校验
        checkBeforeUpdateSettlement(contractIncomeConcludeF);
        ContractIncomeSettlementConcludeE map1 = contractIncomeSettlementConcludeMapper.selectById(contractIncomeConcludeF.getId());
        ContractIncomeSettlementConcludeE map = Global.mapperFacade.map(contractIncomeConcludeF, ContractIncomeSettlementConcludeE.class);
//        map.setSettleCycle(contractIncomeConcludeF.getSettleCycle().stream().collect(Collectors.joining(",")));
//        map.setStartTime(LocalDate.parse(contractIncomeConcludeF.getSettleCycle().get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        map.setEndTime(LocalDate.parse(contractIncomeConcludeF.getSettleCycle().get(1), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BigDecimal plannedCollectionAmountSum = BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;
        BigDecimal sumChooseAmount = BigDecimal.ZERO;
        BigDecimal sumSettleAmount = BigDecimal.ZERO;
        for(int i = 0; i < contractIncomeConcludeF.getContractIncomeSettDetailsSaveFList().size(); i ++){
            plannedCollectionAmountSum = plannedCollectionAmountSum.add(contractIncomeConcludeF.getContractIncomeSettDetailsSaveFList().get(i).getAmount());
        }
        for(int i = 0; i < contractIncomeConcludeF.getContractIncomeConcludeSettdeductionSaveFList().size(); i ++){
            deductionAmount = deductionAmount.add(contractIncomeConcludeF.getContractIncomeConcludeSettdeductionSaveFList().get(i).getAmount());
        }
        map.setPlannedCollectionAmount(plannedCollectionAmountSum);
        map.setDeductionAmount(deductionAmount);
        if(contractIncomeConcludeF.getSaveType().equals("2")){
            map.setReviewStatus(2);
        }
        List<String> planId = contractIncomeConcludeF.getPlanIdList();
        List<ContractIncomePlanConcludeE> contractIncomePlanConcludeVList = contractIncomePlanConcludeService.getByIdList(planId);
        for(ContractIncomePlanConcludeE s : contractIncomePlanConcludeVList){
            sumChooseAmount = sumChooseAmount.add(s.getPlannedCollectionAmount());
            sumSettleAmount = sumSettleAmount.add(s.getSettlementAmount());
        }
        if(plannedCollectionAmountSum.add(sumSettleAmount).compareTo(sumChooseAmount) > 0){
            throw new OwlBizException("结算总金额不能超过付款计划总金额!");
        }
        map.setTermDate(contractIncomeConcludeF.getTermDate());
        contractIncomeSettlementConcludeMapper.updateById(map);

        contractIncomeSettDetailsMapper.deleteBySettlementId(map.getId());
        List<ContractIncomeSettDetailsE> contractIncomeSettDetailsEList =
                Global.mapperFacade.mapAsList(contractIncomeConcludeF.getContractIncomeSettDetailsSaveFList(),
                        ContractIncomeSettDetailsE.class);

        List<ContractIncomePlanConcludeE> planFunList = contractIncomePlanConcludeMapper.getFunDateList(map.getContractId(),contractIncomeSettDetailsEList.stream().map(ContractIncomeSettDetailsE :: getPayFundId).collect(Collectors.toList()),
                Global.mapperFacade.mapAsList(contractIncomeConcludeF.getContractIncomeSettlementPeriodSaveFList(), IncomePlanPeriodV.class));
        for(ContractIncomeSettDetailsE s : contractIncomeSettDetailsEList){
            if (StringUtils.isNotBlank(s.getTaxRateId())) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项税率.getCode(), s.getTaxRateId());
                if (CollectionUtils.isNotEmpty(value)) {
                    s.setTaxRate(value.get(0).getName());
                }
            }
            ContractIncomePlanConcludeE planFun = planFunList.stream().filter(p -> s.getPayFundId().equals(p.getContractPayFundId())).findFirst().orElse(new ContractIncomePlanConcludeE());
            s.setStartDate(planFun.getCostStartTime());
            s.setEndDate(planFun.getCostEndTime());
            if(null != s.getId()){
                s.setId(null);
                contractIncomeSettDetailsMapper.insert(s);
            }else{
                s.setSettlementId(map.getId());
                contractIncomeSettDetailsMapper.insert(s);
            }
        }

        contractIncomeConcludeSettdeductionMapper.deleteBySettlementId(map.getId());
        List<ContractIncomeConcludeSettdeductionE> contractIncomeConcludeSettdeductionEList =
                Global.mapperFacade.mapAsList(contractIncomeConcludeF.getContractIncomeConcludeSettdeductionSaveFList(),
                        ContractIncomeConcludeSettdeductionE.class);
        for(ContractIncomeConcludeSettdeductionE s : contractIncomeConcludeSettdeductionEList){
            if(null != s.getId()){
                s.setId(null);
                contractIncomeConcludeSettdeductionMapper.insert(s);
            }else{
                s.setSettlementId(map.getId());
                contractIncomeConcludeSettdeductionMapper.insert(s);
            }
        }


        settlementPlanRelationMapper.deleteBySettlementId(contractIncomeConcludeF.getId());
        settlementPeriodMapper.deleteBySettlementId(contractIncomeConcludeF.getId());

        //计算金额减免逻辑
        List<ContractIncomeConcludeSettdeductionSaveF> concludeSettdeductionSaveList =
                Global.mapperFacade.mapAsList(contractIncomeConcludeF.getContractIncomeConcludeSettdeductionSaveFList(),
                        ContractIncomeConcludeSettdeductionSaveF.class);
        extractedReductionAmount(concludeSettdeductionSaveList, contractIncomePlanConcludeVList);

        if (CollectionUtils.isNotEmpty(contractIncomeConcludeF.getContractIncomeSettlementPeriodSaveFList())){
            List<ContractIncomeConcludeSettlementPeriodE> periodList = Lists.newArrayList();
            for (ContractIncomeSettlementPeriodF periodF : contractIncomeConcludeF.getContractIncomeSettlementPeriodSaveFList()) {
                ContractIncomeConcludeSettlementPeriodE periodE = new ContractIncomeConcludeSettlementPeriodE();
                periodE.setSettlementId(map.getId());
                periodE.setStartDate(periodF.getStartDate());
                periodE.setEndDate(periodF.getEndDate());
                periodList.add(periodE);
            }
            settlementPeriodMapper.insertBatch(periodList);
        }
        if (CollectionUtils.isNotEmpty(contractIncomeConcludeF.getPlanIdList())){
            List<ContractIncomeConcludeSettlementPlanRelationE> relationList = Lists.newArrayList();
            for (String curPlanId : contractIncomeConcludeF.getPlanIdList()) {
                ContractIncomeConcludeSettlementPlanRelationE relationE = new ContractIncomeConcludeSettlementPlanRelationE();
                relationE.setPlanId(curPlanId);
                relationE.setSettlementId(map.getId());
                relationList.add(relationE);
            }
            settlementPlanRelationMapper.insertBatch(relationList);
        }

        dealSettlementContractSnapshot(contractIncomeConcludeF.getId(), contractIncomeConcludeF.getContractId(),true);
        handlePidPaySettlementInfo(map1.getPid());
    }

    private void checkBeforeUpdateSettlement(ContractIncomeSettlementConcludeUpdateF updateF) {
        List<String> planIdList = updateF.getPlanIdList();
        Map<String, List<ContractIncomePlanConcludeE>> groupMap = checkPlanList(planIdList);

        List<String> existedSettlementIds = settlementPlanRelationMapper.selectExistRuningSettlement(planIdList);
        if (CollectionUtils.isNotEmpty(existedSettlementIds)){
            existedSettlementIds = existedSettlementIds.stream()
                    .filter(e -> !e.equals(updateF.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(existedSettlementIds)){
                throw new OwlBizException("所选结算计划被其他结算单选择，请重新选择");
            }
        }
        // 所有金额必须大于等于0
        if (Objects.nonNull(updateF.getActualSettlementAmount()) && updateF.getActualSettlementAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("实际结算金额不能为空或小于0");
        }
        if (Objects.nonNull(updateF.getTotalSettledAmount()) && updateF.getTotalSettledAmount().compareTo(BigDecimal.ZERO) < 0){
            throw new OwlBizException("累计结算金额不能为空或小于0");
        }
        if (CollectionUtils.isNotEmpty(updateF.getContractIncomeSettDetailsSaveFList())) {
            for (ContractIncomeSettDetailsUpdateF detailsSaveF : updateF.getContractIncomeSettDetailsSaveFList()) {
                if (detailsSaveF.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("结算明细-金额不能小于0");
                }
            }
        } else {
            throw new OwlBizException("结算明细不能为空");
        }
        if (CollectionUtils.isNotEmpty(updateF.getContractIncomeConcludeSettdeductionSaveFList())) {
            for (ContractIncomeConcludeSettdeductionUpdateF deductionSaveF : updateF.getContractIncomeConcludeSettdeductionSaveFList()) {
                if (deductionSaveF.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new OwlBizException("扣款明细汇总金额不能小于0");
                }
            }
        }

        // 扣款总金额不能大于结算明细的汇总金额
        BigDecimal settlementAmount = updateF.getContractIncomeSettDetailsSaveFList().stream()
                .map(ContractIncomeSettDetailsUpdateF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deductionAmount = updateF.getContractIncomeConcludeSettdeductionSaveFList().stream()
                .map(ContractIncomeConcludeSettdeductionUpdateF::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (deductionAmount.compareTo(settlementAmount) > 0) {
            throw new OwlBizException("扣款总金额不能大于结算明细的汇总金额");
        }

        // 结算明细分组金额不能大于对应分组成本预估金额的汇总
        Map<String, List<ContractIncomeSettDetailsUpdateF>> groupSettleMap = updateF.getContractIncomeSettDetailsSaveFList().stream()
                .collect(Collectors.groupingBy(ContractIncomeSettDetailsUpdateF::getPayFundId));
        for (Map.Entry<String, List<ContractIncomeSettDetailsUpdateF>> entry : groupSettleMap.entrySet()) {
            String key = entry.getKey();
            List<ContractIncomePlanConcludeE> curPlans = groupMap.get(key);
            if (CollectionUtils.isEmpty(curPlans)){
                throw new OwlBizException("结算明细的清单项、费项、税率 要和所选结算计划保持一致");
            }
            List<ContractIncomeSettDetailsUpdateF> value = entry.getValue();
            BigDecimal settleAmount = value.stream()
                    .map(ContractIncomeSettDetailsUpdateF::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal unSettleAmount = curPlans.stream().map(e -> e.getPlannedCollectionAmount().subtract(e.getSettlementAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (settleAmount.compareTo(unSettleAmount) > 0) {
                throw new OwlBizException("结算明细分组金额不能大于对应分组结算计划未结算金额");
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public String updateSettlementStep(String settlementId, Integer step) {
        int updateResult = contractIncomeSettlementConcludeMapper.updateSettlementStep(settlementId, step);
        // 第三步，更新审批状态为待提交
        if (step == 2){
            contractIncomeSettlementConcludeMapper.updateReviewStatus(settlementId, ReviewStatusEnum.待提交.getCode());
        }
        return "更新成功";
    }

    /**
     * @param id 根据Id删除
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(String id) {
        ContractIncomeSettlementConcludeE s = contractIncomeSettlementConcludeMapper.selectById(id);
        String pid = s.getPid();
        contractIncomeSettlementConcludeMapper.deleteById(id);
        handlePidPaySettlementInfo(pid);
        LambdaQueryWrapper<ContractIncomeSettlementConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomeSettlementConcludeE::getPid, pid)
                .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
        List<ContractIncomeSettlementConcludeE> concludeEList = contractIncomeSettlementConcludeMapper.selectList(queryWrapper);
        if(!ObjectUtils.isNotEmpty(concludeEList)){
            contractIncomeSettlementConcludeMapper.deleteById(pid);
        }
        // 删除计量周期表
        settlementPeriodMapper.deleteBySettlementId(id);
        // 删除成本预估计划关联表
        settlementPlanRelationMapper.deleteBySettlementId(id);
        return true;
    }

    /**
     * @param id 根据Id提交确收单
     */
    public String submitId(String id) {
        ContractIncomeSettlementConcludeE settlement = contractIncomeSettlementConcludeMapper.selectById(id);
        LambdaQueryWrapper<ContractProcessRecordE> queryWrappers = new LambdaQueryWrapper<>();
        queryWrappers.eq(ContractProcessRecordE::getContractId, settlement.getId())
                .eq(ContractProcessRecordE::getDeleted, 0);
        List<ContractProcessRecordE> processList = contractProcessRecordMapper.selectList(queryWrappers);
        if(CollectionUtils.isNotEmpty(processList) && !userId().equals(processList.get(0).getOperator())){
            throw new OwlBizException("流程发起人为“"+processList.get(0).getOperatorName()+"”，非流程发起人不允许发起流程！");
        }
        //校验数据
        checkSubmit(settlement);
        //发起流程
        String fwSSoUrl = null;
        try {
            fwSSoUrl = createProcess(settlement);
        } catch (Exception e) {
            log.info("确收审批流程发起异常：{}",e.getMessage());
            throw new OwlBizException("OA流程发起超时，请稍后重试！");
        }
        //更新确收单审批状态
        ContractIncomeSettlementConcludeE concludeE = new ContractIncomeSettlementConcludeE();
        concludeE.setId(settlement.getId());
        concludeE.setReviewStatus(ReviewStatusEnum.审批中.getCode());
        contractIncomeSettlementConcludeMapper.updateById(concludeE);
        return fwSSoUrl;
    }

    /**
     * 校验是否能发起流程
     *
     * @param settlement
     */
    private void checkSubmit(ContractIncomeSettlementConcludeE settlement) {
        if (ReviewStatusEnum.审批中.getCode().equals(settlement.getReviewStatus())) {
            throw new OwlBizException("当前确收单已经处于审批中,无需再发起审批");
        }
        //查询该合同下是否有其它审批中的确收单
        long unFinishedCount = this.count(Wrappers.<ContractIncomeSettlementConcludeE>lambdaQuery()
                .eq(ContractIncomeSettlementConcludeE::getContractId, settlement.getContractId())
                .eq(ContractIncomeSettlementConcludeE::getReviewStatus, ReviewStatusEnum.审批中.getCode())
                .eq(ContractIncomeSettlementConcludeE::getDeleted, 0));
        if (unFinishedCount > 0) {
            throw new OwlBizException("该合同下已存在审批中的确收单");
        }
    }

    /**
     * 创建流程
     *
     * @param settlement
     * @return
     */
    private String createProcess(ContractIncomeSettlementConcludeE settlement) {
        ContractProcessRecordE record = contractProcessRecordMapper.selectOne(Wrappers.<ContractProcessRecordE>lambdaQuery()
                .eq(ContractProcessRecordE::getContractId, settlement.getId())
                .eq(ContractProcessRecordE::getType, ContractConcludeEnum.INCOME_SETTLEMENT.getCode())
                .eq(ContractProcessRecordE::getDeleted, 0));
        if (ObjectUtils.isNotEmpty(record) && StringUtils.isNotBlank(record.getProcessId())) {
            BusinessInfoF businessInfoF = buildBusinessInfoF(settlement);
            businessInfoF.setProcessId(record.getProcessId());
            log.info("确收单更新审批表单数据:{}", JSON.toJSONString(businessInfoF));
            //响应结构保持不变
            ProcessCreateV processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
            if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
                log.info("收款单流程更新失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
                record.setReviewStatus(ReviewStatusEnum.已拒绝.getCode());
                contractProcessRecordMapper.updateById(record);
                throw new OwlBizException("流程更新失败");
            }
            //流程正常发起了，将流程记录表的审批状态修改为"审批中"
            record.setReviewStatus(ReviewStatusEnum.审批中.getCode());
            contractProcessRecordMapper.updateById(record);
            //流程名更新成功后再继续原逻辑
            log.info("当前结算单已经存在流程数据:{}", JSON.toJSONString(record));
            return validateFw(record.getProcessId());
        }
        BusinessInfoF businessInfoF = buildBusinessInfoF(settlement);
        log.info("确收单新增审批表单数据:{}", JSON.toJSONString(businessInfoF));
        ProcessCreateV processCreateV = null;
        if (isDev) {
            ProcessCreateReturnV ES_RETURN = new ProcessCreateReturnV();
            ES_RETURN.setZZSTAT("S");
            ProcessCreateResultV ET_RESULT = new ProcessCreateResultV();
            ET_RESULT.setRequestid("mock requestId");
            processCreateV = new ProcessCreateV();
            processCreateV.setES_RETURN(ES_RETURN);
            processCreateV.setET_RESULT(ET_RESULT);
        } else {
            processCreateV = externalFeignClient.wfApproveCreate(businessInfoF);
        }
        if (ObjectUtils.isEmpty(processCreateV)) {
            throw new OwlBizException("获取的流程数据为空,创建流程失败");
        }
        if (!"S".equals(processCreateV.getES_RETURN().getZZSTAT())) {
            log.info("支出合同流程创建失败，原因：{}", processCreateV.getES_RETURN().getZZMSG());
            throw new OwlBizException(processCreateV.getES_RETURN().getZZMSG());
        }
        String requestId = processCreateV.getET_RESULT().getRequestid();
        if (StringUtils.isBlank(requestId)) {
            throw new OwlBizException("获取到的流程id为空");
        }

        ContractProcessRecordE contractProcessRecordE = Builder.of(ContractProcessRecordE::new)
                .with(ContractProcessRecordE::setProcessId, requestId)
                .with(ContractProcessRecordE::setContractId, settlement.getId())
                .with(ContractProcessRecordE::setReviewStatus, ReviewStatusEnum.审批中.getCode())
                .with(ContractProcessRecordE::setTenantId, settlement.getTenantId())
                .with(ContractProcessRecordE::setCreator, settlement.getCreator())
                .with(ContractProcessRecordE::setCreatorName, settlement.getCreatorName())
                .with(ContractProcessRecordE::setType, ContractConcludeEnum.INCOME_SETTLEMENT.getCode())
                .build();

        ContractProcessRecordE recordE = contractProcessRecordMapper.selectOne(Wrappers.<ContractProcessRecordE>lambdaQuery()
                .eq(ContractProcessRecordE::getProcessId, requestId)
                .eq(ContractProcessRecordE::getDeleted, 0));
        if (ObjectUtils.isNotEmpty(recordE)) {
            log.info("返回的支出流程已存在,进行流程数据更新");
            contractProcessRecordE.setId(recordE.getId());
            contractProcessRecordMapper.updateById(contractProcessRecordE);
        } else {
            contractProcessRecordMapper.insert(contractProcessRecordE);
        }

        return validateFw(requestId);
    }

    private BusinessInfoF buildBusinessInfoF(ContractIncomeSettlementConcludeE settlement) {
        BusinessInfoF businessInfoF = new BusinessInfoF();
        //基本参数，flowType还是9999，formType设置为4
        businessInfoF.setFormDataId(settlement.getId());
        businessInfoF.setEditFlag(0);
        businessInfoF.setFormType(ContractConcludeEnum.INCOME_SETTLEMENT.getCode());
        businessInfoF.setFlowType(bizCode);
        businessInfoF.setContractName(settlement.getTitle());
        //下面是表单参数
        businessInfoF.setSsqy(settlement.getBelongRegion());
        businessInfoF.setSrlx(settlement.getSettlementClassify());

        return businessInfoF;
    }

    private String validateFw(String requestId){
        FwSSoBaseInfoF fwSSoBaseInfoF = new FwSSoBaseInfoF();
        UserInfoRv s = userFeignClient.getUsreInfoByUserId(userId());
        if (ObjectUtils.isNotEmpty(s)) {
            fwSSoBaseInfoF.setUSERCODE(externalFeignClient.getIphoneByEmpCode(s.getMobileNum()));
            fwSSoBaseInfoF.setDEPTCODE(externalFeignClient.getDepetByEmpCode(s.getMobileNum()));
        }
        fwSSoBaseInfoF.setRequestId(requestId);
        return externalFeignClient.validateFw(fwSSoBaseInfoF);
    }

    /**
     * @param id 根据Id反审核
     */
    public void returnId(String id) {
        ContractIncomeSettlementConcludeE map = contractIncomeSettlementConcludeMapper.selectById(id);
        map.setReviewStatus(0);
        contractIncomeSettlementConcludeMapper.updateById(map);
    }

    private QueryWrapper<ContractIncomeSettlementConcludePageF> conditionPage(QueryWrapper<ContractIncomeSettlementConcludePageF> queryModel, String tenantId) {
        queryModel.eq("cck.deleted", 0);
        queryModel.eq("cck.tenantId", tenantId);
        return queryModel;
    }

    /**
     * @param contractSettlementsBillF 参数
     */
    public String invoice(ContractSettlementsBillF contractSettlementsBillF) {
        ContractSettlementsBillE map = Global.mapperFacade.map(contractSettlementsBillF, ContractSettlementsBillE.class);
        map.setTenantId(tenantId());
        contractPayBillMapper.insert(map);
        ContractIncomeSettlementConcludeE map1 = contractIncomeSettlementConcludeMapper.selectById(contractSettlementsBillF.getSettlementId());
        map1.setInvoiceApplyAmount(map1.getInvoiceApplyAmount().add(contractSettlementsBillF.getAmount()));
        contractIncomeSettlementConcludeMapper.updateById(map1);
        return map.getId();
    }

    /**
     * @param contractSettlementsFundF 参数
     */
    public String setFund(ContractSettlementsFundF contractSettlementsFundF) {
        ContractSettlementsFundE map = Global.mapperFacade.map(contractSettlementsFundF, ContractSettlementsFundE.class);
        map.setTenantId(tenantId());
        map.setPayNotecode("ZC" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date()));
        contractSettlementsFundMapper.insert(map);
        ContractIncomeSettlementConcludeE map1 = contractIncomeSettlementConcludeMapper.selectById(contractSettlementsFundF.getSettlementId());
        map1.setPaymentAmount(map1.getPaymentAmount().add(contractSettlementsFundF.getAmount()));
        contractIncomeSettlementConcludeMapper.updateById(map1);
        return map.getId();
    }

    /**
     * 批量保存图片
     */
    private String savePics(List<FileVo> fileVoList, String businessId, String tenantId) {
        if (fileVoList == null || fileVoList.isEmpty()) {
            return null;
        }

        List<FormalF> formalFs = generateFormalFs(fileVoList, businessId, tenantId);
        List<String> fileList = new ArrayList<>();
        for (int i = 0; i < fileVoList.size(); i++) {
            FileVo fileVo = fileVoList.get(i);
            fileList.add(fileVo.getType() == 0 ? fileStorage.submit(fileVo, formalFs.get(i)).getFileKey() : fileVo.getFileKey());
        }
        return String.join(COMMA, fileList);
    }

    /**
     * 生成FormalF列表方法
     */
    private List<FormalF> generateFormalFs(List<FileVo> fileVoList, String businessId, String tenantId) {
        int size;
        if (fileVoList == null || (size = fileVoList.size()) == 0) {
            return Collections.emptyList();
        }
        List<FormalF> formalFs = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            FormalF formalF = FormalF.builder()
                    .serverName(CONTRACT_INCOME_CONCLUDE_SETTLEMENT)
                    .clazz(ContractIncomeSettlementConcludeE.class)
                    .businessId(businessId + i)
                    .tenantId(tenantId)
                    .build();
            formalFs.add(formalF);
        }
        return formalFs;
    }

    public ContractIncomePlanPeriodV getPlanPeriod(String contractId) {
        List<IncomePlanPeriodV> payPlanPeriodList = contractIncomePlanConcludeMapper.getPlanPeriod(contractId);
        return ContractIncomePlanPeriodV.builder().incomePlanPeriodList(payPlanPeriodList).build();
    }

    public List<ContractIncomePlanForSettlementV> getPlanList(ContractIncomePlanListF contractIncomePlanListF) {
        List<IncomePlanPeriodF> periodList = contractIncomePlanListF.getPeriodList();
        // 获取periodList最大的结束时间
        Date endDate = periodList.stream().map(IncomePlanPeriodF::getEndDate).max(Date::compareTo).orElse(null);
        List<ContractIncomePlanForSettlementV> originPlanList = contractIncomePlanConcludeMapper.getOriginPlanList(contractIncomePlanListF.getContractId(),periodList);
        if (CollectionUtils.isEmpty(originPlanList)){
            return Collections.emptyList();
        }
        Map<Integer, Date> minStartTimeBySplitMode = originPlanList.stream()
                .filter(plan -> plan.getSplitMode() != null && plan.getCostStartTime() != null)
                .collect(Collectors.groupingBy(
                        ContractIncomePlanForSettlementV::getSplitMode,
                        Collectors.collectingAndThen(
                                Collectors.minBy(Comparator.comparing(ContractIncomePlanForSettlementV::getCostStartTime)),
                                optional -> optional.map(ContractIncomePlanForSettlementV::getCostStartTime).orElse(null)
                        )
                ));
        for (Map.Entry<Integer, Date> entry : minStartTimeBySplitMode.entrySet()) {
            Integer splitMode = entry.getKey();
            Date minStartTime = entry.getValue();
            List<ContractIncomePlanForSettlementV> originPlanBefarList = contractIncomePlanConcludeMapper.getOriginPlanDateList(contractIncomePlanListF.getContractId(),splitMode,minStartTime);
            if(CollectionUtils.isNotEmpty(originPlanBefarList)){
                originPlanList.addAll(originPlanBefarList);
            }
        }
        originPlanList.sort(Comparator.nullsLast(
                Comparator.comparing(
                        ContractIncomePlanForSettlementV::getCostStartTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                )
        ));
        // 20250520 需求 by@YK1730084330JrsAA 李琦, 若某一组的计划都处于部分核销/全额核销,  则该组计划不返回
        List<String> notSettlePlanGroupList = contractIncomePlanConcludeMapper.notSettlementGroup(contractIncomePlanListF.getContractId());
        originPlanList.removeIf(e -> !notSettlePlanGroupList.contains(e.getSettlePlanGroup()));
        if (CollectionUtils.isEmpty(originPlanList)){
            return Collections.emptyList();
        }
        //查询该合同对应计划下收入计划数据
        LambdaQueryWrapper<PayIncomePlanE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PayIncomePlanE::getPlanId,originPlanList.stream().map(ContractIncomePlanForSettlementV :: getPlanId).collect(Collectors.toList()))
                .eq(PayIncomePlanE::getContractId,contractIncomePlanListF.getContractId())
                .eq(PayIncomePlanE::getDeleted, 0);
        List<PayIncomePlanE> payIncomePlanList = contractPayIncomePlanMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(payIncomePlanList)){
            return originPlanList;
        }
        Set<String> payIncomePlanIds = payIncomePlanList.stream()
                .map(PayIncomePlanE::getPlanId)
                .collect(Collectors.toSet());
        originPlanList.forEach(plan -> {
            boolean exists = payIncomePlanIds.contains(plan.getPlanId());
            plan.setIsHaveIncomePlan(exists);
        });
        return originPlanList;
    }

    public PageV<ContractIncomeSettdeductionDetailV> contractSettdeductionDetailPage(PageF<SearchF<?>> request) {
        PageF<SearchF<?>> pageConditions = new PageF<>();
        BeanUtils.copyProperties(request, pageConditions);
        pageConditions.setPageNum(1);
        pageConditions.setPageSize(Integer.MAX_VALUE);
        PageV<ContractIncomeSettlementPageV2> settlementPage = this.pageV2(pageConditions);
        if (Objects.isNull(settlementPage) || CollectionUtils.isEmpty(settlementPage.getRecords())) {
            return PageV.of(request, 0, new ArrayList<>());
        }
        List<String> settlementIds = settlementPage.getRecords().stream()
                .map(ContractIncomeSettlementPageV2::getChildren)
                .flatMap(Collection::stream)
                .map(ContractIncomeSettlementPageV2::getId)
                .distinct()
                .collect(Collectors.toList());
        Page<?> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());
        IPage<ContractIncomeSettdeductionDetailV> pageList = contractIncomeConcludeSettdeductionMapper
                .contractIncomeSettdeductionDetailPage(pageF, settlementIds);
        if (Objects.isNull(pageList) || CollectionUtils.isEmpty(pageList.getRecords())) {
            return PageV.of(request, 0, new ArrayList<>());
        }
        //处理periodDisplay+type字段
        List<DictionaryCode> dictionaryCodes = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.款项业务类型.getCode(), null);
        dictionaryCodes.addAll(customDictionaryCode());
        Map<String, DictionaryCode> dictionaryCodeMap = dictionaryCodes.stream()
                .collect(Collectors.toMap(DictionaryCode::getCode, Function.identity(), (v1, v2) -> v1));
        pageList.getRecords().forEach(detail -> {
            if (CollectionUtils.isNotEmpty(detail.getPeriods())) {
                List<String> dateList = detail.getPeriods().stream().map(p -> {
                    String start = DateUtil.format(p.getStartDate(), "yyyy.MM.dd");
                    String end = DateUtil.format(p.getEndDate(), "yyyy.MM.dd");
                    return start + "-" + end;
                }).collect(Collectors.toList());
                detail.setPeriodDisplay(String.join(",", dateList));
                if (dictionaryCodeMap.containsKey(detail.getTypeId())) {
                    detail.setType(dictionaryCodeMap.get(detail.getTypeId()).getName());
                }
            }
        });

        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    /**
     * hotfix：增加"品质扣款"、"缺勤扣款"、"其他扣款"三个项目
     *
     * @return
     */
    private List<DictionaryCode> customDictionaryCode() {
        List<DictionaryCode> list = new ArrayList<>();
        DictionaryCode d1 = new DictionaryCode();
        d1.setCode("100");
        d1.setName("品质扣款");
        list.add(d1);

        DictionaryCode d2 = new DictionaryCode();
        d2.setCode("99");
        d2.setName("缺勤扣款");
        list.add(d2);

        DictionaryCode d3 = new DictionaryCode();
        d3.setCode("98");
        d3.setName("其他扣款");
        list.add(d3);

        return list;
    }

    public ContractIncomeSettlementConcludeEditV getEditInfo(ContractIncomeSettlementConcludeListF contractIncomePlanConcludeListF) {
        ContractIncomeSettlementConcludeEditV contractPaySettlementConcludeEditV = new ContractIncomeSettlementConcludeEditV();

        ContractIncomeSettlementConcludeE concludeE = contractIncomeSettlementConcludeMapper.selectById(contractIncomePlanConcludeListF.getId());
        ContractPaySettlementConcludeInfoV contractIncomeSettlementConcludeInfoV = Global.mapperFacade.map(concludeE, ContractPaySettlementConcludeInfoV.class);
        ContractIncomeSettlementConcludeV contractPaySettlementConcludeV = Global.mapperFacade.map(concludeE, ContractIncomeSettlementConcludeV.class);

        ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.queryByContractId(concludeE.getContractId());
        contractPaySettlementConcludeV.setChangContractAmount(contractIncomeConcludeE.getChangContractAmount());
        contractPaySettlementConcludeEditV.setContractIncomeSettlementConcludeV(contractPaySettlementConcludeV);

        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList = contractIncomePlanConcludeMapper.getHowOrder(contractIncomePlanConcludeListF.getContractId(), null);
        contractPaySettlementConcludeEditV.setContractIncomePlanConcludeVList(contractIncomePlanConcludeVList);

        List<ContractIncomeSettDetailsV> contractIncomeSettDetailsVList = contractIncomeSettDetailsService.getDetailsBySettlementId(contractIncomePlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractIncomeSettDetailsVList(contractIncomeSettDetailsVList);

        List<ContractIncomeConcludeSettdeductionV> contractIncomeConcludeSettdeductionVList = contractIncomeConcludeSettdeductionService.getDetailsBySettlementId(contractIncomePlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractIncomeConcludeSettdeductionVList(contractIncomeConcludeSettdeductionVList);

        List<ContractIncomePlanForSettlementV> contractIncomePlanForSettlementVList = settlementPlanRelationMapper.getPlanList(contractIncomePlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractIncomePlanForSettlementVList(contractIncomePlanForSettlementVList);

        List<IncomePlanPeriodV> contractIncomeSettlementPeriodVList = settlementPeriodMapper.getPeriodList(contractIncomePlanConcludeListF.getId());
        contractPaySettlementConcludeEditV.setContractIncomeSettlementPeriodVList(contractIncomeSettlementPeriodVList);
        return contractPaySettlementConcludeEditV;
    }

    private static String listToInClause(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        return list.stream()
//                .map(item -> "'" + item + "'")
                .collect(Collectors.joining(","));
    }

    /**
     * 确收单-列表查询-V2
     **/
    public PageV<ContractIncomeSettlementPageV2> pageV2(PageF<SearchF<?>> request) {
        ContractOrgPermissionV orgPermissionV = contractOrgCommonService.queryAuthOrgIds(userId(), orgIds());
        if (RadioEnum.NONE.equals(orgPermissionV.getRadio())) {
            return PageV.of(request, 0, Collections.emptyList());
        }

        //合同业务线
        Field lineSelectField = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "contractBusinessLine".equals(field.getName()))
                .findFirst().orElse(null);
        if(Objects.nonNull(lineSelectField)){
            if(lineSelectField.getValue().equals(ContractBusinessLineEnum.全部.getCode())){
                request.getConditions().getFields().removeIf(e -> e.getName().equals("contractBusinessLine"));
            }
        }

        Page<?> pageF = Page.of(request.getPageNum(), request.getPageSize(), request.isCount());

        Field type1Field = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "cpe.conmanagetype".equals(field.getName()))
                .findFirst().orElse(null);
        Field type2Field = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) && "cpe.conincrementype".equals(field.getName()))
                .findFirst().orElse(null);
        QueryWrapper<?> queryModel = request.getConditions().getQueryModel();

        if (Objects.nonNull(type1Field) && Objects.nonNull(type2Field)){
            List<String> type1Values = (List<String>) type1Field.getValue();
            List<String> type2Values = (List<String>) type2Field.getValue();
            if (CollectionUtils.isNotEmpty(type1Values) &&
                    CollectionUtils.isNotEmpty(type2Values) &&
                    type1Values.size() > 1){
                // 同时传递增值和其他的话 删除条件,自行拼接sql
                request.getConditions().getFields().removeIf(e -> e.getName().startsWith("c.") || e.getName().startsWith("cpe."));
                queryModel = request.getConditions().getQueryModel();
                type1Values.removeIf("4"::equals);
                StringBuilder sb = new StringBuilder();
                sb.append("( (cpe.conmanagetype = '4' and cpe.conincrementype in (");
                for (String value : type2Values) {
                    sb.append("'").append(value).append("',");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append(") ) or cpe.conmanagetype in (");
                for (String value : type1Values) {
                    sb.append("'").append(value).append("',");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append("))");
                // 这里用param拼接会有问题，直接拼好sql
                queryModel.apply(sb.toString());
            }
        }


        if (RadioEnum.APPOINT.equals(orgPermissionV.getRadio()) && CollectionUtils.isNotEmpty(orgPermissionV.getOrgIds())) {
            queryModel.in("c.departId", orgPermissionV.getOrgIds());
        }
        queryModel.ne("s.pid",'0');
        queryModel.eq("s.deleted",0);
        queryModel.groupBy("s.pid");
        queryModel.orderByDesc("MAX(s.gmtCreate)");
        // 根据搜索条件查询出pid!=0的确收单的pid - 锁定合同
        IPage<String> pids = contractIncomeSettlementConcludeMapper.getPidsByCondition(pageF, queryModel);
        if(pids.getRecords().size() == 0){
            return PageV.of(request, 0, Collections.emptyList());
        }
        // 这里去掉合同的搜索条件，因为已经锁定了pid
        request.getConditions().getFields().removeIf(e -> e.getName().startsWith("c.") || e.getName().startsWith("cpe."));
        request.getConditions().getFields().removeIf(e -> e.getName().contains("contractBusinessLine"));

        QueryWrapper<?> queryModel2 = request.getConditions().getQueryModel();
        queryModel2.eq("s.deleted",0);
        List<ContractIncomeSettlementPageV2> list = contractIncomeSettlementConcludeMapper.selectPageV2ByPids(pids.getRecords(), queryModel2);
        // 获取这些结算单的pid[因为最多只有两级],对应的就是合同的数据
        List<String> settlementIds = list.stream().map(Tree::getId).collect(Collectors.toList());
        List<String> planIds = Lists.newArrayList();
        Map<String,List<String>> contractToPlanIdsMap = new HashMap<>();

        Field payableTimeCondition = request.getConditions().getFields()
                .stream().filter(field -> StringUtils.isNotBlank(field.getName()) &&"p.plannedCollectionTime".equals(field.getName()))
                .findFirst().orElse(null);


        fillPeriodAndTermDateInfoWithAssemblySummaryData(list, settlementIds,planIds,contractToPlanIdsMap,payableTimeCondition);
        // 合同的数据查询
        List<ContractIncomeSettlementPageV2> contractList = getContractIncomeSettlementPageV2List(pids);
        summaryCalculateForContract(planIds, contractToPlanIdsMap, contractList);

        // 手动树形组装 - 避免框架代码排序混乱
        Map<String, List<ContractIncomeSettlementPageV2>> listMap = list.stream().collect(Collectors.groupingBy(ContractIncomeSettlementPageV2::getPid));
        for (ContractIncomeSettlementPageV2 contractPageV2 : contractList) {
            //节点-处理时间拼接
            contractPageV2.setContractStartEndDisplay(combineDate(contractPageV2.getContractStartDate(), contractPageV2.getContractEndDate()));
            contractPageV2.setRangeUnsettledStartEndDisplay(combineDate(contractPageV2.getRangeUnsettledStartDate(), contractPageV2.getRangeUnsettledEndDate()));
            List<ContractIncomeSettlementPageV2> curList = listMap.get(contractPageV2.getId());
            if (CollectionUtils.isNotEmpty(curList)) {
                //子节点处理时间拼接
                curList.forEach(item -> {
                    item.setContractStartEndDisplay(combineDate(item.getContractStartDate(), item.getContractEndDate()));
                    item.setRangeUnsettledStartEndDisplay(combineDate(item.getRangeUnsettledStartDate(), item.getRangeUnsettledEndDate()));
                });
                // list 按照 创建时间倒排序
                curList.sort(Comparator.comparing(ContractIncomeSettlementPageV2::getGmtCreate).reversed());
                BigDecimal rangeToSettleAmountFromSettlement =
                        curList.stream().map(ContractIncomeSettlementPageV2::getAmountPayable).reduce(BigDecimal.ZERO, BigDecimal::add);
                contractPageV2.setRangeToSettleAmountFromSettlement(rangeToSettleAmountFromSettlement);
                BigDecimal rangeDeductAmountFromSettlement =
                        curList.stream().map(ContractIncomeSettlementPageV2::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                contractPageV2.setRangeDeductAmountFromSettlement(rangeDeductAmountFromSettlement);
                BigDecimal rangeActualSettleAmountFromSettlement =
                        rangeToSettleAmountFromSettlement.subtract(rangeDeductAmountFromSettlement);
                contractPageV2.setRangeActualSettleAmountFromSettlement(rangeActualSettleAmountFromSettlement);
            }
            contractPageV2.setChildren(curList);
        }

        return PageV.of(request, pids.getTotal(), contractList);
    }

    private void summaryCalculateForContract(List<String> planIds,
                                             Map<String, List<String>> contractToPlanIdsMap,
                                             List<ContractIncomeSettlementPageV2> contractList) {
        QueryWrapper<ContractIncomePlanConcludeE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractIncomePlanConcludeE.DELETED, 0)
                .in(ContractPayPlanConcludeE.ID, planIds);
        List<ContractIncomePlanConcludeE> planEntityList;
        if (CollectionUtils.isEmpty(planIds)){
            planEntityList = Collections.emptyList();
        } else {
            planEntityList = contractIncomePlanConcludeMapper.selectList(queryWrapper);
        }
        Map<String, ContractIncomePlanConcludeE> planIdToEntityMap = planEntityList.stream()
                .collect(Collectors.toMap(ContractIncomePlanConcludeE::getId, e -> e));
        for (ContractIncomeSettlementPageV2 contractPageV : contractList) {
            List<String> curPlanIdList = contractToPlanIdsMap.get(contractPageV.getContractId());
            if (CollectionUtils.isEmpty(curPlanIdList)){
                contractPageV.setRangeAmountPayable(BigDecimal.ZERO);
                contractPageV.setRangeSettledAmount(BigDecimal.ZERO);
                contractPageV.setRangeUnsettledAmount(BigDecimal.ZERO);
                contractPageV.setRangeUnsettledStartDate(null);
                contractPageV.setRangeUnsettledEndDate(null);
                contractPageV.setRangeUnsettledTermDate(null);
            } else {
                BigDecimal curRangeAmountPayable = BigDecimal.ZERO;
                BigDecimal curRangeSettledAmount = BigDecimal.ZERO;
                BigDecimal curUnRangeSettledAmount = BigDecimal.ZERO;
                Date curRangeUnsettledStartDate = null;
                Date curRangeUnsettledEndDate = null;
                List<Integer> curRangeUnsettledTermDateList = Lists.newArrayList();
                // 拿出所有的成本预估计划，并按照费项目+清单项目+税率分组，后续对照使用
                List<ContractIncomePlanConcludeE> curPlanEntityList = planEntityList.stream().filter(e -> curPlanIdList.contains(e.getId())).collect(Collectors.toList());
                Map<String, List<ContractIncomePlanConcludeE>> curContractPlanGroup = curPlanEntityList.stream()
                        .collect(Collectors.groupingBy(e -> e.getChargeItemId() + "_" + e.getServiceType() + "_" + e.getTaxRateId()));
                int groupSize = curContractPlanGroup.size();
                Map<Integer,List<SettlementPlanBO>> boMap = Maps.newHashMap();
                Map<String,Integer> groupInfoToTermCountMap = Maps.newHashMap();
                for (String curPlanId : curPlanIdList) {
                    ContractIncomePlanConcludeE curPayPlan = planIdToEntityMap.get(curPlanId);
                    // 应结算金额 [都加]
                    curRangeAmountPayable = curRangeAmountPayable.add(curPayPlan.getPlannedCollectionAmount());
                    // 已结算金额 [都加]
                    curRangeSettledAmount = curRangeSettledAmount.add(curPayPlan.getSettlementAmount());
                    // 未结算金额 [都加]
                    if (Objects.nonNull(curPayPlan.getPaymentStatus()) && curPayPlan.getPaymentStatus() != 2){
                        curUnRangeSettledAmount = curUnRangeSettledAmount.add(curPayPlan.getPlannedCollectionAmount().subtract(curPayPlan.getSettlementAmount()));
                    }
                    if (Objects.nonNull(curPayPlan.getPaymentStatus()) && curPayPlan.getPaymentStatus() == 0){
                        // 维护分组的未结算期数
                        String key = curPayPlan.getChargeItemId() + "_" + curPayPlan.getServiceType() + "_" + curPayPlan.getTaxRateId();
                        Integer unSettleCnt = groupInfoToTermCountMap.getOrDefault(key, 0);
                        groupInfoToTermCountMap.put(key, unSettleCnt + 1);

                        // 维护期数分组
                        List<SettlementPlanBO> curBoList = boMap.getOrDefault(curPayPlan.getTermDate(), Lists.newArrayList());
                        curBoList.add(new SettlementPlanBO(curPayPlan.getCostStartTime(), curPayPlan.getCostEndTime()));
                        boMap.put(curPayPlan.getTermDate(), curBoList);
                        // 未结算周期结束时间 = 未结算的最大结束时间
                        if (Objects.nonNull(curPayPlan.getCostEndTime())){
                            Date date = Date.from(curPayPlan.getCostEndTime().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                            if (Objects.isNull(curRangeUnsettledEndDate)){
                                curRangeUnsettledEndDate = date;
                            } else if (date.after(curRangeUnsettledEndDate)){
                                curRangeUnsettledEndDate = date;
                            }
                        }
                    }
                }
                // 未结算周期开始时间 = 均未结算 的最小期数 的最大开始时间
                List<SettlementPlanBO> minTermBoList = boMap.entrySet().stream().filter(entry -> entry.getValue().size() == groupSize)
                        .min(Comparator.comparingInt(Map.Entry::getKey))
                        .map(Map.Entry::getValue)
                        .orElse(Lists.newArrayList());
                if (minTermBoList.size() > 0){
                    // 取最大的开始时间
                    curRangeUnsettledStartDate = minTermBoList.stream()
                            .filter(e -> Objects.nonNull(e.getStartTime()))
                            .map(e -> Date.from(e.getStartTime().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))
                            .max(Date::compareTo).orElse(null);
                }
                // 未结算期数合计 = 分组后,count(未结算) 的最小值
                // 取groupInfoToTermCountMap的value的最小值
                contractPageV.setRangeAmountPayable(curRangeAmountPayable);
                contractPageV.setRangeSettledAmount(curRangeSettledAmount);
                contractPageV.setRangeUnsettledAmount(curUnRangeSettledAmount);
                contractPageV.setRangeUnsettledStartDate(curRangeUnsettledStartDate);
                contractPageV.setRangeUnsettledEndDate(curRangeUnsettledEndDate);
                contractPageV.setRangeUnsettledTermDate(groupInfoToTermCountMap.values().stream().min(Integer::compareTo).orElse(null));
            }
        }
    }

    private List<ContractIncomeSettlementPageV2> getContractIncomeSettlementPageV2List(IPage<String> pids) {
        QueryWrapper<ContractIncomeSettlementConcludeE> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(ContractIncomeSettlementConcludeE.DELETED, 0)
                .eq(ContractIncomeSettlementConcludeE.PID,0)
                .in(ContractIncomeSettlementConcludeE.ID, pids.getRecords());
        List<ContractIncomeSettlementConcludeE> contractSettlementList =
                contractIncomeSettlementConcludeMapper.selectList(queryWrapper1);
        if (CollectionUtils.isEmpty(contractSettlementList)){
            throw new OwlBizException("结算单数据异常,缺失合同数据");
        }
        List<String> contractIdList = contractSettlementList.stream()
                .map(ContractIncomeSettlementConcludeE::getContractId)
                .filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<ContractIncomeSettlementPageV2> contractList =
                contractIncomeSettlementConcludeMapper.selectPageV2OfContract(contractIdList);
        // 合同对象要组装id 和 pid
        Map<String, String> contractIdToSettlementIdMap = contractSettlementList.stream()
                .collect(Collectors.toMap(ContractIncomeSettlementConcludeE::getContractId, ContractIncomeSettlementConcludeE::getId));

        List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收入合同管理类别.getCode(), null);
        Map<String,DictionaryCode> dicMap = value.stream().collect(Collectors.toMap(DictionaryCode::getCode,v->v,(v1,v2)->v1));

        contractList.forEach(e -> {
            e.setId(contractIdToSettlementIdMap.get(e.getContractId()));
            e.setPid("0");
            e.setContractStatusName(ContractRevStatusEnum.parseName(e.getContractStatusCode()));
            e.setContractCategoryName(Objects.nonNull(dicMap.get(e.getConManageType())) ? dicMap.get(e.getConManageType()).getName() : null);
            e.setContractBusinessLineName(ContractBusinessLineEnum.parseName(e.getContractBusinessLine()));
            if (StringUtils.isNotBlank(e.getSplitMode())){
                Set<String> splitModeName = Sets.newHashSet();
                for (String s : e.getSplitMode().split(",")) {
                    try{
                        splitModeName.add(SplitModeEnum.parseName(Integer.valueOf(s)));
                    } catch (Exception ex){
                        log.error("合同结算周期转换异常,合同结算周期:{}", s);
                    }
                }
                e.setSplitModeName(String.join(",", splitModeName));
            }
        });
        // 使用合同对象的id 按照pids的顺序对合同进行排序
        List<String> sortedContractIdList = pids.getRecords();
        contractList.sort((o1, o2) -> {
            int io1 = sortedContractIdList.indexOf(o1.getId());
            int io2 = sortedContractIdList.indexOf(o2.getId());
            if (io1 != -1){
                io1 = sortedContractIdList.size() - io1;
            }
            if (io2 != -1){
                io2 = sortedContractIdList.size() - io2;
            }
            return io2 - io1;
        });
        return contractList;
    }

    /**
     * 字段补全，期数=清单项-费项-期数{逗号分隔}, 计量周期= 开始~结束{逗号分隔}
     **/
    private void fillPeriodAndTermDateInfoWithAssemblySummaryData(List<ContractIncomeSettlementPageV2> list,
                                                                  List<String> settlementIds,
                                                                  List<String> planIds,
                                                                  Map<String, List<String>> contractToPlanIdsMap,
                                                                  Field payableTimeCondition) {
        List<SettlementSimpleStr> periodSimpleStrList = settlementPeriodMapper.getSimpleStrList(settlementIds);
        Map<String, String> settlementIdToPeriodStrMap = periodSimpleStrList.stream()
                .collect(Collectors.toMap(SettlementSimpleStr::getSettlementId, SettlementSimpleStr::getStr));

        List<SettlementSimpleStr> termDateSimpleStrList =  settlementPlanRelationMapper.getSimpleStrList(settlementIds);
        Map<String, String> settlementIdToTermDateStrMap = termDateSimpleStrList.stream()
                .collect(Collectors.toMap(SettlementSimpleStr::getSettlementId, SettlementSimpleStr::getStr));

        if (Objects.isNull(payableTimeCondition)){
            throw new OwlBizException("应结算日期必填");
        }
        Date plannedDateTimeStart = null;
        Date plannedDateTimeEnd = null;

        if (null != payableTimeCondition.getValue()) {
            LocalDateTime[] startAndEnd = WithinDateTimeEnum.valueOf(payableTimeCondition.getValue().toString()).startAndEnd();
            plannedDateTimeStart = Date.from(startAndEnd[0].atZone(ZoneId.systemDefault()).toInstant());
            plannedDateTimeEnd = Date.from(startAndEnd[1].atZone(ZoneId.systemDefault()).toInstant());
        } else if (payableTimeCondition.getMap() != null && !payableTimeCondition.getMap().isEmpty()) {
            plannedDateTimeStart = DateUtil.parse(payableTimeCondition.getMap().get("start").toString(),"yyyy-MM-dd");
            plannedDateTimeEnd = DateUtil.parse(payableTimeCondition.getMap().get("end").toString(),"yyyy-MM-dd");
        }

        if (Objects.isNull(plannedDateTimeStart) || Objects.isNull(plannedDateTimeEnd)){
            throw new OwlBizException("应结算日期的开始时间和结束时间必填");
        }
        // 筛选成本计划 只用应结算日期筛选
        List<SettlementSimpleStr> settlementIdToPlanIdStrList =
                settlementPlanRelationMapper.getSettlementPlanIdSimpleStrList(settlementIds, plannedDateTimeStart, plannedDateTimeEnd);
        Map<String, List<String>> settlementIdToPlanIdListMap = settlementIdToPlanIdStrList.stream()
                .filter(e -> StringUtils.isNotBlank(e.getStr()))
                .collect(Collectors.toMap(SettlementSimpleStr::getSettlementId, e -> Arrays.asList(e.getStr().split(","))));


        list.forEach(e -> {
            e.setPeriodsStr(settlementIdToPeriodStrMap.get(e.getId()));
            e.setTermDateStr(settlementIdToTermDateStrMap.get(e.getId()));
            e.setReviewStatusName(ReviewStatusEnum.parseName(e.getReviewStatus()));
            e.setPaymentStatusName(PaymentStatusEnum.parseName(e.getPaymentStatus()));
            e.setSettleStatusName(IncomeSettleStatusEnum.parseName(e.getSettleStatus()));

            List<String> curPlanIdList = settlementIdToPlanIdListMap.get(e.getId());
            if (CollectionUtils.isNotEmpty(curPlanIdList)){
                planIds.addAll(curPlanIdList);
                List<String> curPlanId = contractToPlanIdsMap.getOrDefault(e.getContractId(), Lists.newArrayList());
                curPlanId.addAll(curPlanIdList);
                curPlanId = curPlanId.stream().distinct().collect(Collectors.toList());
                contractToPlanIdsMap.put(e.getContractId(), curPlanId);
            }
        });
    }

    private String combineDate(Date startDate, Date endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            return null;
        }
        String startDateStr = DateUtil.format(startDate, DatePattern.NORM_DATE_PATTERN);
        String endDateStr = DateUtil.format(endDate, DatePattern.NORM_DATE_PATTERN);
        return startDateStr + "~" + endDateStr;
    }

    public List<String> checkSettleStatus(List<String> planIdList) {
        return contractIncomeSettlementConcludeMapper.checkSettleStatus(planIdList);
    }
    public List<String> getSettlementByPlan(List<String> planIdList) {
        return contractIncomeSettlementConcludeMapper.getSettlementByPlan(planIdList);
    }

    public void handleToReviewStatus(String id, Integer reviewStatus) {
        ContractIncomeSettlementConcludeE concludeE = new ContractIncomeSettlementConcludeE();
        concludeE.setReviewStatus(reviewStatus);
        concludeE.setId(id);
        this.updateById(concludeE);
    }

    public void handleApproveCompletedDataChange(String id) {
        ContractIncomeSettlementConcludeE concludeE=new ContractIncomeSettlementConcludeE();
        concludeE.setId(id);
        concludeE.setReviewStatus(ReviewStatusEnum.已通过.getCode());
        concludeE.setPlannedCollectionTime(LocalDate.now());
        concludeE.setSettleStatus(2);
        concludeE.setApproveCompletedTime(LocalDateTime.now());
        this.updateById(concludeE);
    }

    public void handleIncomePlanState(String settleId) {
        log.info("进入确收单核销逻辑,当前确收单id:{}", settleId);
        //查关联的结算计划id
        List<ContractIncomeConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractIncomeConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractIncomeConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return;
        }
        log.info("当前确收单关联的收款计划id集合信息:{}", JSON.toJSONString(relations));
        //查询周期信息
        List<IncomePlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return;
        }
        log.info("当前确收单关联的周期信息:{}", JSON.toJSONString(periodList));
        List<String> planIds = relations.stream()
                .map(ContractIncomeConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractIncomePlanConcludeE> planConcludes = contractIncomePlanConcludeService.queryByCostTime(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return;
        }
        //查关联的结算详情
        List<ContractIncomeSettDetailsE> settDetails = contractIncomeSettDetailsService.list(Wrappers.<ContractIncomeSettDetailsE>lambdaQuery()
                .eq(ContractIncomeSettDetailsE::getSettlementId, settleId)
                .eq(ContractIncomeSettDetailsE::getDeleted, 0));
        if (CollectionUtils.isEmpty(settDetails)) {
            return;
        }
        log.info("当前确收单关联的确收详情信息:{}", JSON.toJSONString(settDetails));
        //转换为分组
        Map<String, List<ContractIncomePlanConcludeE>> planConcludeMap = getPlanConcludeMap(planConcludes);
        log.info("根据收款计划id集合和周期信息匹配出来的收款计划详情:{}", JSON.toJSONString(planConcludeMap));
        //汇总所有收款计划id，查询全部的收入计划
        List<String> allIncomePlanIds = planConcludes.stream().map(ContractIncomePlanConcludeE::getId).collect(Collectors.toList());
        List<PayIncomePlanE> payIncomePlanES = contractPayIncomePlanService.list(Wrappers.<PayIncomePlanE>lambdaQuery()
                .in(PayIncomePlanE::getPlanId, allIncomePlanIds)
                .eq(PayIncomePlanE::getDeleted, 0));
        //转为map
        Map<String, List<PayIncomePlanE>> payIncomePlanMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(payIncomePlanES)) {
            payIncomePlanMap = payIncomePlanES.stream().collect(Collectors.groupingBy(PayIncomePlanE::getPlanId));
        }
        log.info("收款计划下的收入计划信息:{}", JSON.toJSONString(payIncomePlanMap));
        //遍历进行已结算金额处理
        for (ContractIncomeSettDetailsE detail : settDetails) {
            //获取分组key
            String groupKey = groupKey(detail);
            if (!planConcludeMap.containsKey(groupKey)) {
                continue;
            }
            //获取key对应的结算计划列表
            List<ContractIncomePlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
            //转bo-list，参与核销逻辑 后续使用
            List<PlanWriteOffBo> planWriteOffBoList = planConcludeList.stream()
                    .map(PlanWriteOffBo::transferByIncomePlan)
                    .collect(Collectors.toList());
            // bo 根据 planId 转map
            Map<String, PlanWriteOffBo> planWriteOffBoMap = planWriteOffBoList.stream()
                    .collect(Collectors.toMap(PlanWriteOffBo::getPlanId, Function.identity()));
            //当前结算明细金额
            BigDecimal amount = detail.getAmount();
            //按照原逻辑进行已结算金额和结算状态的维护
            for (ContractIncomePlanConcludeE concludeE : planConcludeList) {
                PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(concludeE.getId());
                if (concludeE.getPaymentStatus() == 2) {
                    //如果已经完成结算了就continue
                    continue;
                }
                if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) > 0) {
                    //amount减去当前处理的金额，多出的用于后续核销
                    amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                    concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                    concludeE.setPaymentStatus(2);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayIncomeFinished(1);
                    }
                } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) == 0) {
                    //amount减去当前处理的金额，其实就等于0了，amount全部用于核销
                    amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                    concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                    concludeE.setPaymentStatus(2);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayIncomeFinished(1);
                    }
                    break;
                } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) < 0) {
                    concludeE.setSettlementAmount(concludeE.getSettlementAmount().add(amount));
                    //amount核销不完，将amount累加到已结算金额后，将amount置为0
                    amount=BigDecimal.ZERO;
                    concludeE.setPaymentStatus(1);
                    if (Objects.nonNull(writeOffBo)){
                        writeOffBo.setWriteOffFlag(true);
                        concludeE.setPayIncomeFinished(1);
                    }
                    break;
                }
            }
            //测试下来没问题，核销结束后，若amount还大于0，直接加到每组最后一个的结算金额上
            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                ContractIncomePlanConcludeE contractIncomePlanConcludeE = planConcludeList.get(planConcludeList.size() - 1);
                log.info("核销完该组收款计划后amount金额还大于0,剩余金额:{},当前组最后一个收款计划当前已结算金额:{}", amount, contractIncomePlanConcludeE.getSettlementAmount());
                //保险起见还是加一个，虽然走到这里一定是从循环第二个if出来的
                contractIncomePlanConcludeE.setPaymentStatus(2);
                contractIncomePlanConcludeE.setSettlementAmount(contractIncomePlanConcludeE.getSettlementAmount().add(amount));
            }

            doHandleIncomePlanSettleCalculate(detail, planConcludeList, planWriteOffBoMap);
            //doHandleIncomeCostPlanV3(planWriteOffBoList,payIncomePlanMap);
        }
        List<PayIncomePlanE> payCostPlanNewList = new ArrayList<>();
        doHandlePayCostPlanV4(payIncomePlanES, settDetails, planConcludes, payCostPlanNewList);
        //批量更新
        contractIncomePlanConcludeService.updateBatchById(planConcludes);
        //更新成功后推送枫行梦
        ContractIncomeSettlementConcludeE concludeE = contractIncomeSettlementConcludeMapper.selectById(settleId);
        ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeMapper.selectById(concludeE.getContractId());
        List<ContractIncomeConcludePlanFxmReceiptRecordE> receiptRecords = contractInfoToFxmCommonService.pushIncomePlanReceipt2Fxm(planConcludes, incomeConcludeE);
        if (CollectionUtils.isNotEmpty(receiptRecords)) {
            fxmReceiptRecordService.saveBatch(receiptRecords);
        }
        if (CollectionUtils.isNotEmpty(payCostPlanNewList)) {
            contractPayIncomePlanService.updateBatchById(payCostPlanNewList);
        }
        //处理父级的结算金额，逻辑不变，plannedCollectionAmount字段就是该单子的结算总额，等于结算明细中amount字段的和
        List<ContractIncomePlanConcludeE> pidConcludeList = contractIncomePlanConcludeMapper.selectList(Wrappers.<ContractIncomePlanConcludeE>lambdaQuery()
                .eq(ContractIncomePlanConcludeE::getContractId, concludeE.getContractId())
                .eq(ContractIncomePlanConcludeE::getPid, "0")
                .eq(ContractIncomePlanConcludeE::getDeleted, 0));
        if(CollectionUtils.isNotEmpty(pidConcludeList)){
            ContractIncomePlanConcludeE updateConclude = new ContractIncomePlanConcludeE();
            updateConclude.setId(pidConcludeList.get(0).getId());
            updateConclude.setSettlementAmount(pidConcludeList.get(0).getSettlementAmount().add(concludeE.getPlannedCollectionAmount()));
            contractIncomePlanConcludeService.updateById(updateConclude);
        }
    }
    public void writeOffIncomePlanAndCost(String settleId) {
        log.info("进入确收单核销逻辑,当前确收单id:{}", settleId);

        ContractIncomeSettlementConcludeE incomeSettlement = contractIncomeSettlementConcludeMapper.selectById(settleId);

        //查关联的结算详情
        List<ContractIncomeSettDetailsE> settDetails = contractIncomeSettDetailsService.list(Wrappers.<ContractIncomeSettDetailsE>lambdaQuery()
                .eq(ContractIncomeSettDetailsE::getSettlementId, settleId)
                .eq(ContractIncomeSettDetailsE::getDeleted, 0));
        log.info("当前确收单关联的确收详情信息:{}", JSON.toJSONString(settDetails));
        if (CollectionUtils.isEmpty(settDetails)) {
            return;
        }
        List<String> funIdList = settDetails.stream()
                .map(ContractIncomeSettDetailsE::getPayFundId)
                .distinct()
                .collect(Collectors.toList());
        //查询周期信息
        List<IncomePlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        log.info("当前确收单关联的周期信息:{}", JSON.toJSONString(periodList));
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return;
        }

        //-------【核销收款计划金额】------
        List<ContractIncomeConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractIncomeConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractIncomeConcludeSettlementPlanRelationE::getSettlementId, settleId));
        log.info("当前确收单关联的收款计划id集合信息:{}", JSON.toJSONString(relations));
        if (CollectionUtils.isNotEmpty(relations)) {
            List<String> planIds = relations.stream()
                    .map(ContractIncomeConcludeSettlementPlanRelationE::getPlanId)
                    .distinct()
                    .collect(Collectors.toList());
            //根据周期信息查结算计划信息，之后的逻辑都不变
            List<ContractIncomePlanConcludeE> planConcludes = contractIncomePlanConcludeService.queryByCostTime(planIds, periodList);
            if (CollectionUtils.isNotEmpty(planConcludes)) {
                //转换为分组
                Map<String, List<ContractIncomePlanConcludeE>> planConcludeMap = getPlanConcludeMap(planConcludes);
                log.info("根据收款计划id集合和周期信息匹配出来的收款计划详情:{}", JSON.toJSONString(planConcludeMap));
                //遍历进行已结算金额处理
                for (ContractIncomeSettDetailsE detail : settDetails) {
                    //获取分组key
                    String groupKey = groupKey(detail);
                    if (!planConcludeMap.containsKey(groupKey)) {
                        continue;
                    }
                    //获取key对应的结算计划列表
                    List<ContractIncomePlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
                    //转bo-list，参与核销逻辑 后续使用
                    List<PlanWriteOffBo> planWriteOffBoList = planConcludeList.stream()
                            .map(PlanWriteOffBo::transferByIncomePlan)
                            .collect(Collectors.toList());
                    // bo 根据 planId 转map
                    Map<String, PlanWriteOffBo> planWriteOffBoMap = planWriteOffBoList.stream()
                            .collect(Collectors.toMap(PlanWriteOffBo::getPlanId, Function.identity()));
                    //当前结算明细金额
                    BigDecimal amount = detail.getAmount();
                    //按照原逻辑进行已结算金额和结算状态的维护
                    for (ContractIncomePlanConcludeE concludeE : planConcludeList) {
                        PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(concludeE.getId());
                        if (concludeE.getPaymentStatus() == 2) {
                            //如果已经完成结算了就continue
                            continue;
                        }
                        if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) > 0) {
                            //amount减去当前处理的金额，多出的用于后续核销
                            amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                            concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                            concludeE.setPaymentStatus(2);
                            if (Objects.nonNull(writeOffBo)){
                                writeOffBo.setWriteOffFlag(true);
                                concludeE.setPayIncomeFinished(1);
                            }
                        } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) == 0) {
                            //amount减去当前处理的金额，其实就等于0了，amount全部用于核销
                            amount = amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount()));
                            concludeE.setSettlementAmount(concludeE.getPlannedCollectionAmount());
                            concludeE.setPaymentStatus(2);
                            if (Objects.nonNull(writeOffBo)){
                                writeOffBo.setWriteOffFlag(true);
                                concludeE.setPayIncomeFinished(1);
                            }
                            break;
                        } else if (amount.subtract(concludeE.getPlannedCollectionAmount().subtract(concludeE.getSettlementAmount())).compareTo(BigDecimal.ZERO) < 0) {
                            concludeE.setSettlementAmount(concludeE.getSettlementAmount().add(amount));
                            //amount核销不完，将amount累加到已结算金额后，将amount置为0
                            amount=BigDecimal.ZERO;
                            concludeE.setPaymentStatus(1);
                            if (Objects.nonNull(writeOffBo)){
                                writeOffBo.setWriteOffFlag(true);
                                concludeE.setPayIncomeFinished(1);
                            }
                            break;
                        }
                    }
                    //测试下来没问题，核销结束后，若amount还大于0，直接加到每组最后一个的结算金额上
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        ContractIncomePlanConcludeE contractIncomePlanConcludeE = planConcludeList.get(planConcludeList.size() - 1);
                        log.info("核销完该组收款计划后amount金额还大于0,剩余金额:{},当前组最后一个收款计划当前已结算金额:{}", amount, contractIncomePlanConcludeE.getSettlementAmount());
                        //保险起见还是加一个，虽然走到这里一定是从循环第二个if出来的
                        contractIncomePlanConcludeE.setPaymentStatus(2);
                        contractIncomePlanConcludeE.setSettlementAmount(contractIncomePlanConcludeE.getSettlementAmount().add(amount));
                    }

                    doHandleIncomePlanSettleCalculate(detail, planConcludeList, planWriteOffBoMap);
                    //doHandleIncomeCostPlanV3(planWriteOffBoList,payIncomePlanMap);
                }

                //批量更新
                contractIncomePlanConcludeService.updateBatchById(planConcludes);
                //更新成功后推送枫行梦
                ContractIncomeConcludeE incomeConcludeE = contractIncomeConcludeMapper.selectById(incomeSettlement.getContractId());
                List<ContractIncomeConcludePlanFxmReceiptRecordE> receiptRecords = contractInfoToFxmCommonService.pushIncomePlanReceipt2Fxm(planConcludes, incomeConcludeE);
                if (CollectionUtils.isNotEmpty(receiptRecords)) {
                    fxmReceiptRecordService.saveBatch(receiptRecords);
                }
                //处理父级的结算金额，逻辑不变，plannedCollectionAmount字段就是该单子的结算总额，等于结算明细中amount字段的和
                List<ContractIncomePlanConcludeE> pidConcludeList = contractIncomePlanConcludeMapper.selectList(Wrappers.<ContractIncomePlanConcludeE>lambdaQuery()
                        .eq(ContractIncomePlanConcludeE::getContractId, incomeSettlement.getContractId())
                        .eq(ContractIncomePlanConcludeE::getPid, "0")
                        .eq(ContractIncomePlanConcludeE::getDeleted, 0));
                if(CollectionUtils.isNotEmpty(pidConcludeList)){
                    ContractIncomePlanConcludeE updateConclude = new ContractIncomePlanConcludeE();
                    updateConclude.setId(pidConcludeList.get(0).getId());
                    updateConclude.setSettlementAmount(pidConcludeList.get(0).getSettlementAmount().add(incomeSettlement.getPlannedCollectionAmount()));
                    contractIncomePlanConcludeService.updateById(updateConclude);
                }
            }
        }

        //-------【核销收入计划金额】------
        //根据清单ID及结算周期查询范围内的收入计划
        List<PayIncomePlanE> payIncomePlanList = new ArrayList<>();
        if(Objects.nonNull(settDetails.get(0).getStartDate()) && Objects.nonNull(settDetails.get(0).getEndDate())){
            for(ContractIncomeSettDetailsE settD : settDetails){
                payIncomePlanList.addAll(contractPayIncomePlanMapper.getCostListByPlanAndCostTime(
                        incomeSettlement.getContractId(),
                        settD.getPayFundId(),
                        Date.from(settD.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(settD.getEndDate().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            }
        }else{
            if (CollectionUtils.isNotEmpty(periodList)){
                List<ContractIncomePlanConcludeE> planFunList = contractIncomePlanConcludeMapper.getFunDateList(incomeSettlement.getContractId(),settDetails.stream().map(ContractIncomeSettDetailsE :: getPayFundId).collect(Collectors.toList()),
                        periodList);
                for(ContractIncomePlanConcludeE settD : planFunList){
                    payIncomePlanList.addAll(contractPayIncomePlanMapper.getCostListByPlanAndCostTime(
                            incomeSettlement.getContractId(),
                            settD.getContractPayFundId(),
                            Date.from(settD.getCostStartTime().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(settD.getCostEndTime().atStartOfDay(ZoneId.systemDefault()).toInstant())));
                }
            }
        }
        if(CollectionUtils.isNotEmpty(payIncomePlanList)){
            List<PayIncomePlanE> payCostPlanNewList = new ArrayList<>();
            doHandlePayCostPlanV5(payIncomePlanList, settDetails, payCostPlanNewList);
            if (CollectionUtils.isNotEmpty(payCostPlanNewList)) {
                contractPayIncomePlanService.updateBatchById(payCostPlanNewList);
            }
        }
    }

    private void doHandlePayCostPlanV5(List<PayIncomePlanE> payCostPlanOldList, List<ContractIncomeSettDetailsE> settDetails,List<PayIncomePlanE> payCostPlanNewList) {
        //根据结算单获取费项对应减免金额
        List<ContractIncomeConcludeSettdeductionV> contractPayConcludeSettdeductionVList = contractIncomeConcludeSettdeductionService.getDetailsBySettlementId(settDetails.get(0).getSettlementId());
        Map<Long, BigDecimal> sumAmountByChargeItemId = new HashMap<>();
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            sumAmountByChargeItemId = contractPayConcludeSettdeductionVList.stream()
                    .collect(Collectors.groupingBy(
                            ContractIncomeConcludeSettdeductionV::getChargeItemId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ContractIncomeConcludeSettdeductionV::getAmount,
                                    BigDecimal::add
                            )
                    ));
        }
        List<Long> chargeItemIds = settDetails.stream().map(ContractIncomeSettDetailsE::getChargeItemId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            List<Long> removeAll = contractPayConcludeSettdeductionVList.stream().map(ContractIncomeConcludeSettdeductionV::getChargeItemId).collect(Collectors.toList());
            chargeItemIds = chargeItemIds.stream().filter(chargeItemId -> !removeAll.contains(chargeItemId)).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(chargeItemIds)){
            for(Long chargeItemId : chargeItemIds){
                sumAmountByChargeItemId.put(chargeItemId, BigDecimal.ZERO);
            }
        }
        //计算每条清单计算金额（结算明细金额-对应费项的减免金额）
        Map<String, BigDecimal> funMapChargeAmountMap = new HashMap<>();
        Map<String, BigDecimal> funMapChargeDetailAmountMap = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : sumAmountByChargeItemId.entrySet()) {
            //获取费项对应清单列表
            List<ContractIncomeSettDetailsE> funSettDetailList = settDetails.stream()
                    .filter(settDetail -> settDetail.getChargeItemId().equals(entry.getKey()))
                    .collect(Collectors.toList());
            //获取同费项清单总金额
            BigDecimal settDetailTotalAmount = funSettDetailList.stream().map(ContractIncomeSettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> funSettMap = new HashMap<>();
            funSettDetailList.forEach(plan-> {
                funSettMap.put(plan.getId(), plan.getAmount());
            });
            //获取费项对应核销金额
            Map<String, BigDecimal> funSettReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funSettMap, settDetailTotalAmount, entry.getValue());
            funMapChargeDetailAmountMap.putAll(funSettReductionAmountMap);
            funSettDetailList.forEach(plan-> {
                funMapChargeAmountMap.put(plan.getId(), plan.getAmount().subtract(funSettReductionAmountMap.get(plan.getId())));
            });
        }

        //
        for (Map.Entry<String, BigDecimal> entry : funMapChargeAmountMap.entrySet()) {
            ContractIncomeSettDetailsE settDetailNew = settDetails.stream().filter(settDetail -> settDetail.getId().equals(entry.getKey())).findFirst().get();
            //获取该清单计算金额
            BigDecimal costPlanAmountTotal = entry.getValue();
            BigDecimal funMapChargeDetailAmount = funMapChargeDetailAmountMap.get(entry.getKey());
            //获取结算明细下计划对应的成本计划数据
            List<PayIncomePlanE> planCostList = payCostPlanOldList.stream().filter(planOld -> planOld.getPayFundId().equals(settDetailNew.getPayFundId())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(planCostList)){
                continue;
            }
            List<CommonRangeDateBO> dateBOList = new ArrayList<>();
            planCostList.forEach(x->{
                CommonRangeDateBO dateBO = new CommonRangeDateBO();
                BeanUtils.copyProperties(x,dateBO);
                dateBOList.add(dateBO);
            });
            Map<String, BigDecimal> costPlanMap = commonRangeAmountService.calculateDistributedAmounts(dateBOList,costPlanAmountTotal);
            Map<String, BigDecimal> costPlanReductionMap = commonRangeAmountService.calculateDistributedAmounts(dateBOList,funMapChargeDetailAmount);

            for(PayIncomePlanE cost : planCostList){
                cost.setPaymentAmount(costPlanMap.get(cost.getId()));
                cost.setReductionAmount(costPlanReductionMap.get(cost.getId()));
                extracted(cost);
                // 有关后续的核销
                /*cost.setSettlementStatus(cost.getPaymentAmount()
                        .compareTo(cost.getPlannedCollectionAmount()) >= 0 ? 2 : 1);*/
                cost.setSettlementStatus(SettlementStatusEnum.已确收.getCode());
            }

            /*planCostList = planCostList.stream()
                    .sorted(Comparator.comparing(PayIncomePlanE::getCostStartTime))
                    .collect(Collectors.toList());
            LocalDate costStartTime = planCostList.stream()
                    .map(PayIncomePlanE::getCostStartTime)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            LocalDate costEndTime = planCostList.stream()
                    .map(PayIncomePlanE::getCostEndTime)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            CommonRangeAmountBO settlementAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,costPlanAmountTotal);
            CommonRangeAmountBO funMapChargeDetailAmountBo = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,funMapChargeDetailAmount);
            if(planCostList.size() <= 3){
                for (int i = 0; i < planCostList.size(); i++) {
                    PayIncomePlanE payCostPlan = planCostList.get(i);
                    if(i == 0){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getStartMonthAmount());
                        extracted(payCostPlan);
                    }else if  (i == planCostList.size() - 1){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getEndMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getEndMonthAmount());
                        extracted(payCostPlan);
                    }else{
                        payCostPlan.setPaymentAmount(settlementAmountBO.getAvgMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getAvgMonthAmount());
                        extracted(payCostPlan);
                    }
                    // 有关后续的核销
                    payCostPlan.setSettlementStatus(payCostPlan.getPaymentAmount()
                            .compareTo(payCostPlan.getPlannedCollectionAmount()) >= 0 ? 2 : 1);
                }
            }else{
                BigDecimal useAmount = BigDecimal.ZERO;
                BigDecimal useChargeAmount = BigDecimal.ZERO;
                //核销月平均金额
                BigDecimal avgPaymentAmount = settlementAmountBO.getAvgMonthAmount();
                //减免月平均金额
                BigDecimal avgChargeAmount = funMapChargeDetailAmountBo.getAvgMonthAmount();
                for (int i = 0; i < planCostList.size(); i++) {
                    PayIncomePlanE payCostPlan = planCostList.get(i);
                    if  (i == planCostList.size() - 1){
                        payCostPlan.setPaymentAmount(costPlanAmountTotal.subtract(useAmount));
                        payCostPlan.setReductionAmount(funMapChargeDetailAmount.subtract(useChargeAmount));
                        extracted(payCostPlan);
                    }else{
                        //判断开始日期是否为当月第一天
                        Boolean isFirstDayOfMonth = payCostPlan.getCostStartTime().getDayOfMonth() == 1;
                        //当前月份天数
                        int daysInMonth = payCostPlan.getCostStartTime().lengthOfMonth();
                        //当前时间段占据时间
                        int nowDay;
                        // 如果开始日期不是当月第一天，
                        if (!isFirstDayOfMonth) {
                            //计算开始时间到月底的剩余天数
                            LocalDate lastDayOfMonth = payCostPlan.getCostStartTime().with(TemporalAdjusters.lastDayOfMonth());
                            nowDay = lastDayOfMonth.getDayOfMonth() - payCostPlan.getCostStartTime().getDayOfMonth()+1;
                        }else {
                            //开始时间是当月第一天
                            //计算结束时间到月初天数
                            LocalDate firstDayOfMonth = payCostPlan.getCostEndTime().with(TemporalAdjusters.firstDayOfMonth());
                            nowDay = payCostPlan.getCostEndTime().getDayOfMonth() - firstDayOfMonth.getDayOfMonth()+1;
                        }
                        BigDecimal ratio = BigDecimal.valueOf(nowDay)
                                .divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_DOWN);

                        BigDecimal thisPaymentAmount = avgPaymentAmount.multiply(ratio).setScale( 0, RoundingMode.DOWN);
                        BigDecimal thisReductionmentAmount = avgChargeAmount.multiply(ratio).setScale( 0, RoundingMode.DOWN);
                        useAmount = useAmount.add(thisPaymentAmount);
                        useChargeAmount = useChargeAmount.add(thisReductionmentAmount);
                        payCostPlan.setPaymentAmount(thisPaymentAmount);
                        payCostPlan.setReductionAmount(thisReductionmentAmount);
                        extracted(payCostPlan);
                    }
                }


            }*/
            BigDecimal settlementTaxAmount = settDetailNew.getTaxRateAmount();
            if ("差额纳税".equals(planCostList.get(0).getTaxRate())) {
                int periodCount = planCostList.size();
                if (settlementTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal averageAmount = settlementTaxAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                    BigDecimal sumAllocated = BigDecimal.ZERO;
                    for (int i = 0; i < periodCount; i++) {
                        BigDecimal taxAmount;
                        if (i < periodCount - 1) {
                            taxAmount = averageAmount;
                            sumAllocated = sumAllocated.add(averageAmount);
                        } else {
                            taxAmount = settlementTaxAmount.subtract(sumAllocated);
                        }
                        planCostList.get(i).setTaxAmount(taxAmount);
                        planCostList.get(i).setNoTaxAmount(planCostList.get(i).getPaymentAmount().subtract(taxAmount));
                    }
                }
            }
            payCostPlanNewList.addAll(planCostList);
        }

    }

    private void doHandlePayCostPlanV4(List<PayIncomePlanE> payCostPlanOldList, List<ContractIncomeSettDetailsE> settDetails,
                                       List<ContractIncomePlanConcludeE> planConcludes, List<PayIncomePlanE> payCostPlanNewList) {

        if(CollectionUtils.isEmpty(payCostPlanOldList)){
            return;
        }
        //根据结算单获取费项对应减免金额
        List<ContractIncomeConcludeSettdeductionV> contractPayConcludeSettdeductionVList = contractIncomeConcludeSettdeductionService.getDetailsBySettlementId(settDetails.get(0).getSettlementId());
        Map<Long, BigDecimal> sumAmountByChargeItemId = new HashMap<>();
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            sumAmountByChargeItemId = contractPayConcludeSettdeductionVList.stream()
                    .collect(Collectors.groupingBy(
                            ContractIncomeConcludeSettdeductionV::getChargeItemId,
                            Collectors.reducing(
                                    BigDecimal.ZERO,
                                    ContractIncomeConcludeSettdeductionV::getAmount,
                                    BigDecimal::add
                            )
                    ));
        }
        List<Long> chargeItemIds = settDetails.stream().map(ContractIncomeSettDetailsE::getChargeItemId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(contractPayConcludeSettdeductionVList)){
            List<Long> removeAll = contractPayConcludeSettdeductionVList.stream().map(ContractIncomeConcludeSettdeductionV::getChargeItemId).collect(Collectors.toList());
            chargeItemIds = chargeItemIds.stream().filter(chargeItemId -> !removeAll.contains(chargeItemId)).collect(Collectors.toList());
        }
        if(CollectionUtils.isNotEmpty(chargeItemIds)){
            for(Long chargeItemId : chargeItemIds){
                sumAmountByChargeItemId.put(chargeItemId, BigDecimal.ZERO);
            }
        }
        //计算每条清单计算金额（结算明细金额-对应费项的减免金额）
        Map<String, BigDecimal> funMapChargeAmountMap = new HashMap<>();
        Map<String, BigDecimal> funMapChargeDetailAmountMap = new HashMap<>();
        for (Map.Entry<Long, BigDecimal> entry : sumAmountByChargeItemId.entrySet()) {
            //获取费项对应清单列表
            List<ContractIncomeSettDetailsE> funSettDetailList = settDetails.stream()
                    .filter(settDetail -> settDetail.getChargeItemId().equals(entry.getKey()))
                    .collect(Collectors.toList());
            //获取同费项清单总金额
            BigDecimal settDetailTotalAmount = funSettDetailList.stream().map(ContractIncomeSettDetailsE::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, BigDecimal> funSettMap = new HashMap<>();
            funSettDetailList.forEach(plan-> {
                funSettMap.put(plan.getId(), plan.getAmount());
            });
            //获取费项对应核销金额
            Map<String, BigDecimal> funSettReductionAmountMap = BigDecimalUtils.calculateDistributedAmounts(funSettMap, settDetailTotalAmount, entry.getValue());
            funMapChargeDetailAmountMap.putAll(funSettReductionAmountMap);
            funSettDetailList.forEach(plan-> {
                funMapChargeAmountMap.put(plan.getId(), plan.getAmount().subtract(funSettReductionAmountMap.get(plan.getId())));
            });
        }

        //
        for (Map.Entry<String, BigDecimal> entry : funMapChargeAmountMap.entrySet()) {
            ContractIncomeSettDetailsE settDetailNew = settDetails.stream().filter(settDetail -> settDetail.getId().equals(entry.getKey())).findFirst().get();
            //获取该清单计算金额
            BigDecimal costPlanAmountTotal = entry.getValue();
            BigDecimal funMapChargeDetailAmount = funMapChargeDetailAmountMap.get(entry.getKey());
            //获取该清单对应计划列表
            List<ContractIncomePlanConcludeE> payPlanList = planConcludes.stream()
                    .filter(plan -> plan.getContractPayFundId().equals(settDetailNew.getPayFundId()))
                    .collect(Collectors.toList());
            List<String> allCostPlanIds = payPlanList.stream().map(ContractIncomePlanConcludeE::getId).collect(Collectors.toList());
            //获取结算明细下计划对应的成本计划数据
            List<PayIncomePlanE> planCostList = payCostPlanOldList.stream().filter(payCostPlanE -> allCostPlanIds.contains(payCostPlanE.getPlanId())).collect(Collectors.toList());
            planCostList = planCostList.stream()
                    .sorted(Comparator.comparing(PayIncomePlanE::getCostStartTime))
                    .collect(Collectors.toList());
            LocalDate costStartTime = planCostList.stream()
                    .map(PayIncomePlanE::getCostStartTime)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            LocalDate costEndTime = planCostList.stream()
                    .map(PayIncomePlanE::getCostEndTime)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            CommonRangeAmountBO settlementAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,costPlanAmountTotal);
            CommonRangeAmountBO funMapChargeDetailAmountBo = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,funMapChargeDetailAmount);
            if(planCostList.size() <= 3){
                for (int i = 0; i < planCostList.size(); i++) {
                    PayIncomePlanE payCostPlan = planCostList.get(i);
                    if(i == 0){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getStartMonthAmount());
                        extracted(payCostPlan);
                    }else if  (i == planCostList.size() - 1){
                        payCostPlan.setPaymentAmount(settlementAmountBO.getEndMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getEndMonthAmount());
                        extracted(payCostPlan);
                    }else{
                        payCostPlan.setPaymentAmount(settlementAmountBO.getAvgMonthAmount());
                        payCostPlan.setReductionAmount(funMapChargeDetailAmountBo.getAvgMonthAmount());
                        extracted(payCostPlan);
                    }
                    // 有关后续的核销
                    payCostPlan.setSettlementStatus(payCostPlan.getPaymentAmount()
                            .compareTo(payCostPlan.getPlannedCollectionAmount()) >= 0 ? 2 : 1);
                }
            }else{
                BigDecimal useAmount = BigDecimal.ZERO;
                BigDecimal useChargeAmount = BigDecimal.ZERO;
                //核销月平均金额
                BigDecimal avgPaymentAmount = settlementAmountBO.getAvgMonthAmount();
                //减免月平均金额
                BigDecimal avgChargeAmount = funMapChargeDetailAmountBo.getAvgMonthAmount();
                for (int i = 0; i < planCostList.size(); i++) {
                    PayIncomePlanE payCostPlan = planCostList.get(i);
                    if  (i == planCostList.size() - 1){
                        payCostPlan.setPaymentAmount(costPlanAmountTotal.subtract(useAmount));
                        payCostPlan.setReductionAmount(funMapChargeDetailAmount.subtract(useChargeAmount));
                        extracted(payCostPlan);
                    }else{
                        //判断开始日期是否为当月第一天
                        Boolean isFirstDayOfMonth = payCostPlan.getCostStartTime().getDayOfMonth() == 1;
                        //当前月份天数
                        int daysInMonth = payCostPlan.getCostStartTime().lengthOfMonth();
                        //当前时间段占据时间
                        int nowDay;
                        // 如果开始日期不是当月第一天，
                        if (!isFirstDayOfMonth) {
                            //计算开始时间到月底的剩余天数
                            LocalDate lastDayOfMonth = payCostPlan.getCostStartTime().with(TemporalAdjusters.lastDayOfMonth());
                            nowDay = lastDayOfMonth.getDayOfMonth() - payCostPlan.getCostStartTime().getDayOfMonth()+1;
                        }else {
                            //开始时间是当月第一天
                            //计算结束时间到月初天数
                            LocalDate firstDayOfMonth = payCostPlan.getCostEndTime().with(TemporalAdjusters.firstDayOfMonth());
                            nowDay = payCostPlan.getCostEndTime().getDayOfMonth() - firstDayOfMonth.getDayOfMonth()+1;
                        }
                        BigDecimal ratio = BigDecimal.valueOf(nowDay)
                                .divide(BigDecimal.valueOf(daysInMonth), 10, RoundingMode.HALF_DOWN);

                        BigDecimal thisPaymentAmount = avgPaymentAmount.multiply(ratio).setScale( 0, RoundingMode.DOWN);
                        BigDecimal thisReductionmentAmount = avgChargeAmount.multiply(ratio).setScale( 0, RoundingMode.DOWN);
                        useAmount = useAmount.add(thisPaymentAmount);
                        useChargeAmount = useChargeAmount.add(thisReductionmentAmount);
                        payCostPlan.setPaymentAmount(thisPaymentAmount);
                        payCostPlan.setReductionAmount(thisReductionmentAmount);
                        extracted(payCostPlan);
                    }
                }


            }
            BigDecimal settlementTaxAmount = settDetailNew.getTaxRateAmount();
            if ("差额纳税".equals(payPlanList.get(0).getTaxRate())) {
                int periodCount = planCostList.size();
                if (settlementTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal averageAmount = settlementTaxAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                    BigDecimal sumAllocated = BigDecimal.ZERO;
                    for (int i = 0; i < periodCount; i++) {
                        BigDecimal taxAmount;
                        if (i < periodCount - 1) {
                            taxAmount = averageAmount;
                            sumAllocated = sumAllocated.add(averageAmount);
                        } else {
                            taxAmount = settlementTaxAmount.subtract(sumAllocated);
                        }
                        planCostList.get(i).setTaxAmount(taxAmount);
                        planCostList.get(i).setNoTaxAmount(planCostList.get(i).getPaymentAmount().subtract(taxAmount));
                    }
                }
            }
            payCostPlanNewList.addAll(planCostList);
        }

    }

    private void doHandleIncomeCostPlanV2(List<PlanWriteOffBo> planWriteOffBoList,
                                          Map<String, List<PayIncomePlanE>> payIncomePlanMap) {
        if (CollectionUtils.isEmpty(planWriteOffBoList) || MapUtils.isEmpty(payIncomePlanMap)){
            return;
        }
        for (PlanWriteOffBo bo : planWriteOffBoList) {
            if (!bo.isWriteOffFlag()){
                continue;
            }
            List<PayIncomePlanE> payIncomePlanList = payIncomePlanMap.get(bo.getPlanId());
            if (CollectionUtils.isEmpty(payIncomePlanList)){
                continue;
            }
            BigDecimal settlementAmount = bo.getSettlementAmount();
            BigDecimal settlementNoTaxAmount = bo.getSettlementNoTaxAmount();
            BigDecimal settlementTaxAmount = bo.getSettlementTaxAmount();
            BigDecimal totalDayLength = BigDecimal.valueOf(bo.getDayLength());
            for (int i = 0; i < payIncomePlanList.size(); i++) {
                PayIncomePlanE payIncomePlan = payIncomePlanList.get(i);
                if (i == payIncomePlanList.size() - 1){
                    // 最后一个,无脑走尾差
                    payIncomePlan.setPaymentAmount(settlementAmount);
                    payIncomePlan.setTaxAmount(settlementTaxAmount);
                    payIncomePlan.setNoTaxAmount(settlementNoTaxAmount);
                } else {
                    // 按照costStartTime到costEndTime的天数差和bo的天数比值进行均摊,保留两位小数,同时维护三个金额
                    long payIncomePlanDayLength = ChronoUnit.DAYS.between(payIncomePlan.getCostStartTime(),
                            payIncomePlan.getCostEndTime()) + 1;
                    BigDecimal curDayLengthRate = BigDecimal.valueOf(payIncomePlanDayLength).divide(totalDayLength, 6,
                            RoundingMode.HALF_UP);
                    BigDecimal curCostPlanSettlementAmount =
                            settlementAmount.multiply(curDayLengthRate).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal curCostPlanSettlementNoTaxAmount =
                            settlementNoTaxAmount.multiply(curDayLengthRate).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal curCostPlanSettlementTaxAmount =
                            settlementTaxAmount.multiply(curDayLengthRate).setScale(2, RoundingMode.HALF_UP);
                    if (settlementAmount.compareTo(curCostPlanSettlementAmount) >= 0){
                        payIncomePlan.setPaymentAmount(curCostPlanSettlementAmount);
                        settlementAmount = settlementAmount.subtract(curCostPlanSettlementAmount);
                    } else {
                        payIncomePlan.setPaymentAmount(settlementAmount);
                        settlementAmount = BigDecimal.ZERO;
                    }

                    if (settlementNoTaxAmount.compareTo(curCostPlanSettlementNoTaxAmount) >= 0){
                        payIncomePlan.setNoTaxAmount(curCostPlanSettlementNoTaxAmount);
                        settlementNoTaxAmount = settlementNoTaxAmount.subtract(curCostPlanSettlementNoTaxAmount);
                    } else {
                        payIncomePlan.setNoTaxAmount(settlementNoTaxAmount);
                        settlementNoTaxAmount = BigDecimal.ZERO;
                    }

                    if (settlementTaxAmount.compareTo(curCostPlanSettlementTaxAmount) >= 0){
                        payIncomePlan.setTaxAmount(curCostPlanSettlementTaxAmount);
                        settlementTaxAmount = settlementTaxAmount.subtract(curCostPlanSettlementTaxAmount);
                    } else {
                        payIncomePlan.setTaxAmount(settlementTaxAmount);
                        settlementTaxAmount = BigDecimal.ZERO;
                    }
                }
                // 有关后续的核销
                payIncomePlan.setSettlementStatus(payIncomePlan.getPaymentAmount()
                        .compareTo(payIncomePlan.getPlannedCollectionAmount()) >= 0 ? 2 : 1);

            }
        }
    }
    private void doHandleIncomeCostPlanV3(List<PlanWriteOffBo> planWriteOffBoList,
                                          Map<String, List<PayIncomePlanE>> payIncomePlanMap) {
        if (CollectionUtils.isEmpty(planWriteOffBoList) || MapUtils.isEmpty(payIncomePlanMap)){
            return;
        }
        for (PlanWriteOffBo bo : planWriteOffBoList) {
            if (!bo.isWriteOffFlag()){
                continue;
            }
            List<PayIncomePlanE> payIncomePlanList = payIncomePlanMap.get(bo.getPlanId());
            if (CollectionUtils.isEmpty(payIncomePlanList)){
                continue;
            }
            BigDecimal settlementAmount = bo.getSettlementAmount();
            BigDecimal settlementNoTaxAmount = bo.getSettlementNoTaxAmount();
            BigDecimal settlementTaxAmount = bo.getSettlementTaxAmount();
            LocalDate costStartTime = payIncomePlanList.stream()
                    .map(PayIncomePlanE::getCostStartTime)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            LocalDate costEndTime = payIncomePlanList.stream()
                    .map(PayIncomePlanE::getCostEndTime)
                    .max(Comparator.naturalOrder())
                    .orElse(null);
            CommonRangeAmountBO settlementAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,settlementAmount);
            CommonRangeAmountBO settlementTaxAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,settlementTaxAmount);
            CommonRangeAmountBO settlementNoTaxAmountBO = commonRangeAmountService.getRangeAmount(SplitModeEnum.MONTH.getCode(),costStartTime,costEndTime,settlementNoTaxAmount);

            BigDecimal totalDayLength = BigDecimal.valueOf(bo.getDayLength());
            for (int i = 0; i < payIncomePlanList.size(); i++) {
                PayIncomePlanE payIncomePlan = payIncomePlanList.get(i);
                if(i == 0){
                    payIncomePlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                    extracted(payIncomePlan);
                }else if  (i == payIncomePlanList.size() - 1){
                    payIncomePlan.setPaymentAmount(settlementAmountBO.getStartMonthAmount());
                    extracted(payIncomePlan);
                }else{
                    payIncomePlan.setPaymentAmount(settlementAmountBO.getEndMonthAmount());
                    extracted(payIncomePlan);
                }
                // 有关后续的核销
                payIncomePlan.setSettlementStatus(payIncomePlan.getPaymentAmount()
                        .compareTo(payIncomePlan.getPlannedCollectionAmount()) >= 0 ? 2 : 1);
            }
            if ("差额纳税".equals(payIncomePlanList.get(0).getTaxRate())) {
                int periodCount = payIncomePlanList.size();
                if (settlementTaxAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal averageAmount = settlementTaxAmount.divide(new BigDecimal(periodCount), 2, RoundingMode.HALF_UP);
                    BigDecimal sumAllocated = BigDecimal.ZERO;
                    for (int i = 0; i < periodCount; i++) {
                        BigDecimal taxAmount;
                        if (i < periodCount - 1) {
                            taxAmount = averageAmount;
                            sumAllocated = sumAllocated.add(averageAmount);
                        } else {
                            taxAmount = settlementTaxAmount.subtract(sumAllocated);
                        }
                        payIncomePlanList.get(i).setTaxAmount(taxAmount);
                        payIncomePlanList.get(i).setNoTaxAmount(payIncomePlanList.get(i).getPaymentAmount().subtract(taxAmount));
                    }
                }
            }
        }
    }
    private static void extracted(PayIncomePlanE payIncomePlan) {
        if (!"差额纳税".equals(payIncomePlan.getTaxRate())) {
            BigDecimal taxRateDecimal = parseTaxRate(payIncomePlan.getTaxRate());
            BigDecimal noTaxAmount = payIncomePlan.getPaymentAmount().divide(BigDecimal.ONE.add(taxRateDecimal),2, RoundingMode.HALF_UP);
            payIncomePlan.setNoTaxAmount(noTaxAmount);
            payIncomePlan.setTaxAmount(payIncomePlan.getPaymentAmount().subtract(noTaxAmount));
        }
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
    private void doHandleIncomePlanSettleCalculate(ContractIncomeSettDetailsE detail, List<ContractIncomePlanConcludeE> planConcludeList, Map<String, PlanWriteOffBo> planWriteOffBoMap) {
        // 按照从小到达详细处理核销金额,便于后续成本计划的核销处理
        BigDecimal taxRateAmount = detail.getTaxRateAmount();
        BigDecimal noTaxAmount = detail.getAmountWithOutRate();
        int idx = 0;
        for (ContractIncomePlanConcludeE planConcludeE : planConcludeList) {
            idx++;
            PlanWriteOffBo writeOffBo = planWriteOffBoMap.get(planConcludeE.getId());
            if (Objects.isNull(writeOffBo) || !writeOffBo.isWriteOffFlag()){
                continue;
            }
            BigDecimal settlementAmount = planConcludeE.getSettlementAmount();
            String taxRate = planConcludeE.getTaxRate();
            boolean diffTaxFlag = StringUtils.equals(DIFF_TAX_TYPE_NAME, taxRate);
            BigDecimal taxRateNumber = BigDecimal.ZERO;
            if (!diffTaxFlag){
                taxRateNumber = new BigDecimal(taxRate).divide(new BigDecimal(100),2,
                        RoundingMode.HALF_UP);
            }
            // 计算基准的税额和不含税金额-核销口径[预计金额]
            writeOffBo.setSettlementAmount(settlementAmount);
            BigDecimal curSettlementTaxAmount = null;
            BigDecimal curSettlementNoTaxAmount = null;
            // 全额核销
            if (settlementAmount.compareTo(planConcludeE.getPlannedCollectionAmount()) == 0) {
                curSettlementTaxAmount = planConcludeE.getTaxAmount();
                curSettlementNoTaxAmount = planConcludeE.getNoTaxAmount();
            } else {
                // 部分核销 和 超额核销
                if (diffTaxFlag){
                    curSettlementTaxAmount = planConcludeE.getTaxAmount().multiply(
                            settlementAmount.divide(planConcludeE.getPlannedCollectionAmount(), 6,
                                    RoundingMode.HALF_UP)
                    ).setScale(2, RoundingMode.HALF_UP);
                    curSettlementNoTaxAmount = settlementAmount.subtract(curSettlementTaxAmount);
                } else {
                    curSettlementNoTaxAmount = settlementAmount.divide(BigDecimal.ONE.add(taxRateNumber),
                            2, RoundingMode.HALF_UP);
                    curSettlementTaxAmount = settlementAmount.subtract(curSettlementNoTaxAmount);
                }
            }
            // 如果是差额纳税,且是最后一个,直接走尾差
            if (diffTaxFlag && idx == planConcludeList.size()){
                writeOffBo.setSettlementTaxAmount(taxRateAmount);
                taxRateAmount = BigDecimal.ZERO;
            } else {
                if (taxRateAmount.compareTo(curSettlementTaxAmount) >= 0){
                    taxRateAmount = taxRateAmount.subtract(curSettlementTaxAmount);
                    writeOffBo.setSettlementTaxAmount(curSettlementTaxAmount);
                } else {
                    writeOffBo.setSettlementTaxAmount(taxRateAmount);
                    taxRateAmount = BigDecimal.ZERO;
                }
            }

            // 如果是差额纳税,且是最后一个,直接走尾差
            if (diffTaxFlag && idx == planConcludeList.size()){
                writeOffBo.setSettlementNoTaxAmount(noTaxAmount);
                noTaxAmount = BigDecimal.ZERO;
            } else {
                if (noTaxAmount.compareTo(curSettlementNoTaxAmount) >= 0){
                    noTaxAmount = noTaxAmount.subtract(curSettlementNoTaxAmount);
                    writeOffBo.setSettlementNoTaxAmount(curSettlementNoTaxAmount);
                } else {
                    writeOffBo.setSettlementNoTaxAmount(noTaxAmount);
                    noTaxAmount = BigDecimal.ZERO;
                }
            }

            planConcludeE.setSettlementTaxAmount(writeOffBo.getSettlementTaxAmount());
            planConcludeE.setSettlementNoTaxAmount(writeOffBo.getSettlementNoTaxAmount());
        }
    }


    public void singleHandlePayIncomePlan(String settleId) {
        log.info("进入确收单核销逻辑,当前确收单id:{}", settleId);
        ContractIncomeSettlementConcludeE settlement = this.getById(settleId);
        if (!ReviewStatusEnum.已通过.getCode().equals(settlement.getReviewStatus())) {
            return;
        }

        //查关联的结算计划id
        List<ContractIncomeConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractIncomeConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractIncomeConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return;
        }
        log.info("当前确收单关联的收款计划id集合信息:{}", JSON.toJSONString(relations));
        //查询周期信息
        List<IncomePlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return;
        }
        log.info("当前确收单关联的周期信息:{}", JSON.toJSONString(periodList));
        List<String> planIds = relations.stream()
                .map(ContractIncomeConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractIncomePlanConcludeE> planConcludes = contractIncomePlanConcludeService.queryByCostTimeNotFinished(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return;
        }
        //查关联的结算详情
        List<ContractIncomeSettDetailsE> settDetails = contractIncomeSettDetailsService.list(Wrappers.<ContractIncomeSettDetailsE>lambdaQuery()
                .eq(ContractIncomeSettDetailsE::getSettlementId, settleId)
                .eq(ContractIncomeSettDetailsE::getDeleted, 0));
        if (CollectionUtils.isEmpty(settDetails)) {
            return;
        }
        log.info("当前确收单关联的确收详情信息:{}", JSON.toJSONString(settDetails));
        //转换为分组
        Map<String, List<ContractIncomePlanConcludeE>> planConcludeMap = getPlanConcludeMap(planConcludes);
        log.info("根据收款计划id集合和周期信息匹配出来的收款计划详情:{}", JSON.toJSONString(planConcludeMap));
        //汇总所有收款计划id，查询全部的收入计划
        List<String> allIncomePlanIds = planConcludes.stream().map(ContractIncomePlanConcludeE::getId).collect(Collectors.toList());
        List<PayIncomePlanE> payIncomePlanES = contractPayIncomePlanService.list(Wrappers.<PayIncomePlanE>lambdaQuery()
                .in(PayIncomePlanE::getPlanId, allIncomePlanIds)
                .eq(PayIncomePlanE::getDeleted, 0));
        if (CollectionUtils.isEmpty(payIncomePlanES)) {
            return;
        }
        //转为map
        Map<String, List<PayIncomePlanE>> payIncomePlanMap = payIncomePlanES.stream().collect(Collectors.groupingBy(PayIncomePlanE::getPlanId));
        log.info("收款计划下的收入计划信息:{}", JSON.toJSONString(payIncomePlanMap));
        //遍历进行已结算金额处理
        for (ContractIncomeSettDetailsE detail : settDetails) {
            //获取分组key
            String groupKey = groupKey(detail);
            if (!planConcludeMap.containsKey(groupKey)) {
                continue;
            }
            //获取key对应的结算计划列表
            List<ContractIncomePlanConcludeE> planConcludeList = planConcludeMap.get(groupKey);
            //核销这批收款计划的收入计划
            handlePayIncomePlan(detail, planConcludeList, payIncomePlanMap);
        }
        //批量更新
        contractIncomePlanConcludeService.updateBatchById(planConcludes); //更新结算计划的成本计划-核销状态
        contractPayIncomePlanService.updateBatchById(payIncomePlanES);
    }

    /**
     * 批量处理收款单下收入计划的核销
     *
     * @param settleIds
     */
    public void batchHandlePayIncomePlan(List<String> settleIds) {
        if (CollectionUtils.isEmpty(settleIds)) {
            return;
        }
        settleIds.forEach(this::singleHandlePayIncomePlan);
    }

    /**
     * 核销收入计划
     *
     * @param planConcludes
     */
    private void handlePayIncomePlan(ContractIncomeSettDetailsE detail, List<ContractIncomePlanConcludeE> planConcludes, Map<String, List<PayIncomePlanE>> payIncomePlanMap) {
        if (CollectionUtils.isEmpty(planConcludes) || MapUtils.isEmpty(payIncomePlanMap)) {
            return;
        }
        for (ContractIncomePlanConcludeE planConclude : planConcludes) {
            String planConcludeId = planConclude.getId();
            if (!payIncomePlanMap.containsKey(planConcludeId)) {
                continue;
            }
            log.info("当前处理的收款计划id:{}", planConcludeId);
            //获取该收款计划下的收入计划
            List<PayIncomePlanE> payIncomePlanList = payIncomePlanMap.get(planConcludeId);
            BigDecimal settlementAmount = planConclude.getSettlementAmount();
            if (payIncomePlanList.size() == 1) {
                //说明只有1个收入计划
                log.info("收款计划{}下只有一个收入计划,直接设置核销金额", planConcludeId);
                PayIncomePlanE singlePlan = payIncomePlanList.get(0);
                singlePlan.setPaymentAmount(settlementAmount);
                singlePlan.setSettlementStatus(settlementAmount.compareTo(singlePlan.getPlannedCollectionAmount()) >= 0 ? 2 : 1);
                handleSingleTaxAmount(detail, singlePlan);
                //将收款计划设置为已完成收入计划核销
                planConclude.setPayIncomeFinished(1);
                continue;
            }
            //计算总天数
            long totalDays = ChronoUnit.DAYS.between(planConclude.getCostStartTime(), planConclude.getCostEndTime()) + 1;
            log.info("收款计划{}的总天数是:{}", planConcludeId, totalDays);
            BigDecimal preTotal = BigDecimal.ZERO;
            BigDecimal preTaxAmount = BigDecimal.ZERO;
            for (int i = 0; i < payIncomePlanList.size() - 1; i++) {
                PayIncomePlanE payIncomePlanE = payIncomePlanList.get(i);
                //计算该收入计划的天数
                long planDays = ChronoUnit.DAYS.between(payIncomePlanE.getCostStartTime(), payIncomePlanE.getCostEndTime()) + 1;
                //计算占比
                BigDecimal proportion = BigDecimal.valueOf(planDays).divide(BigDecimal.valueOf(totalDays), 6, RoundingMode.HALF_UP);
                //计算该收入计划核销金额
                BigDecimal settAmount = settlementAmount.multiply(proportion).setScale(2, RoundingMode.HALF_UP);
                log.info("收入计划{}天数:{},占比:{},核销金额:{}", payIncomePlanE.getId(), planDays, proportion, settAmount);
                payIncomePlanE.setPaymentAmount(settAmount);
                payIncomePlanE.setSettlementStatus(settAmount.compareTo(payIncomePlanE.getPlannedCollectionAmount()) >= 0 ? 2 : 1);
                handleMiddleTaxAmount(detail, payIncomePlanE, proportion);
                preTotal = preTotal.add(settAmount);
                preTaxAmount = preTaxAmount.add(payIncomePlanE.getTaxAmount());
            }
            //最后一个收入计划使用减法计算核销金额
            BigDecimal lastSettAmount = settlementAmount.subtract(preTotal);
            PayIncomePlanE lastPlan = payIncomePlanList.get(payIncomePlanList.size() - 1);
            log.info("最后一个收入计划id:{},前面收入计划核销金额总数:{},剩余金额:{}", lastPlan.getId(), preTotal, lastSettAmount);
            lastPlan.setPaymentAmount(lastSettAmount);
            lastPlan.setSettlementStatus(lastSettAmount.compareTo(lastPlan.getPlannedCollectionAmount()) >= 0 ? 2 : 1);
            handleLastTaxAmount(detail, planConclude, lastPlan, preTaxAmount);
            //将收款计划设置为已完成收入计划核销
            planConclude.setPayIncomeFinished(1);
        }
    }

    /**
     * 税率转换
     *
     * @param planE
     * @return
     */
    private BigDecimal convertTaxRate(PayIncomePlanE planE) {
        if ("差额纳税".equals(planE.getTaxRate())) {
            return BigDecimal.ZERO;
        }
        BigDecimal taxRatePercentNum = new BigDecimal(planE.getTaxRate().replace("%", ""));
        BigDecimal base = new BigDecimal("100.000000");
        return taxRatePercentNum.divide(base, 6, RoundingMode.HALF_UP);
    }

    /**
     * 处理只有一个收入计划时的税额
     *
     * @param detail
     * @param planE
     */
    private void handleSingleTaxAmount(ContractIncomeSettDetailsE detail, PayIncomePlanE planE){
        BigDecimal taxRate = convertTaxRate(planE);
        planE.setTaxAmount("差额纳税".equals(planE.getTaxRate()) ?
                detail.getTaxRateAmount() :
                planE.getPaymentAmount().multiply(taxRate).setScale(6, RoundingMode.HALF_UP));
        planE.setNoTaxAmount(planE.getPaymentAmount().subtract(planE.getTaxAmount()));
    }

    /**
     * 处理非最后一个收入计划的税额
     *
     * @param detail
     * @param planE
     * @param proportion
     */
    private void handleMiddleTaxAmount(ContractIncomeSettDetailsE detail, PayIncomePlanE planE, BigDecimal proportion){
        BigDecimal taxRate = convertTaxRate(planE);
        planE.setTaxAmount("差额纳税".equals(planE.getTaxRate()) ?
                detail.getTaxRateAmount().multiply(proportion).setScale(6, RoundingMode.HALF_UP) :
                planE.getPaymentAmount().multiply(taxRate).setScale(6, RoundingMode.HALF_UP));
        planE.setNoTaxAmount(planE.getPaymentAmount().subtract(planE.getTaxAmount()));
    }

    /**
     * 处理最后一个收入计划的税额
     *
     * @param detail
     * @param planConclude
     * @param planE
     * @param preTaxAmount
     */
    private void handleLastTaxAmount(ContractIncomeSettDetailsE detail, ContractIncomePlanConcludeE planConclude, PayIncomePlanE planE, BigDecimal preTaxAmount) {
        BigDecimal taxRate = convertTaxRate(planE);
        planE.setTaxAmount("差额纳税".equals(planE.getTaxRate()) ?
                detail.getTaxRateAmount().subtract(preTaxAmount) :
                planConclude.getSettlementAmount().multiply(taxRate).subtract(preTaxAmount));
        planE.setNoTaxAmount(planE.getPaymentAmount().subtract(planE.getTaxAmount()));
    }


    /**
     * 结算计划分组
     *
     * @param planConcludes
     * @return
     */
    private Map<String, List<ContractIncomePlanConcludeE>> getPlanConcludeMap(List<ContractIncomePlanConcludeE> planConcludes) {
        return planConcludes.stream().collect(Collectors.groupingBy(this::groupKey,
                Collectors.collectingAndThen(Collectors.toList(),
                        list -> list.stream().sorted(Comparator.comparing(ContractIncomePlanConcludeE::getTermDate))
                                .collect(Collectors.toList()))));
    }

    /**
     * 获取分组key，费项+清单+税率
     * V2: 合同清单id
     * @param planConclude
     * @return
     */
    private String groupKey(ContractIncomePlanConcludeE planConclude) {
        return planConclude.getContractPayFundId();
//        return planConclude.getChargeItem() + "_" + planConclude.getServiceType() + "_" + planConclude.getTaxRateId();
    }

    /**
     * 获取分组key，费项+清单+税率
     * V2: 合同清单id
     * @param settDetail
     * @return
     */
    private String groupKey(ContractIncomeSettDetailsE settDetail) {
        return settDetail.getPayFundId();
//        return settDetail.getChargeItem() + "_" + settDetail.getTypeId() + "_" + settDetail.getTaxRateId();
    }

    public List<String> queryBySettleId(String settleId) {
        return contractIncomeSettlementConcludeMapper.queryBySettleId(settleId);
    }


    public List<ContractIncomePlanConcludeE> handleIncomePlanStateForBill(String settleId) {
        log.info("进入确收单核销逻辑,当前确收单id:{}", settleId);
        //查关联的结算计划id
        List<ContractIncomeConcludeSettlementPlanRelationE> relations = settlementPlanRelationMapper
                .selectList(Wrappers.<ContractIncomeConcludeSettlementPlanRelationE>lambdaQuery()
                        .eq(ContractIncomeConcludeSettlementPlanRelationE::getSettlementId, settleId));
        if (CollectionUtils.isEmpty(relations)) {
            //说明没有relation信息直接返回
            return null;
        }
        log.info("当前确收单关联的收款计划id集合信息:{}", JSON.toJSONString(relations));
        //查询周期信息
        List<IncomePlanPeriodV> periodList = settlementPeriodMapper.getPeriodList(settleId);
        if (CollectionUtils.isEmpty(periodList)) {
            //说明没有周期信息，直接返回
            return null;
        }
        log.info("当前确收单关联的周期信息:{}", JSON.toJSONString(periodList));
        List<String> planIds = relations.stream()
                .map(ContractIncomeConcludeSettlementPlanRelationE::getPlanId)
                .distinct()
                .collect(Collectors.toList());
        //根据周期信息查结算计划信息，之后的逻辑都不变
        List<ContractIncomePlanConcludeE> planConcludes = contractIncomePlanConcludeService.queryByCostTimeForBill(planIds, periodList);
        if (CollectionUtils.isEmpty(planConcludes)) {
            //说明没有结算计划信息，直接返回
            return null;
        }
        return planConcludes;

    }

    public List<String> getAllContractId(String id) {
        ContractIncomeSettlementConcludeE contractIncomeSettlementConcludeE = this.getById(id);
        if (Objects.isNull(contractIncomeSettlementConcludeE)){
            throw new OwlBizException("确收单不存在,请检查");
        }
        if (StringUtils.isBlank(contractIncomeSettlementConcludeE.getContractId())){
            throw new OwlBizException("结算单关联合同数据异常,请检查");
        }
        LambdaQueryWrapper<ContractIncomeConcludeE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeE::getPid,contractIncomeSettlementConcludeE.getContractId());
        queryWrapper.eq(ContractIncomeConcludeE::getContractType, ContractTypeEnum.补充协议.getCode());
        queryWrapper.eq(ContractIncomeConcludeE::getDeleted,0);
        List<ContractIncomeConcludeE> childContractList = contractIncomeConcludeMapper.selectList(queryWrapper);
        List<String> list = Lists.newArrayList();
        list.add(contractIncomeSettlementConcludeE.getContractId());
        if (CollectionUtils.isNotEmpty(childContractList)){
            list.addAll(childContractList.stream().map(ContractIncomeConcludeE::getId).collect(Collectors.toList()));
        }
        return list;
    }
}
