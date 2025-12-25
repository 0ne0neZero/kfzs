package com.wishare.contract.apps.service.revision.income;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementAddF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludePageF;
import com.wishare.contract.apps.fo.revision.income.ContractIncomeSettlementConcludeUpdateF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomePlanListF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomeSettlementConcludeListF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsBillF;
import com.wishare.contract.apps.fo.revision.pay.bill.ContractSettlementsFundF;
import com.wishare.contract.apps.remote.clients.FinanceFeignClient;
import com.wishare.contract.apps.remote.fo.ResultTemporaryChargeBillF;
import com.wishare.contract.apps.service.contractset.ContractPayIncomePlanService;
import com.wishare.contract.domains.entity.contractset.PayIncomePlanE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeSettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPaySettlementConcludeE;
import com.wishare.contract.domains.entity.revision.pay.PayCostPlanE;
import com.wishare.contract.domains.enums.revision.ContractRevStatusEnum;
import com.wishare.contract.domains.enums.revision.ReviewStatusEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomePlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeSettlementConcludeMapper;
import com.wishare.contract.domains.service.revision.income.ContractIncomeSettlementConcludeService;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeSumV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementDetailsV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettdeductionDetailV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomePlanForSettlementV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomePlanPeriodV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomeSettlementConcludeEditV;
import com.wishare.contract.domains.vo.revision.income.settlement.ContractIncomeSettlementPageV2;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV2;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description:合同付款计划
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:49
 */
@Service
@Slf4j
public class ContractIncomeSettlementConcludeAppService {

    @Setter(onMethod_ = {@Autowired})
    private ContractIncomeSettlementConcludeService contractIncomeFundConcludeService;
    @Autowired
    private ContractIncomeSettlementConcludeMapper contractIncomeSettlementConcludeMapper;
    @Autowired
    private ContractIncomePlanConcludeMapper contractIncomePlanConcludeMapper;
    @Autowired
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;
    @Autowired
    private ContractPayIncomePlanService contractPayIncomePlanService;
    @Autowired
    private FinanceFeignClient financeFeignClient;
    public ContractIncomeSettlementDetailsV getDetailsById(String id){
        return contractIncomeFundConcludeService.getDetailsById(id);
    }

    public PageV<ContractIncomeSettlementConcludeV> page(PageF<SearchF<ContractIncomeSettlementConcludePageF>> request) {
        IPage<ContractIncomeSettlementConcludeV> pageList = contractIncomeFundConcludeService.page(request);
        return PageV.of(request, pageList.getTotal(), pageList.getRecords());
    }

    public ContractIncomeSettlementConcludeSumV accountAmountSum(PageF<SearchF<ContractIncomeSettlementConcludePageF>> request) {
        return contractIncomeFundConcludeService.accountAmountSum(request);
    }

    public String save(ContractIncomeSettlementAddF addF){
        return contractIncomeFundConcludeService.save(addF);
    }


    public void update(ContractIncomeSettlementConcludeUpdateF contractPayConcludeF){
        contractIncomeFundConcludeService.update(contractPayConcludeF);
    }


    public boolean removeById(String id){
        return contractIncomeFundConcludeService.removeById(id);
    }

    public String submitId(String id){
        return contractIncomeFundConcludeService.submitId(id);
    }

    public void returnId(String id){
        contractIncomeFundConcludeService.returnId(id);
    }

    public String invoice(ContractSettlementsBillF contractSettlementsBillF){
        return contractIncomeFundConcludeService.invoice(contractSettlementsBillF);
    }

    public String setFund(ContractSettlementsFundF contractSettlementsFundF){
        return contractIncomeFundConcludeService.setFund(contractSettlementsFundF);
    }

    public String updateSettlementStep(String settlementId, Integer step) {
        return contractIncomeFundConcludeService.updateSettlementStep(settlementId, step);
    }

    public ContractIncomePlanPeriodV getPlanPeriod(String contractId) {
        return contractIncomeFundConcludeService.getPlanPeriod(contractId);
    }

    public List<ContractIncomePlanForSettlementV> getPlanList(ContractIncomePlanListF contractIncomePlanListF) {
        return contractIncomeFundConcludeService.getPlanList(contractIncomePlanListF);
    }

    public ContractIncomeSettlementConcludeEditV editInfo(ContractIncomeSettlementConcludeListF contractIncomePlanConcludeListF) {
        return contractIncomeFundConcludeService.getEditInfo(contractIncomePlanConcludeListF);
    }

    public PageV<ContractIncomeSettlementPageV2> pageV2(PageF<SearchF<?>> request) {
        return contractIncomeFundConcludeService.pageV2(request);
    }

    public PageV<ContractIncomeSettdeductionDetailV> contractSettdeductionDetailPage(PageF<SearchF<?>> request) {
        return contractIncomeFundConcludeService.contractSettdeductionDetailPage(request);
    }

    public List<String> getAllContractId(String id) {
        return contractIncomeFundConcludeService.getAllContractId(id);
    }

