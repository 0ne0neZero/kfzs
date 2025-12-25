package com.wishare.contract.domains.vo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("合同统计-应收应付")
@Data
public class ContractCollectionPlanStatisticsV {

    @ApiModelProperty("应收/应付金额总计")
    private BigDecimal localCurrencyAmountSum;
    @ApiModelProperty("已收/已付金额总计")
    private BigDecimal paymentAmountSum;
    @ApiModelProperty("年月")
    private String dateMonth;
}
