package com.wishare.contract.apps.fo.revision.income;

import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionSaveF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsSaveF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomeSettlementPeriodF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionSaveF;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/6/11:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同结算单信息表新增参数AddF", description = "收入合同结算单信息表新增参数AddF")
public class ContractIncomeSettlementAddF {

    @ApiModelProperty("保存类型")
    private String saveType;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("客户id")
    private String customer;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("申请付款日期")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("付款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;

    @ApiModelProperty("V2.12-期数")
    private String termDate;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("V2.12-计量周期")
    private List<ContractIncomeSettlementPeriodF> contractIncomeSettlementPeriodSaveFList;

    @ApiModelProperty("V2.12-成本预估计划id")
    private List<String> planIdList;

    @ApiModelProperty("V2.12-结算单标题-V2.12")
    private String title;

    @ApiModelProperty("V2.12-所属区域 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域")
    private Integer belongRegion;

    @ApiModelProperty("V2.12-确收类型 0中期确收 1最终确收")
    private Integer settlementType;

    @ApiModelProperty("V2.12-确收类别 0基础物管&非业主增值 1业主增值 2综合业务")
    private Integer settlementClassify;

    @ApiModelProperty("V2.12-所属层级 0项目 1区域公司")
    private Integer belongLevel;

    @ApiModelProperty(value = "V2.12-实际结算金额")
    private BigDecimal actualSettlementAmount;

    @ApiModelProperty(value = "V2.12-本期结算百分比")
    private String currentSettleRatio;

    @ApiModelProperty(value = "V2.12-结算审批完成时间")
    private LocalDateTime approveCompletedTime;

    @ApiModelProperty(value = "V2.12-累计结算金额")
    private BigDecimal totalSettledAmount;

    @ApiModelProperty(value = "V2.12-累计结算百分比")
    private String totalSettledRatio;

    @ApiModelProperty("V2.12-项目类型 0：住宅项目 1：非住宅项目")
    private Integer communityType;

    @ApiModelProperty("附件(前端传数组)")
    private List<FileVo> attachments;

    @ApiModelProperty("结算明细")
    private List<ContractIncomeSettDetailsSaveF> contractIncomeSettDetailsSaveFList;

    @ApiModelProperty("结算扣款明细")
    private List<ContractIncomeConcludeSettdeductionSaveF> contractIncomeConcludeSettdeductionSaveFList;

    @ApiModelProperty("收款计划明细")
    private List<ContractIncomePlanConcludeUpdateF> contractIncomePlanConcludeUpdateFS;
}
