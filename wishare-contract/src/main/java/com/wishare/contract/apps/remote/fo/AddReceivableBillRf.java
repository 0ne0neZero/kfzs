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
@ApiModel("创建应收/付账单请求信息")
public class AddReceivableBillRf extends AddBillRf{

    @ApiModelProperty(value = "计费方式", required = true)
    @NotNull(message = "计费方式不能为空")
    private Integer billMethod;

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

    @ApiModelProperty(value = "应用id", required = true)
    @NotBlank(message = "应用id不能为空")
    private String appId;

    @ApiModelProperty(value = "应用名称", required = true)
    @NotBlank(message = "应用名称不能为空")
    private String appName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "付款方账户")
    private String payerAccount;

    @ApiModelProperty(value = "收款方账户")
    private String payeeAccount;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）",required = true)
    @NotBlank(message = "结算渠道不能为空")
    private String settleChannel;

    @NotBlank(message = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;
}
