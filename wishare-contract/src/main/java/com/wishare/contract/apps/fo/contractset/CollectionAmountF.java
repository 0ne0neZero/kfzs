package com.wishare.contract.apps.fo.contractset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收款/付款/开票金额参数
 * @author ljx
 */
@Data
public class CollectionAmountF {

    @ApiModelProperty("收款计划id")
    private Long collectionPlanId;

    @ApiModelProperty("计划收款日期")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("本次收款/付款/开票金额")
    private BigDecimal operationAmount;

    @ApiModelProperty("开票税额（开票时使用）")
    private BigDecimal invoiceTax;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;
}
