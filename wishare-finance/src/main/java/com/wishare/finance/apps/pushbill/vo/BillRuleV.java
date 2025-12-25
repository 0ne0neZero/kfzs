package com.wishare.finance.apps.pushbill.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherScheduleRuleOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel(value="推单规则明细")
public class BillRuleV {
    @ApiModelProperty(value = "推单规则id")
    private Long id;
    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认5预收结转")
    private Integer eventType;
    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    @ApiModelProperty(value = "试用范围")
    private List<ScopeF> scopeApplication;
    @ApiModelProperty(value = "汇总要求")
    private String summaryRequirements;
    @ApiModelProperty(value = "1 可支付金额 2 实收金额")
    private Integer amountValue;

    @ApiModelProperty(value = "金额正负 1正 2负 3一正一负")
    private Integer amountPlusMinus;


    @ApiModelProperty(value = "过滤条件")
    private List<VoucherRuleConditionOBV> conditions;

    @ApiModelProperty(value = "定时规则信息")
    private VoucherScheduleRuleOBV scheduleRule;

    @ApiModelProperty(value = "推凭模式： 1定时推送  2手动推凭")
    private Integer pushMode;

    @ApiModelProperty(value = "运行状态： 0空闲  1运行中")
    private Integer executeState;
    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;
}
