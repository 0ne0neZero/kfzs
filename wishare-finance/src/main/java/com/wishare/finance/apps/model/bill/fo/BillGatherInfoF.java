package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单交易信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("收款账单信息")
public class BillGatherInfoF {

    @ApiModelProperty(value = "账单id", required = true)
    private String billId;

    @ApiModelProperty(value = "账单支付金额(分)", required = true)
    private Long amount;

    @ApiModelProperty(value = "账单开始时间， 应收账单为必填项")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间， 应收账单为必填项")
    private LocalDateTime endTime;

}
