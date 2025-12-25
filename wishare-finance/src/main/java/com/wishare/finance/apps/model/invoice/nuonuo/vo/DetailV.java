package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("红字信息表明细信息列表")
public class DetailV {

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("规格型号")
    private String specType;

    @ApiModelProperty("数量")
    private String num;

    @ApiModelProperty("单价(不含税)")
    private String taxExcludedPrice;

    @ApiModelProperty("商品金额(不含税)")
    private String taxExcludedAmount;

    @ApiModelProperty("商品税额")
    private String taxAmount;

    @ApiModelProperty("税率，注：蓝票为清单发票时，值有可能为null")
    private String taxRate;

    @ApiModelProperty("商品编码")
    private String goodsCode;

    @ApiModelProperty("企业自编码")
    private String selfCode;

    @ApiModelProperty("优惠政策标识 (0:不使用 1:使用)")
    private String favouredPolicyFlag;

    @ApiModelProperty("优惠政策内容:即征即退、免税、简易征收等")
    private String favouredPolicyName;

    @ApiModelProperty("零税率标识(空:非零税率 1:免税 2:不征税 3:普通零税率)")
    private String zeroRateFlag;

    @ApiModelProperty("商品编码简称")
    private String goodsCodeAbb;

}
