package com.wishare.finance.domains.reconciliation.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 对账流水值对象
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Getter
@Setter
public class ReconciliationFlowDetailOBV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "对账流水id")
    private Long id;

    @ApiModelProperty(value = "流水号")
    private String serialNumber;

    @ApiModelProperty(value = "发票id")
    private Long invoiceId;

    @ApiModelProperty(value = "发票认领金额(单位：分)")
    private Long flowAmount = 0L;

    @ApiModelProperty(value = "交易金额(单位：分)")
    private Long settleAmount = 0L;

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "对方名称")
    private String oppositeName;

    @ApiModelProperty(value = "对方账户")
    private String oppositeAccount;

    @ApiModelProperty(value = "本方账户")
    private String ourAccount;

    @ApiModelProperty(value = "本方名称")
    private String ourName;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "资金用途")
    private String fundPurpose;

    @ApiModelProperty(value = "交易平台")
    private String tradingPlatform;

    @ApiModelProperty(value = "流水类型：1收入 2退款")
    private Integer type;

    @ApiModelProperty(value = "认领类型：1:发票认领;2:账单认领;")
    private String claimType;

    @ApiModelProperty(value = "认领ID类型：1:蓝票;2:红票;3:收款单;4:退款单;")
    private String claimIdType;
}
