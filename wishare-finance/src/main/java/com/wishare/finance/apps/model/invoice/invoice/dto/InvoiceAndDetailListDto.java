package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zyj
 * @date 2023/08/19
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票记录")
public class InvoiceAndDetailListDto {


    private Long priceTaxAmount;

    @ApiModelProperty("税价合计金额")
    private String invoicePrice;

    @ApiModelProperty("发票url")
    private String invoiceUrl;

    @ApiModelProperty("诺诺pdfurl地址")
    private String nuonuoUrl;

//    @ApiModelProperty("发票明细")
//    private List<InvoiceReceiptDetailForMessageDto> invoiceReceiptDetailForMessageDtoList;

//    @ApiModelProperty(value = "开票金额", required = true)
//    private Long invoiceAmount;

    @ApiModelProperty(value = "销方名称", required = true)
    private String salerName;

    @ApiModelProperty(value = "税率")
    private String taxRateString;

    @ApiModelProperty(value = "开票日期")
    private LocalDate billingTime;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

//    @ApiModelProperty("开票失败原因")
//    private String failReason;
//    @ApiModelProperty("金额单位")
//    private String amountUnit = "分";
//
    @ApiModelProperty("发票id")
    private Long id;

    @ApiModelProperty("发票收据主表id")
    private Long invoiceReceiptId;
//
//    @ApiModelProperty("开票类型：1:蓝票;2:红票")
//    private Integer invoiceType;
//
    @ApiModelProperty("票据类型\n" +
            "  1: 增值税普通发票\n" +
            "  2: 增值税专用发票\n" +
            "  3: 增值税电子发票\n" +
            "  4: 增值税电子专票\n" +
            "  5: 收据\n" +
            "  6：电子收据\n" +
            "  7：纸质收据")
    private Integer type;
//
//    @ApiModelProperty("法定单位id")
//    private Long statutoryBodyId;
//
//    @ApiModelProperty("法定单位名称")
//    private String statutoryBodyName;
//
//    @ApiModelProperty("收据编号")
//    private String invoiceReceiptNo;
//
//    @ApiModelProperty("项目ID")
//    private String communityId;
//
//    @ApiModelProperty("项目名称")
//    private String communityName;
//
//    @ApiModelProperty(value = "客户id")
//    private String customerId;
//

//
//    @ApiModelProperty(value = "客户手机号")
//    private String customerPhone;
//
//    @ApiModelProperty("开票单元id")
//    private String invRecUnitId;
//
//    @ApiModelProperty("开票单元名称")
//    private String invRecUnitName;
//

//
//    @ApiModelProperty("开票员")
//    private String clerk;
//
//    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废")
//    private Integer state;
//
//    @ApiModelProperty("系统来源：1 收费系统 2合同系统")
//    private Integer sysSource;
//
//    @ApiModelProperty("发票代码")
//    private String invoiceCode;
//
//    @ApiModelProperty("发票号码")
//    private String invoiceNo;
//    @ApiModelProperty(value = "发票抬头类型：1 个人 2 企业", required = true)
//    private Integer invoiceTitleType;
//
//    @ApiModelProperty(value = "购方税号", required = true)
//    private String buyerTaxNum;
//
//    @ApiModelProperty(value = "销方税号", required = true)
//    private String salerTaxNum;
//
//    @ApiModelProperty(value = "销方电话", required = true)
//    private String salerTel;
//    @ApiModelProperty(value = "发票流水号")
//    private String invoiceSerialNum;
//    @ApiModelProperty(value = "销方地址", required = true)
//    @NotBlank(message = "销方地址不能为空")
//    private String salerAddress;

}
