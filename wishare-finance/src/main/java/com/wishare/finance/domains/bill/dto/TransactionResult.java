package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 交易结果
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
@ApiModel("交易结果信息")
public class TransactionResult {

    @ApiModelProperty(value = "交易订单号")
    private String transactionNo;

    @ApiModelProperty(value = "业务系统交易单号")
    private String bizTransactionNo;

    @ApiModelProperty(value = "交易状态: 0待交易, 1交易中, 2交易成功, 3交易失败, 4交易已取消, 5交易已关闭")
    private Integer transactState;

    @ApiModelProperty(value = "错误代码")
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    private String errMsg;

}
