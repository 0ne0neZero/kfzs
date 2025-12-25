package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@ApiModel("发票预览明细详情")
@Setter
@Getter
public class InvoicePreviewDetailV {

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("规格")
    private String goodsSpecifications;

    @ApiModelProperty("单位")
    private String goodsUnit;

    @ApiModelProperty("数量")
    private String goodsCount;

    @ApiModelProperty("单价")
    private String unitPrice;

    @ApiModelProperty("金额")
    private String goodsAmount;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("税额")
    private String taxAmount;

}