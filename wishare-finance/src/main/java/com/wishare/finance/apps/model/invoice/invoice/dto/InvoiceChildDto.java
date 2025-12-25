package com.wishare.finance.apps.model.invoice.invoice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/12/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票子表vo")
public class InvoiceChildDto {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("发票收据主表id")
    private Long invoiceReceiptId;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("开票金额")
    private Long invoiceAmount;

    @ApiModelProperty("发票url地址")
    private String invoiceUrl;

    @ApiModelProperty("开票状态： 2 开票成功 3 开票失败 5 已红冲  8.部分红冲")
    private Integer state;

    @ApiModelProperty("诺诺发票pdf地址")
    private String nuonuoUrl;

    @ApiModelProperty("失败原因")
    private String failReason;

    @ApiModelProperty("第三方反参")
    private String thridReturnParameter;

    @ApiModelProperty("票据类型\n" +
            "  1: 增值税普通发票\n" +
            "  2: 增值税专用发票\n" +
            "  3: 增值税电子发票\n" +
            "  4: 增值税电子专票\n" +
            "  5: 收据\n" +
            "  6：电子收据\n" +
            "  7：纸质收据")
    private Integer type;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty(value = "客户id")
    private String customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;

    @ApiModelProperty("开票时间")
    private LocalDateTime billingTime;

    @ApiModelProperty("开票员")
    private String clerk;
}
