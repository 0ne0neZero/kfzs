package com.wishare.finance.domains.voucher.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 凭证核算方案凭证规则信息
 *
 * @author dxclay
 * @since 2023-04-03
 */
@Getter
@Setter
@ApiModel(value="凭证核算方案凭证规则信息")
public class VoucherSchemeRuleOBV {

    @ApiModelProperty(value = "核算方案id")
    private Long voucherSchemeId;

    @ApiModelProperty(value = "凭证规则id")
    @NotNull(message = "凭证规则id不能为空")
    private Long voucherRuleId;

    @ApiModelProperty(value = "方案名称")
    private String ruleName;

}
