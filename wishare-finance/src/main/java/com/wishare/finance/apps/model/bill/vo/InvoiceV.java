package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/24
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票明细")
public class InvoiceV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("发票id")
    private Long id;

    @ApiModelProperty("发票编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("开票人")
    private String clerk;

    @ApiModelProperty("价税合计金额")
    private Long priceTaxAmount;

    @ApiModelProperty("开具发票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("票据类型" +
            "  1: 增值税普通发票\n" +
            "  2: 增值税专用发票\n" +
            "  3: 增值税电子发票\n" +
            "  4: 增值税电子专票\n" +
            "  5: 收据\n" +
            "  6：电子收据\n" +
            "  7：纸质收据")
    private Integer type;

    @ApiModelProperty("购方名称")
    private String buyerName;

    @ApiModelProperty("销方名称")
    private String salerName;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废")
    private Integer state;
}
