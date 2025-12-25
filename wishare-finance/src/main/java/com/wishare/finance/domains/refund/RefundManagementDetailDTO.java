package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class RefundManagementDetailDTO {

    @ApiModelProperty(value = "项目id")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "费项编码")
    private Long chargeItem;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "房间id")
    private String cpUnitId;

    @ApiModelProperty(value = "房号")
    private String cpUnitName;

    @ApiModelProperty(value = "账单编号")
    private String unionBillsString;
    private String billNo;

    @ApiModelProperty(value = "应收账单类型 1应收，3临时")
    private Integer billType;
    @ApiModelProperty(value = "应收账单类型 1应收，3临时")
    private String billTypeName;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "是否是违约金：0-否/1-是")
    private Integer overdue;

    @ApiModelProperty(value = "是否是违约金")
    private String isDefaultName;

    @ApiModelProperty(value = "收款金额（单位：分）")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "已退款/已结转金额（元（单位：分）")
    private BigDecimal refundCarriedAmount;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    /**
     * 支付申请单编号
     */
    private String payApplyCode;
    /**
     * 支付申请单id
     */
    private Long payApplyId;
}
