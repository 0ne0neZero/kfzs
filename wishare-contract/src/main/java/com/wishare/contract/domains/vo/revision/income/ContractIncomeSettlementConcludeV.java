package com.wishare.contract.domains.vo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/10:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同结算单表视图对象V", description = "收入合同结算单表视图对象V")
public class ContractIncomeSettlementConcludeV {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("结算单编号")
    private String payFundNumber;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同编码")
    private String contractNo;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    @ApiModelProperty("V2.12-合同变更后金额")
    private BigDecimal changContractAmount;
    @ApiModelProperty("期数")
    private String termDate;
    @ApiModelProperty("计划付款日期")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("收款状态")
    private Integer paymentStatus;
    @ApiModelProperty("结算状态")
    private Integer settleStatus;
    @ApiModelProperty("收款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;
    @ApiModelProperty("开票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("开票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("收款金额")
    private String paymentAmount;
    @ApiModelProperty("结算时间")
    private LocalDateTime paymentDate;
    @ApiModelProperty("计划状态")
    private Integer planStatus;
    @ApiModelProperty("审核状态")
    private Integer reviewStatus;
    @ApiModelProperty("创建人")
    private String creatorName;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("V2.12-备注")
    private String remark;

    @ApiModelProperty("结算单标题")
    private String title;

    @ApiModelProperty("所属区域")
    private Integer belongRegion;

    @ApiModelProperty("结算类型")
    private Integer settlementType;

    @ApiModelProperty("确收类别")
    private Integer settlementClassify;

    @ApiModelProperty("所属层级")
    private Integer belongLevel;

    @ApiModelProperty("实际结算金额")
    private BigDecimal actualSettlementAmount;

    @ApiModelProperty("本期结算百分比")
    private String currentSettleRatio;

    @ApiModelProperty("V2.12-结算审批完成时间")
    private LocalDateTime approveCompletedTime;

    @ApiModelProperty("累计结算金额")
    private BigDecimal totalSettledAmount;

    @ApiModelProperty("累计结算百分比")
    private String totalSettledRatio;

    @ApiModelProperty("项目类型")
    private Integer communityType;

    @ApiModelProperty("收款计划ID")
    private String planId;
}
