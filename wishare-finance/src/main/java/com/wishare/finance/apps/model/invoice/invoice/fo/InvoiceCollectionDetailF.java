package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 收票明细信息
 * @author dxclay
 * @since  2023/2/21
 * @version 1.0
 */
@Getter
@Setter
@ApiModel("收票明细信息")
public class InvoiceCollectionDetailF {

    @ApiModelProperty("规格型号")
    private String specType;

    @ApiModelProperty("数量（精确到小数点后8位，开具红票时数量为负数）")
    private String num;

    @ApiModelProperty(value = "税率，注：1、纸票清单红票存在为null的\n" +
            "情况；2、二手车发票税率为null或者0",required = true)
    @NotBlank(message = "税率不能为空")
    private String taxRate;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("单价（精确到小数点后8位），当单价(price)为空时，数量(num)也必须为空；(price)为空时，含税金额(taxIncludedAmount)、不含税金额(taxExcludedAmount)、税额(tax)都不能为空")
    private String price;

    @ApiModelProperty(value = "商品编码（商品税收分类编码开发者自行填写）",required = true)
    @NotBlank(message = "商品编码不能为空(税目不能为空)")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称（如invoiceLineProperty =1，\n" +
            "则此商品行为折扣行，折扣行不允许多行\n" +
            "折扣，折扣行必须紧邻被折扣行，商品名\n" +
            "称必须与被折扣行一致）",required = true)
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @ApiModelProperty("含税金额(单位：分)")
    private Long taxIncludedAmount;

    @ApiModelProperty(value = "单价含税标志：0:不含税,1:含税")
    private Integer withTaxFlag;

    @ApiModelProperty("票据关联的账单信息")
    private InvoiceBillInfoF bill;

}
