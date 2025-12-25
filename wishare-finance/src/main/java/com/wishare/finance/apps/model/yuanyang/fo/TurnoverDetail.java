package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.apps.model.bill.fo.PayerF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 资金上缴明细
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/23
 */
@Getter
@Setter
@ApiModel(value = "资金上缴明细", parent = PayerF.class)
public class TurnoverDetail extends PayerF {

    @ApiModelProperty(value = "资金上缴明细唯一标识, 子订单唯一标识（必传）", required = true)
    @NotBlank(message = "资金上缴明细唯一标识不能为空")
    private String turnoverBusinessId;

    @ApiModelProperty(value = "上缴账簿编码", required = true)
    @NotBlank(message = "上缴账簿编码不能为空")
    private String accountBookCode;

    @ApiModelProperty(value = "上缴金额", required = true)
    @NotNull(message = "上缴金额不能为空")
    private Long turnoverAmount;

}
