package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("账单开票信息")
public class BillInvoiceDto {

    @ApiModelProperty("发票id")
    private Long id;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("购方税号")
    private String buyerTaxNum;

    @ApiModelProperty("开票员")
    private String clerk;

    @ApiModelProperty("客户id")
    private String customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("客户手机号")
    private String customerPhone;

    @ApiModelProperty("价税合计金额")
    private Long priceTaxAmount;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("发票抬头类型：1 个人 2 企业")
    private Integer invoiceTitleType;

    @ApiModelProperty("开票类型：1:蓝票;2:红票")
    private Integer invoiceType;

    @ApiModelProperty("销方税号")
    private String salerTaxNum;

    @ApiModelProperty("销方电话")
    private String salerTel;

    @ApiModelProperty("销方地址")
    private String salerAddress;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废")
    private Integer state;

    @ApiModelProperty("系统来源：1 收费系统 2合同系统")
    private Integer sysSource;

    @ApiModelProperty("发票url")
    private String invoiceUrl;

    @ApiModelProperty("诺诺pdfurl地址")
    private String nuonuoUrl;

    @ApiModelProperty("开票失败原因")
    private String failReason;

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("票据类型1: 增值税普通发票2: 增值税专用发票3: 增值税电子发票4: 增值税电子专票5: 收据6：电子收据7：纸质收据")
    private Integer type;

}
