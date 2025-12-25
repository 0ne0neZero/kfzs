package com.wishare.finance.infrastructure.remote.vo.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/9/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("退款信息")
public class BillRefundRV {

    @ApiModelProperty("申请退款时间")
    private LocalDateTime approveTime;

    @ApiModelProperty("退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty("操作人")
    private String operatorName;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("退款状态：0待退款，1退款中，2已退款，3未生效")
    private Integer state;
}
