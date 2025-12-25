package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("批量开票")
public class InvoiceBatchRf {

    @ApiModelProperty("红字信息表编号.专票冲红时此项必填，且必须在备注中注明“开具红字增值税专用发票信息表编号ZZZZZZZZZZZZZZZZ”字样，" +
            "其中“Z”为开具红字增值税专用发票所需要的长度为16位信息表编号")
    private String billInfoNo;

    @ApiModelProperty(value = "账单ids", required = true)
    @NotNull(message = "账单ids不能为空")
    private List<Long> billIds;

    @ApiModelProperty("账单开票金额")
    private List<InvoiceBillAmountRf> invoiceBillAmounts;

    @ApiModelProperty(value = "账单类型（1应收 2预收 3临时）", required = true)
    @NotNull(message = "账单类型不能为空")
    private Integer billType;

    @ApiModelProperty("价税合计（开票金额含税）")
    @NotNull(message = "价税合计不能为空")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "抬头类型：1个人，2企业", required = true)
    @NotNull(message = "抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty(value = "开票人", required = true)
    @NotBlank(message = "开票人不能为空")
    private String clerk;

    @ApiModelProperty(value = "开票类型：1:蓝票;2:红票 （全电发票暂不支持红票）", required = true)
    @NotNull(message = "开票类型不能为空")
    private Integer invoiceType;

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

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "购方税号")
    private String buyerTaxNum;

    @ApiModelProperty(value = "购方电话")
    private String buyerTel;

    @ApiModelProperty(value = "购方地址")
    private String buyerAddress;

    @ApiModelProperty("购方银行开户行及账号")
    private String buyerAccount;

//    @ApiModelProperty(value = "销方税号")
//    private String salerTaxNum;
//
//    @ApiModelProperty(value = "销方电话")
//    private String salerTel;
//
//    @ApiModelProperty(value = "销方地址")
//    private String salerAddress;

    @ApiModelProperty("销方银行开户行及账号")
    private String salerAccount;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "发票来源：1.开具的发票 2.收入的发票", required = true)
    @NotNull(message = "发票来源不能为空")
    private Integer invSource;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机;2,站内信")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushMode;
    @ApiModelProperty(value = "购方手机（pushMode为1或2时，此项为必填）")
    private String buyerPhone;
    @ApiModelProperty(value = "推送邮箱（pushMode为0或2时，此项为必填）")
    private String email;

    @ApiModelProperty("冲红原因")
    private String redReason;

    @ApiModelProperty("扩展字段：合同系统（合同名称）")
    private String extendFieldOne;
}
