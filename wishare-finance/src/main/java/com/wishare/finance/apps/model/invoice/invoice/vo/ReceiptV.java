package com.wishare.finance.apps.model.invoice.invoice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("收据信息")
public class ReceiptV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty("票据类型\n" +
            "  1: 增值税普通发票\n" +
            "  2: 增值税专用发票\n" +
            "  3: 增值税电子发票\n" +
            "  4: 增值税电子专票\n" +
            "  5: 收据\n" +
            "  6：电子收据\n" +
            "  7：纸质收据")
    private Integer type;

    @ApiModelProperty(value = "账单类型：1.应收账单 2.预收账单 3.临时缴费账单")
    private Integer billType;

    @ApiModelProperty(value = "账单缴费日期")
    private LocalDateTime billPayTime;

    @ApiModelProperty("收据id")
    private Long id;

    @ApiModelProperty("收据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "房间名称")
    private String roomName;

    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;

    @ApiModelProperty("开票单元id")
    private String invRecUnitId;

    @ApiModelProperty("开票单元名称")
    private String invRecUnitName;

    @ApiModelProperty("税价合计金额")
    private BigDecimal priceTaxAmount;

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("开票员")
    private String clerk;

    @ApiModelProperty("开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废")
    private Integer state;

    @ApiModelProperty("系统来源：1 收费系统 2合同系统")
    private Integer sysSource;

    @ApiModelProperty("收据明细")
    private List<InvoiceReceiptDetailV> invoiceReceiptDetailVS;

    @ApiModelProperty("账单编号（导出使用多个编号“，”分割）")
    private String billNos;

    @ApiModelProperty(value = "推送状态  0待推送 1已推送 2 推送失败")
    private Integer pushState;

    @ApiModelProperty("最新发送时间")
    private LocalDateTime lastPushTime;

    @ApiModelProperty("外部签章pdf(e签宝)")
    private String signReceiptUrl;

    @ApiModelProperty("签章状态0：关闭 1：开启")
    private Integer signStatus;

    @ApiModelProperty("pdf")
    private String receiptUrl;

    @ApiModelProperty("0 - 申请签署 1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署）")
    private Integer signSealStatus;

    @ApiModelProperty(value = "购方手机（用来推送短信）")
    private String buyerPhone;

    @ApiModelProperty(value = "作废收据pdf url地址")
    private String voidPdf;

    @ApiModelProperty(value = "0 - 申请签署 1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署） 8 - 未知（调用服务异常等）")
    private Integer signVoidStatus;

    @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败 4:不需要")
    private Integer voidSendStatus;

    @ApiModelProperty(value = "模板样式")
    private Integer templateStyle;

    @ApiModelProperty(value = "开具申请编号")
    private String signApplyNo;

    @ApiModelProperty(value = "作废申请编号")
    private String voidSignApplyNo;


}
