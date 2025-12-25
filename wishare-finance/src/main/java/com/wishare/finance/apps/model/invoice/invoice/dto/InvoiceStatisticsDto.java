package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/9/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("分页统计发票信息")
public class InvoiceStatisticsDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("发票数")
    private Long invoiceTotal;

    @ApiModelProperty("开具金额")
    private Long priceTaxAmountTotal;
}
