package com.wishare.contract.apps.fo.revision.income;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
* <p>
* 合同收入损益表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-10-24
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同收入损益表更新请求参数", description = "合同收入损益表")
public class ContractIncomeConcludeProfitLossUpdateF {

    /**
    * id 不可为空
    */
    @ApiModelProperty(value = "主键ID",required = true)
    @NotBlank(message = "主键主键ID不可为空")
    @Length(message = "主键ID不可超过 50 个字符",max = 50)
    private String id;
    /**
    * contractId
    */
    @ApiModelProperty("关联合同ID")
    @Length(message = "关联合同ID不可超过 50 个字符",max = 50)
    private String contractId;
    /**
    * payNotecode
    */
    @ApiModelProperty("收款计划编号")
    @Length(message = "收款计划编号不可超过 40 个字符",max = 40)
    private String payNotecode;
    /**
    * customer
    */
    @ApiModelProperty("客户id")
    @Length(message = "客户id不可超过 50 个字符",max = 50)
    private String customer;
    /**
    * customerName
    */
    @ApiModelProperty("客户名称")
    @Length(message = "客户名称不可超过 50 个字符",max = 50)
    private String customerName;
    /**
    * contractNo
    */
    @ApiModelProperty("合同编号")
    @Length(message = "合同编号不可超过 100 个字符",max = 100)
    private String contractNo;
    /**
    * contractName
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 50 个字符",max = 50)
    private String contractName;
    /**
    * termDate
    */
    @ApiModelProperty("期数")
    private Boolean termDate;
    /**
    * plannedCollectionTime
    */
    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;
    /**
    * plannedCollectionAmount
    */
    @ApiModelProperty("计划收付款金额")
    @Digits(integer = 15,fraction =6,message = "计划收付款金额不正确")
    private BigDecimal plannedCollectionAmount;
    /**
    * settlementAmount
    */
    @ApiModelProperty("结算金额")
    @Digits(integer = 15,fraction =6,message = "结算金额不正确")
    private BigDecimal settlementAmount;
    /**
    * deductionAmount
    */
    @ApiModelProperty("减免金额")
    @Digits(integer = 15,fraction =6,message = "减免金额不正确")
    private BigDecimal deductionAmount;
    /**
    * invoiceApplyAmount
    */
    @ApiModelProperty("开票金额")
    @Digits(integer = 15,fraction =6,message = "开票金额不正确")
    private BigDecimal invoiceApplyAmount;
    /**
    * receiptAmount
    */
    @ApiModelProperty("收款金额")
    @Digits(integer = 15,fraction =6,message = "收款金额不正确")
    private BigDecimal receiptAmount;
    /**
    * noReceiptAmount
    */
    @ApiModelProperty("未收款金额")
    @Digits(integer = 15,fraction =6,message = "未收款金额不正确")
    private BigDecimal noReceiptAmount;
    /**
    * planStatus
    */
    @ApiModelProperty("计划状态 0待提交  1未完成  2已完成")
    private Boolean planStatus;
    /**
    * paymentStatus
    */
    @ApiModelProperty("结算状态 0未结算  1未完成  2已完成")
    private Boolean paymentStatus;
    /**
    * invoiceStatus
    */
    @ApiModelProperty("开票状态 0未完成  1已完成")
    private Boolean invoiceStatus;
    /**
    * reviewStatus
    */
    @ApiModelProperty("审核状态0待提交1审批中2已通过3已拒绝")
    private Boolean reviewStatus;
    /**
    * departName
    */
    @ApiModelProperty("部门名称")
    @Length(message = "部门名称不可超过 50 个字符",max = 50)
    private String departName;
    /**
    * splitMode
    */
    @ApiModelProperty("拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)")
    private Boolean splitMode;
    /**
    * chargeItem
    */
    @ApiModelProperty("费项")
    @Length(message = "费项不可超过 50 个字符",max = 50)
    private String chargeItem;
    /**
    * chargeItemId
    */
    @ApiModelProperty("费项ID")
    @Length(message = "费项ID不可超过 40 个字符",max = 40)
    private String chargeItemId;
    /**
    * taxRateId
    */
    @ApiModelProperty("税率ID,额外需要维护，不展示")
    private Long taxRateId;
    /**
    * taxRate
    */
    @ApiModelProperty("税率")
    @Length(message = "税率不可超过 20 个字符",max = 20)
    private String taxRate;
    /**
    * noTaxAmount
    */
    @ApiModelProperty("不含税金额")
    @Digits(integer = 15,fraction =6,message = "不含税金额不正确")
    private BigDecimal noTaxAmount;
    /**
    * taxAmount
    */
    @ApiModelProperty("税额")
    @Digits(integer = 15,fraction =6,message = "税额不正确")
    private BigDecimal taxAmount;
    /**
    * remark
    */
    @ApiModelProperty("备注")
    @Length(message = "备注不可超过 1,000 个字符",max = 1000)
    private String remark;
    /**
    * howOrder
    */
    @ApiModelProperty("第几笔")
    private Boolean howOrder;
    /**
    * ratioAmount
    */
    @ApiModelProperty("金额比例")
    @Digits(integer = 10,fraction =2,message = "金额比例不正确")
    private BigDecimal ratioAmount;
    /**
    * serviceType
    */
    @ApiModelProperty("服务类型")
    private Boolean serviceType;
    /**
    * noPlanAmount
    */
    @ApiModelProperty("未计划金额")
    @Digits(integer = 15,fraction =6,message = "未计划金额不正确")
    private BigDecimal noPlanAmount;
    /**
    * taxRateIdPath
    */
    @ApiModelProperty("税率路径")
    @Length(message = "税率路径不可超过 255 个字符",max = 255)
    private String taxRateIdPath;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;

    @ApiModelProperty("保存类型1暂存 2提交")
    @NotBlank(message = "保存类型不能为空")
    private String saveType;
}
