package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.reconciliation.enums.ReconcileExecuteStateEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileTypeEnum;
import com.wishare.finance.domains.reconciliation.support.ReconPreconditionsOBVJSONListTypeHandler;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.support.JSONTypeHandler;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 对账规则表
 *
 * @author dxclay
 * @since 2022-10-12
 */
@Getter
@Setter
@Slf4j
@TableName(value = "reconcile_rule", autoResultMap = true)
public class ReconcileRuleE{

    @TableId
    private Long id;

    @ApiModelProperty(value = "对账维度规则")
    @TableField(typeHandler= JacksonTypeHandler.class)
    private ReconcileDimensionRuleOBV dimensionRule;

    @ApiModelProperty(value = "定时规则")
    @TableField(typeHandler= JSONTypeHandler.class)
    private ReconcileScheduleRuleOBV scheduleRule;

    @ApiModelProperty(value = "对账前置条件")
    @TableField(typeHandler= ReconPreconditionsOBVJSONListTypeHandler.class)
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
    private Integer executeState = 0;

    @ApiModelProperty(value = "对账前置条件-收款方式")
    private String payWayConditions;

    @ApiModelProperty(value = "对账前置条件-收款渠道")
    private String payChannelConditions;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;


    public ReconcileRuleE() {
    }

    public ReconcileRuleE(ReconcileModeEnum reconcileMode) {
        this.dimensionRule = new ReconcileDimensionRuleOBV();
        this.scheduleRule = new ReconcileScheduleRuleOBV();
        this.preconditions = new ArrayList<>();
        this.reconcileType = ReconcileTypeEnum.手动对账.getCode();
        this.reconcileMode = reconcileMode.getCode();
        this.ruleName = reconcileMode.getValue();
    }

    /**
     * 检查维度规则
     */
    public void checkDimension(){
        ErrorAssertUtil.isTrueThrow402(Objects.nonNull(dimensionRule) && dimensionRule.notEmpty(), ErrorMessage.RECONCILE_RULE_NOT_DIMENSION);
    }

    /**
     * 执行规则
     * @return
     */
    public boolean execute(){
        executeState = ReconcileExecuteStateEnum.运行中.getCode();
        return true;
    }

    /**
     * 执行完成
     * @return
     */
    public boolean finish(){
        executeState = ReconcileExecuteStateEnum.待运行.getCode();
        return true;
    }

    /**
     * 是否启用
     */
    public boolean enabled(){
       return disabled == 0;
    }

    /**
     * 校验是否可以对账
     * @param type 类型  0立即对账， 1定时任务对账
     * @return
     */
    public boolean checkExecute(int type){
        ErrorAssertUtil.isTrueThrow402(execute(), ErrorMessage.RECONCILE_DISABLED);
        ErrorAssertUtil.isTrueThrow402(ReconcileExecuteStateEnum.运行中.equalsByCode(executeState), ErrorMessage.RECONCILE_RUNNING);
        boolean checkRun = true;
        if (type == 1 && ReconcileTypeEnum.自动对账.equalsByCode(reconcileType)){
            checkRun = scheduleRule.checkRun();
            if (!checkRun){
                log.info("自动对账规则为月底前" + scheduleRule.getSpecifiedTime() + ", 执行时间未到！");
            }
        }
        return checkRun;
    }

    /**
     * 查询建造者
     * @return
     */
    public DimensionSearchBuilder searchBuilder(){
        return new DimensionSearchBuilder(this.dimensionRule);
    }

    public static class DimensionSearchBuilder {

        private Long statutoryBodyId;
        private String communityId;
        private String costCenterId;
        private String sbAccountId;
        private Integer payWay;
        private String payChannel;
        private ReconcileDimensionRuleOBV dimensionRule;

        private List<ReconcilePreconditionsOBV> preconditions;

        public DimensionSearchBuilder(ReconcileDimensionRuleOBV dimensionRule) {
            this.dimensionRule = dimensionRule;
        }

        public DimensionSearchBuilder statutoryBodyId(Long statutoryBodyId) {
            this.statutoryBodyId = statutoryBodyId;
            return this;
        }

        public DimensionSearchBuilder communityId(String communityId) {
            this.communityId = communityId;
            return this;
        }

        public DimensionSearchBuilder costCenterId(String costCenterId) {
            this.costCenterId = costCenterId;
            return this;
        }

        public DimensionSearchBuilder sbAccountId(String sbAccountId) {
            this.sbAccountId = sbAccountId;
            return this;
        }

        public DimensionSearchBuilder payWay(Integer payWay) {
            this.payWay = payWay;
            return this;
        }

        public DimensionSearchBuilder payChannel(String payChannel) {
            this.payChannel = payChannel;
            return this;
        }

        public SearchF<?> build(){
            SearchF<?> searchF = new SearchF<>();
            List<Field> fields = new ArrayList<>();
            if (Objects.nonNull(dimensionRule.getStatutoryBody()) && dimensionRule.getStatutoryBody().isGroup()){
                fields.add(new Field("statutory_body_id", statutoryBodyId, 1));
            }
            if (Objects.nonNull(dimensionRule.getCommunity()) && dimensionRule.getCommunity().isGroup()){
                fields.add(new Field("community_id", communityId, 1));
            }
            if (Objects.nonNull(dimensionRule.getCostCenter()) && dimensionRule.getCostCenter().isGroup()) {
                fields.add(new Field("cost_center_id", costCenterId, 1));
            }
            if (Objects.nonNull(dimensionRule.getStatutoryBodyAccount()) && dimensionRule.getStatutoryBodyAccount().isGroup()) {
                fields.add(new Field("sb_account_id", sbAccountId, 1));
            }
            if (Objects.nonNull(dimensionRule.getPayChannel()) && dimensionRule.getPayChannel().isGroup()) {
                if (Objects.nonNull(payWay)){
                    fields.add(new Field("pay_way", payWay, 1));
                }
                fields.add(new Field("pay_channel", payChannel, 1));
            }
            fields.add(new Field("reconcile_state", 3, 2));
            //前置条件
            //if (CollectionUtils.isNotEmpty(preconditions)){
            //    for (ReconcilePreconditionsOBV precondition : preconditions) {
            //        fields.add(new Field("sys_source", precondition.getSysSource(), 15));
            //        if (Objects.nonNull(precondition.getHanded()) && precondition.getHanded() == 0){
            //            //0未交账，1部分交账，2已交账
            //            fields.add(new Field("account_handed", List.of(1,2), 15));
            //        }else {
            //            fields.add(new Field("account_handed", 0, 1));
            //        }
            //    }
            //}
            searchF.setFields(fields);
            return searchF;
        }

    }

}
