package com.wishare.finance.domains.invoicereceipt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/7/22 15:10
 * @descrption:
 */
@Data
@ApiModel("发票开票明细-分组合并后用于对接第三方")
public class InvoiceDetailDto {

    @ApiModelProperty(value = "分组所得key")
    private String key;

    @ApiModelProperty(value = "明细类型：1:蓝票明细;2:部分红冲明细;3:全部红冲明细;")
    private Integer detailType;

    @ApiModelProperty(value = "明细蓝票行")
    private Integer blueLineNo;

    @ApiModelProperty(value = "商品ID")
    private String goodsId;

    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品数量")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品单位")
    private String goodsUnit;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品规格型号")
    private String goodsSpecification;


    @ApiModelProperty(value = "单价含税标志：0:不含税,1:含税")
    private Integer withTaxFlag;

    @ApiModelProperty(value = "零税率标识")
    private String freeTaxMark;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "税额")
    private BigDecimal tax;

//    @ApiModelProperty(value = "金额")
//    private BigDecimal goodsTotalPrice;

    // 价税合计=金额+税额
    @ApiModelProperty(value = "价税合计")
    private BigDecimal goodsTotalPriceTax;

    // 全部红冲使用
    @ApiModelProperty(value = "红冲过的税额")
    private List<BigDecimal> redFlushedTax;

    @ApiModelProperty(value = "红冲过的价税合计")
    private List<BigDecimal> redFlushedTotalPriceTax;
}
