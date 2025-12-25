package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author fxl
 * @describe
 * @date 2024/4/1
 */
@Getter
@Setter
public class BillSettleChannelInfo {

    @ApiModelProperty("收款方式 CASH:现金，CHEQUE:支票，SWIPE:银行POS刷卡，BANK:银行汇款，OTHER: 其他")
    @NotBlank(message = "收款方式不能为空")
    private String settleChannel;

    @ApiModelProperty(value = "支付金额")
    @NotNull(message = "支付金额")
    private Long payAmount;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer settleWay;

}
