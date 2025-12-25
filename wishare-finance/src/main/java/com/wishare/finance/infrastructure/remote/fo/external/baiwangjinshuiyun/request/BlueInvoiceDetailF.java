package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 蓝票发票商品明细
 * @author dongpeng
 * @date 2023/10/25 19:32
 *
 */
@Data
@ApiModel("发票商品明细")
public class BlueInvoiceDetailF {

    @ApiModelProperty(value = "单据行号(商品明细序号，整数)",required = true)
    private String djhh;

    @ApiModelProperty(value = "发票行性质(0:正常行\n" +
            "1:折扣行(折扣行金额负)\n" +
            "2:被折扣行(被折扣行金额正))",required = true)
    private String fphxz;

    @ApiModelProperty(value = "商品名称",required = true)
    private String spmc;

    @ApiModelProperty(value = "税收编码(长度19位)",required = true)
    private String ssbm;

    @ApiModelProperty(value = "含税标志(0:不含税1:含税(默认))")
    private String hsbz;

    @ApiModelProperty(value = "规格型号")
    private String ggxh;

    @ApiModelProperty(value = "计量单位(成品油只能为升或吨)")
    private String jldw;

    @ApiModelProperty(value = "商品数量(数电差额征收票商品数量传空值)")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal spsl;

    @ApiModelProperty(value = "含税单价(商品含税单价，数电差额征收票含税单价传空值)")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal hsdj;

    @ApiModelProperty(value = "含税金额（含税标志为1时必填 小数点2位）")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal hsje;

    @ApiModelProperty(value = "不含税单价(商品含税单价，数电差额征收票不含税单价传空值)")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal bhsdj;

    @ApiModelProperty(value = "不含税金额（含税标志为0时必填 小数点2位）")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal bhsje;

    @ApiModelProperty(value = "税率",required = true)
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal tax;

    @ApiModelProperty(value = "税额(含税标志为0时必填 小数点2位)")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal se;

    @ApiModelProperty(value = "零税率标识(0:正常税率(默认)\n" +
            "1:免税\n" +
            "2:不征税\n" +
            "3:普通零税率)")
    private String lslbs;

    @ApiModelProperty(value = "优惠政策标识(1:使用优惠政策\n" +
            "0:不使用(默认))")
    private String yhzcbs;

    @ApiModelProperty(value = "增值税特殊管理(优惠政策标识为1时必填，值为:\n" +
            "免税,\n" +
            "100%先征后退,\n" +
            "50%先征后退,\n" +
            "不征税,\n" +
            "先征后退,\n" +
            "即征即退100%,\n" +
            "即征即退30%,\n" +
            "即征即退50%,\n" +
            "即征即退70%,\n" +
            "按3%易征收,\n" +
            "按5%简易征收,\n" +
            "按5%简易征收减按1.5%计征,\n" +
            "稀土产品,\n" +
            "简易征收,\n" +
            "超税负12%即征即退,\n" +
            "超税负3%即征即退,\n" +
            "超税负6%即征即退,\n" +
            "超税负8%即征即退)")
    private String zzstsgl;

    @ApiModelProperty(value = "商品简称")
    private String spfwjc;

    @ApiModelProperty(value = "商品批次")
    private String sppc;

    @ApiModelProperty(value = "商品代码")
    private String spdm;

    @ApiModelProperty(value = "商品分类")
    private String spfl;

    @ApiModelProperty(value = "商品折扣金额")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal zkje;

    @ApiModelProperty(value = "扣除额(小数点2位)")
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal kce;

}
