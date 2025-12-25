package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("创建账单请求信息")
public class AddBillRf {

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

    @ApiModelProperty(value = "费项id", required = true)
    @NotBlank(message = "费项id不能为空")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "房号ID")
    private String roomId;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "发票类型JSON字符串，例：[1,2] 增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据", required = true)
    @NotNull(message = "增值税普通发票不能为空")
    private String invoiceType;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客 4 法定单位）")
    @NotNull(message = "收费对象类型不能为空")
    private Integer payerType;

    @ApiModelProperty(value = "收费对象类型（收款方类型 0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payeeType;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "账单说明", required = true)
    @NotBlank(message = "账单说明不能为空")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（默认：CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额 （区间为[1, 1000000000]）", required = true)
    @NotNull(message = "账单金额不能为空")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "账单金额格式不正确，允许区间为[1, 1000000000]")
    private Long totalAmount;

    @ApiModelProperty(value = "收款方ID")
    private String payeeId;

    @ApiModelProperty(value = "收款方名称")
    private String payeeName;

    @ApiModelProperty(value = "付款方ID")
    private String payerId;

    @ApiModelProperty(value = "付款方名称")
    private String payerName;

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "账单来源", required = true)
    @NotBlank(message = "账单来源不能为空")
    private String source;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty("外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "应用编码")
    private String appNumber = "CONTRACT_SYS";

    @ApiModelProperty("扩展字段1")
    private String extField1;

    @ApiModelProperty("扩展字段6  合同id")
    private String extField6;

}
