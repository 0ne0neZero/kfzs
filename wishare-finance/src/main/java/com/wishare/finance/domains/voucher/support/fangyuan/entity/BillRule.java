package com.wishare.finance.domains.voucher.support.fangyuan.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.apps.model.bill.fo.VoucherBillGenerateForContractSettlementContext;
import com.wishare.finance.apps.pushbill.fo.ScopeF;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleExecuteStateEnum;
import com.wishare.finance.domains.voucher.entity.VoucherScheduleRuleOBV;
import com.wishare.finance.domains.voucher.support.ListPushBillCommunityHandler;
import com.wishare.finance.domains.voucher.support.ListPushBillTemplateConditionHandler;
import com.wishare.finance.domains.voucher.support.fangyuan.enums.PushModeEnum;
import com.wishare.finance.domains.voucher.support.fangyuan.strategy.core.*;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.*;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.support.Application;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import com.wishare.finance.infrastructure.support.schedule.ScheduleManager;
import com.wishare.finance.infrastructure.support.schedule.ScheduleTask;
import com.wishare.finance.infrastructure.support.schedule.ScheduleTaskHandler;
import com.wishare.finance.infrastructure.support.schedule.Scheduler;
import com.wishare.starter.utils.ErrorAssertUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 推单规则
 */
@Getter
@Setter
@TableName(value = TableNames.VOUCHER_BILL_RULE, autoResultMap = true)
public class BillRule extends BaseEntity {

    @ApiModelProperty(value = "推单规则id")
    private Long id;

    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认5预收结转")
    private Integer eventType;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;
    @ApiModelProperty(value = "试用范围")
    @TableField(typeHandler = ListPushBillCommunityHandler.class)
    private List<ScopeF> scopeApplication;
    @ApiModelProperty(value = "汇总要求")
    private String summaryRequirements;
    @ApiModelProperty(value = "1 可支付金额 2 实收金额")
    private Integer amountValue;

    @ApiModelProperty(value = "金额正负 1正 2负 3一正一负")
    private Integer amountPlusMinus;


    @ApiModelProperty(value = "过滤条件")
    @TableField(typeHandler = ListPushBillTemplateConditionHandler.class)
    private List<VoucherBillRuleConditionOBV> conditions;

    @ApiModelProperty(value = "定时规则信息")
    @TableField(typeHandler = JSONTypeHandler.class)
    private VoucherScheduleRuleOBV scheduleRule;

    @ApiModelProperty(value = "推凭模式： 1定时推送  2手动推凭")
    private Integer pushMode;
    @ApiModelProperty(value = "0  方圆  1 中交")
    private Integer ruleSource;

