package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单合计信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("账单合计信息")
public class BillTotalV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "房号总数", required = true)
    private Long roomTotal;

    @ApiModelProperty(value = "账单总数", required = true)
    private Long billTotal;

    @ApiModelProperty(value = "账单总金额 （单位：分）", required = true)
    private Long amountTotal;

    @ApiModelProperty(value = "账单应收总金额 （单位：分）", required = true)
    private Long receivableAmountTotal;

    @ApiModelProperty(value = "应收减免总金额 （单位：分）", required = true)
    private Long deductibleAmountTotal;

    @ApiModelProperty(value = "实收总金额 （单位：分）", required = true)
    private Long settleAmountTotal;

    @ApiModelProperty(value = "实收减免金额 （单位：分）", required = true)
    private Long discountAmountTotal;

    @ApiModelProperty(value = "退款总金额（单位：分）",required = true)
    private Long refundAmountTotal;

    @ApiModelProperty(value = "结转总金额（单位：分）")
    private Long carriedAmountTotal;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmountTotal;
}
