package com.wishare.contract.apps.service.contractset;

import com.wishare.bizlog.BizLog;
import com.wishare.bizlog.content.Content;
import com.wishare.bizlog.content.PlainTextDataItem;
import com.wishare.bizlog.entity.BizObject;
import com.wishare.bizlog.operator.Operator;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanF;
import com.wishare.contract.apps.fo.contractset.ContractProfitLossPlanSaveF;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.contract.domains.enums.BusinessTypeEnum;
import com.wishare.contract.domains.enums.LogActionEnum;
import com.wishare.contract.domains.service.contractset.ContractConcludeService;
import com.wishare.contract.domains.vo.contractset.ContractDetailsV;
import com.wishare.contract.domains.vo.contractset.ContractProfitLossPlanV;
import com.wishare.contract.infrastructure.utils.FileStorageUtils;
import com.wishare.owl.enhance.IOwlApiBase;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.contract.domains.service.contractset.ContractEngineeringPlanService;
import com.wishare.contract.domains.vo.contractset.ContractEngineeringPlanV;
import com.wishare.contract.apps.fo.contractset.ContractEngineeringPlanF;
import com.wishare.contract.apps.fo.contractset.ContractEngineeringPlanSaveF;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* <p>
* 工程类合同计提信息表
* </p>
*
* @author wangrui
* @since 2022-11-29
*/
@Service
@Slf4j
public class ContractEngineeringPlanAppService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractEngineeringPlanService contractEngineeringPlanService;
    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;
    @Setter(onMethod_ = {@Autowired})
    private ContractProfitLossPlanAppService profitLossPlanAppService;
    @Setter(onMethod_ = {@Autowired})
    private ContractConcludeService contractConcludeService;
    @Setter(onMethod_ = {@Autowired})
    private FileStorageUtils fileStorageUtils;

    public ContractEngineeringPlanV get(ContractEngineeringPlanF contractEngineeringPlanF){
        ContractEngineeringPlanV engineeringPlanV = contractEngineeringPlanService.get(contractEngineeringPlanF).orElse(null);
        if(null != engineeringPlanV){
            engineeringPlanV.setAccrualDataFileVo(fileStorageUtils.getFileNameList(engineeringPlanV.getAccrualData(),engineeringPlanV.getAccrualDataName()));
        }
        return engineeringPlanV;
    }

    public List<ContractEngineeringPlanV> list(ContractEngineeringPlanF contractEngineeringPlanF){
        return contractEngineeringPlanService.listEngineering(contractEngineeringPlanF);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Long save(ContractEngineeringPlanSaveF planSaveF) throws ParseException {
        String code = engineeringCode(planSaveF.getTenantId());
        planSaveF.setAccrualCode(code);
        Long id =contractEngineeringPlanService.save(planSaveF);
        //根据工程计提生成损益计划
        ContractDetailsV contractDetailsV = contractConcludeService.selectById(planSaveF.getContractId());
        ContractProfitLossPlanSaveF profitLossPlanSaveF = new ContractProfitLossPlanSaveF();
        profitLossPlanSaveF.setContractId(planSaveF.getContractId());
        profitLossPlanSaveF.setCostId(Long.parseLong(contractDetailsV.getCommunityId()));
        profitLossPlanSaveF.setChargeItemId(contractDetailsV.getChargeItemId());
        profitLossPlanSaveF.setOrgId(Long.parseLong(contractDetailsV.getOrgId()));
        profitLossPlanSaveF.setLocalCurrencyAmount(planSaveF.getAccrualAmount());
        profitLossPlanSaveF.setAmountTaxIncluded(planSaveF.getAccrualAmount());
        //不含税金额=含税金额/（1+税率）保留两位小数
        BigDecimal taxAmount = contractDetailsV.getTaxRate().multiply(new BigDecimal("0.01")).add(new BigDecimal(1));
        profitLossPlanSaveF.setTaxExcludedAmount(planSaveF.getAccrualAmount().divide(taxAmount, 2, RoundingMode.HALF_UP));
        profitLossPlanSaveF.setTaxRateId(getTaxRateId(contractDetailsV.getTaxRateIdPath()));
        profitLossPlanSaveF.setTaxRate(contractDetailsV.getTaxRate().toString());
        profitLossPlanSaveF.setEngineeringCode(code);
        profitLossPlanSaveF.setConfirmTime(LocalDate.now());
        profitLossPlanSaveF.setBillType(contractDetailsV.getInvoiceType());
        Long profitLossId = profitLossPlanAppService.saveContractProfitLossPlan(profitLossPlanSaveF);
        ContractProfitLossPlanF contractProfitLossPlanF =  new ContractProfitLossPlanF();
        contractProfitLossPlanF.setId(profitLossId);
        List<ContractProfitLossPlanV> contractProfitLossPlanV=profitLossPlanAppService.getProfitLossPlanList(contractProfitLossPlanF);
        if(contractDetailsV.getContractNature().equals(ContractSetConst.INCOME)){
            profitLossPlanAppService.profitLossPlanCreateBill(null, planSaveF.getContractId(), null, true, contractProfitLossPlanV);
        }else{
            profitLossPlanAppService.profitLossPlanCreateBill(null, planSaveF.getContractId(), null, false, contractProfitLossPlanV);
        }
        //计提动态
        BizLog.normal(planSaveF.getContractId().toString(), new Operator(userId(), userName()),
                new BizObject() {
                    @Override
                    public String getObjName() {
                        return BusinessTypeEnum.工程计提申请.getName();
                    }
                },
                LogActionEnum.发起,
                new Content().option(new PlainTextDataItem("本次工程计提申请金额："+planSaveF.getAccrualAmount().toString(), true))
                        .option(new PlainTextDataItem("本次工程计提申请明细："+code, true))
        );
        return id;
    }

    public String engineeringCode(String tenantId) {
        //客户简称+业务模块缩写+年后两位+月日+4位 ，如YYHTJT2208160001；
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());
        Integer count = contractEngineeringPlanService.selectContractCount(tenantId);
        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        String nature = "HTJT";
        //编号
        String code1 = String.format("%0" + 4 + "d", count + 1);
        String code = null;
        if (null != tenantInfoRv && StringUtils.hasText(tenantInfoRv.getEnglishName())) {
            code = tenantInfoRv.getEnglishName() + nature + year + code1;
        } else {
            code = nature + year + code1;
        }
        return code;
    }

    public Long getTaxRateId(String collectionTaxRateId) {
        List<Long> collect =
                Arrays.stream(collectionTaxRateId.split(",")).map(Long::valueOf).collect(Collectors.toList());
        return collect.get(collect.size() - 1);
    }
}
