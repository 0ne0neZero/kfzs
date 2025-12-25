package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:51
 * @descrption:
 */
@Data
@ApiModel(value = "红字确认详情请求数据体")
public class RedConfirmDetailReqF {

    // 必填，长度8
    @ApiModelProperty(value = "蓝字发票明细序号")
    private Integer	originalInvoiceDetailNo = 1;

    // 必填，长度8
    @ApiModelProperty(value = "序号")
    private Integer	goodsLineNo = 1;

    // 必填，长度19
    @ApiModelProperty(value = "税收分类编码(商品和服务税收分类合并编码)")
    private String goodsCode = "";

    // 必填，长度300
    @ApiModelProperty(value = "商品全称（ 简称自定义名称）")
    private String goodsName = "";

    // 非必填，长度120
    @ApiModelProperty(value = "商品服务简称(税收分类简称)")
    private String goodsSimpleName = "";

    // 非必填，长度600
    @ApiModelProperty(value = "项目名称(自定义商品名称)")
    private String projectName = "";

    // 非必填，长度150
    @ApiModelProperty(value = "规格型号")
    private String goodsSpecification = "";

    // 非必填，长度300
    @ApiModelProperty(value = "单位，乐企连接器最大长度限制300，Web连接器最大长度限制22")
    private String goodsUnit = "";

    // 非必填，长度25
    @ApiModelProperty(value = "单价，乐企连接器小数点前12位，小数点后13位，长度 \n" +
            "最大合计16位；Web连接器最大长度25位，整数部分不 \n" +
            "能超过12位、小数部分不能超过13位")
    private String goodsPrice = "";

    // 非必填，长度25
    @ApiModelProperty(value = "数量，乐企连接器小数点前12位，小数点后13位，长度 \n" +
            "最大合计16位；Web连接器最大长度25位，整数部分不 \n" +
            "能超过12位、小数部分不能超过13位")
    private String goodsQuantity = "";

    // 必填
    @ApiModelProperty(value = "税率")
    private BigDecimal goodsTotalTax = BigDecimal.ZERO;

    // 必填
    @ApiModelProperty(value = "金额")
    private BigDecimal goodsTotalPrice = BigDecimal.ZERO;

    // 必填
    @ApiModelProperty(value = "税额")
    private BigDecimal goodsTaxRate = BigDecimal.ZERO;

    // 响应字段
    @ApiModelProperty(value = "特定征税方式代码 01:不征税 02:零税率 03:差额开票 04:免税 05:简易计税 06:减按")
    private String specialTaxCode = "";
}
