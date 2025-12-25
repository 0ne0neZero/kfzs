package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 交账规则
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/13
 */
@Getter
@Setter
public class BillHandAccountRuleV {

    @ApiModelProperty(value = "交账规则id", required = true)
    private Long id;

    @ApiModelProperty(value = "是否启用自动交账 0: 未启用， 1：启用")
    private Integer autoHand;

}
