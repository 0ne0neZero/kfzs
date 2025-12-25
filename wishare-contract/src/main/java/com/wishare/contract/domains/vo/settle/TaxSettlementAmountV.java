package com.wishare.contract.domains.vo.settle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-20
 */
@Data
public class TaxSettlementAmountV {

    @ApiModelProperty("月份")
    private String month;

    @ApiModelProperty("含税金额")
    private BigDecimal taxAmount;
}
