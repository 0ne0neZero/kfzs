package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("对账规则树信息")
public class ReconcileRuleBaseV {

    @ApiModelProperty(value = "对行规则id")
    private Long id;

    @ApiModelProperty(value = "对账规则名称")
    private String ruleName;

    @ApiModelProperty(value = "是否禁用：0启用，1禁用")
    private Integer disabled;

}
