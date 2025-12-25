package com.wishare.contract.domains.vo.revision.income;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.Digits;
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
@ApiModel(value = "收入计划", description = "收入计划")
public class PayIncomePlanV extends Tree<PayIncomePlanV,String> {

    @ApiModelProperty("收入计划id")
    private String planId;

    @ApiModelProperty("收款计划id")
    private String incomePlanId;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("调整原因")
    private String adjustment;

    @ApiModelProperty("暂估确收金额")
    private BigDecimal confirmedAmountReceivedTemp;

    @ApiModelProperty("税额")
    @Digits(integer = 15,fraction =6,message = "税额不正确")
    private BigDecimal taxAmount;
}
