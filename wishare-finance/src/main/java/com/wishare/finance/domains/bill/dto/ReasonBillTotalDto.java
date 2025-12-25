package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("包含欠费原因的账单统计")
public class ReasonBillTotalDto {

    @ApiModelProperty(value = "账单总数", required = true)
    private Long billTotal;

    @ApiModelProperty("应收欠缴金额")
    private Long amountDueArrearsTotal;

    @ApiModelProperty("应收总金额")
    private Long receivableAmountTotal;

    @ApiModelProperty("实收总金额")
    private Long settleAmountTotal;

    @ApiModelProperty("违约金金额 (单位： 分)")
    private Long overdueAmountTotal;

    @ApiModelProperty("实收减免金额 (单位： 分)")
    private Long discountAmountTotal;

    @ApiModelProperty("退款金额")
    private Long refundAmountTotal;

    @ApiModelProperty("结转金额")
    private Long carriedAmountTotal;

    /**
     * 实际应缴金额
     */
    private Long actualUnpayAmountTotal;

    public Long getAmountDueArrearsTotal() {
        return getActualUnpayAmount() == null ? 0 : getActualUnpayAmount();
    }


    public Long getActualUnpayAmount() {
        return (receivableAmountTotal == null ? 0 : receivableAmountTotal)
                + (getOverdueAmountTotal() == null ? 0 :getOverdueAmountTotal())
                - (getDiscountAmountTotal() == null ? 0 :getDiscountAmountTotal())
                + (getRefundAmountTotal() == null ? 0 : getRefundAmountTotal())
                + (getCarriedAmountTotal() == null ? 0 : getCarriedAmountTotal())
                - (getSettleAmountTotal() == null ? 0 : getSettleAmountTotal());
    }
}
