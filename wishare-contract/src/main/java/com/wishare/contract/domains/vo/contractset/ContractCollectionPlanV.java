package com.wishare.contract.domains.vo.contractset;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 合同收款计划信息
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractCollectionPlanV {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("税种税率id")
    private String taxRateIdPath;
    @ApiModelProperty("税种税率id list方式")
    private List<Long> taxRateIdList;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("招投标保证金")
    private Boolean bidBond;
    @ApiModelProperty("计划收款时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划收款金额（原币/含税）")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("本币金额（含税）")
    private BigDecimal localCurrencyAmount;
    @ApiModelProperty("本币金额（不含税）")
    private BigDecimal taxExcludedAmount;
    @ApiModelProperty("损益核算方式")
    private Integer profitLossAccounting;
    @ApiModelProperty("服务开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate serviceStartDate;
    @ApiModelProperty("服务结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate serviceEndDate;
    @ApiModelProperty("合同生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("费项名称")
    private String chargeItemName;
    @ApiModelProperty("部门名称")
    private String orgName;
    @ApiModelProperty("成本中心名称")
    private String costName;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("已开票/收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer invoiceStatus;
    @ApiModelProperty("减免金额")
    private BigDecimal creditAmount;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("款项比例")
    private BigDecimal paymentProportion;
    @ApiModelProperty("创建人Id")
    private String creator;
    @ApiModelProperty("收付款预警状态 0 正常 1 临期 2 逾期")
    private Integer warnState;
    @ApiModelProperty("逾期原因")
    private String overdueReason;
    @ApiModelProperty("逾期说明")
    private String overdueStatement;
    @ApiModelProperty("逾期原因id集")
    private String overdueReasonIds;
}
