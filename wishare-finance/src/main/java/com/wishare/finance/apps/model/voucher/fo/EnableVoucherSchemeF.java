package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 凭证规则启用信息
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证核算方案启用信息")
public class EnableVoucherSchemeF {

    @ApiModelProperty(value = "凭证核算方案id", required = true)
    @NotNull(message = "凭证核算方案id不能为空")
    private Long voucherSchemeId;

    @ApiModelProperty(value = "是否启用：0启用，1禁用", required = true)
    @NotNull(message = "启用参数不能为空")
    private Integer disabled;

}
