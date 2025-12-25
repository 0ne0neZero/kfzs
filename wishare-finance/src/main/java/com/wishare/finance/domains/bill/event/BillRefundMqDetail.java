package com.wishare.finance.domains.bill.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/11/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("退款mq发送")
public class BillRefundMqDetail {

    @ApiModelProperty("退款金额，单位分")
    private Long refundAmount;

    @ApiModelProperty("来源：1退款，2结转")
    private int source;
}
