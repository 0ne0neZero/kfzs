package com.wishare.contract.domains.vo.revision.pay;

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
@ApiModel(value = "支出合同付款计划金额汇总表视图对象", description = "支出合同付款计划金额汇总表视图对象")
public class ContractPaySettlementConcludeSumV {

    @ApiModelProperty("计划支出金额")
    private BigDecimal plannedCollectionAmountSum;
    @ApiModelProperty("付款金额")
    private BigDecimal paymentAmountSum;



}
