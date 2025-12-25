package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 凭证模板删除信息
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证核算方案删除信息")
public class DeleteVoucherSchemeF {

    @ApiModelProperty(value = "凭证核算方案id", required = true)
    @NotNull(message = "凭证核算方案id不能为空")
    private Long voucherSchemeId;

}
