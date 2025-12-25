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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 创建应付账单请求信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("创建应付账单请求信息")
public class AddPayableBillF extends AddBillF{

    @ApiModelProperty(value = "计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    @Digits(integer = 18, fraction = 6, message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMax(value = "1000000000.000000", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    @DecimalMin(value = "0.000001", message = "计费面积格式不正确，允许区间为[0.000001, 1000000000.000000]")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    @Max(value = 1000000000, message = "单价格式不正确，允许区间为[1, 1000000000]")
    @Min(value = 1, message = "单价格式不正确，允许区间为[1, 1000000000]")
    private BigDecimal unitPrice;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "付款方账户")
    private String payerAccount;

    @ApiModelProperty(value = "收款方账户")
    private String payeeAccount;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;
}
