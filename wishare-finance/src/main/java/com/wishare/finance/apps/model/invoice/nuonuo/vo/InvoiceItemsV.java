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
@ApiModel("发票明细集合反参")
public class InvoiceItemsV {

    @ApiModelProperty("扣除额，小数点后两位。差额票时有值")
    private String deduction;

    @ApiModelProperty("简称")
    private String itemCodeAbb;

    @ApiModelProperty("自行编码")
    private String itemSelfCode;

    @ApiModelProperty("商品名称(项目名称)")
    private String itemName;

    @ApiModelProperty("单位")
    private String itemUnit;

    @ApiModelProperty("单价（isIncludeTax=true，是含税单价；isIncl\n" +
            "udeTax=false，是不含税单价）")
    private String itemPrice;

    @ApiModelProperty("税率，注：纸票清单红票存在为null的情况")
    private String itemTaxRate;

    @ApiModelProperty("数量")
    private String itemNum;

    @ApiModelProperty("金额（isIncludeTax=true，是含税金额；isIncl\n" +
            "udeTax=false，是不含税金额）")
    private String itemAmount;

    @ApiModelProperty("金额（\n" +
            "根据含税标志决定是含税金额还是不含税金额）")
    private String itemSumAmount;

    @ApiModelProperty("税额")
    private String itemTaxAmount;

    @ApiModelProperty("规格型号")
    private String itemSpec;

    @ApiModelProperty("商品编码")
    private String itemCode;

    @ApiModelProperty("含税标识 true：含税 false：不含税")
    private String isIncludeTax;

    @ApiModelProperty("发票行性质0, 正常行;1,折扣行;2,被扣行")
    private String invoiceLineProperty;

    @ApiModelProperty("零税率标识:空：非零税率，1：免税，2：不征税，3：普通零税率；（全电发票时为空）")
    private String zeroRateFlag;

    @ApiModelProperty("优惠政策名称（增值税特殊管理）；全电发票时为空")
    private String favouredPolicyName;

    @ApiModelProperty("优惠政策标识:0：不使用;1：使用；（全电发票时\n" +
            "： 01：简易征收 02：稀土产品 03：免税 04\n" +
            "：不征税 05：先征后退 06：100%先征后退 07\n" +
            "：50%先征后退 08：按3%简易征收 09：按5%\n" +
            "简易征收 10：按5%简易征收减按1.5%计征 11\n" +
            "：即征即退30% 12：即征即退50% 13：即征即退70% 14：即征即退100% 15：超税负3%即征\n" +
            "即退 16：超税负8%即征即退 17：超税负12%即\n" +
            "征即退 18：超税负6%即征即退）")
    private String favouredPolicyFlag;
}
