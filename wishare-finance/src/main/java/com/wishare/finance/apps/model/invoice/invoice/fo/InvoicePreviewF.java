package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("发票预览入参")
@Setter
@Getter
public class InvoicePreviewF {

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n" +
            "     9：全电专票", required = true)
    private Integer type;

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "购方税号")
    private String buyerTaxNum;

    @ApiModelProperty(value = "购方电话")
    private String buyerTel;

    @ApiModelProperty(value = "购方地址")
    private String buyerAddress;

    @ApiModelProperty("购方银行开户行及账号")
    private String buyerAccount;

    @ApiModelProperty("销方税号")
    private String salerTaxNum;

    @ApiModelProperty("销方电话")
    private String salerTel;

    @ApiModelProperty("销方地址")
    private String salerAddress;

    @ApiModelProperty("销方银行开户行及账号")
    private String salerAccount;

    @ApiModelProperty("开票单元名称")
    private String invRecUnitName;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "账单ids", required = true)
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("指定账单开票金额")
    private List<InvoiceBillAmount> invoiceBillAmounts;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty("价税合计（开票金额含税）")
    @NotNull(message = "价税合计不能为空")
    @Min(value = 1, message = "价税合计必须大于0")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "抬头类型：1个人，2企业", required = true)
    @NotNull(message = "抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty(value = "开票人")
    private String clerk;

    @ApiModelProperty("收款单id")
    private Long gatherBillId;

    @ApiModelProperty("收款单id列表")
    private List<Long> gatherBillIds;

    @ApiModelProperty("收款单明细id列表")
    private List<Long> gatherDetailBillIds;

    @ApiModelProperty(value = "收款单类型 0-收款单， 1-收款明细")
    private Integer gatherBillType;

    @ApiModelProperty("是否免税：0不免税，1免税， 默认不免税")
    private Integer freeTax;

    @ApiModelProperty("收款明细信息")
    private List<GatherInvoiceBatchF> gatherInvoiceBatchFList;


}