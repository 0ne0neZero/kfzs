package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Linitly
 * @date: 2023/6/19 20:38
 * @descrption:
 */
@Data
@ApiModel(value = "商品明细节点")
public class InvoiceDetailF {

    // 必填，长度8
    @ApiModelProperty(value = "明细行号")
    private Integer goodsLineNo;

    // 非必填，长度8
    @ApiModelProperty(value = "对应蓝票明细行号，税控类红字发票开具时无需传值； 全电类红字发票开具时必须传值；")
    private String goodsOriginalLineNo;

    // 非必填，长度1
    @ApiModelProperty(value = "发票行性质， 0：正常行；1：折扣行；2：被折扣行，默认为0")
    private String invoiceLineNature;

    // 非必填，长度19
    @ApiModelProperty(value = "税收分类编码（末级节点）")
    private String goodsCode;

    // 非必填，长度50
    @ApiModelProperty(value = "商品编码，可以支持根据商品名称或商品编码进行获取【增加商品编码字段】：平台必须维护商品信息")
    private String goodsPersonalCode;

    // 非必填，长度92
    @ApiModelProperty(value = "商品名称， 支持根据商品或商品编码获取商品信息：平台必须维 \n" +
            "护商品信息 ～～二选一必填，税控最大长度限制92，乐企连接器最 \n" +
            "大长度限制260，Web连接器最大长度限制100")
    private String goodsName;

    @ApiModelProperty(value = "商品税目")
    private String goodsTaxItem;

    // 非必填，长度40
    @ApiModelProperty(value = "规格型号，税控最大长度限制40，乐企连接器最大长度限制150， \n" +
            "Web连接器最大长度限制40，")
    private String goodsSpecification;

    // 非必填，长度14
    @ApiModelProperty(value = "计量单位，税控最大长度限制14，乐企连接器最大长度限制300， \n" +
            "Web连接器最大长度限制22，特殊票种为12时必填,只能为辆")
    private String goodsUnit;

    // 非必填，长度14
    @ApiModelProperty(value = "商品数量，税控最大长度限制13；乐企连接器最大长度限制16，小 \n" +
            "数点前12位，小数点后13位；Web连接器最大长度限制25，整数部 \n" +
            "分不能超过12位、小数部分不能超过13位，特殊票种为12时必填")
    private BigDecimal goodsQuantity;

    // 非必填，长度13
    @ApiModelProperty(value = "商品单价，税控最大长度限制13；乐企连接器最大长度限制16，小 \n" +
            "数点前12位，小数点后13位；Web连接器最大长度限制25，整数部 \n" +
            "分不能超过12位、小数部分不能超过13位，特殊票种为12时必填")
    private BigDecimal goodsPrice;

    // 必填
    @ApiModelProperty(value = "金额，小数点后2位,超长自动保留两位小数")
    private BigDecimal goodsTotalPrice;

    // 非必填
    @ApiModelProperty(value = "税额，小数点后2位,超长自动保留两位小数， 如果为空，根据金额、税率计算得出")
    private BigDecimal goodsTotalTax;

    // 必填
    @ApiModelProperty(value = "税率,超长自动保留三位小数")
    private BigDecimal goodsTaxRate;

    // 非必填，长度200
    @ApiModelProperty(value = "优惠政策类型， preferentialMarkFlag=1，使用优惠政策时必传 ,如 \n" +
            "“免税”、“50%先征后退”、“即征即退50%”等")
    private String vatSpecialManagement;

    // 非必填，长度1
    @ApiModelProperty(value = "零税率标识， 空代表无； 1：出口免税和其他免税优惠政策；2：不征增值税；3：普通零税率")
    private String freeTaxMark;

    // 非必填，长度1
    @ApiModelProperty(value = "含税标志： 0：不含税，1:含税 默认为0")
    private String priceTaxMark;

    // 非必填，长度1
    @ApiModelProperty(value = "是否使用优惠政策，0：未使用；1：使用 根据商品信息获取")
    private String preferentialMarkFlag;

    @ApiModelProperty(value = "明细行折扣金额，全电不支持此字段")
    private BigDecimal goodsDiscountAmount;

    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;
}
