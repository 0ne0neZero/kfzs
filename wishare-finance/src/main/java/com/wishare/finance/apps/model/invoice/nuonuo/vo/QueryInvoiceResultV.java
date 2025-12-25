package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票结果查询反参")
public class QueryInvoiceResultV {

    @ApiModelProperty("发票请求流水号")
    private String serialNo;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("发票状态： 2 :开票完成（ 最终状 态），其他状态\n" +
            "分别为: 20:开票中; 21:开票成功签章中;22:开票失\n" +
            "败;24: 开票成功签章失败;3:发票已作废 31: 发票作\n" +
            "废中 备注：22、24状态时，无需再查询，请确认\n" +
            "开票失败原因以及签章失败原因； 注：请以该状\n" +
            "态码区分发票状态")
    private String status;

    @ApiModelProperty("发票状态描述")
    private String statusMsg;

    @ApiModelProperty("失败原因")
    private String failCause;

    @ApiModelProperty("发票pdf地址（若同时返回了ofdUrl与pdfUrl，则pdf文件不能做为原始凭证，请用ofd文件做为原始凭证）")
    private String pdfUrl;

    @ApiModelProperty("发票图片地址")
    private String pictureUrl;

    @ApiModelProperty("开票时间")
    private String invoiceTime;

    @ApiModelProperty("发票代码（全电发票时为空）")
    private String invoiceCode;

    @ApiModelProperty("发票号码（全电发票时为20位）")
    private String invoiceNo;

    @ApiModelProperty("不含税金额")
    private String exTaxAmount;

    @ApiModelProperty("合计税额")
    private String taxAmount;

    @ApiModelProperty("价税合计")
    private String orderAmount;

    @ApiModelProperty("购方名称（付款方名称）")
    private String payerName;

    @ApiModelProperty("购方税号（付款方税号）")
    private String payerTaxNo;

    @ApiModelProperty("购方地址")
    private String address;

    @ApiModelProperty("购方电话")
    private String telephone;

    @ApiModelProperty("购方开户行及账号")
    private String bankAccount;

    @ApiModelProperty("发票种类，包含：增值税电子普通发票、增值税普\n" +
            "通发票、专用发票(电子)、增值税专用发票、收购\n" +
            "发票(电子)、收购发票(纸质)、增值税普通发票(卷\n" +
            "式)、机动车销售统一发票、二手车销售统一发票\n" +
            "、电子发票（增值税专用发票）、电子发票（普通\n" +
            "发票）； 备注：电子发票（增值税专用发票）即\n" +
            "全电专票，电子发票（普通发票）即 全电普票")
    private String invoiceKind;

    @ApiModelProperty("校验码（全电发票时为空）")
    private String checkCode;

    @ApiModelProperty("二维码")
    private String qrCode;

    @ApiModelProperty("税控设备号（机器编码）；全电发票时为空")
    private String machineCode;

    @ApiModelProperty("发票密文（全电发票时为空）")
    private String cipherText;

    @ApiModelProperty("含底图纸票pdf地址")
    private String paperPdfUrl;

    @ApiModelProperty("发票ofd地址（公共服务平台签章时返回）")
    private String ofdUrl;

    @ApiModelProperty("开票员")
    private String clerk;

    @ApiModelProperty("收款人")
    private String payee;

    @ApiModelProperty("复核人")
    private String checker;

    @ApiModelProperty("销方银行账号")
    private String salerAccount;

    @ApiModelProperty("销方电话")
    private String salerTel;

    @ApiModelProperty("销方地址")
    private String salerAddress;

    @ApiModelProperty("销方税号")
    private String salerTaxNum;

    @ApiModelProperty("销方名称")
    private String saleName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("成品油标志：0非成品油，1成品油")
    private String productOilFlag;

    @ApiModelProperty("图片地址（多个图片以逗号隔开）")
    private String imgUrls;

    @ApiModelProperty("分机号")
    private String extensionNumber;

    @ApiModelProperty("终端号")
    private String terminalNumber;

    @ApiModelProperty("对应蓝票发票代码，红票时有值（全电红票时为空）")
    private String oldInvoiceCode;

    @ApiModelProperty("对应蓝票发票号码，红票时有值（全电红票时为20位数字）")
    private String oldInvoiceNo;

    @ApiModelProperty("清单标志:0,非清单;1,清单票")
    private String listFlag;

    @ApiModelProperty("清单项目名称:打印清单时对应发票票面项目名称")
    private String listName;

    @ApiModelProperty("购方手机(开票成功会短信提醒购方)")
    private String phone;

    @ApiModelProperty("购方邮箱推送邮箱(开票成功会邮件提醒购方)")
    private String notifyEmail;

    @ApiModelProperty("是否机动车类专票 0-否 1-是")
    private String vehicleFlag;

    @ApiModelProperty("数据创建时间（回传其他信息时返回）")
    private String createTime;

    @ApiModelProperty("数据更新时间（回传其他信息时返回）")
    private String updateTime;

    @ApiModelProperty("代开标志 0-非代开 1-代开（回传其他信息时返回）")
    private String proxyInvoiceFlag;

    @ApiModelProperty("用于开票的订单的时间（回传其他信息时返回）")
    private String invoiceDate;

    @ApiModelProperty("开票类型 1-蓝票 2-红票（回传其他信息时返回）")
    private String invoiceType;

    @ApiModelProperty("冲红原因 1:销货退回;2:开票有误;3:服务中止;4:发生\n" +
            "销售折让（红票且票种为p、c、e、f、r（成品油\n" +
            "发票除外）且回传其他信息时返回）")
    private String redReason;

    @ApiModelProperty("作废时间（已作废状态下的发票，且回传其他信息时返回）")
    private String invalidTime;

    @ApiModelProperty("作废来源 1-诺诺工作台 2-API接口 3-开票软件 4\n" +
            "-验签失败作废 5-其他（已作废状态下的发票，且\n" +
            "回传其他信息时返回）")
    private String invalidSource;

    @ApiModelProperty("发票特定要素：（后续枚举值会有扩展，回传其他\n" +
            "信息时返回）0-普通 1-成品油发票 31-建安发票\n" +
            "32-房地产销售发票")
    private String specificFactor;

    @ApiModelProperty("购买方经办人姓名（全电发票特有字段）")
    private String buyerManagerName;

    @ApiModelProperty("经办人证件类型：101-组织机构代码证, 102-营业\n" +
            "执照, 103-税务登记证, 199-其他单位证件, 201-\n" +
            "居民身份证, 202-军官证, 203-武警警官证, 204-\n" +
            "士兵证, 205-军队离退休干部证, 206-残疾人证,\n" +
            "207-残疾军人证（1-8级）, 208-外国护照, 210-\n" +
            "港澳居民来往内地通行证, 212-中华人民共和国往\n" +
            "来港澳通行证, 213-台湾居民来往大陆通行证, 214\n" +
            "-大陆居民往来台湾通行证, 215-外国人居留证, 21\n" +
            "6-外交官证 299-其他个人证件(全电发票特有)")
    private String managerCardType;

    @ApiModelProperty("经办人证件号码（全电发票特有字段）")
    private String managerCardNo;

    @ApiModelProperty("发票明细集合")
    private List<InvoiceItemsV> invoiceItems;


}
