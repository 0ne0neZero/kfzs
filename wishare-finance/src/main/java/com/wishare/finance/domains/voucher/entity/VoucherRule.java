package com.wishare.finance.domains.voucher.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleExecuteStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRulePushModeEnum;
import com.wishare.finance.domains.voucher.strategy.VoucherStrategyContext;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.PushMode;
import com.wishare.finance.domains.voucher.strategy.core.TaskVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.support.ListVoucherTemplateConditionHandler;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.support.Application;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import com.wishare.finance.infrastructure.support.schedule.ScheduleManager;
import com.wishare.finance.infrastructure.support.schedule.ScheduleTask;
import com.wishare.finance.infrastructure.support.schedule.ScheduleTaskHandler;
import com.wishare.finance.infrastructure.support.schedule.Scheduler;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 凭证规则
 * @author dxclay
 * @since  2023/3/28
 * @version 1.0
 */
@Getter
@Setter
@TableName(value = TableNames.VOUCHER_RULE, autoResultMap = true)
@Slf4j
public class VoucherRule {

    @ApiModelProperty(value = "凭证规则id")
    private Long id;

    @ApiModelProperty(value = "触发事件类型：1应收计提，2收款结算，3预收应收核销，4账单调整，5账单开票，6冲销作废，7未认领暂收款，8应付计提，9付款结算，10收票结算，11手动生成")
    private Integer eventType;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "指定账簿编码")
    @TableField(typeHandler = JSONTypeHandler.class)
    private VoucherBook book;

    @ApiModelProperty(value = "推凭账簿模式：1指定账簿，2映射账簿")
    private Integer bookMode;

    @ApiModelProperty(value = "凭证模板id")
    private Long voucherTemplateId;

    @ApiModelProperty(value = "过滤条件")
    @TableField(typeHandler = ListVoucherTemplateConditionHandler.class)
    private List<VoucherRuleConditionOBV> conditions;

    @ApiModelProperty(value = "定时规则信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private VoucherScheduleRuleOBV scheduleRule;

    @ApiModelProperty(value = "推凭模式： 1定时推送  2即时推送， 3手动推凭")
    private Integer pushMode;

    @ApiModelProperty(value = "运行状态： 0空闲  1运行中")
    private Integer executeState;
    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "是否删除：0否，1是")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "租户id")
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @ApiModelProperty(value = "创建人名称")
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否禁用
     */
    public boolean checkDisabled(){
        return DataDisabledEnum.禁用.equalsByCode(disabled);
    }

    /**
     * 校验运行设置
     */
    public void checkParam() {
        VoucherEventTypeEnum.valueOfByCode(eventType);
        if (validSchedule()){
            ErrorAssertUtil.notNullThrow403(scheduleRule, ErrorMessage.VOUCHER_RULE_SCHEDULE_ERROR);
        }else {
            //如果不是定时推送，则清除定时任务配置
            scheduleRule = null;
        }
        // 规则必须选择，单据来源
        if (CollUtil.isEmpty(conditions)){
            throw BizException.throw402("请添加过滤条件[单据来源]");
        }

        Set<Integer> set = conditions.stream().map(VoucherRuleConditionOBV::getType).collect(Collectors.toSet());

        if (!set.contains(VoucherRuleConditionTypeEnum.单据来源.getCode())) {
            throw BizException.throw402("请添加过滤条件[单据来源]");
        }
        conditions.forEach(v->{
            if (v.getType().equals(VoucherRuleConditionTypeEnum.单据来源.getCode())){
                if (v.getMethod()!=1){
                    throw BizException.throw402("单据来源请选择包含");
                }
                if (CollUtil.isEmpty(v.getValues()) || v.getValues().size()>1) {
                    throw BizException.throw402("单据来源只能选择一个");
                }
            }
        });

    }

    /**
     * 校验运行设置
     */
    public void checkRun() {
        ErrorAssertUtil.isFalseThrow402(DataDisabledEnum.禁用.equalsByCode(disabled), ErrorMessage.VOUCHER_RULE_IS_DISABLED);
        checkExecuteState();
    }

    /**
     * 校验运行状态
     */
    public void checkExecuteState(){
        ErrorAssertUtil.isFalseThrow402(VoucherRuleExecuteStateEnum.运行中.equalsByCode(executeState), ErrorMessage.VOUCHER_RULE_IS_RUNNING);
    }


    /**
     * 手动运行规则
     */
    public VoucherRuleRecord manualExecute() {
        //运行规则
        VoucherRuleRecord record = VoucherStrategyContext.getStrategy(eventType, PushMode.手动).execute(new ManualVoucherStrategyCommand(id));
        return record;
    }

    /**
     * 即时触发运行规则
     */
    public void taskExecute() {
        //运行规则
        VoucherStrategyContext.getStrategy(eventType, PushMode.定时).execute(new TaskVoucherStrategyCommand(id));
    }


    /**
     * 是否是定时推送
     */
    public boolean validSchedule(){
        return VoucherRulePushModeEnum.定时推送.equalsByCode(pushMode);
    }

    /**
     * 更新定时任务
     * @param oldVoucherRule 更新前规则
     */
    public void updateSchedule(VoucherRule oldVoucherRule){
        //校验之前的规则的运行状态
        oldVoucherRule.checkExecuteState();
        boolean oldScheduleFlag = oldVoucherRule.validSchedule();
        boolean curScheduleFlag = validSchedule();
        //之前不是定时任务，现在不是，不执行
        if (!oldScheduleFlag && !curScheduleFlag) {
            return;
        }
        //之前是定时任务，现在不是，则删除定时任务
        if (oldScheduleFlag && !curScheduleFlag) {
            ScheduleManager.removeTask(oldVoucherRule.getScheduleRule().getScheduleTaskId());
            return;
        }
        //之前不是定时任务，现在是，则注册定时任务
        if (!oldScheduleFlag && curScheduleFlag){
            registerSchedule();
            return;
        }
        //如果之前配置定时任务，但现在更新也配置，则校验任务规则是否变更，变更则更新定时任务
        log.info("updateSchedule-老corn-{},新corn-{}",oldVoucherRule.getScheduleRule().getRule().cron(),
                scheduleRule.getRule().cron());
        if (!oldVoucherRule.getScheduleRule().getRule().cron().equals(scheduleRule.getRule().cron())){
            updateSchedule();
        }
        //启停任务
        if (checkDisabled()){
            //禁用，则停止任务
            ScheduleManager.stopTask(oldVoucherRule.getScheduleRule().getScheduleTaskId());
        }else {
            //启用，则开启任务
            ScheduleManager.startTask(oldVoucherRule.getScheduleRule().getScheduleTaskId());
        }
    }


    /**
     * 注册执行器
     */
    public boolean registerSchedule(){
        if (!validSchedule()){
            return false;
        }
        Scheduler scheduler = new Scheduler().setService(Application.service).setName(Application.name).addTask(generateScheduleTask());
        if (checkDisabled()) {
            ScheduleManager.register(scheduler, true);
        } else {
            ScheduleManager.registerAndStartTask(scheduler, true);
        }
        scheduleRule.setSchedulerId(scheduler.getId());
        scheduleRule.setSchedulerName(StrUtil.trim(scheduler.getName()));

        ScheduleTask scheduleTask = scheduler.getTasks().get(0);
        scheduleRule.setScheduleTaskId(scheduleTask.getId());
        scheduleRule.setScheduleTaskName(scheduleTask.getName());
        return true;
    }

    /**
     * 更新定时任务
     */
    public boolean updateSchedule(){
        if (!validSchedule()){
            return false;
        }
        ScheduleManager.updateJob(scheduleRule.getSchedulerId(), generateScheduleTask());
        return true;
    }

    /**
     * 生成调度任务
     */
    private ScheduleTask generateScheduleTask(){
        return new ScheduleTask()
                .setName("凭证规则-" + ruleName + "(" + ApiData.API.getTenantId().get() + ")")
                .setAuthor("finance-admin")
                .setId(scheduleRule.getScheduleTaskId())
                .setCron(scheduleRule.getRule().cron())
                .setHandler(new ScheduleTaskHandler()
                        .setCode("voucherRuleScheduler")
                        .setName("凭证规则调度器")
                        .setParams(JSON.toJSONString(Map.of("taskId", id, "tenantId", tenantId))));
    }

    /**
     * 启用或禁用
     */
    public void enableOrDisabled(int disabled){
        //checkRun();
        checkExecuteState();
        if (this.disabled == disabled){
            return;
        }
        this.disabled = disabled;
        if (DataDisabledEnum.启用.equalsByCode(this.disabled)){
            enable();
        }else {
            disable();
        }

    }

    /**
     * 启用
     */
    public void enable() {
        //checkRun();
        checkExecuteState();
        if (validSchedule()){
            ScheduleManager.startTask(scheduleRule.getScheduleTaskId());
        }
    }

    /**
     * 禁用
     */
    public void disable() {
        //checkRun();
        checkExecuteState();
        if (validSchedule()){
            ScheduleManager.stopTask(scheduleRule.getScheduleTaskId());
        }
    }

    /**
     * 禁用服务
     */
    public void delete() {
        checkRun();
        if (validSchedule()){
            ScheduleManager.removeTask(scheduleRule.getScheduleTaskId());
        }
    }
}
