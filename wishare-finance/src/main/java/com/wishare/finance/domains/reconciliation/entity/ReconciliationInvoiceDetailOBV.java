package com.wishare.finance.domains.reconciliation.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 对账票据信息值对象
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Setter
@Getter
public class ReconciliationInvoiceDetailOBV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "发票id")
    private Long id;

    @ApiModelProperty(value = "发票号")
    private String invoiceNo;

    @ApiModelProperty(value = "开票金额")
    private Long invoiceAmount;

    @ApiModelProperty(value = "票据金额")
    private Long receiptAmount;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "票据类型\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据")
    private Integer invoiceType;

    @ApiModelProperty(value = "开票时间 格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceTime;

    @ApiModelProperty(value = "开票人ID")
    private String creator;

    @ApiModelProperty(value = "开票人名称")
    private String creatorName;

}
