package com.wishare.contract.domains.vo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ApiModel(value = "收款计划前端交互实体V2")
public class IncomeConcludePlanV2 {

    @ApiModelProperty(value = "收款计划id")
    private String id;

    @ApiModelProperty(value = "父id,无意义字段, 纯透传就好")
    private String pid;

    @ApiModelProperty(value = "客户id")
    private String customer;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "合同id")
    private String contractId;


    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

    @ApiModelProperty(value = "期数")
    private Integer termDate;

    @ApiModelProperty(value = "计划收付款金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty(value = "应收款日期")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAmount;

    @ApiModelProperty(value = "结算状态 未结算:1 未完成:2 已完成:3")
    private Integer paymentStatus;


    @ApiModelProperty(value = "费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty(value = "费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty(value = "收款计划编码")
    private String costEstimationCode;

    @ApiModelProperty(value = "结算计划分组")
    private String settlePlanGroup;

    @ApiModelProperty(value = "合同清单id")
    private String contractPayFundId;


    @ApiModelProperty(value = "暂估确收金额-子级和预估金额一致")
    private BigDecimal confirmedAmountReceivedTemp;

    @ApiModelProperty(value = "最大应收日期-子级和应收日期一致")
    private LocalDate ptMax;

    @ApiModelProperty(value = "最小应收日期-子级和应收日期一致")
    private LocalDate ptMin;

    @ApiModelProperty(value = "是否完成收入计划的核销")
    private Integer payIncomeFinished;

    @ApiModelProperty(value = "核销税额")
    private BigDecimal settlementTaxAmount;

    @ApiModelProperty(value = "核销不含税金额")
    private BigDecimal settlementNoTaxAmount;

    @ApiModelProperty(value = "审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 默认全都是2")
    private Integer reviewStatus;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty(value = "金额比例")
    private BigDecimal ratioAmount;

    @ApiModelProperty(value = "服务类型")
    private Integer serviceType;

    @ApiModelProperty(value = "费项名称")
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

}
