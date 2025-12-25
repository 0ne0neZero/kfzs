package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.apps.model.bill.fo.PayeeF;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 资金下拨明细
 */
@Getter
@Setter
public class AllocateDetails extends PayeeF {

    @ApiModelProperty(value = "资金下拨明细唯一标识, 子订单唯一标识（必传）", required = true)
    @NotBlank(message = "资金下拨明细唯一标识不能为空")
    private String allocateBusinessId;

    @ApiModelProperty(value = "下拨金额", required = true)
    @NotNull(message = "下拨金额不能为空")
    private Long allocateAmount;

    @ApiModelProperty(value = "下拨金额(不含税)", required = true)
    @NotNull(message = "下拨金额(不含税)不能为空")
    private Long allocateNoRateAmount;
}
