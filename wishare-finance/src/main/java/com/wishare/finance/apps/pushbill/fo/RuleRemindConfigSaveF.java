package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 报账汇总规则F
 */
@Data
@ApiModel("报账汇总规则提醒F")
public class RuleRemindConfigSaveF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规则id")
    private Long ruleId;

    @ApiModelProperty("规则明细")
    List<RemindRuleDetailF> ruleDetails;

}
