package com.wishare.finance.apps.pushbill.fo;


import com.wishare.finance.apps.pushbill.validation.AddGroup;
import com.wishare.finance.apps.pushbill.validation.UpdateGroup;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherScheduleRuleOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("推单规则新增信息")
public class BillRuleF {
    @ApiModelProperty(value = "推单规则id")
    @NotNull(groups = {UpdateGroup.class},message = "凭证规则id不能为空")
    private Long id;
    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认6预收结转5收款结转")
    @NotNull(groups = {AddGroup.class},message = "触发事件类型不能为空")
    private Integer eventType;
    @ApiModelProperty(value = "0  方圆  1 中交")
    private Integer ruleSource ;
    @ApiModelProperty(value = "规则名称")
    @Length(groups = {AddGroup.class,UpdateGroup.class},min = 1 ,max = 30,message = "规则名称范围支持1-30个字符")
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
    @NotNull(groups = {AddGroup.class},message = "推凭模式不能为空")
    private Integer pushMode;

    @ApiModelProperty(value = "运行状态： 0空闲  1运行中")
    private Integer executeState;
    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    @NotNull(groups = {AddGroup.class},message = "是否启用不能为空")
    private Integer disabled;

    @ApiModelProperty("规则明细")
    List<RemindRuleDetailF> ruleDetails;
}
