package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:22
 * @descrption: 发票开具接口
 */
@Data
@ApiModel(value = "发票数据请求信息")
public class InvoiceDataReqF {

    // 必填，长度3
    @ApiModelProperty(value = "发票类型代码, 004：增值税专用发票；007：增值税普通发票 \n" +
            "；026：增值税电子发票；025：增值税卷式发票；028:增值税电子 \n" +
            "专用发票 01:全电发票(增值税专用发票) 02:全电发票(普通发票)")
    private String invoiceTypeCode;

    // 非必填，长度2
    @ApiModelProperty(value = "全电纸质发票标志（Y是N否），发票类型代码为01，02时必填； \n" +
            "税控类发票开具不校验此字段；")
    private String paperInvoiceFlag;

    // 非必填，长度32
    @ApiModelProperty(value = "开票类型 0:正数发票（蓝票） 1：负数发票（红票）默认0")
    private String invoiceType;

    // 非必填，长度2
    @ApiModelProperty(value = "特殊票种标志， 00：普通发票；01：农产品销售；02：农产品收购 \n" +
            "；08：成品油 12 机动车（默认是00普通发票）; 全电类发票特殊票 \n" +
            "种标志：03：建筑服务发票；04：货物运输服务发票；05：不动产 \n" +
            "销售服务发票；06：不动产租赁服务发票")
    private String invoiceSpecialMark;

    // 非必填，长度1
    @ApiModelProperty(value = "征税方式， 0：普通征税；2：差额征税（默认是0普通征税）")
    private String taxationMethod;

    // 非必填，长度2
    @ApiModelProperty(value = "差额征税标签：01 差额开票、 02 全额开票 ；发票类型代码为 \n" +
            "01，02时且征税方式为2必填")
    private String taxationLabel;

    // 非必填，长度1
    @ApiModelProperty(value = "0：无清单；1：带清单（专普票发票明细大于等于8行必须带清单 \n" +
            "）：大于8行必须为清单票(电子票只能为非请单票)（默认是0无清 \n" +
            "单），发票类型代码为01，02时该字段无")
    private String invoiceListMark;

    // 非必填，长度1
    @ApiModelProperty(value = "含税标志， 0：不含税；1：含税（默认不含税）")
    private String priceTaxMark;

    // 必填，长度50
    @ApiModelProperty(value = "开票流水号， 唯一标志开票请求。支持数字字母下划线组合。")
    private String serialNo;

    // 非必填，长度20
    @ApiModelProperty(value = "购方单位税号，发票类型代码为004、028、01时 必传")
    private String buyerTaxNo;

    // 必填，长度100
    @ApiModelProperty(value = "购方单位名称，税控最大长度限制100，乐企连接器最大长度限制 \n" +
            "300，Web连接器自然人最大长度限制96，非自然人最大长度限制 \n" +
            "100。")
    private String buyerName;

    // 非必填，长度100
    @ApiModelProperty(value = "购方地址及电话， 增值税专用发票开具时必填，发票类型代码为 \n" +
            "01、02时该字段拆分为地址电话两个字段")
    private String buyerAddressPhone;

    // 非必填，长度100
    @ApiModelProperty(value = "购方开户行及账号， 增值税专用发票开具时必填，发票类型代码 \n" +
            "为01、02时该字段拆分为银行名称、账号两个字段")
    private String buyerBankAccount;

    // 非必填，长度20
    @ApiModelProperty(value = "开票人，税控最大长度限制20，乐企连接器最大长度限制300，如 \n" +
            "果终端有值取终端，如果没有去机构获取，如果都没有会自动获取 \n" +
            "机构下随机用户名称;全电类开具时，根据终端及默认开票员进行匹 \n" +
            "配，如传入的开票人在终端授权下按传入值 开具，如未查询到传入值对应的开票员则通过默认开票员进行开具")
    private String drawer;

    // 非必填，长度16
    @ApiModelProperty(value = "复核人，为空时，如果终端有值取终端，如果没有去 机构获取 \n" +
            "，若都没有则为空！全电类开具时：此字段无需传值，即 发票类 \n" +
            "型代码为01，02时该字段无需传值；")
    private String checker;

    // 非必填，长度16
    @ApiModelProperty(value = "收款人，税控类开具时：若为空，如果终端有值取终端 ,如果没有 \n" +
            "去机构获取，若都没有则为空；全电类开具时：此字段 无需传值 \n" +
            "，即发票类型代码为01，02时该字段无需传值")
    private String payee;

    // 非必填
    @ApiModelProperty(value = "合计金额， 保留两位小数；支持价税分离")
    private BigDecimal invoiceTotalPrice;

    // 非必填
    @ApiModelProperty(value = "合计税额， 保留两位小数；支持价税分离")
    private BigDecimal invoiceTotalTax;

    // 非必填
    @ApiModelProperty(value = "价税合计， 保留两位小数；支持价税分离")
    private BigDecimal invoiceTotalPriceTax;

    // 非必填
    @ApiModelProperty(value = "备注，税控最大长度限制240，乐企连接器最大长度限制450，Web \n" +
            "连接器数电电票230，数电纸票400")
    private String remarks;

    // 非必填，长度20
    @ApiModelProperty(value = "红字信息表/红字确认单编号，开票类型为1时需要必须传值，其中 \n" +
            "发票类型代码为004、028传入红字信息表编号； 发票类型代码为 \n" +
            "01、02时传入红字确认单编号；")
    private String redInfoNo;

