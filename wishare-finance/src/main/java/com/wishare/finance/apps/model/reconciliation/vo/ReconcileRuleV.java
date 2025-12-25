package com.wishare.finance.apps.model.reconciliation.vo;

import com.wishare.finance.domains.reconciliation.entity.ReconcileDimensionRuleOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconcilePreconditionsOBV;
import com.wishare.finance.domains.reconciliation.entity.ReconcileScheduleRuleOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("对账规则信息")
public class ReconcileRuleV {

    @ApiModelProperty(value = "对行规则id")
    private Long id;

    @ApiModelProperty(value = "对账维度规则")
    private ReconcileDimensionRuleOBV dimensionRule;

    @ApiModelProperty(value = "定时规则")
    private ReconcileScheduleRuleOBV scheduleRule;

    @ApiModelProperty(value = "对账前置条件")
    private List<ReconcilePreconditionsOBV> preconditions;

    @ApiModelProperty(value = "对账类型（0自动对账 1手动对账）")
    private Integer reconcileType;

    @ApiModelProperty(value = "对账模式: 0账票流水对账，1商户清分对账")
    private Integer reconcileMode;

    @ApiModelProperty(value = "对账规则名称")
    private String ruleName;

    @ApiModelProperty(value = "对账规则描述")
    private String description;

    @ApiModelProperty(value = "定时任务标识")
    private String scheduleId;

    @ApiModelProperty(value = "是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "对账执行状态：0待运行，1运行中")
    private Integer executeState;

    @ApiModelProperty(value = "对账前置条件-收款方式")
    private String payWayConditions;


    @ApiModelProperty(value = "对账前置条件-收款渠道")
    private String payChannelConditions;

}
