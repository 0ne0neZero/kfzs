package com.wishare.finance.apps.model.invoice.invoice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * 合同发票和收据记录(用于流水认领)
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("发票和收据记录")
public class InvoiceAndReceiptV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("票据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("开票金额")
    private Long priceTaxAmount;

    @ApiModelProperty("红票金额")
    private Long redTaxAmount;

    @ApiModelProperty("开票时间")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime billingTime;

    @ApiModelProperty("开票人")
    private String clerk;

    @ApiModelProperty("票据类型： 1: 增值税普通发票 2: 增值税专用发票  3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据 7：纸质收据")
    private Integer type;

    @ApiModelProperty("票据状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败")
    private Integer state;

    @ApiModelProperty("系统来源：1 收费系统 2合同系统")
    private Integer sysSource;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("成本中心")
    private String costCenterName;

    @ApiModelProperty("法定单位")
    private String statutoryBodyName;
}
