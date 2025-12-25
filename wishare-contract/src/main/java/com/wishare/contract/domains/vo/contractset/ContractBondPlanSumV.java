package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractBondPlanSumV {

    @ApiModelProperty("应收本币金额（元）总和")
    private BigDecimal localCurrencyAmountSum;
    @ApiModelProperty("已收/付款金额总和")
    private BigDecimal paymentAmountSum;
    @ApiModelProperty("已收/退款金额总和")
    private BigDecimal refundAmountSum;

}
