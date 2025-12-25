package com.wishare.finance.domains.reconciliation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 *
 * @Author dxclay
 * @Date 2022/10/27
 * @Version 1.0
 */
@Getter
@Setter
public class FlowInvoiceDetailDto {

    @ApiModelProperty(value = "对账流水id")
    private Long id;

    @ApiModelProperty(value = "流水号")
    private String serialNumber;

    @ApiModelProperty(value = "发票id")
    private Long invoiceId;

    @ApiModelProperty(value = "发票认领金额(单位：分)")
    private Long flowAmount;

    @ApiModelProperty(value = "交易金额(单位：分)")
    private Long settleAmount;

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "对方名称")
    private String oppositeName;

    @ApiModelProperty(value = "对方账户")
    private String oppositeAccount;

    @ApiModelProperty("对方开户行")
    private String oppositeBank;

    @ApiModelProperty(value = "本方账户")
    private String ourAccount;

    @ApiModelProperty(value = "本方名称")
    private String ourName;

    @ApiModelProperty("本方开户行")
    private String ourBank;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "资金用途")
    private String fundPurpose;

    @ApiModelProperty(value = "交易平台")
    private String tradingPlatform;

    @ApiModelProperty(value = "交易方式")
    private String transactionMode;

}
