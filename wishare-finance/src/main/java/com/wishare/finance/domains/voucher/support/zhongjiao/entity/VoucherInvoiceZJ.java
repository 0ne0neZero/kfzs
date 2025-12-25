package com.wishare.finance.domains.voucher.support.zhongjiao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 *
 */
@Getter
@Setter
@TableName("voucher_invoice_ZJ")
public class VoucherInvoiceZJ extends BaseEntity {
    @ApiModelProperty(value = "id")
    private Long id;

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

    @ApiModelProperty(value = "纳税人识别号")
    private String taxNum;

    @ApiModelProperty("收款金额")
    private Long payAmount;

    @ApiModelProperty("税额")
    private Long taxAmount;

    @ApiModelProperty("合同发票主表id")
    private Long contractInvoiceId;

    @ApiModelProperty("报账单主表Id")
    private Long voucherBillId;

}
