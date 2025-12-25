package com.wishare.contract.apps.fo.contractset;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 合同收款计划信息 更新请求参数
 * </p>
 *
 * @author wangrui
 * @since 2022-09-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractCollectionPlanUpdateF {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty(value = "费项Id",required = true)
    @NotNull(message = "费项Id不能为空！")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("成本中心名")
    private String costName;
    @ApiModelProperty(value = "合同Id",required = true)
    @NotNull(message = "合同不能为空！")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("责任部门名")
    private String orgName;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("税种税率id")
    private String taxRateIdPath;
    @ApiModelProperty("税率")
    private BigDecimal taxRate;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("招投标保证金")
    private Boolean bidBond;
    @ApiModelProperty("计划收款时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划收款金额（原币/含税）")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("损益核算方式 1 合同期分摊 2 服务期分摊 3 按时点结算")
    private Integer profitLossAccounting;
    @ApiModelProperty("合同生效日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("服务开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate serviceStartDate;
    @ApiModelProperty("服务结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate serviceEndDate;
    @ApiModelProperty("合同性质 1 收入 2 支出")
    private Integer contractNature;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("已开票/收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus = 0;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer invoiceStatus;
    @ApiModelProperty("租户Id")
    private String tenantId;
    @ApiModelProperty(value = "合同预警状态 0正常 1 临期 2 已到期",hidden = true)
    private Integer warnState;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("款项比例")
    private BigDecimal paymentProportion;
    @ApiModelProperty("是否自动生成损益（工程计提情况下）0 否 1 是")
    private  Integer isNotProfit;
}
