package com.wishare.contract.apps.fo.revision.remote;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/5/15/10:49
 */
@Setter
@Getter
@ApiModel("发票明细信息")
public class InvoiceZJF {
    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票类型")
    private String invoiceType;

    @ApiModelProperty("发票日期")
    private LocalDate invoiceDate;

    @ApiModelProperty("发票抬头")
    private String invoiceTitle;

    @ApiModelProperty("纳税人识别号")
    private String taxNum;

    @ApiModelProperty("收款金额")
    private Long payAmount;

    @ApiModelProperty("税额")
    private Long taxAmount;
}

