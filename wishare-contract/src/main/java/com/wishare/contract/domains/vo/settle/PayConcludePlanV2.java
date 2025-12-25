package com.wishare.contract.domains.vo.settle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * @author longhuadmin
 * 用于预生成、新增、编辑，统一格式
 */
@Data
public class PayConcludePlanV2 {


    @ApiModelProperty(value = "计划id")
    private String id;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "费项成本预估计划分组编码(旧：付款计划编号)")
    private String payNotecode;

    @ApiModelProperty(value = "供应商id")
    private String merchant;

    @ApiModelProperty(value = "供应商名称")
    private String merchantName;

    @ApiModelProperty(value = "父id,无意义字段, 纯透传就好")
    private String pid;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "期数")
    private Integer termDate;

    @ApiModelProperty(value = "预估结算金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty(value = "应结算日期")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    @ApiModelProperty(value = "扣款金额")
    private BigDecimal deductionAmount;

    @ApiModelProperty(value = "结算状态 0未结算:1 未完成:2 已完成")
    private Integer paymentStatus;

    @ApiModelProperty(value = "开票/收票金额")
    private BigDecimal invoiceApplyAmount;

    @ApiModelProperty(value = "开票/收票状态 未完成:1 已完成:2")
    private Integer invoiceStatus;

    @ApiModelProperty(value = "收付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "未付金额")
    private BigDecimal noPayAmount;

    @ApiModelProperty(value = "计划状态 待提交:1 已完成:2 未完成:3")
    private Integer planStatus;

    @ApiModelProperty(value = "第几笔")
    private Integer howOrder;

    @ApiModelProperty(value = "结算汇总金额")
    private BigDecimal settleSumAmount;

    @ApiModelProperty(value = "费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty(value = "费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty(value = "成本预估编码")
    private String costEstimationCode;

    @ApiModelProperty(value = "结算计划分组")
    private String settlePlanGroup;

    @ApiModelProperty(value = "合同清单id")
    private String contractPayFundId;

    @ApiModelProperty(value = "是否完成成本计划的核销")
    private Integer payCostFinished;

    @ApiModelProperty(value = "核销税额")
    private BigDecimal settlementTaxAmount;

    @ApiModelProperty(value = "核销不含税金额")
    private BigDecimal settlementNoTaxAmount;

    @ApiModelProperty(value = "审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝")
    private Integer reviewStatus;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty("金额比例")
    private BigDecimal ratioAmount;

    @ApiModelProperty(value = "服务类型")
    private Integer serviceType;

    @ApiModelProperty(value = "费项")
    private String chargeItem;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "税率ID")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "不含税金额")
    private BigDecimal noTaxAmount;

    @ApiModelProperty(value = "税额")
    private BigDecimal taxAmount;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否使用 被结算单使用或生成成本计划则代表 被使用")
    private Boolean used = false;

    @ApiModelProperty(value = "成本计划id列表")
    private List<String> payCostPlanIdList;

}
