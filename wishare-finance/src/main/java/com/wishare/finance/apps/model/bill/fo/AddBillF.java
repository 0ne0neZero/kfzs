package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Valid
@ApiModel("创建账单请求信息")
public class AddBillF {
    @ApiModelProperty("是否已审核 true已审核，false待审核")
    private Boolean approvedFlag;

    @ApiModelProperty("法定单位id")
    @NotNull(message = "法定单位id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称")
    @NotBlank(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id", required = true)
    @NotNull(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty("业务单元id")
    private Long businessUnitId;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "发票类型JSON字符串，例：[1,2] 增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据", required = true)
    private String invoiceType;

    @ApiModelProperty(value = "付款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer payerLabel;

    @ApiModelProperty(value = "收款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payeeType;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "账单说明", required = true)
    @NotBlank(message = "账单说明不能为空")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（默认：CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额", required = true)
    @NotNull(message = "账单金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许最大值为1000000000")
    private Long totalAmount;

    @ApiModelProperty("是否负数手续费 0：否 1：是")
    private Integer negativeCommission = 0;

    @ApiModelProperty(value = "收款方ID")
    private String payeeId;

    @ApiModelProperty(value = "收款方名称")
    private String payeeName;

    @ApiModelProperty("收款方手机号")
    private String payeePhone;

    @ApiModelProperty(value = "付款方ID")
    private String payerId;

    @ApiModelProperty(value = "付款方名称")
    private String payerName;

    @ApiModelProperty("付款方手机号")
    private String payerPhone;

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "账单来源", required = true)
    @NotBlank(message = "账单来源不能为空")
    private String source;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "应用id", required = true)
    @NotBlank(message = "应用id不能为空")
    private String appId;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "应用编码")
    private String appNumber;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer settleWay;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("收费组织id")
    private String cpOrgId;

    @ApiModelProperty("收费组织名称")
    private String cpOrgName;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty("收费单元id")
    private String cpUnitId;

    @ApiModelProperty("收费单元名称")
    private String cpUnitName;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty(value = "税额-新")
    private BigDecimal taxAmountNew;

    @ApiModelProperty("扩展字段1")
    private String extField1;

    @ApiModelProperty("扩展字段2")
    private String extField2;

    @ApiModelProperty("扩展字段3")
    private String extField3;

    @ApiModelProperty("扩展字段4(是否暂估收入)")
    private String extField4;

    @ApiModelProperty("扩展字段5")
    private String extField5;

    @ApiModelProperty("扩展字段6")
    private String extField6;

    @ApiModelProperty("扩展字段7")
    private String extField7;

    @ApiModelProperty("扩展字段8")
    private String extField8;

    @ApiModelProperty("扩展字段9")
    private String extField9;

    @ApiModelProperty("扩展字段10")
    private String extField10;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "归属月（账期）")
    private LocalDate accountDate;


    @ApiModelProperty(name = "往来单位id")
    private String businessPartnerId;

    @ApiModelProperty(name = "往来单位名称")
    private String businessPartnerName;



}
