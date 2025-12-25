package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("开具收据")
public class ReceiptBatchRf {

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty("收据号")
    private Long receiptNo;

    @ApiModelProperty(value = "开票员名称", required = true)
    @NotBlank(message = "开票员名称不能为空")
    private String clerk;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "收据来源：1.开具的收据 2.收入的收据", required = true)
    @NotNull(message = "收据来源不能为空")
    private Integer invSource;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty("缴费时间")
    private LocalDateTime paymentTime;

    @ApiModelProperty("缴费方式")
    private String paymentType;

    @ApiModelProperty("图章url")
    private String stampUrl;

    @ApiModelProperty("优惠信息" +
            " {\n" +
            "     \"goodsName\":\"\",\n" +
            "     \"price\":\"\",\n" +
            "     \"num\":\"\",\n" +
            "     \"totalPrice\":\"\",\n" +
            "     \"remark\":\"\"\n" +
            "     }")
    private String discountInfo;

    @ApiModelProperty(value = "价税合计", required = true)
    @NotNull(message = "价税合计不能为空")
    @Min(value = 1,message = "价税合计必须大于0")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "账单ids", required = true)
    @NotNull(message = "账单ids不能为空")
    private List<Long> billIds;

    @ApiModelProperty("指定账单开票金额")
    private List<InvoiceBillAmount> invoiceBillAmounts;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushMode;
}
