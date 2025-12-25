package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.domains.reconciliation.enums.ReconcileModeEnum;
import com.wishare.finance.domains.reconciliation.enums.ReconcileResultEnum;
import com.wishare.tools.starter.fo.search.Field;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 对账记录表
 *
 * @author dxclay
 * @since 2022-10-12
 */
@Getter
@Setter
@TableName("reconciliation")
public class ReconciliationE {

    @TableId
    private Long id;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty("成本中心id")
    private String costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "收款账号名称")
    private String sbAccountName;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "对账规则id")
    private Long reconcileRuleId;

    /**
     * {@linkplain ReconcileResultEnum}
     */
    @ApiModelProperty(value = "对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer result;

    @ApiModelProperty(value = "对账状态：0待运行，1运行中，2已完成")
    private Integer state;

    @ApiModelProperty(value = "实收总金额")
    private Long actualTotal = 0L;

    @ApiModelProperty(value = "开票总金额")
    private Long invoiceTotal = 0L;

    @ApiModelProperty(value = "流水认领总金额")
    private Long flowClaimTotal = 0L;

    @ApiModelProperty(value = "账单总数")
    private Long billCount = 0L;

    @ApiModelProperty(value = "平账总账单数")
    private Long balanceCount = 0L;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime executeStartTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime executeEndTime;

    @ApiModelProperty(value = "对账日期")
    private LocalDateTime reconcileTime;

    /**
     * {@linkplain ReconcileModeEnum}
     */
    @ApiModelProperty("对账模式: 0账票流水对账，1商户清分对账")
    private Integer reconcileMode;
    @ApiModelProperty("报账单id")
    private String voucherBillId;


    @ApiModelProperty("报账单编号")
    private String voucherBillNo;
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


    @ApiModelProperty(value = "手续费总金额")
    private Long commission = 0L;

    /**
     * 对账单建造者
     * @param reconcileRule
     * @return
     */
    public static ReconciliationBuilder builder(ReconcileRuleE reconcileRule){
        return new ReconciliationBuilder(reconcileRule);
    }

    public static class ReconciliationBuilder {

        private Long statutoryBodyId;
        private String statutoryBodyName;
        private String communityId;
        private String communityName;
        private String costCenterId;
        private String costCenterName;
        private Long sbAccountId;
        private String sbAccountName;
        private Long reconcileRuleId;
        private Integer result;
        private Integer state;
        private Long actualTotal = 0L;
        private Long invoiceTotal = 0L;
        private Long flowClaimTotal = 0L;
        private Long billCount = 0L;
        private Long balanceCount = 0L;
        private LocalDateTime executeStartTime;
        private LocalDateTime executeEndTime;
        private LocalDateTime reconcileTime;
        private Integer reconcileMode;
        private String payChannel;
        private Integer payWay;


        private ReconcileRuleE reconcileRule;


        public ReconciliationBuilder(ReconcileRuleE reconcileRule) {
            this.reconcileRule = reconcileRule;
        }

        public ReconciliationBuilder statutoryBodyId(Long statutoryBodyId) {
            this.statutoryBodyId = statutoryBodyId;
            return this;
        }

        public ReconciliationBuilder statutoryBodyName(String statutoryBodyName) {
            this.statutoryBodyName = statutoryBodyName;
            return this;
        }

        public ReconciliationBuilder communityId(String communityId) {
            this.communityId = communityId;
            return this;
        }

        public ReconciliationBuilder communityName(String communityName) {
            this.communityName = communityName;
            return this;
        }

        public ReconciliationBuilder costCenterId(String costCenterId) {
            this.costCenterId = costCenterId;
            return this;
        }

        public ReconciliationBuilder costCenterName(String costCenterName) {
            this.costCenterName = costCenterName;
            return this;
        }

        public ReconciliationBuilder sbAccountId(Long sbAccountId) {
            this.sbAccountId = sbAccountId;
            return this;
        }

        public ReconciliationBuilder sbAccountName(String sbAccountName) {
            this.sbAccountName = sbAccountName;
            return this;
        }

        public ReconciliationBuilder reconcileRuleId(Long reconcileRuleId) {
            this.reconcileRuleId = reconcileRuleId;
            return this;
        }

        public ReconciliationBuilder result(Integer result) {
            this.result = result;
            return this;
        }

        public ReconciliationBuilder state(Integer state) {
            this.state = state;
            return this;
        }

        public ReconciliationBuilder actualTotal(Long actualTotal) {
            this.actualTotal = actualTotal;
            return this;
        }

        public ReconciliationBuilder invoiceTotal(Long invoiceTotal) {
            this.invoiceTotal = invoiceTotal;
            return this;
        }

        public ReconciliationBuilder flowClaimTotal(Long flowClaimTotal) {
            this.flowClaimTotal = flowClaimTotal;
            return this;
        }

        public ReconciliationBuilder billCount(Long billCount) {
            this.billCount = billCount;
            return this;
        }

        public ReconciliationBuilder balanceCount(Long balanceCount) {
            this.balanceCount = balanceCount;
            return this;
        }

        public ReconciliationBuilder executeStartTime(LocalDateTime executeStartTime) {
            this.executeStartTime = executeStartTime;
            return this;
        }

        public ReconciliationBuilder executeEndTime(LocalDateTime executeEndTime) {
            this.executeEndTime = executeEndTime;
            return this;
        }

        public ReconciliationBuilder reconcileTime(LocalDateTime reconcileTime) {
            this.reconcileTime = reconcileTime;
            return this;
        }

        public ReconciliationBuilder reconcileMode(Integer reconcileMode) {
            this.reconcileMode = reconcileMode;
            return this;
        }


        public ReconciliationE build(){

            ReconciliationE reconciliationE = new ReconciliationE();
            ReconcileDimensionRuleOBV dimensionRule = reconcileRule.getDimensionRule();
            if (Objects.nonNull(dimensionRule.getStatutoryBody()) && dimensionRule.getStatutoryBody().isGroup()){
                reconciliationE.setStatutoryBodyId(this.statutoryBodyId);
                reconciliationE.setStatutoryBodyName(this.statutoryBodyName);
            }
            if (Objects.nonNull(dimensionRule.getCostCenter()) && dimensionRule.getCostCenter().isGroup()) {
                reconciliationE.setCostCenterId(this.costCenterId);
                reconciliationE.setCostCenterName(this.costCenterName);
            }
            if (Objects.nonNull(dimensionRule.getStatutoryBodyAccount()) && dimensionRule.getStatutoryBodyAccount().isGroup()) {
                reconciliationE.setSbAccountId(this.sbAccountId);
            }
            if (Objects.nonNull(dimensionRule.getPayChannel()) && dimensionRule.getPayChannel().isGroup()) {
                reconciliationE.setPayChannel(this.payChannel);
                reconciliationE.setPayWay(this.payWay);
            }

            reconciliationE.setCommunityId(this.communityId);
            reconciliationE.setCommunityName(this.communityName);
            reconciliationE.setReconcileRuleId(this.reconcileRuleId);
            reconciliationE.setResult(this.result);
            reconciliationE.setState(this.state);
            reconciliationE.setActualTotal(this.actualTotal);
            reconciliationE.setInvoiceTotal(this.invoiceTotal);
            reconciliationE.setFlowClaimTotal(this.flowClaimTotal);
            reconciliationE.setBillCount(this.billCount);
            reconciliationE.setBalanceCount(this.balanceCount);
            reconciliationE.setExecuteStartTime(this.executeStartTime);
            reconciliationE.setExecuteEndTime(this.executeEndTime);
            reconciliationE.setReconcileTime(this.reconcileTime);
            reconciliationE.setReconcileMode(this.reconcileMode);

            return reconciliationE;
        }

    }

}
