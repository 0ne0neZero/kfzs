package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/18
 * @Description: 诺诺请求体
 */
@Getter
@Setter
public class OrderF {

    @ApiModelProperty("终端号（开票终端号，只能 为空或数字）")
    private String terminalNumber;

    @Valid
    @ApiModelProperty(value = "发票明细，支持填写商品明细最大2000行（包含折扣行、被折扣行）",required = true)
    @NotNull(message = "发票明细不能为空")
    private List<InvoiceDetailF> invoiceDetail;

    @ApiModelProperty("购方电话（购方地址+电话总共不超100字符；二手车销售统一发票时必填）")
    private String buyerTel;

    @ApiModelProperty("清单标志：非清单:0；清单:1，默认:0，电票固定为0")
    private String listFlag;

    @ApiModelProperty("推送方式：-1,不推送;0,邮箱;1,手机（默认）;2,邮箱、手机")
    private String pushMode;

    @ApiModelProperty("部门门店id（诺诺系统中的id）")
    private String departmentId;

    @ApiModelProperty("开票员id（诺诺系统中的id）")
    private String clerkId;

    @ApiModelProperty("冲红时，在备注中注明“对应正数发票代码:XXXXXXXXX号码:YYYYYYYY文案，其中“X”为发票代码，“Y”为发票号码，可以不填，接口会自动添加该文案；机动车发票蓝票时备注只能为空")
    private String remark;

    @ApiModelProperty("复核人")
    private String checker;

    @ApiModelProperty("收款人")
    private String payee;

    @ApiModelProperty("购方地址（购方地址+电话总共不超100字符；二手车销售统一发票时必填）")
    private String buyerAddress;

    @ApiModelProperty("购方税号（企业要填，个人可为空；全电专票、二手车销售统一发票时必填）")
    private String buyerTaxNum;

    @ApiModelProperty(value = "开票类型：1:蓝票;2:红票 （全电发票暂不支持红票）",required = true)
    @NotBlank(message = "开票类型不能为空")
    private String invoiceType;

    @ApiModelProperty("发票种类：p,普通发票(电票)(默认);c,普通\n" +
            "发票(纸票);s,专用发票;e,收购发票(电票);f,\n" +
            "收购发票(纸质);r,普通发票(卷式);b,增值税\n" +
            "电子专用发票;j,机动车销售统一发票;u,二手\n" +
            "车销售统一发票;bs:电子发票(增值税专用\n" +
            "发票)-即全电专票,pc:电子发票(普通发票)-\n" +
            "即全电普票")
    private String invoiceLine;

    @ApiModelProperty("推送邮箱（pushMode为0或2时，此项为\n" +
            "必填，同时受企业资质是否必填控制）")
    private String email;

    @ApiModelProperty("销方银行开户行及账号(二手车销售统一发票时必填)")
    private String salerAccount;

    @ApiModelProperty(value = "销方电话",required = true)
    @NotBlank(message = "销方电话不能为空")
    private String salerTel;

    @ApiModelProperty(value = "订单号（每个企业唯一）",required = true)
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty("回传发票信息地址（开票完成、开票失败）")
    private String callBackUrl;

    @ApiModelProperty("红字信息表编号.专票冲红时此项必填，且\n" +
            "必须在备注中注明“开具红字增值税专用\n" +
            "发票信息表编号ZZZZZZZZZZZZZZZ\n" +
            "Z”字样，其 中“Z”为开具红字增值税专\n" +
            "用发票所需要的长度为16位信息表编号\n" +
            "（建议16位，最长可支持24位）")
    private String billInfoNo;

    /*@ApiModelProperty("机动车销售统一发票才需要传")
    private VehicleInfoF vehicleInfo;*/

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "订单时间 2016-01-13 12:30:00",required = true)
    @NotBlank(message = "订单时间不能为空")
    private String invoiceDate;

    @ApiModelProperty("冲红时填写的对应蓝票发票代码（红票必填 10位或12 位， 11位的时候请左补 0）")
    private String invoiceCode;

    @ApiModelProperty("冲红时填写的对应蓝票发票号码（红票必填，不满8位请左补0）")
    private String invoiceNum;

    @ApiModelProperty(value = "销方地址",required = true)
    @NotBlank(message = "销方地址不能为空")
    private String salerAddress;

    @ApiModelProperty("开票员")
    private String clerk;

    @ApiModelProperty("购方手机（pushMode为1或2时，此项为必填，同时受企业资质是否必填控制）")
    private String buyerPhone;

    @ApiModelProperty("购方银行开户行及账号")
    private String buyerAccount;

    @ApiModelProperty("分机号（只能为空或者数字）")
    private String extensionNumber;

    @ApiModelProperty("机器编号（12位盘号）")
    private String machineCode;

    @ApiModelProperty(value = "销方税号（使用沙箱环境请求时消息体参\n" +
            "数salerTaxNum和消息头参数userTax\n" +
            "填写339901999999142）", required = true)
    @NotBlank(message = "销方税号不能为空")
    private String salerTaxNum;

    @ApiModelProperty("清单项目名称：对应发票票面项目名称（listFlag为1时，必填，默认为“详见销货清单”）")
    private String listName;

    @ApiModelProperty("代开标志：0非代开;1代开。代开蓝票时备\n" +
            "注要求填写文案：代开企业税号:***,代开企\n" +
            "业名称:***；代开红票时备注要求填写文案\n" +
            "：对应正数发票代码:***号码:***代开企业\n" +
            "税号:***代开企业名称:***")
    private String proxyInvoiceFlag;

    @ApiModelProperty("特定要素：0普通发票（默认）、1 成品油 、2 稀土（仅支持s、bs票种且编码必须为稀土产品目录中的商品）、" +
            "3 建筑服务、4 货物运输服务、5 不动产销售、6 不动产经营租赁服务、9 旅客运输服务、12 自产农产品销售、16 农产品收购、" +
            "31 建安发票 、 32 房地产销售发票、33 二手车发票反向开具、 34 电子烟、 35 矿产品")
    private String specificFactor;


    private RealPropertyRentInfoF realPropertyRentInfo;
}
