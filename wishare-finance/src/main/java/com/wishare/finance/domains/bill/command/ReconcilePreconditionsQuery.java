package com.wishare.finance.domains.bill.command;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 对账前置条件
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/19
 */
@Getter
@Setter
@ApiModel("对账前置条件")
public class ReconcilePreconditionsQuery {

    @ApiModelProperty(value = "系统编码列表", required = true)
    private List<Integer> sysSource;

    @ApiModelProperty(value = "是否交账：0需交账，1无需交账")
    private Integer handed;

    @ApiModelProperty(value = "支付方式")
    private List<Integer> payWay;
}
