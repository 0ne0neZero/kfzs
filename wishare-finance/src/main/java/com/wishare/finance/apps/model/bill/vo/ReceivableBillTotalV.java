package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 应收账单合计信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("应收账单合计信息")
public class ReceivableBillTotalV {

    @ApiModelProperty(value = "房号总数", required = true)
    private Long rooms;

    @ApiModelProperty(value = "账单总数", required = true)
    private Long bills;

    @ApiModelProperty(value = "账单总金额", required = true)
    private Long amount;

    @ApiModelProperty(value = "实收总金额", required = true)
    private Long settleAmount;

}
