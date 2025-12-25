package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 推单规则运行信息
 *
 */
@Getter
@Setter
@ApiModel("凭证规则推单运行信息")
public class RunBillRuleF {

    @ApiModelProperty(value = "推单规则id", required = true)
    @NotNull(message = "推单规则id不能为空")
    private Long billRuleId;

}
