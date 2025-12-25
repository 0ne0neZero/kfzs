package com.wishare.contract.domains.service.revision.pay.bo;

import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayPlanConcludeE;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author longhuadmin
 */
@Data
public class PlanWriteOffBo {

    private String planId;

    /**
     * 核销标志 默认false
     **/
    private boolean writeOffFlag;

    private Long dayLength;

    private String taxRate;

    private BigDecimal plannedCollectionAmount;

    private BigDecimal noTaxAmount;

    private BigDecimal taxAmount;


    /**
     * 核销含税金额
     **/
    private BigDecimal settlementAmount;

    /**
     * 核销不含税金额
     **/
    private BigDecimal settlementNoTaxAmount;

    /**
     * 核销税额
     **/
    private BigDecimal settlementTaxAmount;

    public static PlanWriteOffBo transferByPayPlan(ContractPayPlanConcludeE payPlanConcludeE){
        PlanWriteOffBo bo = new PlanWriteOffBo();
        bo.setPlanId(payPlanConcludeE.getId());
        bo.setDayLength(ChronoUnit.DAYS.between(payPlanConcludeE.getCostStartTime(), payPlanConcludeE.getCostEndTime())+1);
        bo.setTaxRate(payPlanConcludeE.getTaxRate());
        bo.setPlannedCollectionAmount(payPlanConcludeE.getPlannedCollectionAmount());
        bo.setNoTaxAmount(payPlanConcludeE.getNoTaxAmount());
        bo.setTaxAmount(payPlanConcludeE.getTaxAmount());
        return bo;
    }

    public static PlanWriteOffBo transferByIncomePlan(ContractIncomePlanConcludeE incomePlanConcludeE){
        PlanWriteOffBo bo = new PlanWriteOffBo();
        bo.setPlanId(incomePlanConcludeE.getId());
        bo.setDayLength(ChronoUnit.DAYS.between(incomePlanConcludeE.getCostStartTime(), incomePlanConcludeE.getCostEndTime())+1);
        bo.setTaxRate(incomePlanConcludeE.getTaxRate());
        bo.setPlannedCollectionAmount(incomePlanConcludeE.getPlannedCollectionAmount());
        bo.setNoTaxAmount(incomePlanConcludeE.getNoTaxAmount());
        bo.setTaxAmount(incomePlanConcludeE.getTaxAmount());
        return bo;
    }

}
