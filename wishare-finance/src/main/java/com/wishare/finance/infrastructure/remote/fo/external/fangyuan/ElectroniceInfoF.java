package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author dongpeng
 * @date 2023/6/19 17:17
 * 实时开票接口 发票主要信息表
 */
@Getter
@Setter
@ApiModel("发票主要信息表")
public class ElectroniceInfoF {

    @ApiModelProperty(value = "发票请求唯一流水号，每张发票的发票请求唯一流水号无重复，由企业定义，25位以上" +
            "（参考规则：企业税号+流水号）",required = true)
    @NotBlank(message = "发票请求唯一流水号不能为空")
    private String FPQQLSH;

    @ApiModelProperty(value = "订单号（可重复）",required = true)
    @NotBlank(message = "订单号不能为空")
    private String DDH;

    @ApiModelProperty(value = "开票方纳税人识别号",required = true)
    @NotBlank(message = "开票方纳税人识别号不能为空")
    private String KP_NSRSBH;

    @ApiModelProperty(value = "开票方名称",required = true)
    @NotBlank(message = "开票方名称不能为空")
    private String KP_NSRMC;

    @ApiModelProperty(value = "开票分机号")
    private String NSRDZDAH;

    @ApiModelProperty(value = "发票种类，51盒子版本必填：\n" +
            "51为电子发票；\n" +
            "41为卷式发票")
    private String SWJG_DM;

    @ApiModelProperty(value = "代开标志，自开(0)；一般企业选择0\n" +
            "代开(1)",required = true)
    @NotBlank(message = "代开标志不能为空")
    private String DKBZ;

    @ApiModelProperty(value = "主要开票项目（主要开票商品，或者第一条商品，取项目信息中第一条数据的项目名称（传递大类例如：办公用品））")
    private String KPXM;

    @ApiModelProperty(value = "销货方识别号（必填，如果是电商自营开具发票，填写第3条中的开票方识别号，" +
            "如果是商家驻电商开具发票，填写商家的纳税人识别号）",required = true)
    @NotBlank(message = "销货方识别号不能为空")
    private String XHF_NSRSBH;

    @ApiModelProperty(value = "销货方名称，必填，纳税人名称",required = true)
    @NotBlank(message = "销货方名称不能为空")
    private String XHF_MC;

    @ApiModelProperty(value = "销货方地址",required = true)
    @NotBlank(message = "销货方地址不能为空")
    private String XHF_DZ;

    @ApiModelProperty(value = "销货方电话",required = true)
    @NotBlank(message = "销货方电话不能为空")
    private String XHF_DH;

    @ApiModelProperty(value = "销货方银行账号，开户行及账号",required = true)
    @NotBlank(message = "销货方银行账号不能为空")
    private String XHF_YHZH;

    @ApiModelProperty(value = "购货方名称，购货方名称，即发票抬头。购货方为“个人”时，可输入名称，输入名称是为“个人(名称)”，”（”为半角；例个人(王杰)",required = true)
    @NotBlank(message = "购货方名称不能为空")
    private String GHF_MC;

    @ApiModelProperty(value = "购货方识别号，企业消费，如果填写识别号，需要传输过来")
    private String GHF_NSRSBH;

    @ApiModelProperty(value = "购货方地址")
    private String GHF_DZ;

    @ApiModelProperty(value = "购货方固定电话")
    private String GHF_GDDH;

    @ApiModelProperty(value = "购货方手机，此字段有值会进行短信推送")
    private String GHF_SJ;

    @ApiModelProperty(value = "购货方邮箱，此字段有值会进行邮箱推送")
    private String GHF_EMAIL;

    @ApiModelProperty(value = "购货方银行账号，开户行及账号")
    private String GHF_YHZH;

    @ApiModelProperty(value = "开票员",required = true)
    @NotBlank(message = "开票员不能为空")
    private String KPR;

    @ApiModelProperty(value = "收款员",required = true)
    @NotBlank(message = "收款员不能为空")
    private String SKR;

    @ApiModelProperty(value = "复核人",required = true)
    @NotBlank(message = "复核人不能为空")
    private String FHR;

    @ApiModelProperty(value = "开票类型，1正票、2红票",required = true)
    @NotNull(message = "开票类型不能为空")
    private String KPLX;

    @ApiModelProperty(value = "原发票代码，如果CZDM不是10时候都是必录")
    private String YFP_DM;

    @ApiModelProperty(value = "原发票号码，如果CZDM不是10时候都是必录")
    private String YFP_HM;

    @ApiModelProperty(value = "操作代码，10正票正常开具\n" +
            "20 退货折让红票",required = true)
    @NotBlank(message = "操作代码不能为空")
    private String CZDM;

    @ApiModelProperty(value = "冲红原因，冲红时填写，由企业定义")
    private String CHYY;

    @ApiModelProperty(value = "价税合计金额，小数点后2位，以元为单位精确到分\n" +
            "（负数票时为负数）",required = true)
    @NotNull(message = "价税合计金额不能为空")
    private double KPHJJE;

    @ApiModelProperty(value = "合计不含税金额。所有商品行不含税金额之和，小数点后2位，以元为单位精确到分（单行商品金额之和）\n" +
            "（负数票时为负数）")
    private double HJBHSJE;

    @ApiModelProperty(value = "合计税额。所有商品行税额之和，数点后2位，以元为单位精确到分（单行商品金额之和）\\n\" +\n" +
            "            \"（负数票时为负数）")
    private double HJSE;

    @ApiModelProperty(value = "税收分类编码版本号,目前需要传33.0",required = true)
    @NotBlank(message = "税收分类编码版本号不能为空")
    private String BMB_BBH;

    @ApiModelProperty(value = "备注")
    private String BZ;

    @ApiModelProperty(value = "发票明细",required = true)
    @NotNull(message = "发票明细不能为空")
    private List<ElectroniceDetailF> details;




}
