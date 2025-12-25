package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel("发票预览出参")
@Setter
@Getter
public class InvoicePreviewV {
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

    @ApiModelProperty("销方名称")
    private String salerName;

    @ApiModelProperty("销方税号")
    private String salerTaxNum;

    @ApiModelProperty("销方电话")
    private String salerTel;

    @ApiModelProperty("销方地址")
    private String salerAddress;

    @ApiModelProperty("销方银行开户行及账号")
    private String salerAccount;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "开票人")
    private String clerk;

    @ApiModelProperty("发票详情信息")
    private List<InvoicePreviewDetailV> detailList;

    @ApiModelProperty("商品金额合计")
    private String sumGoodsAmount;

    @ApiModelProperty("商品税额")
    private String sumTaxAmount;

    @ApiModelProperty("价税合计")
    private String sumTaxIncludedAmount;

    @ApiModelProperty("价税合计大写金额")
    private String chineseAmount;

    @ApiModelProperty("开票日期")
    private String invoiceDate;
}