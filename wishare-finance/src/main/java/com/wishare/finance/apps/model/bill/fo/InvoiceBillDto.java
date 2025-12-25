package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/9/27 9:12
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("开票")
public class InvoiceBillDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("票据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty(value = "开票类型：\n" +
        "     1: 增值税普通发票\n" +
        "     2: 增值税专用发票\n" +
        "     3: 增值税电子发票\n" +
        "     4: 增值税电子专票\n" +
        "     5: 收据\n" +
        "     6：电子收据\n" +
        "     7：纸质收据", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("账单结算金额（单位：分）")
    private Long settleAmount;

    @ApiModelProperty("票据结算金额")
    private Long priceTaxAmount;

    @ApiModelProperty("票据id")
    private Long invoiceReceiptId;

    @ApiModelProperty("账单当次的开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败 8.部分红冲")
    private Integer state;
}
