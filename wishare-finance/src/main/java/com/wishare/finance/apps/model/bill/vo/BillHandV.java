package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author phq
 * @date 2022/10/9
 * @Description: 交账信息
 */
@Getter
@Setter
@ApiModel("交账所需信息")
public class BillHandV {

    @ApiModelProperty("账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty("是否挂账：0未挂账，1已挂账")
    private Integer onAccount;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    @ApiModelProperty("是否交账：0未交账，1部分交账，2已交账")
    private Integer accountHanded;

    @ApiModelProperty("实收金额（实收金额 = 应收金额金额 + 违约金金额 - 优惠总额）")
    private Long settleAmount;

    @ApiModelProperty("结转金额")
    private Long carriedAmount;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("主键id 账单id")
    private Long id;

}
