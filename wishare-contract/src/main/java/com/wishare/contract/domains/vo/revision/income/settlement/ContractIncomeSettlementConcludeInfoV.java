package com.wishare.contract.domains.vo.revision.income.settlement;

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
public class ContractIncomeSettlementConcludeInfoV {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("结算单编号")
    private String payFundNumber;
    @ApiModelProperty("供应商名称")
    private String merchantName;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同编码")
    private String contractNo;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    @ApiModelProperty("期数")
    private String termDate;
    @ApiModelProperty("计划付款日期")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("结算时间")
    private LocalDateTime paymentDate;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("付款状态")
    private Integer paymentStatus;
    @ApiModelProperty("付款状态名称")
    private String paymentStatusName;
    @ApiModelProperty("结算状态")
    private Integer settleStatus;
    @ApiModelProperty("结算状态名称")
    private String settleStatusName;
    @ApiModelProperty("付款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("收票状态名称")
    private String invoiceStatusName;
    @ApiModelProperty("付款金额")
    private String paymentAmount;
    @ApiModelProperty("计划状态")
    private Integer planStatus;
    @ApiModelProperty("审核状态")
    private Integer reviewStatus;
    @ApiModelProperty("创建人")
    private String creatorName;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("V2.12-备注")
    private String remark;

    @ApiModelProperty("V2.12-项目类型 0：住宅项目 1：非住宅项目")
    private Integer communityType;

    @ApiModelProperty("V2.12-结算单标题-V2.12")
    private String title;

    @ApiModelProperty("V2.12-所属区域 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域")
    private Integer belongRegion;

    @ApiModelProperty("V2.12-确收类型 0中期结算 1最终结算")
    private Integer settlementType;

    @ApiModelProperty("V2.12-确收分类 确收分类 0基础物管&非业主增值 1业主增值 2综合业务")
    private Integer settlementClassify;

    @ApiModelProperty("V2.12-增值类型 0空间运营 1到家服务 2零售业务 3美居业务 4资产业务 5餐饮业务 6业态运营")
    private Integer additionType;

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
}
