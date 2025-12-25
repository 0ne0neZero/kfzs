package com.wishare.finance.apps.model.invoice.invoice.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@ApiModel("补录发票认领分页入参")
public class InvoiceClaimPageF {

    @ApiModelProperty(value = "单据来源")
    private Integer sysSource;

    @ApiModelProperty(value = "单据类型")
    private Integer billType;

    @ApiModelProperty(value = "收款方式")
    private String payChannel;

    @ApiModelProperty(value = "单据编号")
    private String billNo;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "交易金额最小值")
    private Long minSettleAmount;

    @ApiModelProperty(value = "交易金额最小值")
    private Long maxSettleAmount;

    @ApiModelProperty(value = "收费对象名称")
    private String payerName;

    @ApiModelProperty(value = "交易日期")
    private LocalDate minPayTime;

    @ApiModelProperty(value = "交易日期")
    private LocalDate maxPayTime;

}
