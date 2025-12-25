package com.wishare.contract.domains.dto.revision.income;

import java.math.BigDecimal;
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
* 合同收入损益表
* </p>
*
* @author chenglong
* @since 2023-10-24
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_income_conclude_profit_loss请求对象", description = "合同收入损益表")
public class ContractIncomeConcludeProfitLossD {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("关联合同ID")
    private String contractId;
    @ApiModelProperty("收款计划编号")
    private String payNotecode;
    @ApiModelProperty("客户id")
    private String customer;
    @ApiModelProperty("客户名称")
    private String customerName;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("期数")
    private Boolean termDate;
    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime plannedCollectionTime;
    @ApiModelProperty("计划收付款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("减免金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("开票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收款金额")
    private BigDecimal receiptAmount;
    @ApiModelProperty("未收款金额")
    private BigDecimal noReceiptAmount;
    @ApiModelProperty("计划状态 0待提交  1未完成  2已完成")
    private Boolean planStatus;
    @ApiModelProperty("结算状态 0未结算  1未完成  2已完成")
    private Boolean paymentStatus;
    @ApiModelProperty("开票状态 0未完成  1已完成")
    private Boolean invoiceStatus;
    @ApiModelProperty("审核状态0待提交1审批中2已通过3已拒绝")
    private Boolean reviewStatus;
    @ApiModelProperty("部门名称")
    private String departName;
    @ApiModelProperty("拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)")
    private Boolean splitMode;
    @ApiModelProperty("费项")
    private String chargeItem;
    @ApiModelProperty("费项ID")
    private String chargeItemId;
    @ApiModelProperty("税率ID,额外需要维护，不展示")
    private Long taxRateId;
    @ApiModelProperty("税率")
    private String taxRate;
    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("第几笔")
    private Boolean howOrder;
    @ApiModelProperty("金额比例")
    private BigDecimal ratioAmount;
    @ApiModelProperty("服务类型")
    private Boolean serviceType;
    @ApiModelProperty("未计划金额")
    private BigDecimal noPlanAmount;
    @ApiModelProperty("税率路径")
    private String taxRateIdPath;
    @ApiModelProperty("租户id")
    private String tenantId;
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
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