    @Transactional
    public Boolean deletedIncomeSettlement(String id) {

        ContractIncomeSettlementConcludeE settlement = contractIncomeSettlementConcludeMapper.selectById(id);
        if (Objects.isNull(settlement)){
            throw new OwlBizException("确收单不存在");
        }
        ContractIncomeConcludeE payConcludeE = contractIncomeConcludeMapper.selectById(settlement.getContractId());
        if(Objects.isNull(payConcludeE)){
            throw new OwlBizException("该合同不存在");
        }
        payConcludeE.setStatus(ContractRevStatusEnum.正在履行.getCode());
        contractIncomeConcludeMapper.updateById(payConcludeE);
        //查询结算单关联的结算计划
        List<String> planIdList = contractIncomeSettlementConcludeMapper.getPlanBySettlement(id);
        List<String> costPlanIdList = contractIncomeSettlementConcludeMapper.queryBySettleId(id);
        if(CollectionUtils.isNotEmpty(costPlanIdList)){
            QueryWrapper<PayIncomePlanE> queryModel = new QueryWrapper<>();
            queryModel.eq("contractId", settlement.getContractId());
            queryModel.in("id", costPlanIdList);
            queryModel.eq("deleted",0);
            List<PayIncomePlanE> payCostPlanEList = contractPayIncomePlanService.list(queryModel);
            if(CollectionUtils.isNotEmpty(payCostPlanEList)){
                List<String> billIdList = payCostPlanEList.stream().map(PayIncomePlanE::getBillId).map(String::valueOf).collect(Collectors.toList());
                //[校验]根据临时账单ID获取报账单数据-实签
                String message = financeFeignClient.getVoucherBillSq(billIdList, payConcludeE.getCommunityId());
                if(StringUtils.isNotEmpty(message)){
                    throw new OwlBizException(message);
                }
            }

        }
        for(String planId:planIdList){
            ContractIncomePlanConcludeE planConcludeE = contractIncomePlanConcludeMapper.selectById(planId);
            QueryWrapper<PayIncomePlanE> queryModel = new QueryWrapper<>();
            queryModel.eq("contractId", settlement.getContractId());
            queryModel.eq("planId", planId);
            queryModel.eq("deleted",0);
            List<PayIncomePlanE> payCostPlanEList = contractPayIncomePlanService.list(queryModel);
            if(CollectionUtils.isNotEmpty(payCostPlanEList)){
                List<PayIncomePlanE> payCostPlanNewList = contractPayIncomePlanService.generateIncomePlans(planConcludeE,payConcludeE, Boolean.FALSE);
                //获取成本原始数据，对指定数据进行核销
                for(PayIncomePlanE cost : payCostPlanEList){
                    PayIncomePlanE newCost = payCostPlanNewList.stream().filter(x->cost.getCostStartTime().equals(x.getCostStartTime()) && cost.getCostEndTime().equals(x.getCostEndTime())).findFirst()
                            .orElse(new PayIncomePlanE());
                    cost.setPaymentAmount(Objects.nonNull(newCost.getPaymentAmount()) ? newCost.getPaymentAmount() : BigDecimal.ZERO);
                    cost.setReductionAmount(Objects.nonNull(newCost.getReductionAmount()) ? newCost.getReductionAmount() : BigDecimal.ZERO);
                    cost.setNoTaxAmount(Objects.nonNull(newCost.getNoTaxAmount()) ? newCost.getNoTaxAmount() : BigDecimal.ZERO);
                    cost.setTaxAmount(Objects.nonNull(newCost.getTaxAmount()) ? newCost.getTaxAmount() : BigDecimal.ZERO);
                    cost.setSettlementStatus(newCost.getSettlementStatus());
                }
                contractPayIncomePlanService.updateBatchById(payCostPlanEList);
                List<ResultTemporaryChargeBillF> billList = new ArrayList<>();
                payCostPlanEList.forEach(plan->{
                    ResultTemporaryChargeBillF updateTemporaryChargeBillF = new ResultTemporaryChargeBillF();
                    updateTemporaryChargeBillF.setReductionAmount(BigDecimal.ZERO);
                    updateTemporaryChargeBillF.setId(String.valueOf(plan.getBillId()));
                    updateTemporaryChargeBillF.setReceivableAmount(Long.getLong("0"));
                    updateTemporaryChargeBillF.setSupCpUnitId(payConcludeE.getCommunityId());
                    updateTemporaryChargeBillF.setExtField7(null);
                    updateTemporaryChargeBillF.setTaxAmountNew(plan.getTaxAmount().multiply(new BigDecimal("100")));
                    billList.add(updateTemporaryChargeBillF);
                });
                //根据临时账单ID还原对应临时账单数据及删除合同报账单
                financeFeignClient.deleteReceivableBillAndVoucher(billList);
            }
        }
        //还原结算计划
        contractIncomePlanConcludeMapper.restoreplan(planIdList);
        //删除结算单
        contractIncomeSettlementConcludeMapper.deletedSettlement(id);

        LambdaQueryWrapper<ContractIncomeSettlementConcludeE> querySettWrapper = new LambdaQueryWrapper<>();
        querySettWrapper.eq(ContractIncomeSettlementConcludeE::getContractId, settlement.getContractId())
                .eq(ContractIncomeSettlementConcludeE::getDeleted,0);
        List<ContractIncomeSettlementConcludeE> concludeSettList = contractIncomeSettlementConcludeMapper.selectList(querySettWrapper);
        if(concludeSettList.size() == 1 && concludeSettList.get(0).getPid().equals("0")){
            contractIncomeSettlementConcludeMapper.deletedSettlement(concludeSettList.get(0).getId());
        }
        return Boolean.TRUE;
    }
}
