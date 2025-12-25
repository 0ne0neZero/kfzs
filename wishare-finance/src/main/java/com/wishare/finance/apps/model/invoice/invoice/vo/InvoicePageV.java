package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("发票信息")
public class InvoicePageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty("发票id")
    private Long id;

    @ApiModelProperty("开票类型：1:蓝票;2:红票")
    private Integer invoiceType;

    @ApiModelProperty("票据类型\n" +
            "  1: 增值税普通发票\n" +
            "  2: 增值税专用发票\n" +
            "  3: 增值税电子发票\n" +
            "  4: 增值税电子专票\n" +
            "  5: 收据\n" +
            "  6：电子收据\n" +
            "  7：纸质收据")
    private Integer type;

    @ApiModelProperty("票据类型名称")
    private String typeName;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("收据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;

    @ApiModelProperty("开票单元id")
    private String invRecUnitId;

    @ApiModelProperty("开票单元名称")
    private String invRecUnitName;

    @ApiModelProperty("税价合计金额")
    private BigDecimal priceTaxAmount;

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("开票员")
    private String clerk;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废")
    private Integer state;

    @ApiModelProperty("开票状态名称")
    private String stateName;

    @ApiModelProperty("系统来源：1 收费系统 2合同系统")
    private Integer sysSource;

    @ApiModelProperty("系统来源名称")
    private String sysSourceName;

    @ApiModelProperty("来源：1 系统生成 2 系统导入")
    private Integer source;

    @ApiModelProperty("来源名称")
    private String sourceName;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("发票url")
    private String invoiceUrl;

    @ApiModelProperty("诺诺pdfurl地址")
    private String nuonuoUrl;

    @ApiModelProperty("开票失败原因")
    private String failReason;

    @ApiModelProperty("发票明细")
    private List<InvoiceReceiptDetailV> invoiceReceiptDetailVS;

    @ApiModelProperty(value = "开票金额", required = true)
    @NotNull(message = "开票金额不能为空")
    private Long invoiceAmount;

    @ApiModelProperty(value = "发票抬头类型：1 个人 2 企业", required = true)
    @NotNull(message = "发票抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty(value = "购方税号", required = true)
    @NotBlank(message = "购方税号不能为空")
    private String buyerTaxNum;

    @ApiModelProperty(value = "销方税号", required = true)
    @NotBlank(message = "销方税号不能为空")
    private String salerTaxNum;

    @ApiModelProperty(value = "销方电话", required = true)
    @NotBlank(message = "销方电话不能为空")
    private String salerTel;

    @ApiModelProperty(value = "销方地址", required = true)
    @NotBlank(message = "销方地址不能为空")
    private String salerAddress;

    @ApiModelProperty(value = "发票流水号")
    private String invoiceSerialNum;

    @ApiModelProperty(value = "账单编号，分割（开票导出使用）")
    private String billNoStr;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客）")
    private String payerType;

    @ApiModelProperty(value = "扩展字段：推凭/报账 状态（拈花湾金蝶报账）")
    private String extendFieldTwo;

    @ApiModelProperty(value = "扩展字段：推凭/报账 错误信息（拈花湾金蝶报账）")
    private String extendFieldThree;

}