    // 非必填，长度12
    @ApiModelProperty(value = "原发票代码， 开票类型为1，负数票开具时必传")
    private String originalInvoiceCode;

    // 非必填，长度8
    @ApiModelProperty(value = "原发票号码， 开票类型为1，负数票开具时必传;")
    private String originalInvoiceNo;

    // 非必填，长度20
    @ApiModelProperty(value = "原全电发票号码")
    private String originalEinvoiceNo;

    // 非必填
    @ApiModelProperty(value = "扣除额， 差额征税时必传。数值必须小于价税 合计。")
    private BigDecimal deductibleAmount;

    // 非必填，长度100
    @ApiModelProperty(value = "销方地址及电话，发票类型代码为01、02时该字段拆分为地址、电话两个字段")
    private String sellerAddressPhone;

    // 非必填，长度100
    @ApiModelProperty(value = "销方开户行及账号，发票类型代码为01、02时不用此字段")
    private String sellerBankAccount;

    // 扩展字段
    private Map<String, Object> ext;

    // 非必填，长度150
    @ApiModelProperty(value = "第三方系统名称")
    private String systemName;

    // 非必填，长度50
    @ApiModelProperty(value = "第三方系统id")
    private String systemId;

    // 非必填，长度100
    @ApiModelProperty(value = "客户邮箱")
    private String buyerEmail;

    // 非必填，长度100
    @ApiModelProperty(value = "抄送人邮箱,多个用英文逗号隔开,最多5个抄送人信息")
    private String emailCarbonCopy;

    // 非必填，长度40
    @ApiModelProperty(value = "客户电话")
    private String buyerPhone;

    // 非必填，长度30
    @ApiModelProperty(value = "用户账号，用于个人维度数据标记")
    private String userAccount;

    // 非必填，长度10
    @ApiModelProperty(value = "红冲原因 传数字部分 1:销货退回 2:开票有误 3:服务终止 4:销售折让 \n" +
            "）税控红冲原因：建议按实际开票情况传入红冲原因，若不传系统 \n" +
            "会自动根据情况判断并赋值；全电红冲原因：非必填，如填写的冲 \n" +
            "红原因与红字确认单不一致，将使用确认单对应的原因进行红冲开 \n" +
            "具")
    private String redIssueReason;

    // 非必填，长度10
    @ApiModelProperty(value = "整单折扣类型 1 :按折扣金额价内折扣,2:按折扣金额价外折扣,3:按折 \n" +
            "扣率价内折扣,4:按折扣率价外折扣")
    private String discountType;

    // 非必填
    @ApiModelProperty(value = "整单折扣金额,大于0小于发票总金额，如果是含税发票，大于0小于含税总金额")
    private BigDecimal discountAmount;

    // 非必填
    @ApiModelProperty(value = "整单折扣率,取值[1-100]正整数")
    private Integer discountRate;

    // 非必填，长度10
    @ApiModelProperty(value = "卷式发票票样 01 02 03 04 05 06 07")
    private String mainGoodsName;

    // 非必填，长度120
    @ApiModelProperty(value = "购买方银行名称，发票类型代码为01、02时该字段可用，乐企连接 \n" +
            "器最大长度限制120，Web连接器最大长度限制100")
    private String buyerBankName;

    // 非必填，长度50
    @ApiModelProperty(value = "购买方银行账号，发票类型代码为01、02时该字段可用")
    private String buyerBankNumber;

    // 非必填，长度300
    @ApiModelProperty(value = "购买方地址，发票类型代码为01、02时该字段可用，乐企连接器最 \n" +
            "大长度限制300，Web连接器最大长度限制140")
    private String buyerAddress;

    // 非必填，长度60
    @ApiModelProperty(value = "购买方电话，发票类型代码为01、02时该字段可用")
    private String buyerTelphone;

    // 非必填，长度120
    @ApiModelProperty(value = "销方银行名称，发票类型代码为01、02时该字段可用，乐企连接器 \n" +
            "最大长度限制120，Web连接器最大长度限制100")
    private String sellerBankName;

    // 非必填，长度50
    @ApiModelProperty(value = "销方银行账号，发票类型代码为01、02时该字段可用")
    private String sellerBankNumber;

    // 非必填，长度300
    @ApiModelProperty(value = "销方地址，乐企连接器最大长度限制300，Web连接器最大长度限 \n" +
            "制140，发票类型代码为01、02时该字段可用")
    private String sellerAddress;

    // 非必填，长度60
    @ApiModelProperty(value = "销方电话，发票类型代码为01、02时该字段可用")
    private String sellerTelphone;

    // 非必填，长度60
    @ApiModelProperty(value = "红字信息表UUID，开具全电负数发票必传")
    private String redConfirmUuid;

    // 非必填，长度50
    @ApiModelProperty(value = "合同号")
    private String contractNumber;

    // 非必填，长度50
    @ApiModelProperty(value = "凭证号")
    private String voucherNo;

    @ApiModelProperty(value = "商品明细节点")
    private List<InvoiceDetailF> invoiceDetailsList;

    @ApiModelProperty(value = "全电发票差额扣除额凭证明细，当征税方式为：差额征税-差额开票时必填")
    private List<InvoiceBalanceInfoF> invoiceBalanceinfoList;

    @ApiModelProperty(value = "特定业务信息节点,当特殊票种标志为03、04、05、06时必填")
    private List<invoiceSpecialInfoF> invoiceSpecialInfoList;
}
