package com.wishare.contract.domains.vo.revision.income.settlement;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "结算单页面-成本预估数据列表")
public class ContractIncomePlanForSettlementV {


    @ApiModelProperty(value = "成本预估计划ID",notes = "成本预估计划id")
    private String planId;

    @ApiModelProperty(value = "合同清单id",notes = "合同清单id")
    private String payFundId;

    @ApiModelProperty(value = "期数",notes = "期数")
    private Integer termDate;

    @ApiModelProperty(value = "成本预估计划编码",notes = "成本预估计划编码")
    private String costEstimationCode;

    @ApiModelProperty(value = "成本预估计划开始时间",notes = "成本预估计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date costStartTime;

    @ApiModelProperty(value = "成本预估计划结束时间",notes = "成本预估计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date costEndTime;

    @ApiModelProperty(value = "应结算日期(原计划收款时间)",notes = "应结算日期(原计划收款时间)")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date plannedCollectionTime;

    @ApiModelProperty(value = "预估结算金额",notes = "预估结算金额")
    private BigDecimal plannedCollectionAmount;

    @ApiModelProperty(value = "已结算金额",notes = "已结算金额")
    private BigDecimal settlementAmount;

    @ApiModelProperty(value = "未结算金额",notes = "未结算金额")
    private BigDecimal notSettlementAmount;

    @ApiModelProperty(value = "金额比例",notes = "金额比例")
    private BigDecimal ratioAmount;

    @ApiModelProperty(value = "清单项id",notes = "清单项id")
    private String typeId;

    @ApiModelProperty(value = "清单项名称",notes = "清单项名称")
    private String type;

    @ApiModelProperty(value = "费项id",notes = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称",notes = "费项名称")
    private String chargeItem;

    @ApiModelProperty(value = "税率id",notes = "税率id")
    private String taxRateId;

    @ApiModelProperty(value = "税率",notes = "税率")
    private String taxRate;

    @ApiModelProperty(value = "税额",notes = "税额")
    private BigDecimal taxRateAmount;

    @ApiModelProperty(value = "不含税金额",notes = "不含税金额")
    private BigDecimal amountWithOutRate;

    @ApiModelProperty(value = "备注",notes = "备注")
    private String remark;

    @ApiModelProperty(value = "结算组",notes = "结算组")
    private String settlePlanGroup;

    @ApiModelProperty(value = "是否含有收入计划",notes = "是否含有收入计划")
    private Boolean isHaveIncomePlan = Boolean.FALSE;

    private Integer splitMode;
}
