package com.wishare.contract.domains.vo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/14:01
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同付款计划金额汇总表视图对象", description = "收入合同付款计划金额汇总表视图对象")
public class ContractIncomePlanConcludeSumV {
    @ApiModelProperty("计划收款金额")
    private BigDecimal plannedCollectionAmountSum;
    @ApiModelProperty("收款金额")
    private BigDecimal paymentAmountSum;
}
