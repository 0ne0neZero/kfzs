package com.wishare.contract.domains.vo.revision.income.settlement;

import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeSettlementConcludeV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeConcludeSettdeductionV;
import com.wishare.contract.domains.vo.revision.income.settdetails.ContractIncomeSettDetailsV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPaySettlementConcludeV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPayConcludeSettdeductionV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
import com.wishare.contract.domains.vo.revision.pay.settlement.ContractPayPlanForSettlementV;
import com.wishare.contract.domains.vo.revision.pay.settlement.PayPlanPeriodV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/10:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同结算单表视图对象V", description = "收入合同结算单表视图对象V")
public class ContractIncomeSettlementConcludeEditV {
    @ApiModelProperty("结算单信息")
    private ContractIncomeSettlementConcludeV contractIncomeSettlementConcludeV;
    @ApiModelProperty("付款计划信息")
    private List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList;
    @ApiModelProperty("付款结算单明细信息")
    private List<ContractIncomeSettDetailsV> contractIncomeSettDetailsVList;
    @ApiModelProperty("付款结算单扣款明细信息")
    private List<ContractIncomeConcludeSettdeductionV> contractIncomeConcludeSettdeductionVList;

    @ApiModelProperty("V2.12-结算单-成本预估计划信息")
    private List<ContractIncomePlanForSettlementV> contractIncomePlanForSettlementVList;

    @ApiModelProperty("V2.12-结算单-计量周期")
    private List<IncomePlanPeriodV> contractIncomeSettlementPeriodVList;

}
