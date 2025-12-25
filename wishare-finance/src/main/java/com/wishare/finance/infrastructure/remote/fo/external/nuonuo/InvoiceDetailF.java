package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author xujian
 * @date 2022/8/8
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票明细，支持填写商品明细最大2000行（包含折扣行、被折扣行）")
public class InvoiceDetailF {


    @ApiModelProperty("规格型号")
    private String specType;

    @ApiModelProperty("不含税金额。红票为负。不含税金额、税\n" +
            "额、含税金额任何一个不传时，会根据传\n" +
            "入的单价，数量进行计算，可能和实际数\n" +
            "值存在误差，建议都传入")
    private String taxExcludedAmount;

    @ApiModelProperty("发票行性质：0,正常行;1,折扣行;2,被折扣行，红票只有正常行")
    private String invoiceLineProperty;

    @ApiModelProperty("增值税特殊管理（优惠政策名称）,当favo\n" +
            "uredPolicyFlag为1时，此项必填 （全电\n" +
            "发票时为空）")
    private String favouredPolicyName;

    @ApiModelProperty("数量（精确到小数点后8位，开具红票时数量为负数）")
    private String num;

    @ApiModelProperty(value = "单价含税标志：0:不含税,1:含税",required = true)
    @NotBlank(message = "单价含税标志不能为空")
    private String withTaxFlag;

    @ApiModelProperty("税额，[不含税金额] * [税率] = [税额]；税\n" +
            "额允许误差为 0.06。红票为负。不含税金\n" +
            "额、税额、含税金额任何一个不传时，会\n" +
            "根据传入的单价，数量进行计算，可能和\n" +
            "实际数值存在误差，建议都传入")
    private String tax;

    @ApiModelProperty("优惠政策标识：0,不使用;1,使用; 全电发票\n" +
            "时： 01：简易征收 02：稀土产品 03：免\n" +
            "税 04：不征税 05：先征后退 06：100%\n" +
            "先征后退 07：50%先征后退 08：按3%\n" +
            "简易征收 09：按5%简易征收 10：按5%\n" +
            "简易征收减按1.5%计征 11：即征即退30%\n" +
            "12：即征即退50% 13：即征即退70% 14\n" +
            "：即征即退100% 15：超税负3%即征即退\n" +
            "16：超税负8%即征即退 17：超税负12%\n" +
            "即征即退 18：超税负6%即征即退")
    private String favouredPolicyFlag;

    @ApiModelProperty(value = "税率，注：1、纸票清单红票存在为null的\n" +
            "情况；2、二手车发票税率为null或者0",required = true)
    @NotBlank(message = "税率不能为空")
    private String taxRate;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("扣除额，差额征收时填写，目前只支持填\n" +
            "写一项。 注意：当传0、空或字段不传时\n" +
            "，都表示非差额征税；传0.00才表示差额\n" +
            "征税：0.00 （全电发票暂不支持）")
    private String deduction;

    @ApiModelProperty("单价（精确到小数点后8位），当单价(price)为空时，数量(num)也必须为空；(price)为空时，含税金额(taxIncludedAmount)、不含税金额(taxExcludedAmount)、税额(tax)都不能为空")
    private String price;

    @ApiModelProperty("零税率标识：空,非零税率;1,免税;2,不征税;\n" +
            "3,普通零税率；1、当税率为：0%，且增\n" +
            "值税特殊管理：为“免税”， 零税率标识\n" +
            "：需传“1” 2、当税率为：0%，且增值\n" +
            "税特殊管理：为\"不征税\" 零税率标识：需\n" +
            "传“2” 3、当税率为：0%，且增值税特\n" +
            "殊管理：为空 零税率标识：需传“3”\n" +
            "（全电发票时为空）")
    private String zeroRateFlag;

    @ApiModelProperty("商品编码（商品税收分类编码开发者自行填写）")
    private String goodsCode;

    @ApiModelProperty("自行编码（可不填）")
    private String selfCode;

    @ApiModelProperty(value = "商品名称（如invoiceLineProperty =1，\n" +
            "则此商品行为折扣行，折扣行不允许多行\n" +
            "折扣，折扣行必须紧邻被折扣行，商品名\n" +
            "称必须与被折扣行一致）",required = true)
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @ApiModelProperty("含税金额，[不含税金额] + [税额] = [含税\n" +
            "金额]，红票为负。不含税金额、税额、含\n" +
            "税金额任何一个不传时，会根据传入的单\n" +
            "价，数量进行计算，可能和实际数值存在\n" +
            "误差，建议都传入")
    private String taxIncludedAmount;
}
