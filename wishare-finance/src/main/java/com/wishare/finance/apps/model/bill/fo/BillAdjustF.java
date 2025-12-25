package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 账单调整入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("账单调整入参")
public class BillAdjustF {

    @ApiModelProperty(value = "账单id")
    @NotNull(message = "账单id不能空")
    private Long billId;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他", required = true)
    @NotNull(message = "调整方式不能空")
    private Integer adjustWay;

    @ApiModelProperty(value = "调整内容", required = true)
    @NotBlank(message = "调整内容不能为空")
    private String content;

    @ApiModelProperty(value = "调整原因 1：物业服务终止、失联，2：破产或房屋拍卖、易主房屋，3: 销售承诺、房屋质量问题,4: 服务质量瑕疵,5: 法院判决类、属地法规规定的空置减免类, 99: 其他")
    private Integer reason;

    @ApiModelProperty(value = "调整金额", required = true)
    @Max(value = 1000000000, message = "调整金额格式不正确，区间为[1, 1000000000]")
    @Min(value = 1, message = "调整金额格式不正确，区间为[1, 1000000000]")
    private Long adjustAmount;

    @ApiModelProperty(value = "调整类型： 1减免，2调高，3调低")
    private Integer adjustType;

    @ApiModelProperty(value = "调整比例，区间[0.01, 100]")
    private BigDecimal adjustRatio;

    @ApiModelProperty(value = "单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

    @ApiModelProperty(value = "收费对象ID")
    private String payerId;

    @ApiModelProperty(value = "收费对象名称")
    private String payerName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "计费面积 (单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty(value = "减免凭证号")
    private String voucher;

    @ApiModelProperty(value = "已审核已结算账单调低和减免审核后操作：1退款，2转预收")
    private Integer approvedAction;

    @ApiModelProperty(value = "转预收的费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "转预收的费项名称")
    private String chargeItemName;
}
