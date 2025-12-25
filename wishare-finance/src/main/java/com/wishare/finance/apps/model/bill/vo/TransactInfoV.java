package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/9/6
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("BPM凭证流程统一响应交易订单信息")
public class TransactInfoV {

    @ApiModelProperty(value = "交易订单号")
    private String billNo;

    @ApiModelProperty(value = "支付id")
    private String payId;

    @ApiModelProperty(value = "交易状态: 0待交易, 1交易中, 2交易成功, 3交易失败, 4交易已取消, 5交易已关闭", required = true)
    private Integer transactState;

    @ApiModelProperty(value = "交易信息")
    private String msg;

}
