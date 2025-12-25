package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdvanceBillTotalMoneyV {


    @ApiModelProperty(value = "实收总金额 （单位：分）", required = true)
    private Long netReceiptsAmountTotal = 0L;


    /**
     * 统计账单金额总数
     * 实收金额
     */
    private Long settleAmount = 0L;

    /**
     * 退款金额
     */
    private Long refundAmount = 0L;

    /**
     * 结转金额
     */
    private Long carriedAmount = 0L;

    /**
     * 优惠金额
     */
    private Long preferentialAmount = 0L;



    /**
     * 获取实际收款金额
     * @return
     */
    public Long getNetReceiptsAmountTotal(){
        return settleAmount +preferentialAmount- refundAmount - carriedAmount;
    }

}