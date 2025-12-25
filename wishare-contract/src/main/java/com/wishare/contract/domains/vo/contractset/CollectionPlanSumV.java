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
public class CollectionPlanSumV {

    @ApiModelProperty("本币金额（含税） 应收/应付")
    private BigDecimal localCurrencyAmountSum;

    @ApiModelProperty("已收/已付")
    private BigDecimal paymentAmountSum;

    @ApiModelProperty("未收付金额")
    private BigDecimal beOverdue;
}
