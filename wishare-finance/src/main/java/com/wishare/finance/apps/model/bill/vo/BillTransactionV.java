package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单统一付款响应
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/20
 */
@Getter
@Setter
@ApiModel("账单统一付款响应")
public class BillTransactionV {

    @ApiModelProperty(value = "交易订单号")
    private String transactionNo;

    @ApiModelProperty(value = "业务系统交易单号", required = true)
    private String bizTransactionNo;

    @ApiModelProperty(value = "交易状态: 0待交易, 1交易中, 2交易成功, 3交易失败, 4交易已取消, 5交易已关闭", required = true)
    private Integer transactState;

    @ApiModelProperty(value = "推凭状态：0未推凭，1推凭中，2已推凭，3推凭失败", required = true)
    private Integer voucherState;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票", required = true)
    private Integer invoiceState;

    @ApiModelProperty(value = "错误代码")
    private String errCode;

    @ApiModelProperty(value = "错误消息")
    private String errMsg;

    @ApiModelProperty(value = "交易数据")
    private Object data;

}
