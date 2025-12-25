package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 报销发票信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("报销发票信息")
public class ReimbursementInvoiceF {

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer invoiceType;

    @ApiModelProperty(value = "发票类型名称")
    //@NotBlank(message = "发票代码不能为空")
    private String invoiceTypeName;

    @ApiModelProperty(value = "发票代码")
    //@NotBlank(message = "发票代码不能为空")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码", required = true)
    //@NotBlank(message = "发票号码不能为空")
    private String invoiceNo;

    @ApiModelProperty(value = "开票时间 格式：yyyy-MM-dd HH:mm:ss", required = true)
    //@NotNull(message = "开票时间不能为空")
    private LocalDateTime invoiceDate;

    @ApiModelProperty(value = "发票链接地址", required = true)
    //@NotBlank(message = "发票链接地址不能为空")
    private String invoiceUrl;

    @ApiModelProperty(value = "开票员")
    private String clerk = "";

    @ApiModelProperty(value = "发票抬头类型：1 个人 2 企业", required = true)
    private Integer invoiceTitleType;

    @ApiModelProperty(value = "发票内容")
    private String invoiceContent;

    @ApiModelProperty(value = "价税合计金额(单位：分)" , required = true)
    @NotNull(message = "价税合计金额不能为空")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "税后金额(单位：分)")
    private Long excTaxAmount;

    @ApiModelProperty(value = "税额(单位：分)")
    private Long taxAmount;

}
