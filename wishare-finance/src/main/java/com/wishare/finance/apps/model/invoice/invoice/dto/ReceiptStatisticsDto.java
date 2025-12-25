package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Getter
@Setter
@ApiModel("分页统计收据信息")
public class ReceiptStatisticsDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("收据数")
    private Long receiptTotal;

    @ApiModelProperty("开具金额")
    private Long priceTaxAmountTotal;
}
