package com.wishare.finance.apps.pushbill.fo;

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
@ApiModel("推单规则启用信息")
public class EnableBillRuleF {

    @ApiModelProperty(value = "推单规则id", required = true)
    @NotNull(message = "推单规则id不能为空")
    private Long billRuleId;

    @ApiModelProperty(value = "是否启用：0启用，1禁用", required = true)
    @NotNull(message = "启用参数不能为空")
    private Integer disabled;

}
