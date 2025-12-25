package com.wishare.contract.domains.vo.revision.pay;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@ApiModel(value = "支出合同结算单表视图对象V", description = "支出合同结算单表视图对象V")
public class ContractPaySettlementConcludeEditV {
    @ApiModelProperty("结算单信息")
    private ContractPaySettlementConcludeV contractPaySettlementConcludeV;
    @ApiModelProperty("付款计划信息")
    private List<ContractPayPlanConcludeV> contractPayPlanConcludeVList;
    @ApiModelProperty("付款结算单明细信息")
    private List<ContractPaySettDetailsV> contractPaySettDetailsVList;
    @ApiModelProperty("付款结算单扣款明细信息")
    private List<ContractPayConcludeSettdeductionV> contractPayConcludeSettdeductionVList;

    @ApiModelProperty("V2.12-结算单-成本预估计划信息")
    private List<ContractPayPlanForSettlementV> contractPayPlanForSettlementVList;

    @ApiModelProperty("V2.12-结算单-计量周期")
    private List<PayPlanPeriodV> contractPaySettlementPeriodVList;

}
