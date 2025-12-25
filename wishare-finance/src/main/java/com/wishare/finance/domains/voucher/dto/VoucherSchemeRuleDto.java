package com.wishare.finance.domains.voucher.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 凭证核算方案凭证规则数据项
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/4
 */
@Getter
@Setter
@ApiModel("凭证核算方案凭证规则数据项")
public class VoucherSchemeRuleDto {

    @ApiModelProperty(value = "核算方案id")
    private Long voucherSchemeId;

    @ApiModelProperty(value = "核算方案名称")
    private String voucherSchemeName;

    @ApiModelProperty(value = "凭证规则id")
    private Long voucherRuleId;

}
