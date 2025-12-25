package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("报账汇总规则提醒详情")
public class RemindRuleDetailF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提醒日")
    private Integer remindDay;

    @ApiModelProperty(value = "消息目标类型")
    private Integer targetType;

    @ApiModelProperty(value = "目标信息列表")
    private List<TargetInfoF> targetInfos;

}
