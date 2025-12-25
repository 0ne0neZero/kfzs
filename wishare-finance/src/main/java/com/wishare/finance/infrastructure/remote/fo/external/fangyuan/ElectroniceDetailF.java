package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author dongpeng
 * @date 2023/6/19 17:18
 * 发票明细行信息
 */
@Getter
@Setter
@ApiModel("发票明细表")
public class ElectroniceDetailF {

    @ApiModelProperty(value = "项目名称，折扣项目名称必须与上一行被折扣行同名，且项目金额和税额为负数。折扣行必须紧邻被折扣行的下方。同一个税率下的被折扣项目。",required = true)
    @NotBlank(message = "项目名称不能为空")
    private String XMMC;

    @ApiModelProperty(value = "项目单位")
    private String DW;

    @ApiModelProperty(value = "规格型号")
    private String GGXH;

    @ApiModelProperty(value = "项目数量,小数点后8位, 小数点后都是0时，PDF上只显示整数\n" +
            "（负数票时为负数）")
    private double XMSL;

    @ApiModelProperty(value = "含税标志,表示项目单价和项目金额是否含税。0表示都不含税，1表示都含税。目前输入含税价,即标志为1",required = true)
    @NotBlank(message = "含税标志不能为空")
    private String HSBZ;

    @ApiModelProperty(value = "项目单价,小数点后8位小数点后都是0时，PDF上只显示2位小数；否则只显示至最后一位不为0的数字,目前为含税单价\n" +
            "（负数票时为正数）")
    private double XMDJ;

    @ApiModelProperty(value = "项目编码")
    private String XMBM;

    @ApiModelProperty(value = "商品分类编码，税收分类编码",required = true)
    @NotBlank(message = "商品分类编码不能为空")
    private String SPBM;

    @ApiModelProperty(value = "自行编码")
    private String ZXBM;

    @ApiModelProperty(value = "优惠政策标识,是否享受优惠政策：必填\n" +
            "0：不使用，1使用",required = true)
    @NotBlank(message = "优惠政策标识不能为空")
    private String YHZCBS;

    @ApiModelProperty(value = "零税率标识,空：非零税率，0：出口零税，1：免税，2：不征税，3普通零税率")
    private String LSLBS;

    @ApiModelProperty(value = "增值税特殊管理,当YHZCBS是1时必填，例：免税")
    private String ZZSTSGL;

    @ApiModelProperty(value = "项目金额,小数点后2位，以元为单位精确到分。\n" +
            "等于=单价*数量，根据含税标志，确定此金额是否为含税金额。目前为输入含税金额\n" +
            "（负数票或者为折扣行时为负数）",required = true)
    @NotNull(message = "项目金额不能为空")
    private double XMJE;

    @ApiModelProperty(value = "税率,如果税率为0，表示免税,输入小数如0.17 表示17%",required = true)
    @NotBlank(message = "税率不能为空")
    private String SL;

    @ApiModelProperty(value = "税额,小数点后2位，以元为单位精确到分\n" +
            "（负数票或者为折扣行时为负数)")
    private double SE;

    @ApiModelProperty(value = "扣除额,小数点后2位，以元为单位精确到分")
    private double KCE;
}
