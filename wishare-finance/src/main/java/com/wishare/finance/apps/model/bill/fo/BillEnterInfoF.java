package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单入账信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单入账信息")
public class BillEnterInfoF {

    @ApiModelProperty(value = "账单类型 1:应收，2:预收，3:临时，4:应付", required = true)
    private Integer billType;

    @ApiModelProperty(value = "法定单位id, 法定单位id和编码二选一条件", required = true)
    private String statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称, 法定单位id和编码二选一条件", required = true)
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称")
    @NotBlank(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    @ApiModelProperty(value = "费项id，费项id和编码二选一条件", required = true)
    private String chargeItemId;

    @ApiModelProperty(value = "费项编码，费项id和编码二选一条件", required = true)
    private String chargeItemCode;

    @ApiModelProperty(value = "费项名称", required = true)
    @NotBlank(message = "费项名称不能为空")
    private String chargeItemName;

    @ApiModelProperty(value = "", required = true)
    private String supCpUnitId = "上级收费单元id, 账单最小维度的上级维度，应用系统自定义（如项目id）可用于后续业务操作";

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName = "";

    @ApiModelProperty(value = "收费单元id, 账单最小维度，应用系统自定义（如房号id）可用于后续业务操作", required = true)
    private String cpUnitId = "";

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName = "";

    @ApiModelProperty(value = "收款账号id")
    private String sbAccountId;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号， 可用于账单展示信息", required = true)
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id， 业务唯一标识如订单号等")
    private String outBusId;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty(value = "收费对象属性（1个人，2企业）")
    private Integer customerLabel;

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

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty("计费面积(单位：m²)")
    @Digits(integer = 18, fraction = 6, message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMax(value = "1000000000.000000", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMin(value = "0.000001", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    @Max(value = 1000000000, message = "单价格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "单价格式不正确，允许区间为[1, 1000000000]")
    private Long unitPrice;

    @ApiModelProperty(value = "账单开始时间", required = true)
    @NotBlank(message = "账单开始时间不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间", required = true)
    @NotBlank(message = "账单结束时间不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty("扩展字段1")
    private String extField1;

    @ApiModelProperty("扩展字段2")
    private String extField2;

    @ApiModelProperty("扩展字段3")
    private String extField3;

    @ApiModelProperty("扩展字段4")
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

}
