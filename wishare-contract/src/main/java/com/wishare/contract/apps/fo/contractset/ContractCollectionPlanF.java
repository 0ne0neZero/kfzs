package com.wishare.contract.apps.fo.contractset;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class ContractCollectionPlanF {

    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("费项Id")
    private Long chargeItemId;
    @ApiModelProperty("成本中心Id")
    private Long costId;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("责任部门（组织id）")
    private Long orgId;
    @ApiModelProperty("预算科目")
    private String budgetAccount;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("票据类型")
    private String billType;
    @ApiModelProperty("招投标保证金")
    private Boolean bidBond;
    @ApiModelProperty("计划收款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime serviceStartDate;
    @ApiModelProperty("服务结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime serviceEndDate;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("已收款/付款金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty("已开票/收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer invoiceStatus;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
}