    @ApiModelProperty(value = "运行状态： 0空闲  1运行中")
    private Integer executeState;
    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;
    @ApiModelProperty(value = "是否删除：0否，1是")
    @TableLogic(value = "0", delval = "1")
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
    public BillRule() {
        setId(id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_BILL));
    }

    /**
     * 是否禁用
     * @return
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
            ErrorAssertUtil.notNullThrow403(scheduleRule, ErrorMessage.BILL_RULE_SCHEDULE_ERROR);
        }else {
            //如果不是定时推送，则清除定时任务配置
            scheduleRule = null;
        }
    }

    /**
     * 校验运行设置
     */
    public void checkRun() {
        ErrorAssertUtil.isFalseThrow402(DataDisabledEnum.禁用.equalsByCode(disabled), ErrorMessage.BILL_RULE_IS_DISABLED);
        checkExecuteState();
    }

    /**
     * 校验运行状态
     */
    public void checkExecuteState(){
        ErrorAssertUtil.isFalseThrow402(VoucherRuleExecuteStateEnum.运行中.equalsByCode(executeState), ErrorMessage.BILL_RULE_IS_RUNNING);
    }


    /**
     * 方圆手动运行规则
     */
    public void manualExecute() {
        //运行规则
        PushBillStrategyContext.getStrategy(eventType, PushMethod.手动推送).execute(new ManualBillStrategyCommand(id));
    }

    public void manualZJExecute() {
        //运行规则
        PushBillZJStrategyContext.getStrategy(eventType, ZJPushMethod.手动推送).execute(new ManualBillZJStrategyCommand(id));
        //【对下结算-计提】AbstractPushBillZJStrategy.doExecute
        //【收入确认V2-计提】AbstractPushBillZJStrategy.doExecute
    }

    public List<PushZJBusinessBillForSettlement>  autoTriggerZJExecute(VoucherBillGenerateForContractSettlementContext context) {
        //自动触发
        return PushBillZJStrategyContext.getStrategy(eventType, ZJPushMethod.自动推送).autoExecute(new AutoBillZJStrategyCommand(context));
    }

    /**
     * 即时触发运行规则
     */
    public void taskExecute() {
        //运行规则
        PushBillStrategyContext.getStrategy(eventType, PushMethod.定时推送).execute(new TaskPushBillStrategyCommand(id));
    }


    /**
     * 是否是定时推送
     * @return
     */
    public boolean validSchedule(){
        return PushModeEnum.定时推送.equalsByCode(pushMode);
    }

    /**
     * 更新定时任务
     * @param oldBillRule 更新前规则
     */
    public void updateSchedule(BillRule oldBillRule){
        //校验之前的规则的运行状态
        oldBillRule.checkExecuteState();
        boolean oldScheduleFlag = oldBillRule.validSchedule();
        boolean curScheduleFlag = validSchedule();
        //如果之前和现在都没注册定时任务，则结束
        if (!oldScheduleFlag && !curScheduleFlag) {
            return;
        }
        //如果之前注册了定时任务，但现在更新为不注册了，则删除定时任务
        if (oldScheduleFlag && !curScheduleFlag) {
            ScheduleManager.removeTask(oldBillRule.getScheduleRule().getScheduleTaskId());
            return;
        }
        //如果之前未配置定时任务，但现在更新为配置了，则注册定时任务
        if (!oldScheduleFlag && curScheduleFlag){
            registerSchedule();
            return;
        }
        //如果之前配置定时任务，但现在更新也配置，则校验任务规则是否变更，变更则更新定时任务
        if (oldBillRule.getScheduleRule().getRule().cron().equals(scheduleRule.getRule().cron())){
            updateSchedule();
        }
        //启停任务
        if (!oldBillRule.checkDisabled() && checkDisabled()){
            //如果启用变成禁用，则停止任务
            ScheduleManager.stopTask(oldBillRule.getScheduleRule().getScheduleTaskId());
        }else if (oldBillRule.checkDisabled() && !checkDisabled()){
            //如果禁用变成启用，则开启任务
            ScheduleManager.startTask(oldBillRule.getScheduleRule().getScheduleTaskId());
        }
    }


    /**
     * 注册定时任务
     * @return
     */
    public boolean registerSchedule(){
        if (!validSchedule()){
            return false;
        }
        Scheduler scheduler = new Scheduler().setService(Application.service).setName(Application.name).addTask(generateScheduleTask());
        scheduler = checkDisabled() ? ScheduleManager.register(scheduler, true) : ScheduleManager.registerAndStartTask(scheduler, true);
        scheduleRule.setSchedulerId(scheduler.getId());
        scheduleRule.setSchedulerName(scheduler.getName());
        ScheduleTask scheduleTask = scheduler.getTasks().get(0);
        scheduleRule.setScheduleTaskId(scheduleTask.getId());
        scheduleRule.setScheduleTaskName(scheduleTask.getName());
        return true;
    }

    /**
     * 更新定时任务
     * @return
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
     * @return
     */
    private ScheduleTask generateScheduleTask(){
        return new ScheduleTask()
                .setName("凭证规则-" + ruleName + "(" + ApiData.API.getTenantId().get() + ")")
                .setAuthor("finance-admin")
                .setId(scheduleRule.getScheduleTaskId())
                .setCron(scheduleRule.getRule().cron())
                .setHandler(new ScheduleTaskHandler()
                        .setCode("receiptRuleScheduler")
                        .setName("凭证规则调度器")
                        .setParams(JSON.toJSONString(Map.of("taskId", id, "tenantId", super.getTenantId()))));
    }

    /**
     * 启用或禁用
     * @param disabled
     */
    public void enableOrDisabled(int disabled){
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
        checkExecuteState();
        if (validSchedule()){
            ScheduleManager.removeTask(scheduleRule.getScheduleTaskId());
        }
    }

    @ApiModelProperty(value = "收入确认-实签-中交")
    private transient VoucherBillGenerateForContractSettlementContext voucherBillGenerateForContractSettlementContext;
}
