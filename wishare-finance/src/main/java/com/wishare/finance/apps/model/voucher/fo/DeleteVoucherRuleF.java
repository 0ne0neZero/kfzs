package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 凭证规则删除信息
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证规则删除信息")
public class DeleteVoucherRuleF {

    @ApiModelProperty(value = "凭证规则id", required = true)
    @NotNull(message = "凭证规则id不能为空")
    private Long voucherRuleId;

}
