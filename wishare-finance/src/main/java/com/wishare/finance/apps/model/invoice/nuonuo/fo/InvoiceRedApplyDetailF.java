package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("红字申请明细入参")
public class InvoiceRedApplyDetailF {

    @ApiModelProperty(value = "商品编码", required = true)
    @NotBlank(message = "商品编码不能为空")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @ApiModelProperty("数量，带负号；数量、单价必须都不填，或者都必填，不可只填一个；当数量、单价都不填时，商品金额(不含税)、商品税额都必填。建议保留小数点后8位。")
    private String num;

    @ApiModelProperty("商品税额，带负号，精确到小数点后面两\n" +
            "位。商品金额 (不含税)、商品税额任何一\n" +
            "个不传时，会根据传入的单价含税标志、\n" +
            "单价、 数量进行计算， 可能和实际数值存\n" +
            "在误差，建议都传入。")
    private String taxAmount;

    @ApiModelProperty("商品金额(不含税)；带负号，精确到小数点\n" +
            "后面两位。商品金额 (不含税)、商品税额\n" +
            "任何一个不传时，会根据传入的单价含税\n" +
            "标志、单价、数量进行计算，可能和实际\n" +
            "数值存在误差，建议都传入。")
    private String taxExcludedAmount;

    @ApiModelProperty("扣除额(带负号)，小数点后两位。差额征收\n" +
            "的发票目前只支持一行明细。不含税差额\n" +
            "= 不含税金额 - 扣除额； 税额 = 不含税差\n" +
            "额*税率。")
    private String deductions;

    @ApiModelProperty("单价(不含税)为正数，如单价含税标志 wit\n" +
            "hTaxFlag=false，则此项必填。数量、\n" +
            "单价必须都不填，或者都必填，不可只填\n" +
            "一个；当数量、单价都不填时，商品金额 (\n" +
            "不含税)、商品税额都必填。建议金税盘保\n" +
            "留小数点后8位；百旺盘保留小数点后6位")
    private String taxExcludedPrice;

    @ApiModelProperty("单价(含税)为正数，如单价含税标志withT\n" +
            "axFlag=true，则此项必填。数量、单价\n" +
            "必须都不填，或者都必填，不可只填一个\n" +
            "；当数量、单价都不填时，商品金额(不含\n" +
            "税)、商品税额都必填。 建议金税盘保留小\n" +
            "数点后8位；百旺盘保留小数点后6位。")
    private String price;

    @ApiModelProperty(value = "税率", required = true)
    @NotBlank(message = "税率不能为空")
    private String taxRate;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty(value = "单价含税标志:false,不含税;true,含税", required = true)
    @NotBlank(message = "单价含税标志不能为空")
    private String withTaxFlag;

    @ApiModelProperty("优惠政策标识:false,不使用;true,使用")
    private String favouredPolicyFlag;

    @ApiModelProperty("优惠政策内容:即征即退、免税、简易征收等, 当优惠政策标识favouredPolicyFlag为1时，则此项必填。")
    private String favouredPolicyName;

    @ApiModelProperty("零税率标识:空,非零税率;1,免税;2,不征税;3,普通零税率")
    private String zeroRateFlag;
}
