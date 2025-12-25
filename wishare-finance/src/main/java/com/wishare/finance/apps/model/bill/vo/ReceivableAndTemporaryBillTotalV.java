package com.wishare.finance.apps.model.bill.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceivableAndTemporaryBillTotalV {

    @ApiModelProperty("账单数量")
    private Long numTotal = 0L;

    @ApiModelProperty("最后更新时间")
    private LocalDateTime maxGmtModify;

    @ApiModelProperty(value = "账单总金额 （单位：分）", required = true)
    private Long amountTotal = 0L;

    @ApiModelProperty(value = "未收总金额 （单位：分）", required = true)
    private Long notReceivedAmountTotal = 0L;

    @ApiModelProperty(value = "实收总金额 （单位：分）", required = true)
    private Long netReceiptsAmountTotal = 0L;

    /**
     * 应收金额
     */
    private Long receivableAmount = 0L;

    /**
     * 应收减免金额
     */
    private Long deductibleAmount = 0L;

    /**
     * 违约金金额
     */
    private Long overdueAmount = 0L;

    /**
     * 实收减免金额
     */
    private Long discountAmount = 0L;

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
     * 开票金额
     */
    private Long invoiceAmount = 0L;

    /**
     * 获取实际收款金额
     * @return
     */
    public Long getNetReceiptsAmountTotal(){
        return settleAmount - refundAmount - carriedAmount;
    }
    /**
     * 获取剩余可支付金额
     * @return  可支付金额 = 应收金额 + 违约金金额 - 实收减免金额 + 退款金额 + 结算金额（上次结算金额）-收款金额
     */
    public Long getNotReceivedAmountTotal(){
        return receivableAmount + overdueAmount - discountAmount + refundAmount + carriedAmount - settleAmount;
    }
}