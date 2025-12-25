package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 统计凭证金额信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/15
 */
@Getter
@Setter
@ApiModel("统计凭证金额信息")
public class StaticVoucherAmountV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "推凭金额 单位：分")
    private Long amount;

    public StaticVoucherAmountV() {
    }

    public StaticVoucherAmountV(Long amount) {
        this.amount = amount;
    }
}
