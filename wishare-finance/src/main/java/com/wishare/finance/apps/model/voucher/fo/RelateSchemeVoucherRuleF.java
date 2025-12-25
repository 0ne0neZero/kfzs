package com.wishare.finance.apps.model.voucher.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 凭证模板删除信息
 *
 * @author dxclay
 * @since 2023-04-04
 */
@Getter
@Setter
@ApiModel("凭证核算方案关联信息")
public class RelateSchemeVoucherRuleF {

    @ApiModelProperty(value = "凭证规则id", required = true)
    @NotNull(message = "凭证规则id不能为空")
    private Long voucherRuleId;

    @ApiModelProperty(value = "关联凭证核算方案id列表", required = true)
    @NotNull(message = "凭证核算方案id不能为空")
    private List<Long> voucherSchemeIds;

}
