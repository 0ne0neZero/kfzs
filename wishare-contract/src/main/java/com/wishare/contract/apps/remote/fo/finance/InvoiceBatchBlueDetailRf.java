package com.wishare.contract.apps.remote.fo.finance;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 发票明细(无校检)
 *
 * @author 永遇乐 yeoman <76164451@.qq.com>
 * @line --------------------------------
 * @date 2023/02/25
 */
@Getter
@Setter
public class InvoiceBatchBlueDetailRf {
    /**
     * 账单id
     */
    private Integer billId;
    /**
     * 账单编号
     */
    private String billNo;
    /**
     * 商品编码（商品税收分类编码开发者自行填写）
     */
    @NotBlank
    private String goodsCode;
    /**
     * 商品名称（如invoiceLineProperty =1，\n则此商品行为折扣行，折扣行不允许多行\n折扣，折扣行必须紧邻被折扣行，商品名\n称必须与被折扣行一致）
     */
    @NotBlank
    private String goodsName;
    /**
     * 数量（精确到小数点后8位，开具红票时数量为负数）
     */
    private String num;
    /**
     * 单价（精确到小数点后8位），当单价(price)为空时，数量(num)也必须为空；(price)为空时，含税金额(taxIncludedAmount)、不含税金额(taxExcludedAmount)、税额(tax)都不能为空
     */
    private String price;
    /**
     * 规格型号
     */
    private String specType;
    /**
     * 含税金额(单位：分)
     */
    private Integer taxIncludedAmount;
    /**
     * 税率，注：1、纸票清单红票存在为null的\n情况；2、二手车发票税率为null或者0
     */
    @NotBlank
    private String taxRate;
    /**
     * 单位
     */
    private String unit;

    public static InvoiceBatchBlueDetailRf create(Integer billId, String billNo, String goodsCode, String goodsName,
                                                  String num, String price, String specType, Integer taxIncludedAmount,
                                                  String taxRate, String unit) {
        InvoiceBatchBlueDetailRf f = new InvoiceBatchBlueDetailRf();
        f.billId = billId;
        f.billNo = billNo;
        f.goodsCode = goodsCode;
        f.goodsName = goodsName;
        f.num = num;
        f.price = price;
        f.specType = specType;
        f.taxIncludedAmount = taxIncludedAmount;
        f.taxRate = taxRate;
        f.unit = unit;
        return f;
    }
}