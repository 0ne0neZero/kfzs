package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 账单合计信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 *
 * @see com.wishare.finance.domains.bill.entity.ReceivableBill;
 */
@Setter
@Getter
@ApiModel("账单合计信息")
public class BillTotalDto {

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

    @ApiModelProperty("冲销/作废金额")
    private Long cancelAmout;

    @ApiModelProperty("房间id")
    private String roomId;

    @ApiModelProperty("费项id")
    private Long chargeItemId;


    public Long getCancelAmout() {
        return Objects.nonNull(cancelAmout)?cancelAmout:0;
    }


}
